package com.tigersapp.bdcricket.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * @author Ripon
 */

public class SharedPrefData {

    public static String SHARED_PREF_NAME = "SHARED_PREF_NAME_SPORTS";

    public static String SHARED_KEY_TAG_NICKNAME = "nickname";

    public static String SHARED_KEY_TAG_MOBILE_NO = "mobileno";

    public static String getNickName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String nickname = preferences.getString(SHARED_KEY_TAG_NICKNAME, "no_nickname");

        if(nickname.equalsIgnoreCase("no_nickname")) {
            nickname = Build.MODEL;

            setNickName(context, nickname);
        }

        return nickname;
    }

    public static String getMobileNo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String mobileNo = preferences.getString(SHARED_KEY_TAG_MOBILE_NO, "no_mobileno");

        return mobileNo;
    }

    public static void setNickName(Context context, String nickname) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SHARED_KEY_TAG_NICKNAME, nickname);
        editor.commit();
    }

    public static void setMobileNo(Context context, String mobileno) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SHARED_KEY_TAG_MOBILE_NO, mobileno);
        editor.commit();
    }

}
