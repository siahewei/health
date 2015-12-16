package com.healthcare.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.healthcare.HealthCareApplication;

import java.lang.reflect.Field;
/*
IMSI是国际移动用户识别码的简称(International Mobile Subscriber Identity)
IMSI共有15位，其结构如下：
                    MCC+MNC+MIN
                    MCC：Mobile Country Code，移动国家码，共3位，中国为460;
                    MNC:Mobile NetworkCode，移动网络码，共2位

在中国，移动的代码为电00和02，联通的代码为01，电信的代码为03,合起来就是（也是Android手机中APN配置文件中的代码）：
                    中国移动：46000 46002
                    中国联通：46001
                    中国电信：46003
                    举例，一个典型的IMSI号码为460030912121001

IMEI是International Mobile Equipment Identity （国际移动设备标识）的简称IMEI由15位数字组成的”电子串号”，它与每台手机一一对应，而且该码是全世界唯一的其组成为：
                    1. 前6位数(TAC)是”型号核准号码”，一般代表机型
                    2. 接着的2位数(FAC)是”最后装配号”，一般代表产地
                    3. 之后的6位数(SNR)是”串号”，一般代表生产顺序号
                    4. 最后1位数(SP)通常是”0″，为检验码，目前暂备用
* */
/**
 * 获取设备参数工具类
 *
 * 作者：xjzhao
 * 时间：2014-09-26 下午5:13
 */
public class DevUtils {

