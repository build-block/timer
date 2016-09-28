package me.fulu.timer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import me.fulu.timer.database.DbHelper;
import me.fulu.timer.logic.TaskPlanCalculater;
import me.fulu.timer.model.Task;
import me.fulu.timer.service.SuperTimerService;
import me.fulu.timer.util.NotificationUtil;

public class TaskReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("TaskReceiver", "TaskReceiver ING");
		if (intent.getAction().equals("me.fulu.timer.TASK_COME_ACTION")) {

			int taskId = intent.getIntExtra("uuid", 0);
			Task task = new DbHelper<Task>().query(Task.class, taskId);
			if (task != null) {
				if (task.enable && task.notification) {
					NotificationUtil.showNotification(context, task.name, task.ringtone, taskId);
				}
				
				if(task.deleteAfter) {
					task.remove();
				} else {
					TaskPlanCalculater.doneTask(task);
				}
				if (TaskPlanCalculater.useService) {
					SuperTimerService.refresh(context);
				}
			}
		}
	}
}
