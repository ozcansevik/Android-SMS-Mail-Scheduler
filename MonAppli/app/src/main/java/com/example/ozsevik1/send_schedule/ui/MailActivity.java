package com.example.ozsevik1.send_schedule.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ozsevik1.send_schedule.R;
import com.example.ozsevik1.send_schedule.model.ModelMail;
import com.example.ozsevik1.send_schedule.model.Sender;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

public class MailActivity extends AppCompatActivity implements View.OnClickListener, Observer,
        DatePickerFragment.OnDatePickedListener, TimePickerFragment.OnTimePickedListener{


    private TextView textMail;
    private TextView textSubject;
    private TextView text_mailto;
    private TextView textdate;
    private TextView texttime;
    private TextView text_mailfrom;
    private TextView text_passfrom;
    private ImageButton buttonSend;
    private ModelMail androidEmail;


    private FirebaseJobDispatcher dispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        androidEmail = new ModelMail();

        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

        textMail = (TextView) findViewById(R.id.textMail);

        textSubject = (TextView) findViewById(R.id.textObject);

        text_mailto = (TextView) findViewById(R.id.text_mailto);

        textdate = (TextView) findViewById(R.id.text_date);

        texttime = (TextView) findViewById(R.id.text_time);

        text_mailfrom = (TextView) findViewById(R.id.text_mailfrom);

        text_passfrom = (TextView) findViewById(R.id.text_passfrom);

        ImageButton buttonSelectCalendar = (ImageButton) findViewById(R.id.buttonCalendarMail);
        buttonSelectCalendar.setOnClickListener(this);

        buttonSend = (ImageButton) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);

        androidEmail.addObserver(this);

        AlertDialog.Builder adb=new AlertDialog.Builder(MailActivity.this);
        adb.setTitle(R.string.alertdialog_mail_title);
        adb.setMessage(getString(R.string.alertdialog_mail_message));
        adb.setNegativeButton((R.string.no_button), null);
        adb.show();

    }

    @Override
    public void update(Observable obs, Object arg) {
        textdate.setText(androidEmail.getDateText());
        texttime.setText(androidEmail.getTempsText());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCalendarMail: {

                showSettingDialogCalendar();
                break;
            }
            case R.id.buttonSend: {

                androidEmail.setTexte(textMail.getText().toString());
                androidEmail.addToEmailList(text_mailto.getText().toString());
                androidEmail.setEmailSubject(textSubject.getText().toString());
                androidEmail.setFromEmail(text_mailfrom.getText().toString());
                androidEmail.setFromPassword(text_passfrom.getText().toString());

                if(TextUtils.isEmpty(androidEmail.getEmailSubject())){
                    Toast.makeText(getApplicationContext(),getString(R.string.error_empty_subject),
                            Toast.LENGTH_LONG).show();
                    break;
                }
                if(TextUtils.isEmpty(androidEmail.getFromEmail())){
                    Toast.makeText(getApplicationContext(),getString(R.string.error_empty_emailfrom),
                            Toast.LENGTH_LONG).show();
                    break;
                }
                if(TextUtils.isEmpty(androidEmail.getFromPassword())){
                    Toast.makeText(getApplicationContext(),getString(R.string.error_empty_passfrom),
                            Toast.LENGTH_LONG).show();
                    break;
                }
                if(TextUtils.isEmpty(text_mailto.getText().toString())){
                    Toast.makeText(getApplicationContext(),getString(R.string.error_empty_mailto),
                            Toast.LENGTH_LONG).show();
                    break;
                }
                if(TextUtils.isEmpty(androidEmail.getTexte())){
                    Toast.makeText(getApplicationContext(),getString(R.string.error_empty_text),
                            Toast.LENGTH_LONG).show();
                    break;
                }

                if(TextUtils.isEmpty(androidEmail.getTempsText())) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_time_not_selected),
                            Toast.LENGTH_LONG).show();
                    break;
                }

                if(TextUtils.isEmpty(androidEmail.getDateText())) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_date_not_selected),
                            Toast.LENGTH_LONG).show();
                    break;
                }

                clearText();


                if(androidEmail.getCalendar().before(Calendar.getInstance())){
                    androidEmail.setTotalTime(0); //si l'utilisateur met du temps a appuyer sur envoyer et que l'heure programmée est depassée
                }
                //test pour gerer le cas ou l'utilisater met du temps mais que l'heure n'est pas dépassée, il faut soustraire le temps perdue
                else if ((androidEmail.getCalendar().getTimeInMillis() - Calendar.getInstance().getTimeInMillis())/1000 < androidEmail.getCalendar().getTimeInMillis()/1000) {
                    int duree = (int) (androidEmail.getCalendar().getTimeInMillis() - Calendar.getInstance().getTimeInMillis())/1000;
                    androidEmail.setTotalTime(duree);
                }else
                    androidEmail.calculTime();


                androidEmail.schedule(dispatcher);

                Toast.makeText(getApplicationContext(), getString(R.string.mail_success_program),
                        Toast.LENGTH_LONG).show();
                androidEmail = new ModelMail();
                androidEmail.addObserver(this);
            }
            default:
                break;
        }
    }



    private void clearText() {
        textSubject.setText("");
        texttime.setText("");
        text_mailto.setText("");
        textdate.setText("");
        textMail.setText("");
    }

    public void showSettingDialogCalendar() {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePickerShow");

    }

    public void onDatePick(int duree, int year, int month, int day, int hour, int min, int sec){

        androidEmail.setTimeDatePicker(duree);

        androidEmail.setDateCalendar(day,month,year);
        androidEmail.setTimeCalendar(hour,min,sec);

    }

    public void onTimePick(int duree, int hourOfDay, int min){
        androidEmail.setTimeCalendar(hourOfDay,min,0);
        androidEmail.setTimeTimePicker(duree);
    }

    @Override
    public Sender getSender() {
        return androidEmail;
    }


}
