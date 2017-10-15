package id.co.blogspot.wimsonevel.photoandvideocapture.network;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import id.co.blogspot.wimsonevel.photoandvideocapture.MainActivity;
import id.co.blogspot.wimsonevel.photoandvideocapture.R;

/**
 * Created by kwikkunusantara on 10/15/17.
 */

public class NotificationUtil {

    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;

    private Context context;

    public NotificationUtil(Context context) {
        this.context = context;
    }

    public void showNotification(String title, String message) {
        builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(getPendingIntent(context));

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build()); //0 = ID of notification
    }

    public void showNotificationProgress() {
        builder.setAutoCancel(false);
        builder.setProgress(0, 0, true);
        notificationManager.notify(0, builder.build());
    }

    public void showCompleteNotification(String message) {
        showNotification(context.getString(R.string.app_name), message);
    }

    public void showErrorNotification(String message) {
        showNotification(context.getString(R.string.app_name), message);
    }

    private PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
