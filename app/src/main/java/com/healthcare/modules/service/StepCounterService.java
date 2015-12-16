/*
package com.healthcare.modules.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import com.socks.library.KLog;

*/
/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/7
 *//*

public class StepCounterService extends Service{


    private SensorManager sensorManager;
    private StepDetector stepDetector;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    private StepBinder binder;


    public class StepBinder extends Binder{
        public StepCounterService getInstance(){
            return StepCounterService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        binder = new StepBinder();

        stepDetector = new StepDetector(this);
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(stepDetector, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);

        powerManager = (PowerManager) this
                .getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "S");
        wakeLock.acquire();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (stepDetector != null) {
            sensorManager.unregisterListener(stepDetector);
        }

        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    public int getDeltaStep(){
        if (stepDetector != null){
            return stepDetector.getDeltaStep();
        }else {
            KLog.e("step detector is null");

            return 0;
        }
    }
}
*/
