package me.fulu.timer.logic;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.Log;

import java.util.Date;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;
import hirondelle.date4j.DateTime.DayOverflow;
import me.fulu.timer.model.Task;
import me.fulu.timer.util.DateTimeUtil;

public class TaskPlanCalculater {

    public static Context context;
    public static @DrawableRes int notificationIcon;
    public static boolean useService = false;

    public static void init(Context cx, @DrawableRes int notifyIcon) {
        context = cx;
        notificationIcon = notifyIcon;
    }

    public static void updateTask(Task task) {
        DateTime dt = DateTimeUtil.fromDate(task.startTime);
        if (dt.isInThePast(TimeZone.getDefault()) || task.startTime.getTime() > task.endTime.getTime()) {
            return;
        }
        Intent intent = new Intent("me.fulu.timer.TASK_COME_ACTION");
        intent.putExtra("uuid", task.id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, task.startTime.getTime(), pendingIntent);
    }

    public static void deleteTask(Task task) {
        Intent intent = new Intent("me.fulu.timer.TASK_COME_ACTION");
        intent.putExtra("uuid", task.id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static void doneTask(Task task) {
        resetTask(task);
    }

    //开机重启时 重新计算
    public static void resetTask(Task task) {
        DateTime startDateTime = DateTimeUtil.fromDate(task.startTime);
        DateTime todayDateTime = DateTime.now(TimeZone.getDefault());
        Date nextTime = null;
        if (startDateTime.isInThePast(TimeZone.getDefault())) {

            startDateTime = new DateTime(todayDateTime.getYear(), todayDateTime.getMonth(), todayDateTime.getDay(), startDateTime.getHour(), startDateTime.getMinute(), startDateTime.getSecond(), startDateTime.getNanoseconds());

            CycleCalculater.CyclePattern pattern = CycleCalculater.getPatternFromPatternStr(context, task.pattern);
            Uri uri = Uri.parse("http://fulu.me/index.php?" + task.patternParameter);

            String num = uri.getQueryParameter("num");
            DateTime dateTime = null;

            switch (pattern) {
                case COUNT_DOWN:
                    dateTime = todayDateTime.plus(0, 0, 0, 0, Integer.valueOf(num), 0, 0, DayOverflow.Spillover);
                    nextTime = new Date(dateTime.getMilliseconds(TimeZone.getDefault()));
                    break;
                case WEEK:
                    String weeks = uri.getQueryParameter("week");
                    int j = 0;
                    if (startDateTime.isInThePast(TimeZone.getDefault())) {
                        j = 1;
                    }
                    for (int i = j; i <= 7; i++) {
                        dateTime = startDateTime.plusDays(i);
                        int w = dateTime.getWeekDay() - 1;
                        String week = w + "";
                        if (weeks.contains(week)) {
                            nextTime = new Date(dateTime.getMilliseconds(TimeZone.getDefault()));
                            break;
                        }
                    }
                    break;
                case MONTH:
                    if (startDateTime.isInThePast(TimeZone.getDefault())) {
                        nextTime = DateTimeUtil.addMonths(task.startTime, 1);
                    } else {
                        nextTime = new Date(startDateTime.getMilliseconds(TimeZone.getDefault()));
                    }
                    break;
                case YEAR:
                    if (startDateTime.isInThePast(TimeZone.getDefault())) {
                        nextTime = DateTimeUtil.addYears(task.startTime, 1);
                    } else {
                        nextTime = new Date(startDateTime.getMilliseconds(TimeZone.getDefault()));
                    }
                    break;
                case ONE_DAY:
                    if (!startDateTime.isInThePast(TimeZone.getDefault())) {
                        nextTime = new Date(startDateTime.getMilliseconds(TimeZone.getDefault()));
                    }
                    break;
                case DAY:
                    if (startDateTime.isInThePast(TimeZone.getDefault())) {
                        dateTime = startDateTime.plusDays(Integer.valueOf(num));
                        nextTime = new Date(dateTime.getMilliseconds(TimeZone.getDefault()));
                    } else {
                        nextTime = new Date(startDateTime.getMilliseconds(TimeZone.getDefault()));
                    }

                    break;
                case MINUTE:

                    if (startDateTime.isInThePast(TimeZone.getDefault())) {
                        dateTime = startDateTime.plus(0, 0, 0, 0, Integer.valueOf(num), 0, 0, DayOverflow.Spillover);
                        while (dateTime.isInThePast(TimeZone.getDefault())) {
                            dateTime = dateTime.plus(0, 0, 0, 0, Integer.valueOf(num), 0, 0, DayOverflow.Spillover);
                        }
                        nextTime = new Date(dateTime.getMilliseconds(TimeZone.getDefault()));
                    } else {
                        nextTime = new Date(startDateTime.getMilliseconds(TimeZone.getDefault()));
                    }

                    break;
                case HOUR:

                    if (startDateTime.isInThePast(TimeZone.getDefault())) {
                        dateTime = startDateTime.plus(0, 0, 0, Integer.valueOf(num), 0, 0, 0, DayOverflow.Spillover);
                        while (dateTime.isInThePast(TimeZone.getDefault())) {
                            dateTime = dateTime.plus(0, 0, 0, Integer.valueOf(num), 0, 0, 0, DayOverflow.Spillover);
                        }
                        nextTime = new Date(dateTime.getMilliseconds(TimeZone.getDefault()));
                    } else {
                        nextTime = new Date(startDateTime.getMilliseconds(TimeZone.getDefault()));
                    }
                    break;

                default:
                    break;
            }

            if (nextTime != null)
                task.startTime = nextTime;
            Log.e("TaskPlanCalculater", "resetTask " + task.startTime.toLocaleString());
            task.update();
        }
    }
}
