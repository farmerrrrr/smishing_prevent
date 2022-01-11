package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    public static final String PREFERENCES_NAME = "rebuild_preference";

    private static final boolean DEFAULT_VALUE_BOOLEAN = false;
    private static final boolean FIRST_APP_BOOLEAN = true;
    private static final String PERSONAL_NUMBER="";


    private static SharedPreferences getPreferences(Context context) {

        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

    }

    //boolean 값 설정
    public static void set_protect_monitor_permission(Context context, String key, boolean value) {

        SharedPreferences prefs = getPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(key, value);

        editor.commit();

    }

    //boolean 값 로드
    public static boolean get_protect_monitor_permission(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);

        boolean value = prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN);

        return value;

    }

    public static boolean get_first_app_execute(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);

        boolean value = prefs.getBoolean(key, FIRST_APP_BOOLEAN);

        return value;

    }
    public static void set_first_app_execute(Context context, String key, boolean value) {

        SharedPreferences prefs = getPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(key, value);

        editor.commit();

    }
    public static String get_personal_number(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);

        String value = prefs.getString(key, PERSONAL_NUMBER);

        return value;

    }
    public static void set_personal_number(Context context, String key, String value) {

        SharedPreferences prefs = getPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(key, value);

        editor.commit();

    }

}

