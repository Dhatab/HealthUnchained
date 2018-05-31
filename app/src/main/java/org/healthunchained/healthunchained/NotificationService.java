package org.healthunchained.healthunchained;

import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.widget.Toast;
import android.app.Notification;
import android.app.PendingIntent;
import android.view.View;
import android.widget.RemoteViews;

public class NotificationService extends Service {


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            showNotification();
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();

        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();

        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();

        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {

            Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    Notification status;
    private final String LOG_TAG = "NotificationService";

    private void showNotification() {
// Using RemoteViews to bind custom layouts into Notification
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.status_bar);
        RemoteViews bigViews = new RemoteViews(getPackageName(),
                R.layout.status_bar_expanded);

// showing default album image
        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        bigViews.setImageViewBitmap(R.id.status_bar_album_art,
                Constants.getDefaultAlbumArt(this));

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, NotificationService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, NotificationService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setImageViewResource(R.id.status_bar_play,
                R.drawable.ic_pause_circle_outline_white);
        bigViews.setImageViewResource(R.id.status_bar_play,
                R.drawable.ic_pause_circle_outline_white);

        views.setTextViewText(R.id.status_bar_track_name, "Song Title");
        bigViews.setTextViewText(R.id.status_bar_track_name, "Song Title");

        views.setTextViewText(R.id.status_bar_artist_name, "Artist Name");
        bigViews.setTextViewText(R.id.status_bar_artist_name, "Artist Name");

        bigViews.setTextViewText(R.id.status_bar_album_name, "Album Name");

        status = new Notification.Builder(this).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.ic_launcher_background;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }
}

