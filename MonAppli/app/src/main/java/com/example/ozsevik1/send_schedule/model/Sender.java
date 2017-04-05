package com.example.ozsevik1.send_schedule.model;

import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Observable;
import java.util.UUID;

/**
 * Created by krak on 24/03/17.
 */

public class Sender extends Observable implements Serializable {

    private static final long serialVersionUID = -225946591488560367L;

    private String id;
    private String texte;
    private Calendar date;
    private String date_text;
    private String temps_text;
    private int totalTime;
    private int timeTimePicker;
    private int timeDatePicker;



    public Sender() {
        this.id= UUID.randomUUID().toString();
        this.texte = null;
        this.date = Calendar.getInstance();
        this.date_text = null;
        this.temps_text = null;
        this.timeDatePicker = 0;
        this.timeTimePicker = 0;
        this.totalTime = 0;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String text){
        this.texte = text;
        setChanged();
        notifyObservers();

    }

    public Calendar getCalendar() {
        return date;
    }

    public void setDateCalendar(int day,int month,int year) {
        this.date.set(year,month,day);
        setDateText(day + "/" + (month+1) + "/" + year); //month +1 car on a un calendrier gregorien
        setChanged();
        notifyObservers();
    }

    public void setTimeCalendar(int hour, int min, int sec){

        this.date.set(Calendar.HOUR_OF_DAY,hour);
        this.date.set(Calendar.MINUTE,min);
        this.date.set(Calendar.SECOND,sec);

        if(hour==0 && min==0 && sec==0)
            setTempsText("");

        else {

            if (min <= 9)
                setTempsText(hour + "h" + "0" + min);
            else
                setTempsText(hour + "h" + min);
        }

        setChanged();
        notifyObservers();
    }
    public String getDateText() {
        return date_text;
    }

    public void setDateText(String text) {
        date_text = text;
    }

    public int getTime() {
        return timeTimePicker;
    }

    public void setTimeTimePicker(int timeTimePicker) {
        this.timeTimePicker = timeTimePicker;
    }

    public int getTimeDatePicker() {
        return timeDatePicker;
    }

    public void setTimeDatePicker(int timeDatePicker) {
        this.timeDatePicker = timeDatePicker; }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
        Log.d("TimeSEND", "Time : "+ totalTime);
    }

    public String getTempsText() { return temps_text; }

    public void setTempsText(String text){
        temps_text = text;
    }

    public void cancelJob(FirebaseJobDispatcher dispatcher){
        dispatcher.cancel(this.id);
    }

    public void calculTime(){

        totalTime = timeDatePicker + timeTimePicker;
        Log.d("TimeSEND", "Time : "+ totalTime);
    }

}