    /**
     * 获取屏幕密度（像素比例：0.75/1.0/1.5/2.0）
     *
     * 单位：无
     *
     * @return
     */
    public static float getDensity(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.density;
        }
        return 0;
    }


    /**
     * 获取屏幕密度（每寸像素：120/160/240/320）
     *
     * 单位：dpi
     *
     * @return
     */
    public static float getDensityDpi(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.densityDpi;
        }
        return 0f;
    }

    /**
     * 获取屏幕宽度
     *
     * 单位：px
     *
     * @return
     */
    public static int getScreenWidth(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.widthPixels;
        }
        return 0;
    }

    /**
     * 获取屏幕高度
     *
     * 单位：px
     *
     * @return
     */
    public static int getScreenHeight(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            return dm.heightPixels;
        }
        return 0;
    }

    /**
     * 获取手机状态栏高度
     *
     * 单位：px
     *
     */
    public static int getStatusBarHeight(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int statusBarHeight = 0;
            int x;
            try{
                c = Class.forName("com.android.internal.R$dimen");
                obj = c.newInstance();
                field = c.getField("status_bar_height");
                x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(x);
                return statusBarHeight;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 是否橫屏
     * @param activity
     * @return
     */
    public static boolean isLandscape(final Activity activity){
        if (null != activity){
            int orientation = activity.getRequestedOrientation();
            switch (orientation){
                case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                case ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE:
                case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
                    return true;
                case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                case ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT:
                case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:
                    return false;
            }
        }
        return false;
    }

    /**
     * 是否竖屏
     * @param activity
     * @return
     */
    public static boolean isPortrait(final Activity activity){
        if (null != activity){
            int orientation = activity.getRequestedOrientation();
            switch (orientation){
                case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                case ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE:
                case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
                    return false;
                case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                case ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT:
                case ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT:
                    return true;
            }
        }
        return true;
    }

    /**
     * 获移动终端的唯一标识.如果是GSM网络，返回IMEI；如果是CDMA网络，返回MEID
     * @return
     */
    public static String getDeviceId(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        }
        return null;
    }

    /**
     * 获取移动终端的软件版本，例如：GSM手机的IMEI/SV码
     * @return
     */
    public static String getDeviceSoftwareVersion(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceSoftwareVersion();
        }
        return null;
    }

    /**
     * 获取手机号码，对于GSM网络来说即MSISDN
     * @return
     */
    public static String getLine1Number(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getLine1Number();
        }
        return null;
    }

    /**
     * 获取ISO标准的国家码，即国际长途区号
     * @return
     */
    public static String getNetworkCountryIso(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getLine1Number();
        }
        return null;
    }

    /**
     * 获取MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)(IMSI)
     * @return
     */
    public static String getNetworkOperator(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getNetworkOperator();
        }
        return null;
    }

    /**
     * 获取移动网络运营商的名字(SPN)
     * @return
     */
    public static String getNetworkOperatorName(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getNetworkOperatorName();
        }
        return null;
    }

    /**
     * 获取网络类型
     *
     * NETWORK_TYPE_CDMA   网络类型为CDMA
     * NETWORK_TYPE_EDGE   网络类型为EDGE
     * NETWORK_TYPE_EVDO_0 网络类型为EVDO0
     * NETWORK_TYPE_EVDO_A 网络类型为EVDOA
     * NETWORK_TYPE_GPRS   网络类型为GPRS
     * NETWORK_TYPE_HSDPA  网络类型为HSDPA
     * NETWORK_TYPE_HSPA   网络类型为HSPA
     * NETWORK_TYPE_HSUPA  网络类型为HSUPA
     * NETWORK_TYPE_UMTS   网络类型为UMTS
     *
     * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
     * @return
     */
    public static int getNetworkType(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getNetworkType();
        }
        return TelephonyManager.NETWORK_TYPE_GPRS;
    }

    /**
     * 获取移动终端的类型
     *
     * PHONE_TYPE_CDMA  手机制式为CDMA，电信
     * PHONE_TYPE_GSM   手机制式为GSM，移动和联通
     * PHONE_TYPE_NONE  手机制式未知
     *
     * @return
     */
    public static int getPhoneType(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getPhoneType();
        }
        return TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 获取SIM卡提供商的国家代码
     * @return
     */
    public static String getSimCountryIso(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSimCountryIso();
        }
        return null;
    }

    /**
     * 获取MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)(IMSI)
     * @return
     */
    public static String getSimOperator(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSimOperator();
        }
        return null;
    }

    /**
     * 获取SIM卡运营商名称
     * @return
     */
    public static String getSimOperatorName(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSimOperatorName();
        }
        return null;
    }

    /**
     * 获取SIM卡的序列号(IMEI)
     * @return
     */
    public static String getSimSerialNumber(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSimSerialNumber();
        }
        return null;
    }

    /**
     * 获取移动终端
     * SIM_STATE_ABSENT         SIM卡未找到
     * SIM_STATE_NETWORK_LOCKED SIM卡网络被锁定，需要Network PIN解锁
     * SIM_STATE_PIN_REQUIRED   SIM卡PIN被锁定，需要User PIN解锁
     * SIM_STATE_PUK_REQUIRED   SIM卡PUK被锁定，需要User PUK解锁
     * SIM_STATE_READY          SIM卡可用
     * SIM_STATE_UNKNOWN        SIM卡未知
     * @return
     */
    public static int getSimState(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSimState();
        }
        return TelephonyManager.SIM_STATE_ABSENT;
    }

    /**
     * 获取用户唯一标识，比如GSM网络的IMSI编号
     * @return
     */
    public static String getSubscriberId(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        }
        return null;
    }

    /**
     * 获取语音邮件号码
     * @return
     */
    public static String getVoiceMailNumber(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getVoiceMailNumber();
        }
        return null;
    }

    /**
     * 是否处于漫游状态
     * @return
     */
    public static boolean isNetworkRoaming(){
        Context context = HealthCareApplication.getInstance();
        if (null != context){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.isNetworkRoaming();
        }
        return false;
    }

    /**
     * 获取当前网络状态(是否可用)
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context){
        boolean netWorkStatus = false;
        if(null!=context){
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(null != connManager.getActiveNetworkInfo()){
                netWorkStatus = connManager.getActiveNetworkInfo().isAvailable();
            }
        }
        return netWorkStatus;
    }
}
