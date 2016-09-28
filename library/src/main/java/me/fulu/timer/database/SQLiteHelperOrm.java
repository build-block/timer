package me.fulu.timer.database;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import me.fulu.timer.logic.TaskPlanCalculater;
import me.fulu.timer.model.Task;


public class SQLiteHelperOrm extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "ST.db";
    public static final int DATABASE_VERSION = 1;

    public SQLiteHelperOrm(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public SQLiteHelperOrm() {
        this(TaskPlanCalculater.context);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        
        try {
            TableUtils.createTable(connectionSource, Task.class);
        } catch (SQLException e) {
            Log.e("SQLiteHelperOrm","onCreate", e);
        }
    
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        

    }
    
    
}