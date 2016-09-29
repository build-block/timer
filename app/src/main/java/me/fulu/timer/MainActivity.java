package me.fulu.timer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;

import hirondelle.date4j.DateTime;
import me.fulu.timer.logic.CycleCalculater;
import me.fulu.timer.logic.TaskPlanCalculater;
import me.fulu.timer.model.Task;
import me.fulu.timer.service.SuperTimerService;
import me.fulu.timer.util.DateTimeUtil;

import me.fulu.timer.demo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TaskPlanCalculater.init(getApplicationContext(), R.drawable.prefs_icon);
        TaskPlanCalculater.useService = true;

        setContentView(R.layout.activity_main);

        Task task = new Task();
        task.id = 1;
        task.name = "测试";
        task.startTime = DateTimeUtil.addMinutes(new Date(), 1);
        task.endTime = DateTimeUtil.addMinutes(new Date(), 10);
        task.pattern = CycleCalculater.CyclePattern.MINUTE.toString();
        task.patternParameter = "num=1";

        task.createOrUpdate();

        SuperTimerService.refresh(getApplicationContext());
    }

    private Task createTask(Date startTime, Date endTime, int alertType, int aheadTimeType) {
        int aheadTime = 0;
        switch (aheadTimeType) {
            case 2:
                aheadTime = 5;
                break;
            case 3:
                aheadTime = 15;
                break;
            case 4:
                aheadTime = 60;
                break;
        }

        startTime = DateTimeUtil.addMinutes(startTime, -aheadTime);

        Task task = new Task();
        task.id = 1;
        task.name = "测试";
        task.startTime = startTime;
        task.endTime = endTime;

        if (aheadTimeType == 0) {
            task.enable = false;
        }

        String patternParameter = "num=1";

        DateTime startDateTime = DateTimeUtil.fromDate(task.startTime);

        switch (alertType) {
            case 0:
                task.pattern = CycleCalculater.CyclePattern.ONE_DAY.toString();
                break;
            case 1:
                task.pattern = CycleCalculater.CyclePattern.DAY.toString();
                break;
            case 2:
                task.pattern = CycleCalculater.CyclePattern.WEEK.toString();
                int w = startDateTime.getWeekDay() - 1;
                String week = w + "";
                patternParameter = "week=" + week;
                break;
            case 3:
                task.pattern = CycleCalculater.CyclePattern.MONTH.toString();
                break;
            case 4:
                task.pattern = CycleCalculater.CyclePattern.YEAR.toString();
                break;
        }

        task.patternParameter = patternParameter;

        return task;
    }
}
