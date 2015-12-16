package com.healthcare.modules.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.healthcare.R;
import com.healthcare.common.Constants;
import com.healthcare.common.utils.ConstPreferceUtils;
import com.healthcare.common.utils.JumpUtils;
import com.healthcare.common.widget.CircleBar;
import com.healthcare.common.widget.CstDialog;
import com.healthcare.modules.modle.EventMessage;
import com.healthcare.modules.service.SensorRecodingService;
import com.socks.library.KLog;

import de.greenrobot.event.EventBus;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/11
 */
public class StepShowActivity extends Activity{

    private CircleBar circleBar;
    private Button locusButton;
    private Button switchBtn;
    private Button exportBtn;
    private CstDialog cstDialog;

    int initsteps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.step_show_activity);
        circleBar = (CircleBar) findViewById(R.id.progress_step);
        locusButton = (Button) findViewById(R.id.btn_show_locus);
        switchBtn= (Button) findViewById(R.id.btn_switch);
        exportBtn = (Button) findViewById(R.id.btn_export);
        initsteps = 0;
        circleBar.setText(10000);
        circleBar.setProgress(initsteps, 0);

        Intent intent = new Intent(this, SensorRecodingService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);

        EventBus.getDefault().register(this);

        locusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtils.toLocusActivity(StepShowActivity.this);
            }
        });

        showStatus();

        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Constants.isRecording = !Constants.isRecording;
                if (!Constants.isRecording){
                    openTag();
                }else {
                    Constants.isRecording = false;
                    showStatus();
                    ConstPreferceUtils.setRecordingFlag(Constants.isRecording);
                }
            }
        });

        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.isRecording = false;
                JumpUtils.JumpToExportActvity(StepShowActivity.this);
            }
        });

    }


    private CstDialog.CstDialogOnClickListener cstDialogOnClickListener = new CstDialog.CstDialogOnClickListener() {
        @Override
        public void onClickSure() {
            if (!TextUtils.isEmpty(cstDialog.getEiditText())){
                Constants.isRecording = true;
                ConstPreferceUtils.setRecordingFlag(Constants.isRecording);
                Constants.TAG = cstDialog.getEiditText();
                ConstPreferceUtils.setTagName(Constants.TAG);
                showStatus();
            }
            cstDialog.dismiss();

        }

        @Override
        public void onClickCancel() {
            cstDialog.dismiss();
        }

        @Override
        public void onOther() {

        }
    };

    private void openTag(){
        if(cstDialog == null){
            cstDialog = new CstDialog(StepShowActivity.this);
            cstDialog.setView(false, true, new String[]{"取消", "确认"}, "设置tag名");
            cstDialog.setEditText(Constants.TAG);

        }

        cstDialog.setCstDialogOnClickListener(cstDialogOnClickListener);
        cstDialog.show();
    }

    private void showStatus(){
        if (Constants.isRecording){
            switchBtn.setText("关闭log");
        }else {
            switchBtn.setText("打开log");
        }
    }

    public void onEventMainThread(EventMessage event) {
        if (event != null){
            circleBar.setProgress(event.getSteps(), event.getStepValue());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        KLog.d("data:" + requestCode + "," + resultCode);

        if (requestCode == JumpUtils.STEP_EXPORT && resultCode == JumpUtils.EXPORT_STEP){
            Constants.isRecording = ConstPreferceUtils.getRecordingFlag();
        }
    }
}
