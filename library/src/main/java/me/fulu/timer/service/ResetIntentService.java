package me.fulu.timer.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import me.fulu.timer.database.DbHelper;
import me.fulu.timer.logic.TaskPlanCalculater;
import me.fulu.timer.model.Task;

public class ResetIntentService extends IntentService {

	public ResetIntentService() {
		super("ResetIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.e("ResetIntentService", "onHandleIntent");
		ArrayList<Task> tasks = (ArrayList<Task>) new DbHelper<Task>().queryForAll(Task.class);
		if (tasks != null) {
			for (Task task : tasks) {
				try {
					TaskPlanCalculater.resetTask(task);
				} catch (Exception e) {

				}
			}
		}
		if (TaskPlanCalculater.useService) {
			SuperTimerService.refresh(getApplicationContext());
		}
	}

}
