package com.example.ozsevik1.send_schedule.model;

import android.os.AsyncTask;

import java.util.List;

public class SendMailTask extends AsyncTask {


    public SendMailTask() {

    }

    protected void onPreExecute() {
    }

    @Override
    protected Object doInBackground(Object... args) {
        try {

            ModelMail androidEmail = new ModelMail(args[0].toString(),
                    args[1].toString(), (List) args[2], args[3].toString(),
                    args[4].toString());


            androidEmail.createEmailMessage();

            androidEmail.sendEmail();

        } catch (Exception e) {


        }
        return null;
    }

    @Override
    public void onProgressUpdate(Object... values) {


    }

    @Override
    public void onPostExecute(Object result) {

    }

}