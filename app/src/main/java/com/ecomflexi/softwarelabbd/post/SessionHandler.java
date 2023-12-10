package com.ecomflexi.softwarelabbd.post;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionHandler {

    public static String getPref(String str, Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(str, (String) null);
    }

}
