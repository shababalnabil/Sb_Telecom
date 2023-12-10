package com.ecomflexi.softwarelabbd.activities;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.ecomflexi.softwarelabbd.post.SessionHandler;
import com.google.android.material.snackbar.Snackbar;
import com.ecomflexi.softwarelabbd.Config;
import com.ecomflexi.softwarelabbd.R;
import com.ecomflexi.softwarelabbd.utilities.SharedPref;
import com.ecomflexi.softwarelabbd.utilities.Utils;


public class ActivitySettings extends PreferenceActivity {

    private AppCompatDelegate mDelegate;
    private SharedPref sharedPref;
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preferences);
        parent_view = findViewById(android.R.id.content);

        if (Config.ENABLE_RTL_MODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        sharedPref = new SharedPref(this);

        final EditTextPreference prefName = (EditTextPreference) findPreference(getString(R.string.pref_title_name));
        final EditTextPreference prefEmail = (EditTextPreference) findPreference(getString(R.string.pref_title_email));
//        final EditTextPreference prefPhone = (EditTextPreference) findPreference(getString(R.string.pref_title_phone));
        Preference phonePreference = findPreference(getString(R.string.pref_title_phone));
        final EditTextPreference prefAddress = (EditTextPreference) findPreference(getString(R.string.pref_title_address));

        prefName.setSummary(sharedPref.getYourName());
        prefName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String s = (String) o;
                if(!s.trim().isEmpty()){
                    prefName.setSummary(s);
                    return true;
                }else{
                    Snackbar snackbar = Snackbar.make(parent_view, R.string.pref_msg_invalid_name, Snackbar.LENGTH_LONG);
                    TextView textView = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                    return false;
                }
            }
        });

        prefEmail.setSummary(sharedPref.getYourEmail());
        prefEmail.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String s = (String) o;
                if (Utils.isValidEmail(s)) {
                    prefEmail.setSummary(s);
                    return true;
                } else {
                    Snackbar snackbar = Snackbar.make(parent_view, R.string.pref_msg_invalid_email, Snackbar.LENGTH_LONG);
                    TextView textView = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                    return false;
                }
            }
        });

        String phoneNo = SessionHandler.getPref("phone", getApplicationContext());
//        prefPhone.setSummary(phoneNo);
        phonePreference.setSummary(phoneNo);
        /*prefPhone.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String s = (String) o;
                if(!s.trim().isEmpty()){
                    prefPhone.setSummary(s);
                    return true;
                }else{
                    Snackbar snackbar = Snackbar.make(parent_view, R.string.pref_msg_invalid_phone, Snackbar.LENGTH_LONG);
                    TextView textView = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                    return false;
                }
            }
        });*/

        prefAddress.setSummary(sharedPref.getYourAddress());
        prefAddress.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String s = (String) o;
                if(!s.trim().isEmpty()){
                    prefAddress.setSummary(s);
                    return true;
                }else{
                    Snackbar snackbar = Snackbar.make(parent_view, R.string.pref_msg_invalid_address, Snackbar.LENGTH_LONG);
                    TextView textView = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                    return false;
                }
            }
        });

        initToolbar();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_profile);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }



    /*
     * Support for Activity : DO NOT CODE BELOW ----------------------------------------------------
     */

    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return getDelegate().getMenuInflater();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        getDelegate().setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().setContentView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().addContentView(view, params);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        getDelegate().setTitle(title);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDelegate().onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

}
