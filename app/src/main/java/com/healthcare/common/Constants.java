package com.healthcare.common;

import com.healthcare.common.utils.DevUtils;
import com.healthcare.modules.modle.DaoMaster;
import com.healthcare.modules.modle.DaoSession;
import com.healthcare.modules.modle.dataentityDao;
import com.healthcare.modules.modle.kineticsDao;
import com.healthcare.modules.modle.locusDao;
import com.healthcare.modules.modle.stepbeanDao;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/1
 */
public class Constants {

    public static final int SAMPLING_RATE = 3000;
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "trails";

    public static boolean isRecording = false;

    public static final int DEVICE_WIDTH = DevUtils.getScreenWidth();
    public static final int DEVICE_HEIGHT = DevUtils.getScreenHeight();

    public static DaoMaster daoMaster;
    public static DaoSession daoSession;
    public static locusDao locusDao;
    public static kineticsDao kineticsDao;
    public static dataentityDao dataentityDao;
    public static stepbeanDao stepbeanDao;

    public static boolean isAlgorithmRecordingStep = false;

    public static String TAG;




}
