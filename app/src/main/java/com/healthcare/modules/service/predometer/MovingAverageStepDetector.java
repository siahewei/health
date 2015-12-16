/*
package com.healthcare.modules.service.predometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import com.socks.library.KLog;

*/
/**
 * MovingAverageStepDetector class, step detection filter based on two moving averages
 * with minimum signal power threshold
 *
 * @author Michal Holcik
 *//*

public class MovingAverageStepDetector extends StepDetector {

    private float[] maValues;
    private MovingAverageTD[] ma;
    private CumulativeSignalPowerTD asp;
    private boolean mMASwapState;
    private boolean stepDetected;
    private boolean signalPowerCutoff;
    private long mLastStepTimestamp;
    private double strideDuration;

    private static final long SECOND_IN_NANOSECONDS = (long) Math.pow(10, 9);
    public static final double MA1_WINDOW = 0.15;
    public static final double MA2_WINDOW = 5 * MA1_WINDOW;
    private static final long POWER_WINDOW = SECOND_IN_NANOSECONDS / 10;
    private static final double MAX_STRIDE_DURATION = 2.0; // in seconds
    private static final double MIN_STRIDE_DURATION = 0.2;
    public static final double STEP_LIMIT = 0.5;
    private Kalman kalman;

    public int steps = 0;
    private float stepValue = 0;

    public static final float POWER_CUTOFF_VALUE = 1000.0f;

    private double mWindowMa1;
    private double mWindowMa2;
    private long mWindowPower;
    private static float mPowerCutoff;

    public MovingAverageStepDetector() {
        this(MA1_WINDOW, MA2_WINDOW, POWER_CUTOFF_VALUE);
        KLog.e("life", "MovingAverageStepDetector contuct");
    }
    public MovingAverageStepDetector(double windowMa1, double windowMa2, double powerCutoff) {

        mWindowMa1 = windowMa1;
        mWindowMa2 = windowMa2;
        mPowerCutoff = (float) powerCutoff;

        maValues = new float[4];
        mMASwapState = true;
        ma = new MovingAverageTD[]{new MovingAverageTD(mWindowMa1), new MovingAverageTD(mWindowMa1), new MovingAverageTD(mWindowMa2)};
        asp = new CumulativeSignalPowerTD();
        stepDetected = false;
        signalPowerCutoff = true;

        initPramaters();
    }

    public class MovingAverageStepDetectorState {
        float[] values;
        public boolean[] states;
        double duration;

        MovingAverageStepDetectorState(float[] values, boolean[] states, double duration) {
            this.values = values;
            this.states = states;
        }
    }

    public MovingAverageStepDetectorState getState() {
        return new MovingAverageStepDetectorState(new float[]{maValues[0],
                maValues[1], maValues[2], maValues[3]}, new boolean[]{
                stepDetected, signalPowerCutoff}, strideDuration);
    }

    public float getPowerThreshold() {
        return mPowerCutoff;
    }


    private int lastState = 0;  // down
    private float lastValue = 0;
    private long lastTime = 0;
    private int continuePredo = 0;
    private boolean isLongInterval = false;

    public float processAccelerometerValue2(long timestamp, float[] values) {

        float value = (float) Math.sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2]);
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
                if ((timestamp - lastTime) < 250000000 || lastValue < 0.6){
                    stepDetected = false;
                }else {
                    stepDetected = true;
                    // 如果发现中间有间隔2.5s 没有动作，认为是干扰
                    if ((timestamp - lastTime) > 2500000000L) {
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


    public float processAccelerometerValues(long timestamp, float[] values) {

        float value = (float) Math.sqrt(values[0] * values[0] + values[1] * values[1] + values[2] * values[2]);

        // compute moving averages
        maValues[0] = value;
        for (int i = 1; i < 3; i++) {
            ma[i].push(timestamp, value);
            maValues[i] = (float) ma[i].getAverage();
            value = maValues[i];
        }

        // detect moving average crossover
        stepDetected = false;
        boolean newSwapState = maValues[1] > maValues[2] && maValues[1] > STEP_LIMIT;
        if (newSwapState != mMASwapState) {
            mMASwapState = newSwapState;
            if (mMASwapState) {
                stepDetected = true;
            }
        }

        // compute signal power
        asp.push(timestamp, maValues[1] - maValues[2]);
        // maValues[3] = (float)sp.getPower();
        maValues[3] = (float) asp.getValue();
        signalPowerCutoff = maValues[3] < mPowerCutoff;

        if (stepDetected) {
            asp.reset();
        }

        // step event
        if (stepDetected && !signalPowerCutoff) {

            final long strideDuration = getStrideDuration(timestamp);

            float duration = ((float) strideDuration) / SECOND_IN_NANOSECONDS;

            KLog.i("ration", "duration:" + duration);

            if (MIN_STRIDE_DURATION < duration && duration < MAX_STRIDE_DURATION) {
                KLog.i("logout, 1");
            } else {
                signalPowerCutoff = true;
            }

            mLastStepTimestamp = timestamp;

        }

        return maValues[1];
    }

    */
/*
     * @return stride duration
     *//*

    private long getStrideDuration(long currentTimestamp) {
        if (mLastStepTimestamp == 0) {
            mLastStepTimestamp = currentTimestamp;
        }

        return currentTimestamp - mLastStepTimestamp;
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
                    float[] output = {(float) out[0], (float) out[1], (float) out[2]};
                    stepValue = processAccelerometerValue2(timeStamp, output);

                    if (getState().states[0]) {
                        */
/*steps++;*//*

                        if (!isLongInterval){
                            steps ++;
                        }else if (continuePredo == 8) {
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
*/
