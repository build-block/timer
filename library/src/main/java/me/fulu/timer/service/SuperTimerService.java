package me.fulu.timer.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.TimeZone;

import hirondelle.date4j.DateTime;
import me.fulu.timer.R;
import me.fulu.timer.logic.TaskPlanCalculater;
import me.fulu.timer.model.Task;
import me.fulu.timer.util.DateTimeUtil;

public class SuperTimerService extends Service {

	private final int APP_NOTIFICATION = 0X11111111;

	@Override
	public void onCreate() {
		super.onCreate();

		Intent serviceIntent = new Intent(this, ResetIntentService.class);
		startService(serviceIntent);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		refreshNotification();

		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		restartService();
	}
	
	public static void refresh(Context context){
		context.startService(new Intent(context, SuperTimerService.class));
	}
	
	private void refreshNotification() {
		// Builds the notification and issues it.
		startForeground(APP_NOTIFICATION, getNotification());
	}

	private Notification getNotification() {
		Task task = Task.getNextTask();

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

		intent.setComponent(getPackageManager().getLaunchIntentForPackage(getPackageName()).getComponent());
		intent.addCategory(Intent.CATEGORY_LAUNCHER);

		PendingIntent pendingIntent = PendingIntent.getActivity(
				getApplicationContext(), task == null ? 0 : task.id, intent, 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				getApplicationContext())
				.setSmallIcon(TaskPlanCalculater.notificationIcon)
				.setContentTitle(getDisplayTitle(task))
				.setContentText(getDisplayStr(task))
				.setContentIntent(pendingIntent);

		Notification notification = mBuilder.build();
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		// 设置 n.flags 为 Notification.FLAG_NO_CLEAR ，该标志表示当用户点击 Clear之后，不能清除该通知。
		notification.flags |= Notification.FLAG_NO_CLEAR;//

		return notification;
	}

	private String getDisplayTitle(Task task) {
		String appName="";
		try {
			PackageInfo packageInfo = getPackageManager()
					.getPackageInfo(getPackageName(), 0);
			appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		if (task == null)
			return appName;
		return appName + "  下个提醒";
	}

	private String getDisplayStr(Task task) {
		if (task == null)
			return "暂无提醒";

		DateTime dateTime = DateTimeUtil.fromDate(task.startTime);
		String date = "";
		if (dateTime.isSameDayAs(DateTime.today(TimeZone.getDefault()))) {
			date = "今天";
		} else {
			date = dateTime.format("YYYY-MM-DD");
		}

		return task.name + " " + date + " " + dateTime.format("hh:mm");
	}

	private void restartService() {
		Intent intent = new Intent(getApplicationContext(),
				SuperTimerService.class);
		getApplicationContext().startService(intent);
	}
}
