package com.example.ozsevik1.send_schedule.ui;



import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.ozsevik1.send_schedule.R;

import java.util.Calendar;

/**
 * Created by krak on 07/03/17.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Calendar currentDate;
    OnDatePickedListener mCallback;

    public interface OnDatePickedListener {
        public void onDatePick(int duree, int year, int month, int day, int hour, int min, int sec);
    }


    @Override
    public DatePickerDialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(),this, currentYear, currentMonth, currentDay);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnDatePickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " OnDatePickedListener");
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        currentDate = Calendar.getInstance();

        final Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year,month,day);

        int duree = (int) (selectedDate.getTimeInMillis() - currentDate.getTimeInMillis())/1000;
        Log.d("Duree cal", "Duree cal : " + duree +" " + selectedDate.toString());

        if(duree < 0){
            Toast.makeText(getContext(), getString(R.string.error_time_datePicker),
                    Toast.LENGTH_LONG).show();
            dismiss();
        }
        else {
            mCallback.onDatePick(duree, year, month, day, currentDate.get(Calendar.HOUR_OF_DAY),currentDate.get(Calendar.MINUTE),currentDate.get(Calendar.SECOND));
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(((FragmentActivity)mCallback).getSupportFragmentManager(), "datePickerShow");
        }
    }
}