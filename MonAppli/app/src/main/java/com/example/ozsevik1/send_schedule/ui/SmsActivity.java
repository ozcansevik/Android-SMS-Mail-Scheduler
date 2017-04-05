package com.example.ozsevik1.send_schedule.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ozsevik1.send_schedule.R;
import com.example.ozsevik1.send_schedule.data.InternalStorage;
import com.example.ozsevik1.send_schedule.model.ModelSms;
import com.example.ozsevik1.send_schedule.model.Sender;
import com.example.ozsevik1.send_schedule.util.RecyclerItemClickListener;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SmsActivity extends AppCompatActivity implements View.OnClickListener, Observer,
        DatePickerFragment.OnDatePickedListener, TimePickerFragment.OnTimePickedListener {

    private TextView textSms;
    private TextView text_num;
    private TextView text_nom;
    private ImageButton buttonSend;

    private TextView text_time;
    private TextView text_date;

    private ModelSms modelSms;

    public ModelSms getModelSms() {
        return modelSms;
    }

    private List<ModelSms> listModelSms;

    private FirebaseJobDispatcher dispatcher;

    private RecyclerView listSms;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);


        modelSms = new ModelSms();

        modelSms.addObserver(this);

        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

        listSms = (RecyclerView) findViewById(R.id.list_sms);

        textSms = (TextView) findViewById(R.id.textSms);

        text_num = (TextView) findViewById(R.id.text_num);

        text_time = (TextView) findViewById(R.id.text_time);

        text_date = (TextView) findViewById(R.id.text_date);

        text_nom = (TextView) findViewById(R.id.text_nom);

        ImageButton boutonSelectContact = (ImageButton) findViewById(R.id.boutonSelectContact);

        boutonSelectContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(SmsActivity.this , ContactsListActivity.class);
                startActivityForResult(i, 1);
            }
        });

        ImageButton buttonSelectCalendar = (ImageButton) findViewById(R.id.buttonSelectCalendar);
        buttonSelectCalendar.setOnClickListener(this);


        buttonSend = (ImageButton) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);

        listModelSms = new ArrayList<>();

        try {

            // Retrieve the list from internal storage
            if (( ArrayList<ModelSms>) InternalStorage.readObject(this, "listeSMS") != null)
                listModelSms = ( ArrayList<ModelSms>) InternalStorage.readObject(this, "listeSMS");


        } catch (IOException e) {
            Log.e("SmsActivity", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("SmsActivity", e.getMessage());
        }

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this){
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        };
        listSms.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(listModelSms);

        listSms.setAdapter(mAdapter);
        listSms.setHasFixedSize(true);


        listSms.addOnItemTouchListener(
                new RecyclerItemClickListener(this, listSms ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        AlertDialog.Builder adb=new AlertDialog.Builder(SmsActivity.this);
                        adb.setTitle(R.string.title_dialog_delete_sms);
                        adb.setMessage(getString(R.string.text_dialog_delete_sms));
                        final int positionToRemove = position;
                        adb.setNegativeButton((R.string.no_button), null);
                        adb.setPositiveButton((R.string.yes_button), new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ModelSms sms = listModelSms.get(positionToRemove);
                                listModelSms.remove(positionToRemove);
                                sms.cancelJob(dispatcher);
                                mAdapter.notifyDataSetChanged();
                            }});
                        adb.show();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        //rien pour l'instant
                    }
                })
        );

    }

    public void update(Observable obs, Object obj)
    {
        text_nom.setText(modelSms.getNomContact());
        text_num.setText(modelSms.getNumeroTelephone());
        text_date.setText(modelSms.getDateText());
        text_time.setText(modelSms.getTempsText());

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //On récupere le numero de telephone lorsque l'activité ContactsListActivity est arretée
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String num = data.getStringExtra("num");
                String nom = null;

                if( data.getStringExtra("name") != null) {
                    nom = data.getStringExtra("name");
                    modelSms.setNumeroTelephone(num);
                    modelSms.setNomContact(nom);
                }
                else {
                    modelSms.setNumeroTelephone(num);
                    modelSms.setNomContact(null);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        try{
            InternalStorage.writeObject(this, "listeSMS", listModelSms);

        }catch (IOException e) {
            Log.e("SmsActivity", e.getMessage());
        }

        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSelectCalendar: {
                showSettingDialogCalendar();
                break;
            }
            case R.id.buttonSend: {


                modelSms.setTexte(textSms.getText().toString());

                    if(TextUtils.isEmpty(modelSms.getTexte())){
                        Toast.makeText(getApplicationContext(),getString(R.string.error_empty_text),
                                Toast.LENGTH_LONG).show();
                        break;
                    }

                    if(TextUtils.isEmpty(modelSms.getTempsText())) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_time_not_selected),
                                Toast.LENGTH_LONG).show();
                        break;
                    }

                    if(TextUtils.isEmpty(modelSms.getDateText())) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_date_not_selected),
                                Toast.LENGTH_LONG).show();
                        break;
                    }

                    if(TextUtils.isEmpty(modelSms.getNumeroTelephone())){
                        Toast.makeText(getApplicationContext(), getString(R.string.error_contact_not_selected),
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                

                listModelSms.add(modelSms);
                mAdapter.notifyDataSetChanged();

                if(modelSms.getCalendar().before(Calendar.getInstance())){
                     modelSms.setTotalTime(0); //si l'utilisateur met du temps a appuyer sur envoyer et que l'heure programmée est depassée
                }
                //test pour gerer le cas ou l'utilisater met du temps mais que l'heure n'est pas dépassée, il faut soustraire le temps perdue
                else if ((modelSms.getCalendar().getTimeInMillis() - Calendar.getInstance().getTimeInMillis())/1000 < modelSms.getCalendar().getTimeInMillis()/1000) {
                    int duree = (int) (modelSms.getCalendar().getTimeInMillis() - Calendar.getInstance().getTimeInMillis())/1000;
                    modelSms.setTotalTime(duree);
                }else
                    modelSms.calculTime();

                modelSms.schedule(dispatcher);

                Toast.makeText(this, R.string.text_ajouter_list, Toast.LENGTH_SHORT).show();

                clearText();
                modelSms = new ModelSms();
                modelSms.addObserver(this);
            }
            default:
                break;
        }
    }

    private void clearText() {
        text_nom.setText("");
        text_time.setText("");
        textSms.setText("");
        text_date.setText("");
        text_num.setText("");
    }

    public void showSettingDialogCalendar() {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePickerShow");

    }

    public void onDatePick(int duree, int year, int month, int day, int hour, int min, int sec){

        modelSms.setTimeDatePicker(duree);
        modelSms.setDateCalendar(day, month, year);
        modelSms.setTimeCalendar(hour, min, sec);
        Log.d("modelSms", "hour : " +modelSms.toString());

    }

    public void onTimePick(int duree, int hourOfDay, int min) {

            modelSms.setTimeCalendar(hourOfDay, min, 0);
            modelSms.setTimeTimePicker(duree);
    }

    @Override
    public Sender getSender() {
        return modelSms;
    }
}
