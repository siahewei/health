package com.healthcare.common.utils;

import android.database.sqlite.SQLiteDatabase;

import com.healthcare.HealthCareApplication;
import com.healthcare.common.Constants;
import com.healthcare.modules.modle.DaoMaster;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/2
 */
public class InitUtils {

    public static void initTooslsConfig(){
        // db
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(HealthCareApplication.getInstance(), Constants.DB_NAME, null);
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        Constants.daoMaster = new DaoMaster(db);
        Constants.daoSession = Constants.daoMaster.newSession();
        Constants.locusDao = Constants.daoSession.getLocusDao();
        Constants.kineticsDao = Constants.daoSession.getKineticsDao();
        Constants.dataentityDao = Constants.daoSession.getDataentityDao();
        Constants.stepbeanDao = Constants.daoSession.getStepbeanDao();
        Constants.isRecording = ConstPreferceUtils.getRecordingFlag();
        Constants.TAG = ConstPreferceUtils.getTagName();
    }

}
