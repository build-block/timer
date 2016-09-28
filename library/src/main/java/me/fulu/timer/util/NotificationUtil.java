package me.fulu.timer.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import me.fulu.timer.logic.TaskPlanCalculater;

public class NotificationUtil {

    public static void showNotification(Context context, String message, String ringtone, int nid) {

        Intent intentP = context.getPackageManager().
                getLaunchIntentForPackage(context.getPackageName());

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.setComponent(intentP.getComponent());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        String appName="";

        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setContentTitle(appName)
                .setSmallIcon(TaskPlanCalculater.notificationIcon)
                .setContentText(message).setContentIntent(pendingIntent).setAutoCancel(true);
        if (ringtone != null && !ringtone.isEmpty())
            mBuilder.setSound(Uri.parse(ringtone));
        // Sets an ID for the notification
        int mNotificationId = nid;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
