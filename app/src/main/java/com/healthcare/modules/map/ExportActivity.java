package com.healthcare.modules.map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.healthcare.R;
import com.healthcare.common.Constants;
import com.healthcare.common.utils.DisplayUtils;
import com.healthcare.common.utils.FileUtils;
import com.healthcare.common.utils.JumpUtils;
import com.healthcare.common.utils.TaskExcuteUtil;
import com.healthcare.common.widget.CstDialog;
import com.healthcare.modules.modle.DataItem;
import com.healthcare.modules.modle.dataentity;
import com.healthcare.modules.modle.dataentityDao;
import com.socks.library.KLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/8
 */
public class ExportActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView content;
    private Button btnDelAll;
    private Button btnRefresh;
    private Button btnExport;

    private List<dataentity> dataentities;

    private List<DataItem> dataItems;
    private DataAdapter dataAdapter;
    private Handler handler = new Handler();
    private ProgressBar progressBar;
    private CstDialog cstDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export_activity);
        init();
    }


    public void init() {
        content = (ListView) findViewById(R.id.content);
        btnDelAll = (Button) findViewById(R.id.btn_dele);
        btnRefresh = (Button) findViewById(R.id.btn_refresh);
        btnExport = (Button) findViewById(R.id.btn_export);

        progressBar = (ProgressBar) findViewById(R.id.export_progress);

        btnDelAll.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnExport.setOnClickListener(this);

        dataItems = new ArrayList<DataItem>();
        dataentities = new ArrayList<dataentity>();
        dataAdapter = new DataAdapter(dataItems, this);

        content.setDivider(new ColorDrawable(Color.parseColor("#eceff1")));
        content.setDividerHeight(DisplayUtils.dip2px(10));

        content.setAdapter(dataAdapter);
        content.setOnItemClickListener(this);
        getDataItems();

        cstDialog = new CstDialog(this);
        cstDialog.setView(true, false, new String[]{"取消", "删除", "导出"}, "");
        cstDialog.setCstDialogOnClickListener(cstDialogOnClickListener);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refresh:
                getDataItems();
                break;
            case R.id.btn_dele:
                dataItems.clear();
                Constants.dataentityDao.deleteAll();
                if (dataAdapter != null) {
                    dataAdapter.notifyDataSetChanged();
                }
                break;

            case R.id.btn_export:
                export();
                break;
        }
    }

    public void getDataItems() {
        dataItems.clear();

        handler.post(new Runnable() {
            @Override
            public void run() {
                List<DataItem> tmpdataItems = DataItem.loadDataItems();

                if (tmpdataItems != null && !tmpdataItems.isEmpty()) {
                    dataItems.addAll(tmpdataItems);
                }

                if (dataAdapter != null) {
                    dataAdapter.notifyDataSetChanged();
                }

            }
        });
    }


    public void export() {
        TaskExcuteUtil.getInstance().excute(new Runnable() {
            @Override
            public void run() {

                if (clkDataItem != null) {
                    exportFile(clkDataItem.getTag());
                }
            }
        });
    }

    public void exportFile(String tag) {
        if (tag.equals("all")) {
            dataentities.clear();
            dataentities.addAll(Constants.dataentityDao.loadAll());
            writeFile("all");
        } else {
            dataentities.clear();
            List<dataentity> tmpList = Constants.dataentityDao.queryBuilder().where(dataentityDao.Properties.Tag.eq(tag)).list();
            dataentities.addAll(tmpList);
            writeFile(tag);
        }

    }


    public void writeFile(String fileName) {

        File file = new File(FileUtils.directory, fileName);
        File filePath = file.getParentFile();
        final int counter = dataentities.size();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setMax(counter);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
            }
        });

        if (!filePath.isDirectory() && !filePath.mkdir()) {
            KLog.e("SensorDashbaord", "Could not create directory for log files");
            throw new RuntimeException("could not create directory");
        }

        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fileWriter);

            for (int i = 0; i < counter; i++) {

                final int progress = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(progress);
                    }
                });

                bw.write(dataentities.get(i).getCvsFormat());
            }

            bw.flush();
            bw.close();


            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("*/*");

            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    "SensorDashboard data export");
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            startActivity(Intent.createChooser(emailIntent, "Send data..."));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int clk = (int) id;

        if (clk < dataItems.size() && clk >= 0) {
            clkDataItem = dataItems.get(clk);
            if (clkDataItem != null) {
                cstDialog.setTitle("删除或者导出" + clkDataItem.getTag() + "?");
                cstDialog.show();
            }
        }
    }

    private DataItem clkDataItem;

    private CstDialog.CstDialogOnClickListener cstDialogOnClickListener = new CstDialog.CstDialogOnClickListener() {
        @Override
        public void onClickSure() {
            export();
        }

        @Override
        public void onClickCancel() {
        }

        @Override
        public void onOther() {
            delteItems();
        }
    };


    private void delteItems() {
        String tag = null;
        if (clkDataItem != null) {
            tag = clkDataItem.getTag();
        }

        if (TextUtils.isEmpty(tag)) {
            return;
        }

        final String finalTag = tag;
        handler.post(new Runnable() {
            @Override
            public void run() {
                DeleteQuery<dataentity> bd= Constants.dataentityDao.queryBuilder().where(dataentityDao.Properties.Tag.eq(finalTag)).buildDelete();
                bd.executeDeleteWithoutDetachingEntities();
                Toast.makeText(ExportActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            }
        });

        getDataItems();
    }

    @Override
    public void onBackPressed() {
        Intent itent = new Intent();
        setResult(JumpUtils.EXPORT_STEP, itent);
        super.onBackPressed();

    }
}
