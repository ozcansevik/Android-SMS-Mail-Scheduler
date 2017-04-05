package com.example.ozsevik1.send_schedule.service;


import com.example.ozsevik1.send_schedule.model.SendMailTask;

import com.example.ozsevik1.send_schedule.R;
import com.example.ozsevik1.send_schedule.util.NotificationLauncher;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ozcan on 24/03/2017.
 */

public class ScheduleMail extends JobService {



    @Override
    public boolean onStartJob(JobParameters job) {
        List toEmail = new ArrayList();
        toEmail.add(job.getExtras().get("toEmail").toString());
        String fromEmail = job.getExtras().get("fromEmail").toString();
        String fromPass = job.getExtras().get("fromPass").toString();
        String Subject = job.getExtras().get("Subject").toString();
        String Text = job.getExtras().get("Text").toString();


        new SendMailTask().execute(fromEmail,fromPass,toEmail,Subject,Text);

        NotificationLauncher.launchNotification(getString(R.string.app_name), getString(R.string.error_mail_send), this);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }

}
