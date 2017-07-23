package it.unibo.matteo.jappo.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import it.unibo.matteo.jappo.Activity.SplashScreenActivity;
import it.unibo.matteo.jappo.R;


public class AlarmNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        final String title = context.getString(R.string.notification_title);
        final String subtitle = context.getString(R.string.notification_subtitle);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(subtitle)
                .setDefaults(Notification.DEFAULT_VIBRATE| Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);

        Intent notificationIntent = new Intent(context, SplashScreenActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }
}
