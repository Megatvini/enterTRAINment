package ge.edu.freeuni.android.entertrainment.map;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import ge.edu.freeuni.android.entertrainment.R;


public class NotificationGenerator {

    public static void boom(Context context, String station) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.railway_station)
                        .setContentTitle(station)
                        .setContentText("chavediiit")
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
