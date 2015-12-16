package com.healthcare.modules.service.predometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/15
 */
public class LimitStepDetector extends StepDetector{
    private boolean stepDetected;

    private static final long SECOND_IN_NANOSECONDS = (long) Math.pow(10, 9);
    public static final double STEP_LIMIT = 0.6;
    public static final long TIME_LOW_LIMIT = 250000000;
    public static final long TIME_HIGH_LIMIT = 2500000000L;
    public static final long CONTINU_STEP = 8;

    private Kalman kalman;

    public int steps = 0;
    private float stepValue = 0;


    public LimitStepDetector() {
        stepDetected = false;
        initPramaters();
    }

    private int lastState = 0;  // down
    private float lastValue = 0;
    private long lastTime = 0;
    private int continuePredo = 0;
    private boolean isLongInterval = false;

    public float processAccelerometerValue2(long timestamp, double[] values) {

        float value = (float) valueVector(values);
        stepDetected = false;

        // compute moving averages
        if (value < 0.5){
            return value;
        }

        if (lastValue == 0){
            lastValue = value;
            return value;
        }

        int state = -1;
        if (value > lastValue){
            state = 1;
        }else {
            state = 0;
        }

        if (state != lastState && state == 0){
            if (lastTime == 0){
                lastTime = timestamp;
                stepDetected = true;
            }else {
                if ((timestamp - lastTime) < TIME_LOW_LIMIT || lastValue < STEP_LIMIT){
                    stepDetected = false;
                }else {
                    stepDetected = true;
                    // 如果发现中间有间隔2.5s 没有动作，认为是干扰
                    if ((timestamp - lastTime) > TIME_HIGH_LIMIT) {
                        isLongInterval = true;
                        continuePredo = 0;
                    }else{
                        continuePredo += 1;
                    }

                    lastTime = timestamp;
                }
            }
        }

        lastState = state;
        lastValue = value;
        return value;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                processMeasuring(event.timestamp, event.sensor.getType(), event.values);
            }
        }
    }

    private long accelLastTimeStamp;
    private double[] gyroAccelVector;
    private double[] linAccel;


    public void processMeasuring(long timeStamp, int sensorType, float values[]) {
        double dValues[] = new double[3];
        dValues[0] = values[0];
        dValues[1] = values[1];
        dValues[2] = values[2];

        if (sensorType == 1) {
            if (accelLastTimeStamp > 0) {
                double accelLength = valueVector(dValues);
                if ((accelLength > gravityNoMotionLowLimit) && (accelLength < gravityNoMotionHighLimit)) {
                    if (gyroAccelVector == null) {
                        gyroAccelVector = new double[3];
                    }

                    copyVector(gyroAccelVector, dValues);
                    linAccel = vectorSub(dValues, gyroAccelVector);
                } else {
                    if (gyroAccelVector != null) {
                        linAccel = vectorSub(dValues, gyroAccelVector);
                    }
                }

                if (linAccel != null) {
                    kalman.Predict();
                    Matrix accelMatrix = new Matrix(linAccel, 3);

                    double[] out = kalman.Correct(accelMatrix).getRowPackedCopy();
                    double[] output = {out[0], out[1],  out[2]};
                    stepValue = processAccelerometerValue2(timeStamp, output);

                    if (stepDetected) {
                        /*steps++;*/
                        if (!isLongInterval){
                            steps ++;
                        }else if (continuePredo == CONTINU_STEP) {
                            steps += continuePredo;
                            continuePredo = 0;
                            isLongInterval = false;
                        }

                        //KLog.e("steps:" + (steps) + ",stepvalue:" + stepValue);
                    }
                }
            }
        }

        accelLastTimeStamp = timeStamp;
    }

    public float getStepValue() {
        return stepValue;
    }

    private double valueVector(double vec[]) {
        if (vec.length != 3) {
            return 0f;
        } else {
            return Math.sqrt((vec[0] * vec[0]) + (vec[1] * vec[1]) + (vec[2] * vec[2]));
        }
    }

    private void copyVector(double[] dest, double[] source) {
        dest[0] = source[0];
        dest[1] = source[1];
        dest[2] = source[2];
    }

    private double[] vectorSub(double[] v1, double[] v2) {
        double ret[] = new double[3];

        ret[0] = v1[0] - v2[0];
        ret[1] = v1[1] - v2[1];
        ret[2] = v1[2] - v2[2];
        return ret;
    }

    double gravityNoMotionLowLimit;
    double gravityNoMotionHighLimit;
    float GRAVITY = 9.806f;
    float NOMOTION_ACCEL_LIMIT = 0.1f;

    private void initPramaters() {
        gravityNoMotionLowLimit = GRAVITY * (1.0 - NOMOTION_ACCEL_LIMIT);
        gravityNoMotionHighLimit = GRAVITY * (1.0 + NOMOTION_ACCEL_LIMIT);
        kalman = new Kalman(3, 3);
    }
}
