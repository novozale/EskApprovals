package com.rtsr.eskapprovals;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;


/**
 * Created by administrator on 08/01/2018.
 */

public class SettingsActivity extends PreferenceActivity{
    public static Boolean my_portraitOnly_view;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // старт
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_wrapper);
        addPreferencesFromResource(R.xml.my_preferences);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        my_portraitOnly_view = sharedPref.getBoolean("only_portrait", true);
        if (my_portraitOnly_view == true) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }

        Button backPrefButton = (Button)findViewById(R.id.backPrefButton);
        backPrefButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
     }
}
