
package com.healthcare.modules.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.healthcare.R;
import com.healthcare.common.Constants;
import com.healthcare.common.utils.KineticSensorUtils;
import com.healthcare.modules.map.StepShowActivity;
import com.healthcare.modules.modle.DataQueue;
import com.healthcare.modules.modle.EventMessage;
import com.healthcare.modules.modle.StepsInfoQueue;
import com.healthcare.modules.modle.dataentity;
import com.healthcare.modules.service.predometer.IStepListener;
import com.healthcare.modules.service.predometer.LimitStepDetector;
import com.socks.library.KLog;

import de.greenrobot.event.EventBus;

/**
 * project healthcare
 *
 * @author hewei
 */

public class SensorRecodingService extends Service implements SensorEventListener {

    public static final int SENSORTYPE_STEPER = 4;
    public static final int SENSORTYPE_NA = 0;
    public static final int SENSORTYPE_ACCEL = 1;
    public static final int SENSORTYPE_GYRO = 2;
    public static final int SENSORTYPE_DECTER = 5;

    IBinder mBinder;
    SensorManager sensorManager;
    Sensor accelSensor, gyroSensor, stepCounter, stepDector;

    public float[] accelData;
    public float[] gyroData;
    public float[] angleData;
    private long previousTimestamp;

    private LimitStepDetector mLimitStepDetector;
    private DataQueue<dataentity> dataQueue;
    private StepsInfoQueue stepsInfoQueue;
    private int lastSteps;

    public class LoclBinder extends Binder {
        public SensorRecodingService getInstance() {
            return SensorRecodingService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new LoclBinder();
        accelData = new float[3];
        gyroData = new float[3];
        angleData = new float[3];
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        // 50HZ
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_FASTEST);

        mLimitStepDetector = new LimitStepDetector();
        stepsInfoQueue = new StepsInfoQueue();

        steps = 0;
        stepValue = 0f;
        dataQueue = new DataQueue<dataentity>(Constants.dataentityDao, 200);
    }

    public void addStepListener(IStepListener listener) {
        if (listener != null) {
            mLimitStepDetector.addStepListener(listener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (getSensorType(event.sensor)) {
            case SENSORTYPE_ACCEL:
                dealWithAccel(event);
                break;

            case SENSORTYPE_GYRO:
                updateAngleData(event);
                break;
            case SENSORTYPE_STEPER:
                KLog.d(event.values[0] + "");
                break;

            case SENSORTYPE_DECTER:
                break;
            default:
                break;
        }
    }

    private float stepValue;
    public int steps;

    private void dealWithAccel(SensorEvent event) {
        saveData(event);
        mLimitStepDetector.processMeasuring(event.timestamp, event.sensor.getType(), event.values);
        stepValue = mLimitStepDetector.getStepValue();
        steps = mLimitStepDetector.steps;

        if (lastSteps == 0) {
            lastSteps = steps;
        } else {
            if (steps > lastSteps) {
                // 保存数据库
                //stepsInfoQueue.put(event.timestamp, steps);
                ;
            }

            lastSteps = steps;
        }

        EventBus.getDefault().post(new EventMessage(0, 0, steps, stepValue));

        for (int i = 0; i < accelData.length; i++)
            accelData[i] = event.values[i];
    }

    public float getStepValue() {
        return stepValue;
    }


    private void saveData(SensorEvent event) {
        if (event != null && Constants.isRecording) {
            dataentity dataentity = null;

            if (event.values.length == 3) {
                dataentity = new dataentity(event.timestamp, event.values[0], event.values[1], event.values[2], event.sensor.getType(), event.accuracy, Constants.TAG);
            } else if (event.values.length == 2) {
                dataentity = new dataentity(event.timestamp, event.values[0], event.values[1], 0f, event.sensor.getType(), event.accuracy, Constants.TAG);
            } else if (event.values.length == 1) {
                dataentity = new dataentity(event.timestamp, event.values[0], 0f, 0f, event.sensor.getType(), event.accuracy, Constants.TAG);
            }

            if (dataentity != null) {
                dataQueue.add(dataentity);
            }
        }
    }


    private void updateAngleData(SensorEvent event) {
        if (previousTimestamp >= 0L) {
            // Get the change in time to use for integration
            float dt = (float) (event.timestamp - previousTimestamp) * KineticSensorUtils.NS2S;

            float[] gyroInput = new float[3];
            for (int i = 0; i < angleData.length; i++) {
                // Remove noise from samples
                gyroInput[i] = (float) KineticSensorUtils.gyroNoiseLimiter(event.values[i]);

                // Add the change in angle to angleData by integrating
                angleData[i] += (float) Math.toDegrees(gyroInput[i] * dt);

                // Bound the angle to 360 degrees
                angleData[i] = (float) KineticSensorUtils.boundTo360Degrees(angleData[i]);

                gyroData[i] = gyroInput[i];
            }
        }

        previousTimestamp = event.timestamp;
    }

    private int getSensorType(Sensor sensor) {
        if (sensor == accelSensor) {
            return SENSORTYPE_ACCEL;
        } else if (sensor == gyroSensor) {
            return SENSORTYPE_GYRO;
        } else if (sensor == stepCounter) {
            return SENSORTYPE_STEPER;
        } else if (sensor == stepDector) {
            return SENSORTYPE_DECTER;
        } else {
            return SENSORTYPE_NA;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // FOR FOREGROUND_ID
    int FORE_ID = 1335;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0,
                new Intent(this, StepShowActivity.class), 0);
        Notification notification = new Notification.Builder(this).setContentTitle("step is running")
                .setContentText("keep").setSmallIcon(R.drawable.location_marker)
                .setContentIntent(pendingintent).setAutoCancel(true).setOngoing(true)
                .build();
        startForeground(FORE_ID, notification);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
