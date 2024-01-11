package com.rtsr.eskapprovals;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShipmentAllowanceActivity extends AppCompatActivity {

    private static final String STATE_ID = "id";
    public static Integer my_have_err = 0;
    public static String service_url;
    public static String my_login;
    public static String my_pass;
    public static Boolean my_ssl_check;
    public static Boolean my_portraitOnly_view;
    public static String my_order_number;
    public static String my_customer;
    public static Double my_order_summ;
    public static Double my_order_margin;
    public static Double my_order_fmargin;
    public static Boolean my_isCredit;
    public static Double my_adv_summ;
    public static Double my_creditLimit;
    public static Double my_creditUsage;
    public static Double my_marjin1level;
    public static Double my_marjin2level;
    public static Integer my_overdue_invoices;

    public static Integer my_margin_level;
    public static Integer my_transfer_overlimit;
    public static Integer my_shipments_overlimit;

    public static Integer my_transfer_allowed;
    public static Integer my_memo_required;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // старт
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_allowance);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setIcon(R.mipmap.esk);
        actionbar.setTitle("Электроскандия");
        actionbar.setSubtitle("Разрешение на отгрузку");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        service_url = sharedPref.getString("service_url", "");
        my_login = sharedPref.getString("my_login", "");
        my_pass = sharedPref.getString("my_password", "");
        my_ssl_check = sharedPref.getBoolean("check_certificat", true);
        my_portraitOnly_view = sharedPref.getBoolean("only_portrait", true);

        //-----инициализация
        my_order_summ =0.0;
        my_order_margin = 0.0;
        my_order_fmargin = 0.0;
        my_adv_summ = 0.0;
        my_creditLimit = 0.0;
        my_creditUsage = 0.0;
        my_marjin1level = 0.0;
        my_marjin2level = 0.0;
        my_overdue_invoices = 0;
        my_margin_level = 0;
        my_transfer_overlimit = 0;
        my_shipments_overlimit = 0;
        my_transfer_allowed = 0;
        my_memo_required= 1;
        //-----

        if (my_portraitOnly_view == true) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }

        Button ButtonInfo = (Button)findViewById(R.id.button_infon0);         //вывод информации
        ButtonInfo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                removeAllLayouts();
                RelativeLayout my_rl = (RelativeLayout) findViewById(R.id.progress_Sh_la);
                my_rl.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            synchronized (this) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        GetOrdern0Info();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });

        if (savedInstanceState != null) {
            // Restore some state before we've even inflated our own layout
            // This could be generic things like an ID that our Fragment represents
            my_have_err = savedInstanceState.getInt(STATE_ID, 1);
            if(my_have_err == 0) {
                GetOrdern0Info();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_ID, my_have_err);
    }

    private void removeAllLayouts(){
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // удаление и перевод в невидимость ранее исползовавшихся лейаутов
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        LinearLayout my_ll = null;
        ViewGroup parent = null;
        Integer my_count = null;

        try {
            my_ll = (LinearLayout) findViewById(R.id.infolayout_n0);
            try{
                my_ll.removeAllViews();
            } catch(Exception e) {
            }
            parent = (ViewGroup) my_ll.getParent();
            parent.removeView(my_ll);
        } catch(Exception e) {
            e.printStackTrace();
        }
        try {
            my_ll = (LinearLayout) findViewById(R.id.convto1type_adv_layout);
            try{
                my_ll.removeAllViews();
            } catch(Exception e) {
            }
            parent = (ViewGroup) my_ll.getParent();
            parent.removeView(my_ll);
        } catch(Exception e) {
            e.printStackTrace();
        }
        try {
            my_ll = (LinearLayout) findViewById(R.id.convto1type_credit_layout);
            try{
                my_ll.removeAllViews();
            } catch(Exception e) {
            }
            parent = (ViewGroup) my_ll.getParent();
            parent.removeView(my_ll);
        } catch(Exception e) {
            e.printStackTrace();
        }
        try {
            my_ll = (LinearLayout) findViewById(R.id.convto1type_ovedinvoices_layout);
            try{
                my_ll.removeAllViews();
            } catch(Exception e) {
            }
            parent = (ViewGroup) my_ll.getParent();
            parent.removeView(my_ll);
        } catch(Exception e) {
            e.printStackTrace();
        }
        try {
            my_ll = (LinearLayout) findViewById(R.id.nsh_order_detail_layout);
            try {
                my_ll.removeAllViews();
            } catch(Exception e) {
            }
            parent = (ViewGroup) my_ll.getParent();
            parent.removeView(my_ll);
        } catch(Exception e) {
            e.printStackTrace();
        }

        try {
            my_ll = (LinearLayout) findViewById(R.id.order_n0_decision1);
            try{
                my_ll.removeAllViews();
            } catch(Exception e) {
            }
            parent = (ViewGroup) my_ll.getParent();
            parent.removeView(my_ll);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void GetOrdern0Info(){
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // вывод информации о заказе
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        removeAllLayouts();
        //---------------------получение информации по заказу---------------------------------------
        EditText my_orderNum = (EditText)findViewById(R.id.OrderNumbern0);
        my_order_number = my_orderNum.getText().toString();
        my_order_number = my_order_number.replace(" ", "");
        if (my_order_number.equals("")) {           //-----не заполнен номер заказа
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Внимание!");
            alertDialogBuilder
                    .setMessage("Не заполнен номер заказа")
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {                                    //-----номер заказа заполнен
            my_have_err = 0;
            if (GetOrdern0CommonInfo(my_order_number) == true){
                GetOrderAdvances(my_order_number);
                if (my_isCredit == true) {
                    GetCustomerCreditInfo(my_customer);
                }
                GetCustomerDebtInfo(my_customer);
                GetOrderDetailInfo(my_order_number);
                GetUserRights(my_login);
                ShowDecisionElements();
            }
            RelativeLayout my_rl = (RelativeLayout)findViewById(R.id.progress_Sh_la);
            my_rl.setVisibility(View.INVISIBLE);
        }
    }


    private boolean GetOrdern0CommonInfo(String my_order_number){
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // вывод общей информации о заказе
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        JSONArray jsarr = null;
        JSONObject jsel = null;
        TextView mt = null;

        //---------------------общая информация по заказу---------------------------------------
        try {
            String my_str = new String("");
            HttpWorkAsync my_httpAs = new HttpWorkAsync();
            my_str = my_httpAs.execute(service_url + "GetOrdersn0TypeCommonJson/?order=" + my_order_number,
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
                my_have_err = 1;
                return false;
            } else {
                //-----отрисовка значений
                if (my_str != "") {

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
                        my_have_err = 1;
                        return false;
                    } else {
                        try {
                            LinearLayout my_main_ll = (LinearLayout) findViewById(R.id.shipments_main_layout);
                            LinearLayout my_advll = (LinearLayout) View.inflate(this, R.layout.order_n0_info, null);
                            my_main_ll.addView(my_advll);

                            mt = (TextView) findViewById(R.id.CustCodeNamen0);
                            mt.setText(jsel.getString("CustomerCode") + " " + jsel.getString("CustomerName"));
                            my_customer = jsel.getString("CustomerCode");
                            mt = (TextView) findViewById(R.id.salesmanCodeNamen0);
                            mt.setText(jsel.getString("SalesmanCode") + " " + jsel.getString("SalesmanName"));
                            mt = (TextView) findViewById(R.id.AgreementTypen0);
                            mt.setText(jsel.getString("IsCredit"));
                            if (jsel.getString("IsCredit").equals("Кредитный")) {
                                my_isCredit = true;
                            } else {
                                my_isCredit = false;
                            }
                            if (jsel.getString("StartAgreementDate").trim().equals("")
                                    && jsel.getString("FinAgreementDate").trim().equals("")) {
                            } else {
                                mt = (TextView) findViewById(R.id.AgreementsDatesn0);
                                mt.setText("C " + jsel.getString("StartAgreementDate") + " По " + jsel.getString("FinAgreementDate"));
                            }
                            mt = (TextView) findViewById(R.id.CreditSummn0);
                            mt.setText(String.format("%,.2f", Double.parseDouble(jsel.getString("CreditSumm"))));
                            my_creditLimit = Double.parseDouble(jsel.getString("CreditSumm"));
                            mt = (TextView) findViewById(R.id.CreditDaysn0);
                            mt.setText(jsel.getString("CreditDays"));
                            mt = (TextView) findViewById(R.id.OrderDaten0);
                            mt.setText(jsel.getString("OrderDate"));
                            mt = (TextView) findViewById(R.id.OrderSummn0);
                            mt.setText(String.format("%,.2f", Double.parseDouble(jsel.getString("OrderSumm"))));
                            my_order_summ = Double.parseDouble(jsel.getString("OrderSumm"));
                            mt = (TextView) findViewById(R.id.DeliveryCostn0);
                            mt.setText(String.format("%,.2f", Double.parseDouble(jsel.getString("DeliveryCost"))));
                            my_marjin1level = Double.parseDouble(jsel.getString("Margin1Level"));
                            my_marjin2level = Double.parseDouble(jsel.getString("Margin2Level"));
                            mt = (TextView) findViewById(R.id.OrderMarginPerCentn0);
                            mt.setText(String.format("%,.2f", Double.parseDouble(jsel.getString("OrderMarginPerCent"))));
                            my_order_margin = Double.parseDouble(jsel.getString("OrderMarginPerCent"));
                            if (Double.parseDouble(jsel.getString("OrderMarginPerCent")) < my_marjin2level) {
                                mt.setTextColor(Color.RED);
                            } else {
                                if (Double.parseDouble(jsel.getString("OrderMarginPerCent")) < my_marjin1level) {
                                    mt.setTextColor(Color.rgb(242, 112, 14));
                                } else{
                                    mt.setTextColor(Color.rgb(11, 25, 119));
                                }
                            }
                            mt = (TextView) findViewById(R.id.OrderfMarginPerCentn0);
                            mt.setText(String.format("%,.2f", Double.parseDouble(jsel.getString("OrderfMarginPerCent"))));
                            my_order_fmargin = Double.parseDouble(jsel.getString("OrderfMarginPerCent"));
                            if (Double.parseDouble(jsel.getString("OrderfMarginPerCent")) < my_marjin2level) {
                                mt.setTextColor(Color.RED);
                            } else {
                                if (Double.parseDouble(jsel.getString("OrderfMarginPerCent")) < my_marjin1level) {
                                    mt.setTextColor(Color.rgb(242, 112, 14));
                                } else{
                                    mt.setTextColor(Color.rgb(11, 25, 119));
                                }
                            }

                            return true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            my_have_err = 1;
                            return false;
                        }
                    }
                } else {
                    my_have_err = 1;
                    return false;
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
            my_have_err = 1;
            return false;
        }
    }

    private void GetOrderAdvances(String my_order_number){
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // вывод информации по авансам заказа
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        //---------------------информация по авансам заказа-----------------------------------------
        JSONArray jsarr = null;
        JSONObject jsel = null;
        TextView mt = null;

        try {
            String my_str = new String("");
            HttpWorkAsync my_httpAs = new HttpWorkAsync();
            my_str = my_httpAs.execute(service_url + "GetOrdersAdvancesJson/?order=" + my_order_number,
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
                my_have_err = 1;
            } else {
                //-----отрисовка значений
                LinearLayout my_main_ll = (LinearLayout) findViewById(R.id.shipments_main_layout);
                LinearLayout my_advll = (LinearLayout) View.inflate(this, R.layout.advances_info, null);
                my_main_ll.addView(my_advll);
                jsarr = new JSONArray(my_str);
                my_adv_summ = 0.0;
                if(jsarr.length() > 0) {
                    for (int i = 0; i < jsarr.length(); i++) {
                        jsel = jsarr.getJSONObject(i);

                        LinearLayout my_list = (LinearLayout) View.inflate(this, R.layout.advances_list, null);
                        mt = (TextView)my_list.findViewById(R.id.my_adv_date);
                        mt.setText(jsel.getString("AdvanceDate"));
                        mt = (TextView)my_list.findViewById(R.id.my_adv_summ);
                        mt.setText(String.format("%,.2f", Double.parseDouble(jsel.getString("AdvanceSumm"))));
                        my_adv_summ = my_adv_summ + Double.parseDouble(jsel.getString("AdvanceSumm"));
                        my_advll.addView(my_list);
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
            my_have_err = 1;
        }
    }

    private void GetCustomerCreditInfo(String my_customer){
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // вывод информации использованию кредитного лимита (на сколько отгружено неоплаченного)
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        JSONArray jsarr = null;
        JSONObject jsel = null;
        TextView mt = null;
        Double cr_lim;

        //---------------------общая информация по использованию кредитного лимита------------------
        try {
            String my_str = new String("");
            HttpWorkAsync my_httpAs = new HttpWorkAsync();
            my_str = my_httpAs.execute(service_url + "GetCreditInfoJson/?customer=" + my_customer,
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
                my_have_err = 1;
            } else {
                //-----отрисовка значений
                if (my_str != "") {
                    jsarr = new JSONArray(my_str);
                    jsel = jsarr.getJSONObject(0);
                    try {
                        LinearLayout my_main_ll = (LinearLayout) findViewById(R.id.shipments_main_layout);
                        LinearLayout my_advll = (LinearLayout) View.inflate(this, R.layout.credit_state, null);
                        my_main_ll.addView(my_advll);
                        mt = (TextView) findViewById(R.id.credit_used_summ_cs);
                        mt.setText(String.format("%,.2f", Double.parseDouble(jsel.getString("CreditUsedSumm"))));
                        my_creditUsage = Double.parseDouble(jsel.getString("CreditUsedSumm"));
                        if (Double.parseDouble(jsel.getString("CreditUsedSumm")) > my_creditLimit) {
                            mt.setTextColor(Color.RED);
                        } else {
                            mt.setTextColor(Color.rgb(11, 25, 119));
                        }
                        mt = (TextView) findViewById(R.id.CreditSumm);
                        mt = (TextView) findViewById(R.id.credit_summ_cs);
                        mt.setText(String.format("%,.2f", my_creditLimit));

                    } catch (JSONException e) {
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
                        my_have_err = 1;
                    }
                } else {
                }
            }
        }catch (Exception e) {
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
            my_have_err = 1;
        }
    }

    private void GetCustomerDebtInfo(String my_customer){
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // вывод информации по задолженности клиента
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        //---------------------информация по задолженным СФ-----------------------------------------
        JSONArray jsarr = null;
        JSONObject jsel = null;
        TextView mt = null;

        try {
            String my_str = new String("");
            HttpWorkAsync my_httpAs = new HttpWorkAsync();
            my_str = my_httpAs.execute(service_url + "GetOwedInvoicesJson/?customer=" + my_customer,
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
                my_have_err = 1;
            } else {
                //-----отрисовка значений
                LinearLayout my_main_ll = (LinearLayout) findViewById(R.id.shipments_main_layout);
                LinearLayout my_invl = (LinearLayout) View.inflate(this, R.layout.owedinvoices_info, null);
                my_main_ll.addView(my_invl);
                jsarr = new JSONArray(my_str);
                my_overdue_invoices = 0;
                if(jsarr.length() > 0) {
                    for (int i = 0; i < jsarr.length(); i++) {
                        jsel = jsarr.getJSONObject(i);

                        LinearLayout my_list = (LinearLayout) View.inflate(this, R.layout.ovedinvoices_list, null);
                        mt = (TextView)my_list.findViewById(R.id.ovedinvoices_InvoiceNum);
                        mt.setText(jsel.getString("InvoiceNum"));
                        mt = (TextView)my_list.findViewById(R.id.ovedinvoices_InvoiceDate);
                        mt.setText(jsel.getString("InvoiceDate"));
                        mt = (TextView)my_list.findViewById(R.id.ovedinvoices_InvoiceDueDate);
                        mt.setText(jsel.getString("InvoiceDueDate"));
                        mt = (TextView)my_list.findViewById(R.id.ovedinvoices_InvoiceOwedSumm);
                        mt.setText(String.format("%,.2f", Double.parseDouble(jsel.getString("InvoiceOwedSumm"))));
                        my_overdue_invoices++;
                        my_invl.addView(my_list);
                    }
                }
            }
        }catch (Exception e) {
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
            my_have_err = 1;
        }
    }

    private void GetOrderDetailInfo(String my_order_number){
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // вывод детальной информации по заказу
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        //---------------------информация по строкам заказа-----------------------------------------
        JSONArray jsarr = null;
        JSONObject jsel = null;
        TextView mt = null;
        try {
            String my_str = new String("");
            HttpWorkAsync my_httpAs = new HttpWorkAsync();
            my_str = my_httpAs.execute(service_url + "GetOrderNShDetailJson/?order=" + my_order_number,
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
                my_have_err = 1;
            } else {
                //-----отрисовка значений
                LinearLayout my_main_ll = (LinearLayout) findViewById(R.id.shipments_main_layout);
                LinearLayout my_invl = (LinearLayout) View.inflate(this, R.layout.nsh_order_detail, null);
                my_main_ll.addView(my_invl);
                jsarr = new JSONArray(my_str);
                if(jsarr.length() > 0) {
                    for (int i = 0; i < jsarr.length(); i++) {
                        jsel = jsarr.getJSONObject(i);

                        LinearLayout my_list = (LinearLayout) View.inflate(this, R.layout.nshn0_order_detail_list, null);

                        mt = (TextView)my_list.findViewById(R.id.nshn0_order_detail_StringNum);
                        mt.setText(jsel.getString("StringNum"));
                        mt = (TextView)my_list.findViewById(R.id.nshn0_order_detail_ItemCode);
                        mt.setText(jsel.getString("ItemCode") + " " + jsel.getString("ItemName"));
                        mt = (TextView)my_list.findViewById(R.id.nshn0_order_detail_ItemQTY);
                        mt.setText(String.format("%,.3f", Double.parseDouble(jsel.getString("ItemQTY"))));
                        mt = (TextView)my_list.findViewById(R.id.nshn0_order_detail_ItemPrice);
                        mt.setText(String.format("%,.2f", Double.parseDouble(jsel.getString("ItemPrice"))));
                        mt = (TextView)my_list.findViewById(R.id.nshn0_order_detail_ItemCalcPriCost);
                        mt.setText(String.format("%,.2f", Double.parseDouble(jsel.getString("ItemCalcPriCost"))));
                        mt = (TextView)my_list.findViewById(R.id.nshn0_order_detail_ItemCalcMargin);
                        mt.setText(String.format("%,.2f", Double.parseDouble(jsel.getString("ItemCalcMargin"))));
                        if (Double.parseDouble(jsel.getString("ItemCalcMargin")) < my_marjin2level) {
                            mt.setTextColor(Color.RED);
                        } else {
                            if (Double.parseDouble(jsel.getString("ItemCalcMargin")) < my_marjin1level) {
                                mt.setTextColor(Color.rgb(242,112,14));
                            } else {
                                mt.setTextColor(Color.rgb(11, 25, 119));
                            }
                        }
                        mt = (TextView)my_list.findViewById(R.id.nshn0_order_detail_ItemMargin);
                        mt.setText(String.format("%,.2f", Double.parseDouble(jsel.getString("ItemMargin"))));
                        if (Double.parseDouble(jsel.getString("ItemMargin")) < my_marjin2level) {
                            mt.setTextColor(Color.RED);
                        } else {
                            if (Double.parseDouble(jsel.getString("ItemMargin")) < my_marjin1level) {
                                mt.setTextColor(Color.rgb(242,112,14));
                            } else {
                                mt.setTextColor(Color.rgb(11, 25, 119));
                            }
                        }

                        my_invl.addView(my_list);
                    }
                }
            }
        }catch (Exception e) {
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
            my_have_err = 1;
        }
    }

    private void GetUserRights(String my_login) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // получение информации о правах, назначенных пользователю
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        //---------------------информация по правам пользователя------------------------------------
        JSONArray jsarr = null;
        JSONObject jsel = null;

        try{
            my_margin_level = 0;
            my_transfer_overlimit = 0;
            my_shipments_overlimit = 0;

            String my_str = new String("");
            HttpWorkAsync my_httpAs = new HttpWorkAsync();
            my_str = my_httpAs.execute(service_url + "GetScaUserRightsJson/?user=" + my_login,
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
                my_have_err = 1;
            } else {
                jsarr = new JSONArray(my_str);
                if(jsarr.length() > 0) {
                    for (int i = 0; i < jsarr.length(); i++) {
                        jsel = jsarr.getJSONObject(i);

                        my_margin_level = Integer.parseInt(jsel.getString("MarginLevel"));
                        my_transfer_overlimit = Integer.parseInt(jsel.getString("TransferOverLimit"));
                        my_shipments_overlimit = Integer.parseInt(jsel.getString("ShipmentsOverLimit"));
                    }
                }
            }
        }catch (Exception e) {
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
            my_have_err = 1;
        }
    }

    private void ShowDecisionElements(){
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // вывод управляющих элементов для принятия решения
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        Integer my_currmargin_level;
        Integer my_currdebt;

        my_transfer_allowed = 0;
        LinearLayout my_main_ll = (LinearLayout) findViewById(R.id.shipments_main_layout);
        LinearLayout my_desl = (LinearLayout) View.inflate(this, R.layout.order_n0_decision, null);
        my_main_ll.addView(my_desl);
        //------------------проверки - разрешено ли переводить в 1 тип
        //-----уровень маржи
        if (my_order_fmargin < my_marjin2level) {        //уровень 2
            my_currmargin_level = 2;
        } else {
            if (my_order_fmargin < my_marjin1level) {    //уровень 1
                my_currmargin_level = 1;
            } else {
                my_currmargin_level = 0;                //уровень 0
            }
        }

        //-----задолженность
        //if ((my_creditLimit + my_adv_summ - my_order_summ - my_creditUsage + 10) < 0
        if ((my_creditLimit + my_adv_summ - my_creditUsage + 10) < 0
                || (my_overdue_invoices > 0)) {         //есть задолженность
            my_currdebt = 1;
        } else {
            my_currdebt = 0;
        }

        //-----разрешение на перевод в 1 тип
        if ((my_currmargin_level <= my_margin_level) && (my_currdebt <= my_transfer_overlimit)) {
            my_transfer_allowed = 1;
        } else {
            my_transfer_allowed = 0;
        }

        //-----Необходимость комментариев
        if (my_currmargin_level == 0 && my_currdebt == 0) {
            my_memo_required = 0;
        } else {
            my_memo_required = 1;
        }

        //-------------------смена доступности и видимости элементов в зависимости
        //-------------------от прав на перевод
        if (my_transfer_allowed == 1) {
            if (my_memo_required == 0) {
                //-----прячем текст приглашения и текст комментария
                TextView mt = (TextView) findViewById(R.id.ord_n0_dec_lbl1);
                mt.setVisibility(View.INVISIBLE);
                mt.setHeight(0);
                EditText me = (EditText) findViewById(R.id.ord_n0_edt1);
                me.setVisibility(View.INVISIBLE);
                me.setHeight(0);
                LinearLayout ml = (LinearLayout)findViewById(R.id.order_n0_decision1);
                ml.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
            }
        } else {
            //-----прячем текст приглашения, текст комментария и кнопку
            TextView mt = (TextView) findViewById(R.id.ord_n0_dec_lbl1);
            mt.setVisibility(View.INVISIBLE);
            mt.setHeight(0);
            EditText me = (EditText) findViewById(R.id.ord_n0_edt1);
            me.setVisibility(View.INVISIBLE);
            me.setHeight(0);
            Button mb = (Button) findViewById(R.id.ord_n0_des_button1);
            mb.setVisibility(View.INVISIBLE);
            mb.setHeight(0);
            LinearLayout ml = (LinearLayout)findViewById(R.id.order_n0_decision1);
            ml.getLayoutParams().height = 0;
        }

        //--------------------Назначаем процедуру кнопке
        if (my_transfer_allowed == 1) {
            Button ButtonInfo = (Button)findViewById(R.id.ord_n0_des_button1);         //вывод информации
            ButtonInfo.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    MakeAllowanceShipment();
                }
            });
        }
    }

    private void MakeAllowanceShipment(){
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // Выдача разрешения на отгрузку заказа
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        JSONArray jsarr = null;
        JSONObject jsel = null;
        String my_err_code = "0";
        String my_err_message = "";
        String my_comment_str = "";

        EditText my_comment = (EditText)findViewById(R.id.ord_n0_edt1);
        my_comment_str = my_comment.getText().toString();
        my_comment_str = my_comment_str.replace(" ", "");
        if (my_memo_required == 1 && my_comment_str.equals("")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Внимание!");
            alertDialogBuilder
                    .setMessage("Не заполнена причина выдачи разрешения на отгрузку с преодолением ограничений")
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            my_have_err = 1;
        } else {
            try {
                String my_str = new String("");
                HttpWorkAsync my_httpAs = new HttpWorkAsync();
                my_str = my_httpAs.execute(service_url + "GetAllowanceShipmentRezJson/?order="
                                + my_order_number + "&user=" + my_login + "&Comment=" + my_comment_str,
                        my_login, my_pass, my_ssl_check).get().toString();
                if (my_httpAs.get_my_error() != "") {
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
                    my_have_err = 1;
                } else {
                    jsarr = new JSONArray(my_str);
                    if (jsarr.length() > 0) {
                        for (int i = 0; i < jsarr.length(); i++) {
                            jsel = jsarr.getJSONObject(i);

                            my_err_code = jsel.getString("ErrorNum");
                            my_err_message = jsel.getString("ErrorValue");
                        }
                        if (my_err_code.equals("0") == false) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                            alertDialogBuilder.setTitle("Ошибка!");
                            alertDialogBuilder
                                    .setMessage(my_err_message)
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                            my_have_err = 1;
                        } else {
                            removeAllLayouts();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                            alertDialogBuilder.setTitle("Внимание!");
                            alertDialogBuilder
                                    .setMessage("Выдано разрешение на отгрузку заказа " + my_order_number + ".")
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                            my_have_err = 1;
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
                my_have_err = 1;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //
        // вывод меню
        //
        ////////////////////////////////////////////////////////////////////////////////////////////
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_back, menu);
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
            case R.id.menu_back:    //выход из окна
                finish();
                return true;
             default:
                return false;
        }
    }
}
