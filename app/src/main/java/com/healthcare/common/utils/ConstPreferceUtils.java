package com.healthcare.common.utils;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/12
 */
public class ConstPreferceUtils {

    public static final String IS_RECORDING = "constant";
    private static final String PREFERENCE_NAME = "setting_preference";

    private static final String TAG_NAME = "tag_name";

    public static void setRecordingFlag(boolean isRecording){
        PreferencesUtils.setPreferences(PREFERENCE_NAME, IS_RECORDING, isRecording);
    }

    public static boolean getRecordingFlag(){
        return PreferencesUtils.getPreference(PREFERENCE_NAME, IS_RECORDING, false);
    }

    public static void setTagName(String tag){
        PreferencesUtils.setPreferences(PREFERENCE_NAME, TAG_NAME, tag);
    }

    public static String getTagName(){
        return PreferencesUtils.getPreference(PREFERENCE_NAME, TAG_NAME, "common");
    }

}
