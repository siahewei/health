package com.healthcare.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.healthcare.HealthCareApplication;

/**
 * SharedPreferences工具类
 *
 * 作者：xjzhao
 * 时间：2015-02-20 上午11:14
 */
public class PreferencesUtils {

	public static void setPreferences(String preference, String key, boolean value){
        Context context = HealthCareApplication.getInstance();
        if(context == null) return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    
    public static void setPreferences(String preference, String key, long value){
        Context context = HealthCareApplication.getInstance();
        if(context==null) return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }
    
    public static void setPreferences(String preference, String key, String value){
        Context context = HealthCareApplication.getInstance();
        if(context==null) return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    
    public static String getPreference(String preference, String key, String defaultValue){
        Context context = HealthCareApplication.getInstance();
        if(context==null) return null;
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }
    
    public static void setPreferences(String preference, String key, int value){
        Context context = HealthCareApplication.getInstance();
    	if(context==null) return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    
    public static boolean getPreference(String preference, String key, boolean defaultValue){
        Context context = HealthCareApplication.getInstance();
    	if(context==null) return defaultValue;
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }
    
    public static long getPreference(String preference, String key, long defaultValue){
        Context context = HealthCareApplication.getInstance();
    	if(context==null) return -1;
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }
    
    public static int getPreference(String preference, String key, int defaultValue){
        Context context = HealthCareApplication.getInstance();
    	if(context==null) return -1;
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }
    
    public static void clearPreference(String preference){
        Context context = HealthCareApplication.getInstance();
        if(context==null) return;
    	SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
    	Editor editor = sharedPreferences.edit();
    	editor.clear();
    	editor.commit();
    }
}