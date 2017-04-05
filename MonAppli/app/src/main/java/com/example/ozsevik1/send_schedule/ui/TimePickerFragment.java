package com.example.ozsevik1.send_schedule.ui;



import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ozsevik1.send_schedule.R;
import com.example.ozsevik1.send_schedule.model.Sender;

import java.util.Calendar;

/**
 * Created by krak on 07/03/17.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private Calendar currentTime;


    OnTimePickedListener mCallback;


    public interface OnTimePickedListener {
        public void onTimePick(int duree, int hourOfDay, int min);
        public Sender getSender();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);


        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (TimePickerFragment.OnTimePickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " OnDatePickedListener");
        }
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        //on re recupère une instance qui nous servira pour avoir l'heure actuelle
        //utile lorsque l'utilisateur reste sur l'horloge sans selectionne d'heure pdt un moment
        //il faut reactualiser l'heure actuelle
        currentTime = Calendar.getInstance();
        Sender send = mCallback.getSender();
        final Calendar selectedTime = send.getCalendar();
        selectedTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
        selectedTime.set(Calendar.MINUTE,minute);
        selectedTime.set(Calendar.SECOND,0);

        int duree = (int) (selectedTime.getTimeInMillis() - currentTime.getTimeInMillis())/1000;

        Log.d("TimeTimePicker", "Time : " + duree +" " + selectedTime.toString());

        if(duree < 0){
            Toast.makeText(getContext(), getString(R.string.error_time_timePicker),
                    Toast.LENGTH_LONG).show();
            mCallback.onTimePick(0,0,0);
            dismiss();

        }

        else {

            mCallback.onTimePick(duree,hourOfDay,minute);

            // Log.d("Time","Time : " + diffHeure + " " +diffMin+ " " + duree );
            Log.d("Time", "Time : " + duree);
        }
    }
}