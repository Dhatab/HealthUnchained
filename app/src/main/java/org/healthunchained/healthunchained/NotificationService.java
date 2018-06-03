package org.healthunchained.healthunchained;

import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Notification;
import android.app.PendingIntent;
import android.view.View;
import android.widget.RemoteViews;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;
import static org.healthunchained.healthunchained.App.CHANNEL_1_ID;
import static org.healthunchained.healthunchained.Constants.NOTIFICATION_ID.FOREGROUND_SERVICE;
import static org.healthunchained.healthunchained.MainActivity.body;
import static org.healthunchained.healthunchained.MainActivity.mediaPlayer;
import static org.healthunchained.healthunchained.MainActivity.title;

public class NotificationService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

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
            //Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            mediaPlayer.start();
            //Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();

        } else if (intent.getAction().equals(Constants.ACTION.PAUSE_ACTION)){
            mediaPlayer.pause();
            //Toast.makeText(this, "Clicked Paused", Toast.LENGTH_SHORT).show();

        /*else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();

            else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();*/


        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            stopForeground(true);
            mediaPlayer.stop();
            stopSelf();
        }
        return START_STICKY;
    }

    private void showNotification() {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.status_bar);
        RemoteViews bigViews = new RemoteViews(getPackageName(),R.layout.status_bar_expanded);
        views.setViewVisibility(R.id.status_bar_album_art, View.VISIBLE);
        bigViews.setImageViewBitmap(R.id.status_bar_album_art, Constants.getDefaultAlbumArt(this));

        Intent notificationIntent = new Intent(this, NotificationService.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent playIntent = new Intent(this, NotificationService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);

        Intent pauseIntent = new Intent(this, NotificationService.class);
        pauseIntent.setAction(Constants.ACTION.PAUSE_ACTION);
        PendingIntent ppauseIntent = PendingIntent.getService(this, 0, pauseIntent, 0);

        Intent closeIntent = new Intent(this, NotificationService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0, closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_play, pplayIntent);
        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        views.setOnClickPendingIntent(R.id.status_pause, ppauseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_pause, ppauseIntent);



        /*Intent nextIntent = new Intent(this, NotificationService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);
        Intent previousIntent = new Intent(this, NotificationService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);*/

        views.setTextViewText(R.id.status_bar_title, title);
        views.setTextViewText(R.id.status_bar_body, body);
        bigViews.setTextViewText(R.id.status_bar_title, title);
        bigViews.setTextViewText(R.id.status_bar_body, body);

        Notification status = new NotificationCompat.Builder(this, CHANNEL_1_ID).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.ic_music_note;
        status.contentIntent = pendingIntent;
        status.priority = -2;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);

    }
}

