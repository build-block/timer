package me.fulu.timer.model;

import java.io.Serializable;

import me.fulu.timer.database.DbHelper;


public abstract class BaseModel implements Serializable {

	private static final long serialVersionUID = 6962948501899931186L;
	
	private static DbHelper<BaseModel> mDbHelper = new DbHelper<BaseModel>();

    public BaseModel() {
    };

    public void remove() {
        getDbHelper().remove(this);
    }

    public void update() {
        getDbHelper().update(this);
    }

    public void createOrUpdate() {
        getDbHelper().createOrUpdate(this);
    }

    private DbHelper<BaseModel> getDbHelper() {
        if (mDbHelper == null) {
            mDbHelper = new DbHelper<BaseModel>();
        }

        return mDbHelper;
    }
}
