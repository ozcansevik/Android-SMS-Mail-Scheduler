package com.example.ozsevik1.send_schedule.model;

import android.os.Bundle;

import com.example.ozsevik1.send_schedule.service.ScheduleSMS;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.io.Serializable;

/**
 * Created by ozsevik1 on 01/03/17.
 */
public class ModelSms extends Sender implements Serializable, Scheduler {

    private String numeroTelephone;
    private String nomContact;

    public ModelSms() {
        super();
        numeroTelephone = null;
        nomContact = null;

    }

    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
        setChanged();
        notifyObservers();
    }

    public String getNomContact() {
        return nomContact;
    }

    public void setNomContact(String nomContact) {
        this.nomContact = nomContact;
        setChanged();
        notifyObservers();
    }


    @Override
    public void schedule(FirebaseJobDispatcher dispatcher){

        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString("numero", numeroTelephone);
        myExtrasBundle.putString("texte", getTexte());

        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(ScheduleSMS.class)
                // uniquely identifies the job
                .setTag(getId())
                // one-off job
                .setRecurring(false)
                //persist after reboot
                .setLifetime(Lifetime.FOREVER)
                // start between totalTime and totalTime+1 seconds from now
                .setTrigger(Trigger.executionWindow(getTotalTime(), getTotalTime()+1))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setExtras(myExtrasBundle)
                .build();
        dispatcher.mustSchedule(myJob);

    }

    @Override
    public String toString() {
        return "Texte : \"" + getTexte() + "\" à envoyer au numéro : " + getNumeroTelephone() +
                " (" + getNomContact() + ") à " + getTempsText() + " le " +  getDateText();
    }
}

