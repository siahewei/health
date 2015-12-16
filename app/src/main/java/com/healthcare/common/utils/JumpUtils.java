package com.healthcare.common.utils;

import android.app.Activity;
import android.content.Intent;

import com.healthcare.modules.map.ExportActivity;
import com.healthcare.modules.map.LocusActivity;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/8
 */
public class JumpUtils {

    public static final int STEP_EXPORT = 1;
    public static final int EXPORT_STEP = 2;

    public static void JumpToExportActvity(Activity activity){
        if(activity != null){

            Intent intent = new Intent(activity, ExportActivity.class);
            activity.startActivityForResult(intent, JumpUtils.STEP_EXPORT);
        }
    }

    public static void toLocusActivity(Activity activity){
        if(activity != null){

            Intent intent = new Intent(activity, LocusActivity.class);
            activity.startActivity(intent);
        }
    }

}
