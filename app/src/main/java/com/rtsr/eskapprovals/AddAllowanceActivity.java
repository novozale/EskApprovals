package com.rtsr.eskapprovals;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddAllowanceActivity extends AppCompatActivity {
    public static String service_url;
    public static String my_login;
    public static String my_pass;
    public static Boolean my_ssl_check;
    public static Boolean my_view;
    public static String OrderN = null;
    public static String MyReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView mt;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_allowance);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.esk);
        actionbar.setTitle("Электроскандия");
        actionbar.setSubtitle("Выдача разрешения на редактирование");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        service_url = sharedPref.getString("service_url", "");
        my_login = sharedPref.getString("my_login", "");
        my_pass = sharedPref.getString("my_password", "");
        my_ssl_check = sharedPref.getBoolean("check_certificat", true);
        my_view = sharedPref.getBoolean("screen_orientation", false);
        if (my_view == false) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Bundle order_param = getIntent().getExtras();
        if(order_param != null){
            OrderN = order_param.getString("OrderN");
            mt = (TextView) findViewById(R.id.nz_order_num_val);
            mt.setText(OrderN);
        }
    }

    public void on_nz_add_cancel(View v) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // отмена выдачи разрешения на редактирование
        //
        ////////////////////////////////////////////////////////////////////////////////////////////

        this.onBackPressed();
    }

    public void on_nz_add_write(View v) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // запись выдачи разрешения на редактирование
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        JSONArray jsarr = null;
        JSONObject jsel = null;

        if (CheckReasonFilled()) {
            try {
                String my_str = new String("");
                HttpWorkAsync my_httpAs = new HttpWorkAsync();
                my_str = my_httpAs.execute(service_url + "GetEditAllowanceRezJson/?order="
                                + OrderN + "&Comment=" + MyReason,
                        my_login, my_pass, my_ssl_check).get().toString();
                if (my_httpAs.get_my_error() !=  ""){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Ошибка!");
                    alertDialogBuilder
                            .setMessage(my_httpAs.my_error)
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    if (!my_str.equals("") && !my_str.equals("[]")) {
                        jsarr = new JSONArray(my_str);
                        jsel = jsarr.getJSONObject(0);
                        if (jsel.getString("ErrorNum").equals("0") == false) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                            alertDialogBuilder.setTitle("Ошибка!");
                            alertDialogBuilder
                                    .setMessage(jsel.getString("ErrorValue"))
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                            alertDialogBuilder.setTitle("Внимание!");
                            alertDialogBuilder
                                    .setMessage("Разрешение на редактирование заказа " + OrderN + " выдано.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent my_intent = new Intent(AddAllowanceActivity.this, EditAllowanceActivity.class);
                                                    startActivity(my_intent);
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Ошибка!");
                alertDialogBuilder
                        .setMessage(e.getLocalizedMessage())
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent my_intent = new Intent(AddAllowanceActivity.this, EditAllowanceActivity.class);
                                        startActivity(my_intent);
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }

    private Boolean CheckReasonFilled() {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // проверка - заполнена ли причина выдачи разрешения на редактирование
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        Boolean MyRez = false;
        TextView mt;

        mt = (TextView) findViewById(R.id.nz_order_reason_val);
        MyReason = mt.getText().toString();
        if (MyReason.equals("")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Внимание!");
            alertDialogBuilder
                    .setMessage("необходимо заполнить причину выдачи разрешения на редактирование заказа")
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            MyRez = true;
        }
        return MyRez;
    }
}