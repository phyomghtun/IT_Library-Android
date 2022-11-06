package com.mmitlibrary.apk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.mmitlibrary.apk.R;

public class FirstActivity extends AppCompatActivity {
    String image = "image";
    String state="state";
    String temp1="";
    String temp2="";
    Context context;
    String GET_JSON_DATA_HTTP_URL2 = "https://androidarea.xyz/islamic/ads.php";
    JsonArrayRequest jsonArrayRequest2 ;
    RequestQueue requestQueue2 ;

    static SharedPreferences sp;
    public static String myprefer = null;
    SharedPreferences.Editor editor;

    List<GetDataAdapter> GetDataAdapter1;
    private SwipeRefreshLayout mSwipeLayout;
    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerView.Adapter recyclerViewadapter;
    RecyclerViewAdapter adapter;

    String GET_JSON_DATA_HTTP_URL = "https://androidarea.xyz/itlibrary/json.php";
    String JSON_IMAGE_TITLE_NAME1 = "book_url";
    String JSON_IMAGE_URL = "image_url";
    String JSON_IMAGE_TITLE_NAME2 = "title";
    String JSON_IMAGE_TITLE_NAME3 = "size";
    String JSON_IMAGE_TITLE_NAME4 = "des";
    String JSON_IMAGE_TITLE_NAME5 = "book_name";
    ProgressDialog progressDialog;
    JsonArrayRequest jsonArrayRequest ;
    RequestQueue requestQueue ;
    AdRequest adRequest;
    String font1="pds.ttf";
    int i=1;
    GetDataAdapter GetDataAdapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

     //   Toast.makeText(FirstActivity.this,getUserCountry(FirstActivity.this),Toast.LENGTH_LONG).show();

        ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                1);


        sp = getSharedPreferences(myprefer, Context.MODE_PRIVATE);
        editor = sp.edit();

        if(sp.contains("Font")) {
            get();
        }

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.tc_ic);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isOnline2()) {
                    JSON_DATA_WEB_CALL();
                   // new SanZtoU(FirstActivity.this).ForceToUni("Font1.ttf", true);
                } else{
                    mSwipeLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(),"No internet connection", Toast.LENGTH_SHORT).show();
                }
            } });

        mSwipeLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);


        GetDataAdapter1 = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);

        // recyclerViewlayoutManager = new LinearLayoutManager(this);

        // recyclerView.setLayoutManager(recyclerViewlayoutManager);
        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        JSON_DATA_WEB_CALL();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.getProgress();
        progressDialog.show();
        progressDialog.setCancelable(false);


    }

    public void JSON_DATA_WEB_CALL(){

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

        GetDataAdapter1.clear();

        for(int i = 0; i<array.length(); i++) {

            GetDataAdapter2 = new GetDataAdapter();

            JSONObject json = null;
            try {

                json = array.getJSONObject(i);

                GetDataAdapter2.setImageTitleNamee1(json.getString(JSON_IMAGE_TITLE_NAME1));

                String tt;
                byte[] bytes0 = json.getString(JSON_IMAGE_TITLE_NAME2).getBytes();
                tt= new String(android.util.Base64.decode(bytes0, Base64.DEFAULT));
                GetDataAdapter2.setImageTitleNamee2(tt);

                String s;
                byte[] bytes = json.getString(JSON_IMAGE_TITLE_NAME3).getBytes();
                s= new String(android.util.Base64.decode(bytes, Base64.DEFAULT));
                GetDataAdapter2.setImageTitleNamee3(s);

                String d;
                byte[] bytes1 = json.getString(JSON_IMAGE_TITLE_NAME4).getBytes();
                d= new String(android.util.Base64.decode(bytes1, Base64.DEFAULT));
                GetDataAdapter2.setImageTitleNamee4(d);

                GetDataAdapter2.setImageTitleNamee5(json.getString(JSON_IMAGE_TITLE_NAME5));

                GetDataAdapter2.setImageServerUrl(json.getString(JSON_IMAGE_URL));

                //  progressDialog.dismiss();

            } catch (JSONException e) {

                e.printStackTrace();
            }
            GetDataAdapter1.add(GetDataAdapter2);
        }
        recyclerViewadapter = new RecyclerViewAdapter(GetDataAdapter1, this);
        recyclerView.setAdapter(recyclerViewadapter);
        progressDialog.dismiss();
        mSwipeLayout.setRefreshing(false);

    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    /////////////
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_src, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.down) {
            // Toast.makeText(this, "You Selected share", Toast.LENGTH_LONG).show();
            Intent i=new Intent(FirstActivity.this,DownloadedActivity.class);
            startActivity(i);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
            }
        }else if(item.getItemId() == R.id.font){
           String f= sp.getString("Font",null);
           if(sp.contains("Font")) {
               if (f.equals("zawgyi.ttf")) {
                   change("unicode.ttf");
                   JSON_DATA_WEB_CALL();
               } else {
                   change("zawgyi.ttf");
                   JSON_DATA_WEB_CALL();
               }
           }else{
               change("unicode.ttf");
               JSON_DATA_WEB_CALL();
           }
        }
        return super.onOptionsItemSelected(item);
    }
    protected boolean isOnline2() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void requestPerm() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }
    public void change(String ff) {
        sp = getSharedPreferences(myprefer, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Font", ff);
        editor.commit();
        TypefaceUtils.overrideFont(getApplicationContext(), "SERIF", ff);
    }

    public void get(){
        if(sp.contains("Font")){
            String ff=sp.getString("Font",null);
            TypefaceUtils.overrideFont(getApplicationContext(), "SERIF", ff);
        }
    }
//////////////////
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
    ///////////
}
