package com.example.ozsevik1.send_schedule.service;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.ozsevik1.send_schedule.R;
import com.example.ozsevik1.send_schedule.util.NotificationLauncher;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
/**
 * Created by krak on 17/03/17.
 */

public class ScheduleSMS extends JobService {


        @Override
        public boolean onStartJob(JobParameters job) {

            String text = job.getExtras().get("texte").toString();
            String phoneNo = job.getExtras().get("numero").toString();
            Log.d("Numero", "Num :" + phoneNo);

            Log.d("SMS", "Sms : " + text);
            try {

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, text, null, null);
                NotificationLauncher.launchNotification(getString(R.string.app_name), getString(R.string.sms_send), this);

            } catch (Exception e) {
                NotificationLauncher.launchNotification(getString(R.string.app_name), getString(R.string.error_sms_send), this);

            }

            return false; // Answers the question: "Is there still work going on?"
        }

        @Override
        public boolean onStopJob(JobParameters job) {
            return false; // Answers the question: "Should this job be retried?"
        }


}
