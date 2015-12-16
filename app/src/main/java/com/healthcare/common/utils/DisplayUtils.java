package com.healthcare.common.utils;

import android.content.Context;

import com.healthcare.HealthCareApplication;

/**
 * 作者：xjzhao
 * 时间：2015-02-20 上午10:59
 */
public class DisplayUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        Context context = HealthCareApplication.getInstance();
        if (null == context) return (int) dpValue;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        Context context = HealthCareApplication.getInstance();
        if (null == context) return (int) pxValue;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px（正常字体下，1sp=1dp）
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        Context context = HealthCareApplication.getInstance();
        if (null == context) return (int) spValue;
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * sp转dp
     * @param spValue
     * @return
     */
    public static int sp2dp(float spValue) {
        int sp2Px = sp2px(spValue);
        return px2dip(sp2Px);
    }
}
