package com.youngsoft.cookconverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.youngsoft.cookconverter.ui.preferences.FragmentPreferences;
import com.youngsoft.cookconverter.ui.preferences.SettingsActivity;
import com.youngsoft.cookconverter.ui.util.GlobalFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        Toolbar toolbarMainActivity = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbarMainActivity);
        toolbarMainActivity.setTitleTextColor(getResources().getColor(R.color.colorPrimaryLight));
        toolbarMainActivity.getOverflowIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_measures, R.id.navigation_baking, R.id.navigation_servings, R.id.navigation_ingredients)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Set default shared preferences on first load
        setDefaultSharedPreferences();

    }

    /**
     * set the default shared preferences the first time this is called
     */
    private void setDefaultSharedPreferences() {

        // Sets default values only once, first time this is called.
        // The third argument is a boolean that indicates whether
        // the default values should be set more than once. When false,
        // the system sets the default values only if this method has never
        // been called in the past.
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Get the 
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        if (preferences.getInt(FragmentPreferences.KEY_PREF_DEFAULT_UNIT, -1) == -1) {
            editor.putInt(FragmentPreferences.KEY_PREF_DEFAULT_UNIT, 1);
            editor.commit();
        }

        //if (preferences.getInt(FragmentPreferences.KEY_PREF_DEFAULT_VOLUME_UNIT, -1) == -1) {
        //    editor.putInt(FragmentPreferences.KEY_PREF_DEFAULT_VOLUME_UNIT, 7);
        //    editor.commit();
        //}

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ActivityNavigator activityNavigator = new ActivityNavigator(this);

        switch (item.getItemId()) {
            // action with ID action_information was selected
            case R.id.action_information:
                GlobalFragment globalFragment = (GlobalFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).getChildFragmentManager().getFragments().get(0);
                globalFragment.displayInformationAlert();
                break;
            // action with ID action_licences was selected
            case R.id.action_licences:
                Intent intent = new Intent(this, OssLicensesMenuActivity.class);
                startActivity(intent);
                break;
            // action with ID action_settings was selected
            case R.id.action_preferences:
                activityNavigator.navigate(activityNavigator
                        .createDestination()
                        .setIntent(new Intent(this, SettingsActivity.class)), null, null, null);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.popBackStack();
        findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        return true;
    }

    @Override
    public void onBackPressed() {
        findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        super.onBackPressed();
    }
}
