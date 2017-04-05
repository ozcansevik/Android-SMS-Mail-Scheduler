package com.example.ozsevik1.send_schedule.model;

import android.os.Bundle;

import com.example.ozsevik1.send_schedule.service.ScheduleMail;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ModelMail extends Sender implements Serializable, Scheduler{

    final String emailPort = "587";// gmail's smtp port
    final String smtpAuth = "true";
    final String starttls = "true";
    final String emailHost = "smtp.gmail.com";




    String fromEmail;
    String fromPassword;
    List<String> toEmailList;
    String emailSubject;

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getFromPassword() {
        return fromPassword;
    }

    public void setFromPassword(String fromPassword) {
        this.fromPassword = fromPassword;
    }

    public List<String> getToEmailList() {
        return toEmailList;
    }

    public void addToEmailList(String toEmail) {
        toEmailList.add(toEmail);
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public ModelMail() {
        this.fromEmail = null;
        this.fromPassword = null;
        this.toEmailList = new ArrayList<>();
        this.emailSubject = null;
    }

    public ModelMail(String fromEmail, String fromPassword,
                     List<String> toEmailList, String emailSubject, String emailBody) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmailList = toEmailList;
        this.emailSubject = emailSubject;
        this.setTexte(emailBody);

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);

    }

    public MimeMessage createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
        for (String toEmail : toEmailList) {

            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail));
        }

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(getTexte(), "text/html");// for a html email
        // emailMessage.setText(emailBody);// for a text email

        return emailMessage;
    }

    public void sendEmail() throws AddressException, MessagingException {

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromEmail, fromPassword);

        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
    }

    @Override
    public void schedule(FirebaseJobDispatcher dispatcher){

        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString("fromEmail", fromEmail);
        myExtrasBundle.putString("fromPass", fromPassword);
        myExtrasBundle.putString("toEmail", toEmailList.get(0));
        myExtrasBundle.putString("Text", getTexte());
        myExtrasBundle.putString("Subject",emailSubject);

        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(ScheduleMail.class)
                // uniquely identifies the job
                .setTag(getId())
                // one-off job
                .setRecurring(false)
                // don't persist past a device reboot
                //.setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                //persist after reboot
                .setLifetime(Lifetime.FOREVER)
                // start between totalTime and totalTime+5 seconds from now
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
        return "Texte : \"" + getTexte() + "\" à envoyer à l'adresse e-mail : " +
                " à " + getTempsText() + " le " +  getDateText();
    }
}