package com.healthcare;

import android.app.Application;
import android.content.Context;

import com.healthcare.common.utils.InitUtils;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/2
 */
public class HealthCareApplication extends Application{

    public static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        HealthCareApplication.instance = this;
        InitUtils.initTooslsConfig();
    }

    public static Context getInstance(){
        return instance;
    }
}
