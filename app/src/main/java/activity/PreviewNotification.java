package activity;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.aueb.idry.R;

public class PreviewNotification {
    private Context context;
    private NotificationManagerCompat notificationManager;
    private PendingIntent pendingIntentStop, pendingIntentResume;
    private RemoteViews collapsedView, expandedView;
    private Notification notification;

    public static final String DRYER_STOP = "actionStop";
    public static final String DRYER_RESUME = "actionResume";

    private final int resumeBtnID = R.id.notification_preview_resume_btn;
    private final int stopBtnID = R.id.notification_preview_stop_btn;

    public PreviewNotification(Context context) {
        this.context = context;

        notificationManager = NotificationManagerCompat.from(context);
        Intent intentStop = new Intent(context, PreviewNotificationService.class)
                .setAction(DRYER_STOP);
        pendingIntentStop = PendingIntent.getBroadcast(context, 0, intentStop,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentResume = new Intent(context, PreviewNotificationService.class)
                .setAction(DRYER_RESUME);
        pendingIntentResume = PendingIntent.getBroadcast(context, 0, intentResume,
                PendingIntent.FLAG_UPDATE_CURRENT);

        collapsedView = new RemoteViews(context.getPackageName(), R.layout.notification_preview_collapsed);
        expandedView = new RemoteViews(context.getPackageName(), R.layout.notification_preview_expanded);

        expandedView.setOnClickPendingIntent(resumeBtnID, pendingIntentResume);
        expandedView.setOnClickPendingIntent(stopBtnID, pendingIntentStop);

        expandedView.setTextViewText(R.id.notification_preview_expanded_duration, "Hello world!");

        notification = new NotificationCompat.Builder(context, App.NOT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_settings_icon)
                .setOnlyAlertOnce(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .build();

    }

    public void showNotificationWithDelay(String routineName) {
        notificationManager.cancel(1);
        setRemoteViewsContent(R.string.notification_delay, routineName, R.string.notification_delay_hint);
        notificationManager.notify(1, notification);
    }

    public void showNotificationReadyToStart(String routineName) {
        notificationManager.cancel(1);
        setButtonsVisibility(View.VISIBLE, View.GONE);
        setDurationVisibility(View.GONE);
        setRemoteViewsContent(R.string.notification_on_start, routineName, R.string.notification_on_start_hint);
        notificationManager.notify(1, notification);
    }

    public void updateNotificationTime(String time) {
        expandedView.setTextViewText(R.id.notification_preview_expanded_duration, time);
        notificationManager.notify(1, notification);
    }

    public void showStartNotification() {
        notificationManager.cancel(1);
        setButtonsVisibility(View.GONE, View.VISIBLE);
        setDurationVisibility(View.VISIBLE);
        setRemoteViewsContent(R.string.notification_running, R.string.notification_duration_label);
        notificationManager.notify(1, notification);
    }

    public void showResumeNotification() {
        notificationManager.cancel(1);
        setButtonsVisibility(View.GONE, View.VISIBLE);
        setDurationVisibility(View.VISIBLE);
        setRemoteViewsContent(R.string.notification_on_resume, R.string.notification_duration_label);
        notificationManager.notify(1, notification);
    }

    public void showPauseNotification() {
        notificationManager.cancel(1);
        setButtonsVisibility(View.VISIBLE, View.GONE);
        setDurationVisibility(View.GONE);
        setRemoteViewsContent(R.string.notification_on_pause, R.string.notification_on_pause_hint);
        notificationManager.notify(1, notification);
    }

    public void showFinishNotification() {
        notificationManager.cancel(1);
        setButtonsVisibility(View.GONE, View.GONE);
        setRemoteViewsContent(R.string.notification_finish, R.string.notification_finish_hint);
        setDurationVisibility(View.GONE);
        notificationManager.notify(1, notification);
    }

    private void setButtonsVisibility(int resume, int stop) {
        expandedView.setViewVisibility(resumeBtnID, resume);
        expandedView.setViewVisibility(stopBtnID, stop);
    }

    private void setDurationVisibility(int duration) {
        expandedView.setViewVisibility(R.id.notification_preview_expanded_duration,
                duration);
    }

    private void setRemoteViewsContent(int collapsedViewLabel,
                                         String expandedViewRoutineName,
                                         int expandedViewInfo) {
        expandedView.setTextViewText(R.id.notification_preview_expanded_routine_name,
                expandedViewRoutineName);
        setRemoteViewsContent(collapsedViewLabel, expandedViewInfo);
    }

    private void setRemoteViewsContent(int collapsedViewLabel,
                                         int expandedViewInfo) {
        collapsedView.setTextViewText(R.id.notification_preview_collapsed_label,
                context.getString(collapsedViewLabel));
        expandedView.setTextViewText(R.id.notification_preview_expanded_info,
                context.getString(expandedViewInfo));
    }
}