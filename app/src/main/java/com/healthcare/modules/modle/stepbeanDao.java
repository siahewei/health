package com.healthcare.modules.modle;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.healthcare.modules.modle.stepbean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "STEPBEAN".
*/
public class stepbeanDao extends AbstractDao<stepbean, Void> {

    public static final String TABLENAME = "STEPBEAN";

    /**
     * Properties of entity stepbean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Day = new Property(0, String.class, "day", false, "DAY");
        public final static Property Bengin = new Property(1, Long.class, "bengin", false, "BENGIN");
        public final static Property End = new Property(2, Long.class, "end", false, "END");
        public final static Property Stepcount = new Property(3, Integer.class, "stepcount", false, "STEPCOUNT");
        public final static Property Source = new Property(4, Integer.class, "source", false, "SOURCE");
    };


    public stepbeanDao(DaoConfig config) {
        super(config);
    }
    
    public stepbeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"STEPBEAN\" (" + //
                "\"DAY\" TEXT," + // 0: day
                "\"BENGIN\" INTEGER," + // 1: bengin
                "\"END\" INTEGER," + // 2: end
                "\"STEPCOUNT\" INTEGER," + // 3: stepcount
                "\"SOURCE\" INTEGER);"); // 4: source
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STEPBEAN\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, stepbean entity) {
        stmt.clearBindings();
 
        String day = entity.getDay();
        if (day != null) {
            stmt.bindString(1, day);
        }
 
        Long bengin = entity.getBengin();
        if (bengin != null) {
            stmt.bindLong(2, bengin);
        }
 
        Long end = entity.getEnd();
        if (end != null) {
            stmt.bindLong(3, end);
        }
 
        Integer stepcount = entity.getStepcount();
        if (stepcount != null) {
            stmt.bindLong(4, stepcount);
        }
 
        Integer source = entity.getSource();
        if (source != null) {
            stmt.bindLong(5, source);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public stepbean readEntity(Cursor cursor, int offset) {
        stepbean entity = new stepbean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // day
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // bengin
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // end
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // stepcount
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4) // source
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, stepbean entity, int offset) {
        entity.setDay(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setBengin(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setEnd(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setStepcount(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setSource(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(stepbean entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(stepbean entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}