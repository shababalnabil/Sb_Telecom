package com.ecomflexi.softwarelabbd;

import android.app.Activity;
import android.os.Bundle;

public class Develop extends Activity {
    //public static String DeV = "https://app.myrecharge24.com.bd";
    public static String DeV = BuildConfig.WEB_URL;

    /* Foysal Tech && ict Foysal */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.develop);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
