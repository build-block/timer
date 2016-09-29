package me.fulu.timer.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.HashMap;

import me.fulu.timer.logic.CycleCalculater;
import me.fulu.timer.logic.TaskPlanCalculater;

import me.fulu.timer.database.DbHelper;

@DatabaseTable
public class Task extends BaseModel {
    
	private static final long serialVersionUID = 6165702206258066656L;

	@DatabaseField(generatedId = true, columnName = "_id")
    public int id;
    
    @DatabaseField
    public String name;

    @DatabaseField
    public Date startTime;

    @DatabaseField
    public Date endTime;
    
    @DatabaseField
    public String pattern = CycleCalculater.CyclePattern.WEEK.toString();
    
    @DatabaseField
    public String patternParameter;

    @DatabaseField
    public Boolean notification=true;
    
    @DatabaseField
    public String ringtone;
    
    @DatabaseField
    public Boolean deleteAfter=false;
    
    @DatabaseField
    public Boolean enable = true;
    
    private static DbHelper<Task> DbHelper = new DbHelper<Task>();
    
    @Override
    public void createOrUpdate() {
        if (startTime.getTime() < new Date().getTime()) {
            enable = false;
        }
    	super.createOrUpdate();
    	TaskPlanCalculater.updateTask(this);
    }
    
    @Override
    public void update() {
    	super.update();
    	TaskPlanCalculater.updateTask(this);
    }
    
    @Override
    public void remove() {
    	super.remove();
    	TaskPlanCalculater.deleteTask(this);
    }
    
    public static Task getNextTask(){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("enable", true);
        return DbHelper.queryForFirst(Task.class, map, "startTime", true);
    }
}
