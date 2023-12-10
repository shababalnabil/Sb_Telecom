package com.ecomflexi.softwarelabbd.activities;

import static com.ecomflexi.softwarelabbd.utilities.Constant.GET_SHIPPING;
import static com.ecomflexi.softwarelabbd.utilities.Constant.POST_ORDER;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ecomflexi.softwarelabbd.Develop;
import com.ecomflexi.softwarelabbd.Welcome;
import com.ecomflexi.softwarelabbd.post.SessionHandler;
import com.ecomflexi.softwarelabbd.sliderAdapter.AllSliderActivity;
import com.ecomflexi.softwarelabbd.sliderAdapter.SliderAdapterAll;
import com.ecomflexi.softwarelabbd.sliderAdapter.SliderData;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.material.snackbar.Snackbar;
//import com.onesignal.OneSignal;
import com.ecomflexi.softwarelabbd.Config;
import com.ecomflexi.softwarelabbd.R;
import com.ecomflexi.softwarelabbd.utilities.DBHelper;
import com.ecomflexi.softwarelabbd.utilities.SharedPref;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class ActivityCheckout extends AppCompatActivity {

    TextView PaymentVar;
    private static String Json_Url;
    String paymentData, textMore;
    RequestQueue requestQueue;
    Button btn_submit_order;
    EditText edt_name, edt_email, edt_phone, edt_address, edt_shipping, edt_order_list, edt_order_total, edt_comment;
    String str_name, str_email, str_phone, str_address, str_shipping, str_order_list, str_order_total, str_comment;
    String data_order_list = "";
    double str_tax;
    String str_currency_code;
    ProgressDialog progressDialog;
    DBHelper dbhelper;
    ArrayList<ArrayList<Object>> data;
    private static final String ALLOWED_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    View view;
    private String rand = getRandomString(9);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String date = dateFormat.format(Calendar.getInstance().getTime());
    SharedPref sharedPref;
    private Spinner spinner;
    private ArrayList<String> arrayList;
    private JSONArray result;
    String Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        view = findViewById(android.R.id.content);

        if (Config.ENABLE_RTL_MODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        sharedPref = new SharedPref(this);

        String newPhone = SessionHandler.getPref("phone", getApplicationContext());
        sharedPref.setYourPhone(newPhone);

        GetData getData = new GetData();
        getData.execute();
        setupToolbar();
        getSpinnerData();
        getTaxCurrency();

        Dev(Develop.DeV);
        Json_Url = getPref23(ImagesContract.URL, getApplicationContext());

        dbhelper = new DBHelper(this);
        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        // Creating Volley newRequestQueue
        requestQueue = Volley.newRequestQueue(ActivityCheckout.this);
        progressDialog = new ProgressDialog(ActivityCheckout.this);

        btn_submit_order = findViewById(R.id.btn_submit_order);

        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_phone = findViewById(R.id.edt_phone);
        edt_address = findViewById(R.id.edt_address);
        edt_shipping = findViewById(R.id.edt_shipping);
        edt_order_list = findViewById(R.id.edt_order_list);
        edt_order_total = findViewById(R.id.edt_order_total);
        edt_comment = findViewById(R.id.edt_comment);
        PaymentVar = findViewById(R.id.paymentText);

        edt_order_list.setEnabled(false);
        edt_phone.setEnabled(false);

        getDataFromDatabase();
        submitOrder();

    }

    public void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_checkout);
        }
    }

    private void getSpinnerData() {

        arrayList = new ArrayList<String>();
        spinner = findViewById(R.id.spinner);

        StringRequest stringRequest = new StringRequest(GET_SHIPPING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    result = jsonObject.getJSONArray("result");
                    getShipping(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        edt_shipping.setText(setShipping(i));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void getShipping(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject json = jsonArray.getJSONObject(i);
                arrayList.add(json.getString("shipping_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(ActivityCheckout.this, R.layout.spinner_item, arrayList);
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(myAdapter);
    }

    private String setShipping(int position) {
        String name = "";
        try {
            JSONObject json = result.getJSONObject(position);
            name = json.getString("shipping_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    public void submitOrder() {
        btn_submit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValueFromEditText();
            }
        });
    }

    public void getValueFromEditText() {

        str_name = edt_name.getText().toString();
        str_email = edt_email.getText().toString();
        str_phone = SessionHandler.getPref("phone", getApplicationContext());
//        str_phone = edt_phone.getText().toString();
        str_address = edt_address.getText().toString();
        str_shipping = edt_shipping.getText().toString();
        str_order_list = edt_order_list.getText().toString();
        str_order_total = edt_order_total.getText().toString();
        str_comment = edt_comment.getText().toString();

        if (str_name.equalsIgnoreCase("") || str_email.equalsIgnoreCase("") || str_phone.equalsIgnoreCase("") || str_address.equalsIgnoreCase("") || str_shipping.equalsIgnoreCase("") || str_comment.equalsIgnoreCase("") || str_order_list.equalsIgnoreCase("")) {
            Snackbar.make(view, R.string.checkout_fill_form, Snackbar.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.checkout_dialog_title);
            builder.setMessage(R.string.checkout_dialog_msg);
            builder.setCancelable(false);
            builder.setPositiveButton(getResources().getString(R.string.dialog_option_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestAction();
                    //new sendData().execute();
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.dialog_option_no), null);
            builder.setCancelable(false);
            builder.show();
        }
    }

    public void requestAction() {

        progressDialog.setTitle(getString(R.string.checkout_submit_title));
        progressDialog.setMessage(getString(R.string.checkout_submit_msg));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(final String ServerResponse) {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        dialogSuccessOrder();
                    }
                }, 2000);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("code", rand);
                params.put("name", str_name);
                params.put("email", str_email);
                params.put("phone", str_phone);
                params.put("address", str_address);
                params.put("shipping", str_shipping);
                params.put("order_list", str_order_list);
                params.put("order_total", str_order_total);
                params.put("comment", str_comment);
                params.put("player_id", OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId());

                params.put("date", date);
                params.put("server_url", Config.ADMIN_PANEL_URL);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ActivityCheckout.this);
        requestQueue.add(stringRequest);
    }

    public void getTaxCurrency() {
        Intent intent = getIntent();
        str_tax = intent.getDoubleExtra("tax", 0);
        str_currency_code = intent.getStringExtra("currency_code");
    }

    public void getDataFromDatabase() {

        data = dbhelper.getAllData();

        double Order_price = 0;
        double Total_price = 0;
        double tax = 0;

        for (int i = 0; i < data.size(); i++) {
            ArrayList<Object> row = data.get(i);

            String Menu_name = row.get(1).toString();
            String Quantity = row.get(2).toString();

            double Sub_total_price = Double.parseDouble(row.get(3).toString());

            String _Sub_total_price = String.format(Locale.GERMAN, "%1$,.0f", Sub_total_price);

            Order_price += Sub_total_price;

            if (Config.ENABLE_DECIMAL_ROUNDING) {
                data_order_list += (Quantity + " " + Menu_name + " " + _Sub_total_price + " " + str_currency_code + ",\n");
            } else {
                data_order_list += (Quantity + " " + Menu_name + " " + Sub_total_price + " " + str_currency_code + ",\n");
            }
        }

        if (data_order_list.equalsIgnoreCase("")) {
            data_order_list += getString(R.string.no_order_menu);
        }

        tax = Order_price * (str_tax / 100);
        Total_price = Order_price + tax;

        String price_tax = String.format(Locale.GERMAN, "%1$,.0f", str_tax);
        String _Order_price = String.format(Locale.GERMAN, "%1$,.0f", Order_price);
        String _tax = String.format(Locale.GERMAN, "%1$,.0f", tax);
        String _Total_price = String.format(Locale.GERMAN, "%1$,.0f", Total_price);

        if (Config.ENABLE_DECIMAL_ROUNDING) {
            data_order_list += "\n" + getResources().getString(R.string.txt_order) + " " + _Order_price + " " + str_currency_code + "\n" + getResources().getString(R.string.txt_tax) + " " + price_tax + " % : " + _tax + " " + str_currency_code + "\n" + getResources().getString(R.string.txt_total) + " " + _Total_price + " " + str_currency_code;


            edt_order_total.setText(_Total_price + " " + str_currency_code);

        } else {
            data_order_list += "\n" + getResources().getString(R.string.txt_order) + " " + Order_price + " " + str_currency_code + "\n" + getResources().getString(R.string.txt_tax) + " " + str_tax + " % : " + tax + " " + str_currency_code + "\n" + getResources().getString(R.string.txt_total) + " " + Total_price + " " + str_currency_code;

            edt_order_total.setText(Total_price + " " + str_currency_code);
        }

        edt_order_list.setText(data_order_list);

    }

    public void dialogSuccessOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.checkout_success_title);
        builder.setMessage(R.string.checkout_success_msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.checkout_option_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dbhelper.addDataHistory(rand, str_order_list, str_order_total, date);
                dbhelper.deleteAllData();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder stringBuilder = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            stringBuilder.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return stringBuilder.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        String phoneNo = SessionHandler.getPref("phone", getApplicationContext());
        edt_name.setText(sharedPref.getYourName());
        edt_email.setText(sharedPref.getYourEmail());
//        edt_phone.setText(sharedPref.getYourPhone());
        edt_phone.setText(phoneNo);
        edt_address.setText(sharedPref.getYourAddress());
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void Dev(String str) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        edit.putString(ImagesContract.URL, str);
        edit.commit();
    }


    public static String getPref23(String str, Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(str, "never");
    }

    public class GetData extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(Json_Url + "/mdsabbir.php");
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    int data = inputStreamReader.read();
                    while (data != -1) {
                        current += (char) data;
                        data = inputStreamReader.read();
                    }
                    return current;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("PaymentText");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    paymentData = jsonObject1.getString("Text");
                    textMore = jsonObject1.getString("TextMore");

                    if (!paymentData.equals("")) {
                        PaymentVar.setText(paymentData);
                    } else {
                        PaymentVar.setText("কিছু সমস্যা হয়েছে ইন্টারনেট কানেক্ট দিয়ে আবার পেজটি লোড করুন");
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
