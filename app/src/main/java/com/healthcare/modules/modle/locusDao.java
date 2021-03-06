package com.healthcare.modules.modle;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.healthcare.modules.modle.locus;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "LOCUS".
*/
public class locusDao extends AbstractDao<locus, Long> {

    public static final String TABLENAME = "LOCUS";

    /**
     * Properties of entity locus.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Longtitude = new Property(1, Double.class, "longtitude", false, "LONGTITUDE");
        public final static Property Lattitude = new Property(2, Double.class, "lattitude", false, "LATTITUDE");
        public final static Property Accuracy = new Property(3, Float.class, "accuracy", false, "ACCURACY");
        public final static Property Date = new Property(4, java.util.Date.class, "date", false, "DATE");
        public final static Property UserName = new Property(5, String.class, "userName", false, "USER_NAME");
    };


    public locusDao(DaoConfig config) {
        super(config);
    }
    
    public locusDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"LOCUS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"LONGTITUDE\" REAL," + // 1: longtitude
                "\"LATTITUDE\" REAL," + // 2: lattitude
                "\"ACCURACY\" REAL," + // 3: accuracy
                "\"DATE\" INTEGER," + // 4: date
                "\"USER_NAME\" TEXT);"); // 5: userName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"LOCUS\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, locus entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Double longtitude = entity.getLongtitude();
        if (longtitude != null) {
            stmt.bindDouble(2, longtitude);
        }
 
        Double lattitude = entity.getLattitude();
        if (lattitude != null) {
            stmt.bindDouble(3, lattitude);
        }
 
        Float accuracy = entity.getAccuracy();
        if (accuracy != null) {
            stmt.bindDouble(4, accuracy);
        }
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(5, date.getTime());
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(6, userName);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public locus readEntity(Cursor cursor, int offset) {
        locus entity = new locus( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getDouble(offset + 1), // longtitude
            cursor.isNull(offset + 2) ? null : cursor.getDouble(offset + 2), // lattitude
            cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3), // accuracy
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // date
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // userName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, locus entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLongtitude(cursor.isNull(offset + 1) ? null : cursor.getDouble(offset + 1));
        entity.setLattitude(cursor.isNull(offset + 2) ? null : cursor.getDouble(offset + 2));
        entity.setAccuracy(cursor.isNull(offset + 3) ? null : cursor.getFloat(offset + 3));
        entity.setDate(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setUserName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(locus entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(locus entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
