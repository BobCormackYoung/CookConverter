package com.youngsoft.cookconverter.ui.preferences;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.youngsoft.cookconverter.R;

public class SettingsActivity extends AppCompatActivity {

    Fragment fragmentPreferences = new FragmentPreferences();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbarSettingsActivity = findViewById(R.id.toolbar_settings_activity);
        setSupportActionBar(toolbarSettingsActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbarSettingsActivity.setTitleTextColor(getResources().getColor(R.color.colorPrimaryLight));
        toolbarSettingsActivity.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_settings,
                fragmentPreferences).commit();
    }
}
