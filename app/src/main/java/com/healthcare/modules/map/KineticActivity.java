
package com.healthcare.modules.map;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.healthcare.R;
import com.healthcare.common.Constants;
import com.healthcare.common.utils.JumpUtils;
import com.healthcare.modules.service.SensorRecodingService;
import com.socks.library.KLog;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/5
 */

public class KineticActivity extends Activity implements View.OnClickListener{

    private SensorRecodingService sensorRecodingService;

    private Handler handler = new Handler();


    private TextView tvAngle1;
    private TextView tvAngle2;
    private TextView tvAngle3;
    private TextView tvaccel1;
    private TextView tvaccel2;
    private TextView tvaccel3;
    private TextView tvGyro1;
    private TextView tvGyro2;
    private TextView tvGyro3;

    private Button btnExport;
    private Button btnStart;
    private Button btnStop;
    private EditText etTag;
    private TextView tvStep;
    private TextView tvStepValue;




    public static final long UI_UPDATE_DELAY = (long)(0.25 * 1000); // in seconds


    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            SensorRecodingService.LoclBinder loclBinder = (SensorRecodingService.LoclBinder) service;
            sensorRecodingService = loclBinder.getInstance();
            //sensorRecodingService.addStepListener(stepListener);

            KLog.d("service connect");
            isBoundService = true;
            recordingThread.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBoundService = false;
            sensorRecodingService = null;
            KLog.d("Service Disconnected");

        }
    };


    private boolean isBoundService = false;

    private SensorRecordingThread recordingThread = new SensorRecordingThread();

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_begin:
                if (etTag.getText() != null && !TextUtils.isEmpty(etTag.getText().toString())){
                    Constants.TAG = etTag.getText().toString();
                }
                Constants.isRecording = true;
                break;

            case R.id.btn_stop:
                Constants.isRecording = false;
                break;

            case R.id.btn_export:
                Constants.isRecording = false;
                JumpUtils.JumpToExportActvity(this);
                break;
        }
    }

    class SensorRecordingThread extends Thread{

        @Override
        public void run() {

            while (isBoundService){
                //sensorRecodingService.accelData
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                    }
                });

                try {
                    Thread.sleep(UI_UPDATE_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kinetic_activity);
        init();

        // Bind to (and start) recording service
        Intent intent = new Intent(this, SensorRecodingService.class);
        bindService(intent, connection, BIND_AUTO_CREATE); //

    }

    private void init(){
        tvaccel1 = (TextView) findViewById(R.id.accel1);
        tvaccel2 = (TextView) findViewById(R.id.accel2);
        tvaccel3 = (TextView) findViewById(R.id.accel3);

        tvAngle1 = (TextView) findViewById(R.id.angle1);
        tvAngle2 = (TextView) findViewById(R.id.angle2);
        tvAngle3 = (TextView) findViewById(R.id.angle3);

        tvGyro1 = (TextView) findViewById(R.id.gyro1);
        tvGyro2 = (TextView) findViewById(R.id.gyro2);
        tvGyro3 = (TextView) findViewById(R.id.gyro3);

        btnExport = (Button) findViewById(R.id.btn_export);
        etTag = (EditText) findViewById(R.id.tag_input);
        btnStart = (Button) findViewById(R.id.btn_begin);
        btnStop = (Button) findViewById(R.id.btn_stop);

        tvStepValue = (TextView)findViewById(R.id.tvstep_value);
        tvStep = (TextView)findViewById(R.id.tvsteps);

        btnExport.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);


    }

    private void refresh(){
        tvaccel1.setText("acce x: " + Float.toString(sensorRecodingService.accelData[0]));
        tvaccel2.setText("acce y: " + Float.toString(sensorRecodingService.accelData[1]));
        tvaccel3.setText("acce z: " + Float.toString(sensorRecodingService.accelData[2]));

        // Update gyro values
        tvGyro1.setText("gyro x: " + Float.toString(sensorRecodingService.gyroData[0]));
        tvGyro2.setText("gyro y: " + Float.toString(sensorRecodingService.gyroData[1]));
        tvGyro3.setText("gyro z: " + Float.toString(sensorRecodingService.gyroData[2]));

        // Update gyro values
        tvAngle1.setText("angle x: " + Float.toString(sensorRecodingService.angleData[0]));
        tvAngle2.setText("angle y: " + Float.toString(sensorRecodingService.angleData[1]));
        tvAngle3.setText("angle z: " + Float.toString(sensorRecodingService.angleData[2]));

        tvStepValue.setText("stepvalue: " + Float.toString(sensorRecodingService.getStepValue()));
        tvStep.setText("步数："+Integer.toString(sensorRecodingService.steps));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        safelyUnbind();
    }

    int step = 0;




    private void safelyUnbind(){
        if (isBoundService) {
            unbindService(connection);
            isBoundService = false;
        }
    }
}
