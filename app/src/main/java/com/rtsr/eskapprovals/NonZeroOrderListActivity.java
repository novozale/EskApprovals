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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NonZeroOrderListActivity extends AppCompatActivity {
    public static String service_url;
    public static String my_login;
    public static String my_pass;
    public static Boolean my_ssl_check;
    public static ListView listView1 = null;
    public static Boolean my_view;
    public static SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_zero_order_list);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.esk);
        actionbar.setTitle("Электроскандия");
        actionbar.setSubtitle("Список заказов 1 и 4 типа");

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
        listView1 = (ListView) findViewById(R.id.non_zero_sales_order_list);
        EditText inputSearch = (EditText) findViewById(R.id.editTextOrderN);
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                NonZeroOrderListActivity.this.sa.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        GetNonZeroOrderList();
    }

    public void GetNonZeroOrderList() {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // вывод списка заказов не 0 типа, по которым нет разрешения на редактирование
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
                my_str = my_httpAs.execute(service_url + "GetNonZeroOrderListForCollectionJson",
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
                                listView1 = (ListView) findViewById(R.id.non_zero_sales_order_list);
                                String[] from = {"OrderN", "OrderType", "Customer", "OrderSumm"};
                                int[] to = {R.id.nz_sales_order_order_n, R.id.nz_sales_order_item_ordertype,
                                        R.id.nz_sales_order_item_customer, R.id.nz_sales_order_item_ordersumm};
                                sa = new SimpleAdapter(this, listItems, R.layout.non_zero_non_edit_orders, from, to);
                                listView1.setAdapter(sa);

                                listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String OrderN;
                                        Object o = listView1.getItemAtPosition(i);
                                        HashMap hm = (HashMap) o;
                                        OrderN = hm.get("OrderN").toString();
                                        Intent my_intent = new Intent(NonZeroOrderListActivity.this, AddAllowanceActivity.class);
                                        //my_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        my_intent.putExtra("OrderN", OrderN);
                                        startActivity(my_intent);
                                        return false;
                                    }
                                    });
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
                    datum.put("OrderType", jsel.getString("OrderType"));
                    datum.put("Customer", jsel.getString("Customer"));
                    datum.put("OrderSumm", jsel.getString("OrderSumm"));
                    aList.add(datum);
                }
            }
        } catch (JSONException je){
            je.printStackTrace();
        }
        return  aList;
    }

    @Override
    public void onBackPressed() {
        Intent my_intent = new Intent(NonZeroOrderListActivity.this, EditAllowanceActivity.class);
        my_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(my_intent);
    }
}