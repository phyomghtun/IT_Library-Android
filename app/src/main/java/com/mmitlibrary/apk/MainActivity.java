package com.mmitlibrary.apk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.mmitlibrary.apk.R;

public class MainActivity extends AppCompatActivity {
    static SharedPreferences sp;
    public static String myprefer = null;
    SharedPreferences.Editor editor;
    String image = "image";
    String state="state";
    String temp1="";
    String temp2="";
    Context context;
    String GET_JSON_DATA_HTTP_URL = "https://androlover.com/islamic/ads.php";
    JsonArrayRequest jsonArrayRequest ;
    RequestQueue requestQueue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            sp = getSharedPreferences(myprefer, Context.MODE_PRIVATE);
            editor = sp.edit();
            if (!NetTest()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Unstable Connection Please Check Your Network")
                        .setCancelable(false)
                        .setTitle("Network Fail")
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                if(getUserCountry(MainActivity.this).equals("th") || getUserCountry(MainActivity.this).equals("mm")) {
                    change();
                }else{
                    change2();
                }
            }

    }
    public void change(){

            Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    JSON_DATA_WEB_CALL();
                    finish();
                    Intent i = new Intent(MainActivity.this, FirstActivity.class);
                    //   i.putExtra("image",temp1);
                    //   i.putExtra("state",temp2);
                    startActivity(i);

                }
            }, 3000);

    }
    public void change2(){

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                JSON_DATA_WEB_CALL();
                finish();
                Intent i = new Intent(MainActivity.this, OtherActivity.class);
                //   i.putExtra("image",temp1);
                //   i.putExtra("state",temp2);
                startActivity(i);

            }
        }, 3000);

    }
    private boolean NetTest() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void JSON_DATA_WEB_CALL() {

        jsonArrayRequest = new JsonArrayRequest(GET_JSON_DATA_HTTP_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);




    }
    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {

            GetDataAdapter GetDataAdapter2 = new GetDataAdapter();

            JSONObject json = null;
            try {

                json = array.getJSONObject(i);

                temp1=json.getString(image);
                temp2=json.getString(state);

            } catch (JSONException e) {

                e.printStackTrace();
            }

        }
    }
    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }
}
