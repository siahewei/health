package com.healthcare.modules.modle;

import com.healthcare.common.Constants;
import com.healthcare.common.utils.TaskExcuteUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/15
 */
public class StepsInfoQueue {

    private static final int DATA_FROM_ALGORITHM = 1;
    private static final long TIME_INTERVAL_LIMIT = 60 * 1 * 1000000000L; // every 6 minutes

    private int lastRecordSteps = 0;    // 上次写数据库的步数
    private long lastRecordTime = 0;    // 上次写数据库的时间

    private int lastSteps = 0;          //
    private long lastTimeStamp = 0;     //
    private int dataSource;

    public StepsInfoQueue() {
        this(DATA_FROM_ALGORITHM);
    }

    public StepsInfoQueue(int dataSource) {
        this.dataSource = DATA_FROM_ALGORITHM;
    }

    public void put(long timeStamp, int steps) {

        if (lastTimeStamp == 0) {
            lastTimeStamp = timeStamp;
            lastSteps = steps;
            lastRecordTime = timeStamp;
            return;
        }

        if (timeStamp - lastTimeStamp > TIME_INTERVAL_LIMIT) {
            saveData(lastRecordTime, lastTimeStamp, lastSteps - lastRecordSteps);
            lastRecordTime = timeStamp;
            lastRecordSteps = lastSteps;
        }

        lastTimeStamp = timeStamp;
        lastSteps = steps;
    }

    private void saveData(final long beginTime, final long endTime, final int deltaSteps) {

        TaskExcuteUtil.getInstance().excute(new Runnable() {
            @Override
            public void run() {
                stepbean stepbean = new stepbean(getDay(beginTime), beginTime, endTime, deltaSteps, dataSource);
                Constants.stepbeanDao.insert(stepbean);
            }
        });

    }

    public String getDay(long timeStamp) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return  sdf.format(new Date(timeStamp / 1000000));
    }
}
