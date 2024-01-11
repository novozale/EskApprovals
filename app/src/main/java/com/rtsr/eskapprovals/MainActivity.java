package com.rtsr.eskapprovals;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static Boolean my_portraitOnly_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // старт
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.esk);
        actionbar.setTitle("Электроскандия");
        actionbar.setSubtitle("Выдача разрешений");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        my_portraitOnly_view = sharedPref.getBoolean("only_portrait", true);
        if (my_portraitOnly_view == true) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }

        Button Button1Type = (Button)findViewById(R.id.button);         //перевод в 1 тип
        Button1Type.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent my_intent = new Intent(MainActivity.this, ConversionTo1TypeActivity.class);
                startActivity(my_intent);
            }
        });

        Button ButtonShipment = (Button)findViewById(R.id.button2);     //разрешение на отгрузку
        ButtonShipment.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent my_intent = new Intent(MainActivity.this, ShipmentAllowanceActivity.class);
                startActivity(my_intent);
            }
        });

        Button ButtonEdit = (Button)findViewById(R.id.button3);     //разрешение на редактирование заказа 1 типа
        ButtonEdit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent my_intent = new Intent(MainActivity.this, EditAllowanceActivity.class);
                startActivity(my_intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // вывод меню
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // Меню
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        switch(item.getItemId()){
            case R.id.main_settings:    //основные настройки
                Intent my_intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(my_intent);
                return true;
            case R.id.main_exit:        //выход из прложенния
                 exit_my_app();
                return true;
            default:
                return false;
        }
    }

    public void exit_my_app() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Выйти из приложения?");
        alertDialogBuilder
                .setMessage("Нажмите \"Да\" для выхода!")
                .setCancelable(false)
                .setPositiveButton("Да",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
