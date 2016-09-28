package me.fulu.timer.logic;

import android.content.Context;

public class CycleCalculater {
    public enum CyclePattern {
        WEEK, MONTH, YEAR ,ONE_DAY, DAY, HOUR, MINUTE, COUNT_DOWN
    }

    public static CycleCalculater.CyclePattern getPatternFromPatternStr(Context context, String p) {
        CycleCalculater.CyclePattern pattern = CycleCalculater.CyclePattern.WEEK;
        if (p.equals(CycleCalculater.CyclePattern.WEEK.toString())) {
            pattern = CycleCalculater.CyclePattern.WEEK;
        } else if (p.equals(CycleCalculater.CyclePattern.ONE_DAY.toString())) {
            pattern = CycleCalculater.CyclePattern.ONE_DAY;
        } else if (p.equals(CycleCalculater.CyclePattern.DAY.toString())) {
            pattern = CycleCalculater.CyclePattern.DAY;
        } else if (p.equals(CycleCalculater.CyclePattern.HOUR.toString())) {
            pattern = CycleCalculater.CyclePattern.HOUR;
        } else if (p.equals(CycleCalculater.CyclePattern.MINUTE.toString())) {
            pattern = CycleCalculater.CyclePattern.MINUTE;
        } else if (p.equals(CycleCalculater.CyclePattern.COUNT_DOWN.toString())) {
            pattern = CycleCalculater.CyclePattern.COUNT_DOWN;
        }

        return pattern;
    }
}
