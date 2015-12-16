package com.healthcare.modules.modle;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.healthcare.HealthCareApplication;
import com.healthcare.common.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/14
 */
public class SqliteHelper {

    private Context mContext;
    private DbHelper mDbHelper;
    private SQLiteDatabase  mDb;

    private static SqliteHelper instance;

    public static SqliteHelper getInstance(){
        if (instance == null){
            synchronized (SqliteHelper.class){
                instance = new SqliteHelper();
            }
        }

        return instance;
    }

    private SqliteHelper() {
        mContext = HealthCareApplication.getInstance();
        mDbHelper = new DbHelper(mContext, Constants.DB_NAME, null, Constants.DB_VERSION);
        mDb = mDbHelper.getReadableDatabase();
    }

    public void close(){
        mDbHelper.close();
    }

    public final static String QUERY_DATA_ITEM = "SELECT rowid, COUNT(TAG) FROM " + dataentityDao.TABLENAME
                                                    + " GROUP BY " + "TAG";


    public List<DataItem> getDataItemList(){

        Cursor cursor = mDb.query(dataentityDao.TABLENAME, new String[]{"TAG", "COUNT(TAG) AS count"}, null, null, "TAG", null, null);

        List<DataItem> dataItems = null;

        if (cursor.moveToFirst()){
            dataItems = new ArrayList<DataItem>();
            for (int i = 0; i < cursor.getCount(); i ++){
                dataItems.add(new DataItem(cursor.getString(cursor.getColumnIndex("TAG")), cursor.getInt(cursor.getColumnIndex("count"))));
                cursor.moveToNext();
            }
        }

        return dataItems;
    }



    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
