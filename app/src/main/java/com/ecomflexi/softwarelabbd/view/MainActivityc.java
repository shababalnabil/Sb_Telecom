package com.ecomflexi.softwarelabbd.view;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.ecomflexi.softwarelabbd.R;
import com.ecomflexi.softwarelabbd.databinding.ActivityMaincBinding;

public class MainActivityc extends AppCompatActivity {
    public final int REQUEST_CODE = 1;
    ActivityMaincBinding binding;

    /* Foysal Tech && ict Foysal */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityMaincBinding inflate = ActivityMaincBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.container, ContactListFragment.newInstance()).setCustomAnimations(R.anim.slide_in, R.anim.slide_out).commitNow();
    }
}
