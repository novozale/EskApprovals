package com.rtsr.eskapprovals;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditAllowanceActivity extends AppCompatActivity {
    public static String service_url;
    public static String my_login;
    public static String my_pass;
    public static Boolean my_ssl_check;
    public static ListView listView1 = null;
    public static Boolean my_view;
    public static SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // старт
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_allowance);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.esk);
        actionbar.setTitle("Электроскандия");
        actionbar.setSubtitle("Редактирование заказов 1 типа");

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
        GetAllowedEditOrderList();
    }

    public void GetAllowedEditOrderList() {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // вывод списка заказов с разрешением на редактирование
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        JSONArray jsarr = null;
        JSONObject jsel = null;

        if (my_login.equals("") || my_pass.equals("")) {           //-----не заполнен логин или пароль
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Внимание!");
            alertDialogBuilder
                    .setMessage("Не заполнен логин или пароль")
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
            try {
                //---------------------список заказов---------------------------------------------------
                String my_str = new String("");
                HttpWorkAsync my_httpAs = new HttpWorkAsync();
                my_str = my_httpAs.execute(service_url + "GetAllowedEditOrderListForCollectionJson",
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
                    //-----отрисовка значений
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
                            try {
                                ArrayList<Map<String, String>> listItems = getArrayListFromJSONArray(jsarr);
                                listView1 = (ListView) findViewById(R.id.allowed_sales_order_list);
                                String[] from = {"OrderN", "Salesman", "AllowanceDate", "AllowanceReason"};
                                int[] to = {R.id.sales_order_order_n, R.id.sales_order_item_salesman,
                                        R.id.sales_order_item_allowance_date, R.id.sales_order_item_allowance_reason};
                                sa = new SimpleAdapter(this, listItems, R.layout.allowed_edit_orders, from, to);
                                listView1.setAdapter(sa);

                                /*listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        final String OrderN;

                                        Object o = listView1.getItemAtPosition(i);
                                        HashMap hm = (HashMap) o;
                                        OrderN = hm.get("OrderN").toString();
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditAllowanceActivity.this);
                                        alertDialogBuilder.setTitle("Внимание!");
                                        alertDialogBuilder
                                                .setMessage("Удалить разрешение на редактирование заказа " + OrderN + "?")
                                                .setCancelable(false)
                                                .setPositiveButton("Yes",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                RemoveOrderEditAllowance(OrderN);
                                                                dialog.cancel();
                                                            }
                                                        })
                                                .setNegativeButton("No",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                        return false;
                                    }
                                });*/
                            } catch (Exception e) {
                            }
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
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }

    private ArrayList<Map<String,String>> getArrayListFromJSONArray(JSONArray jsonArray){
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // получение листа значений из массива JSON
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        ArrayList<Map<String,String>> aList = new ArrayList<Map<String,String>>();
        JSONObject jsel = null;

        try{
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsel = jsonArray.getJSONObject(i);
                    Map<String, String> datum = new HashMap<String, String>();
                    datum.put("OrderN", jsel.getString("OrderN"));
                    datum.put("Salesman", jsel.getString("Salesman"));
                    datum.put("AllowanceDate", jsel.getString("AllowanceDate"));
                    datum.put("AllowanceReason", jsel.getString("AllowanceReason"));
                    aList.add(datum);
                }
            }
        } catch (JSONException je){
            je.printStackTrace();
        }
        return  aList;
    }

    private void RemoveOrderEditAllowance(String OrderN) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // Удаление разрешения на редактирование для заказа
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        JSONArray jsarr = null;
        JSONObject jsel = null;

        /*Toast toast = Toast.makeText(EditAllowanceActivity.this, "Удаление разрешения для заказа " + OrderN, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.rgb(180,198,231), PorterDuff.Mode.SRC_IN);
        toast.show();*/

        try {
            String my_str = new String("");
            HttpWorkAsync my_httpAs = new HttpWorkAsync();
            my_str = my_httpAs.execute(service_url + "GetDeleteAllowanceRezJson/?order=" + OrderN,
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
                                .setMessage("Удалено разрешение на редактирование заказа " + OrderN + ".")
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent my_intent = new Intent(EditAllowanceActivity.this, EditAllowanceActivity.class);
                                                my_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public void on_add_allowance(View v) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // Добавление разрешения на редактирование для заказа
        //
        ////////////////////////////////////////////////////////////////////////////////////////////

        Intent my_intent = new Intent(EditAllowanceActivity.this, NonZeroOrderListActivity.class);
        my_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(my_intent);
    }

    @Override
    public void onBackPressed() {
        Intent my_intent = new Intent(EditAllowanceActivity.this, MainActivity.class);
        my_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(my_intent);
    }

    public void on_remove_click(View v) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // Удаление разрешения на редактирование для заказа
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        String OrderN;
        OrderN = "";

        ImageView iv = (ImageView)findViewById(R.id.button_delete_order);
        ViewGroup row = (ViewGroup) iv.getParent();
        TextView tv = (TextView) row.getChildAt(1);
        OrderN = tv.getText().toString();
        /*RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
        int position = holder.getPosition();
        Object o = listView1.getItemAtPosition(position);
        HashMap hm = (HashMap) o;
        OrderN = hm.get("OrderN").toString();*/

        Toast toast = Toast.makeText(EditAllowanceActivity.this, "Удаление разрешения для заказа " + OrderN, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.rgb(180,198,231), PorterDuff.Mode.SRC_IN);
        toast.show();
    }
}