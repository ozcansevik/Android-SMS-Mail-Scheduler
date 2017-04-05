package com.example.ozsevik1.send_schedule.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.ozsevik1.send_schedule.R;
import com.example.ozsevik1.send_schedule.ui.MainActivity;
import com.example.ozsevik1.send_schedule.ui.SmsActivity;

/**
 * Created by ozcan on 19/03/2017.
 */

public class NotificationLauncher {

    public static void launchNotification(String title, String content,Context context){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_sms_black)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setPriority(2)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setVibrate(new long[] { 100, 100, 100, 100});


        Intent resultIntent = new Intent(context, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(SmsActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setProgress(100, 100, false);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());

    }


}
