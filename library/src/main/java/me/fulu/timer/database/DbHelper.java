package me.fulu.timer.database;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DbHelper<T> {

    private static SQLiteHelperOrm mSqLiteHelperOrm;

    private Dao<T, Long> mDao;

    public Dao<T, Long> getDao(Class<T> c) {
        if (null == mDao) {
            try {
                mDao = DaoManager.createDao(getDB().getConnectionSource(), c);
                // mDao = getDB().getDao(c);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return mDao;
    }

    public int countOfNotNullField(Class<T> c, String fieldName) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.setCountOf(true);
            query.where().isNotNull(fieldName);
            return (int)dao.countOf(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "count", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return 0;
    }

    public int count(Class<T> c) {

        try {
            Dao<T, Long> dao = getDao(c);
            return (int)dao.countOf();
        } catch (SQLException e) {
            Log.e("DbHelper", "count", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return 0;
    }

    public int create(T po) {

        try {
            Dao<T, Long> dao = getDao((Class<T>)po.getClass());
            return dao.create(po);
        } catch (SQLException e) {
            Log.e("DbHelper", "create", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return -1;
    }

    public int createOrUpdate(T po) {

        try {
            Dao dao = getDao((Class<T>)po.getClass());
            return dao.createOrUpdate(po).getNumLinesChanged();
        } catch (SQLException e) {
            Log.e("DbHelper", "createOrUpdate", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return -1;
    }

    public int remove(T po) {

        try {
            Dao<T, Long> dao = getDao((Class<T>)po.getClass());
            return dao.delete(po);
        } catch (SQLException e) {
            Log.e("DbHelper", "remove", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return -1;
    }

    public int remove(Collection<T> po) {

        try {
            Dao dao = getDao((Class<T>)po.getClass());
            return dao.delete(po);
        } catch (SQLException e) {
            Log.e("DbHelper", "remove", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return -1;
    }

    public void removeAll(Class<T> c, String columnName, Object value) {

        try {
            Dao dao = getDao(c);

            DeleteBuilder<T, String> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq(columnName, value);
            deleteBuilder.delete();

        } catch (SQLException e) {
            Log.e("DbHelper", "removeAll", e);
        } finally {
            // if (db != null)
            // db.close();
        }

    }

    public void removeAll(Class<T> c, String columnName) {

        try {
            Dao dao = getDao(c);

            DeleteBuilder<T, String> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().isNotNull(columnName);
            deleteBuilder.delete();

        } catch (SQLException e) {
            Log.e("DbHelper", "removeAll", e);
        } finally {
            // if (db != null)
            // db.close();
        }

    }

    public void updateNoNullToNull(Class<T> c, String updatedColumnName) {
        try {

            Dao<T, Long> dao = getDao(c);
            UpdateBuilder<T, Long> updateBuilder = dao.updateBuilder();
            updateBuilder.where().isNotNull(updatedColumnName);
            updateBuilder.updateColumnValue(updatedColumnName, null);
            updateBuilder.update();

        } catch (SQLException e) {
            Log.e("DbHelper", "update", e);
        } finally {
            // if (db != null)
            // db.close();
        }
    }

    /**
     * Update model
     * 
     * @param c
     * @param values
     * @param columnName where
     * @param value where
     * @return
     */
    public int update(Class<T> c, String updatedColumnName, Object updatedValue, String columnName, Object value) {

        try {

            if (value instanceof String && !((String)value).contains("\"")) {
                value = ((String)value).replace("'", "''");
            }

            Dao<T, Long> dao = getDao(c);
            UpdateBuilder<T, Long> updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq(columnName, value);

            updateBuilder.updateColumnValue(updatedColumnName, updatedValue);

            return updateBuilder.update();
        } catch (SQLException e) {
            Log.e("DbHelper", "update", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return -1;
    }

    /**
     * Update model
     * 
     * @param c
     * @param values
     * @param columnName where
     * @param value where
     * @return
     */
    public int update(Class<T> c, HashMap<String, Object> values, String columnName, Object value) {

        try {
            Dao<T, Long> dao = getDao(c);
            UpdateBuilder<T, Long> updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq(columnName, value);
            for (String key : values.keySet()) {
                updateBuilder.updateColumnValue(key, values.get(key));
            }
            return updateBuilder.update();
        } catch (SQLException e) {
            Log.e("DbHelper", "update", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return -1;
    }

    public int update(T po) {

        try {
            Dao<T, Long> dao = getDao((Class<T>)po.getClass());
            return dao.update(po);
        } catch (SQLException e) {
            Log.e("DbHelper", "update", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return -1;
    }

    public List<T> queryForAll(Class<T> c) {

        try {
            Dao<T, Long> dao = getDao(c);
            return dao.queryForAll();
        } catch (SQLException e) {
            Log.e("DbHelper", "queryForAll", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return new ArrayList<T>();
    }

    public List<T> queryForAllOrderby(Class<T> c, String orderFieldName) {
        return queryForAllOrderby(c, orderFieldName, false);
    }

    public List<T> queryForAllOrderby(Class<T> c, String oderByField, boolean asc) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.orderBy(oderByField, asc);
            return dao.query(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "queryForAllOrderby", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return new ArrayList<T>();
    }

    public List<T> queryForAllOrderby(Class<T> c, String field, Long before, Long end, boolean asc, int limit) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.orderBy(field, asc);
            if (before != null) {
                query.where().lt(field, before);
            }

            if (end != null) {
                query.where().and().gt(field, end);
            }

            if (limit > 0) {
                query.limit(Long.valueOf(limit));
            }

            return dao.query(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "queryForAllOrderby", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return new ArrayList<T>();
    }

    public List<T> queryForAllOrderby(Class<T> c, String oderByField1, String orderByField2, String filedNotZero, String extra) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            ;
            query.where().ne(filedNotZero, 0);
            query.orderByRaw(oderByField1 + " desc , " + orderByField2 + " desc;");
            return dao.query(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "queryForAllOrderby", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return new ArrayList<T>();
    }

    public List<T> queryForAllOrderby(Class<T> c, String orderByField1, String orderByField2, String orderByField3) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.orderByRaw(orderByField1 + " desc, " + orderByField2 + " desc, " + orderByField3 + " asc ;");
            return dao.query(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "queryForAllOrderby", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return new ArrayList<T>();
    }

    public List<T> queryForAllNotEqualOrderby(Class<T> c, String fieldName, Long value, String orderFieldName, boolean asc) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.orderBy(orderFieldName, asc);
            query.where().ne(fieldName, value);
            return dao.query(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "queryForAllOrderby", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return new ArrayList<T>();
    }

    public List<T> queryForAllOrderby(Class<T> c, String fieldName, Object value, String orderFieldName, boolean asc) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.orderBy(orderFieldName, asc);
            query.where().eq(fieldName, value);
            return dao.query(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "queryForAllOrderby", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return new ArrayList<T>();
    }

    public List<T> queryForAllOrderby(Class<T> c, HashMap<String, Object> fields, String orderFieldName, boolean asc, Long limit) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.orderBy(orderFieldName, asc);

            Iterator iter = fields.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry)iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                query.where().eq(key.toString(), val);
            }

            if (limit != null && limit > 0L) {
                query.limit(limit);
            }

            return dao.query(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "queryForAllOrderby", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return new ArrayList<T>();
    }

    public List<T> queryForAllIn(Class<T> c, String fieldName, Iterable<?> objects) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.where().in(fieldName, objects);
            return dao.query(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "query", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return null;
    }

    public List<T> queryForAllIn(Class<T> c, String fieldName, Iterable<?> objects, String orderFieldName, boolean asc) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.orderBy(orderFieldName, asc);
            query.where().in(fieldName, objects);
            return dao.query(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "query", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return null;
    }

    public List<T> queryForAll(Class<T> c, String fieldName, Object value) {

        try {
            Dao<T, Long> dao = getDao(c);
            return dao.queryForEq(fieldName, value);
        } catch (SQLException e) {
            Log.e("DbHelper", "queryForAll", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return new ArrayList<T>();
    }

    public T queryForKey(Class<T> c, String fieldName, Object value, String orderFieldName, boolean asc) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.orderBy(orderFieldName, asc);
            query.where().eq(fieldName, value);
            return dao.queryForFirst(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "queryForAllOrderby", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return null;
    }

    public T queryForFirst(Class<T> c, String orderFieldName, boolean asc) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.orderBy(orderFieldName, asc);
            return dao.queryForFirst(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "queryForAllOrderby", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return null;
    }

    public T queryForFirst(Class<T> c, HashMap<String, Object> fields, String orderFieldName, boolean asc) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();

            Where<T, Long> w = query.where();

            Iterator iter = fields.entrySet().iterator();
            int i = 0;
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry)iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (i == 0) {
                    w.eq(key.toString(), val);
                } else {
                    w.and();
                    w.eq(key.toString(), val);
                }
                i++;
            }

            query.orderBy(orderFieldName, asc);
            return dao.queryForFirst(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "queryForAllOrderby", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return null;
    }

    /**  */
    public T query(Class<T> c, String fieldName, Object value) {

        try {
            Dao<T, Long> dao = getDao(c);
            if (value instanceof String && !((String)value).contains("\"")) {
                value = ((String)value).replace("'", "''");
            }
            List<T> result = dao.queryForEq(fieldName, value);
            if (result != null && result.size() > 0)
                return result.get(0);
        } catch (SQLException e) {
            Log.e("DbHelper", "count", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return null;
    }

    public T query(Class<T> c, long id) {

        try {
            Dao<T, Long> dao = getDao(c);
            return dao.queryForId(id);
        } catch (SQLException e) {
            Log.e("DbHelper", "query", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return null;
    }

    public List<T> queryNotNullFieldOrderby(Class<T> c, String fieldName, String orderFieldName, boolean asc) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.where().isNotNull(fieldName);
            query.orderBy(orderFieldName, asc);
            return dao.query(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "query", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return null;
    }

    public List<T> queryNotNullFieldOrderbyIn(Class<T> c, String fieldName, Iterable<?> objects, String orderFieldName, boolean asc) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.where().isNotNull(fieldName).in(fieldName, objects);
            query.orderBy(orderFieldName, asc);
            return dao.query(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "query", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return null;
    }

    public List<T> queryNotNullField(Class<T> c, String fieldName) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.where().isNotNull(fieldName);
            return dao.query(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "query", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return null;
    }

    public List<T> queryNotNullField(Class<T> c, String fieldName, String orderByField1, String orderByField2, String orderByField3) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.where().isNotNull(fieldName);
            query.orderByRaw(orderByField1 + " desc , " + orderByField2 + " desc , " + orderByField3 + " asc;");
            return dao.query(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "query", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return null;
    }

    public List<T> queryNotNullField(Class<T> c, String fieldName, String orderByField1, String orderByField2, String orderByField3,
                                     String orderByField4, String fieldNotZero) {

        try {
            Dao<T, Long> dao = getDao(c);
            QueryBuilder<T, Long> query = dao.queryBuilder();
            query.where().isNotNull(fieldName).and().ne(fieldNotZero, 0);
            query.orderByRaw(orderByField1 + " desc , " + orderByField2 + " desc , " + orderByField3 + " desc , " + orderByField4 + " asc;");
            return dao.query(query.prepare());
        } catch (SQLException e) {
            Log.e("DbHelper", "query", e);
        } finally {
            // if (db != null)
            // db.close();
        }
        return null;
    }

    public static SQLiteHelperOrm getDB() {
        if (mSqLiteHelperOrm == null) {
            mSqLiteHelperOrm = new SQLiteHelperOrm();
        }

        return mSqLiteHelperOrm;
    }

    public static synchronized void close() {
        if (mSqLiteHelperOrm != null) {
            try {
                mSqLiteHelperOrm.close();
            } finally {
                mSqLiteHelperOrm = null;
            }

        }
    }
}