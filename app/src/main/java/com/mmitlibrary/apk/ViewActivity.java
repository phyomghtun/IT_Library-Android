package com.mmitlibrary.apk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mmitlibrary.apk.R;

public class ViewActivity extends AppCompatActivity {
    String st;
    NetworkImageView networkImageView;
    ImageLoader imageLoader1;
    Button btn,cancle;
    private InterstitialAd mInterstitialAd;
    AdRequest adRequest1;
    ProgressDialog mProgressDialog;
    String image_link, link, name, bookname;
    String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        overridePendingTransition(R.anim.fade,0);

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/ITLibrary").getPath());

        if (!folder.exists()) {
          folder.mkdir();
        }

        Intent intent = getIntent();
        image_link = intent.getStringExtra("img_link");
        link = intent.getStringExtra("link");
        name = intent.getStringExtra("name");
        bookname = intent.getStringExtra("book_name");




        btn = (Button) findViewById(R.id.btn);
        cancle=findViewById(R.id.cancle);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0,R.anim.fade_out);
            }
        });

        networkImageView = (NetworkImageView) findViewById(R.id.image_view);
        imageLoader1 = ServerImageParseAdapter.getInstance(this).getImageLoader();

        imageLoader1.get(image_link,
                ImageLoader.getImageListener(
                        networkImageView,//Server Image
                        R.drawable.icon,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );

        networkImageView.setImageUrl(image_link, imageLoader1);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/ITLibrary").getPath() + File.separator + bookname);


        if (file.exists()) {
            btn.setText("Read");
            state = "exist";
        } else {
            btn.setText("Download");
            state = "noexist";
            ///////////////
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.equals("exist")) {
                    Intent i=new Intent(ViewActivity.this,ReadActivity.class);
                    i.putExtra("bookname",bookname);
                    startActivity(i);

                } else {
                    Toast.makeText(ViewActivity.this, "Download", Toast.LENGTH_SHORT).show();
                    mProgressDialog = new ProgressDialog(ViewActivity.this);
                    mProgressDialog.setMessage("Downloading");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setCancelable(false);

                    final ViewActivity.DownloadTask downloadTask = new ViewActivity.DownloadTask(ViewActivity.this);
                    downloadTask.execute(link);
                    state = "exist";
                }
            }
        });


    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/ITLibrary").getPath() + File.separator + bookname);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show();
            btn.setText("Read");

            adRequest1 = new AdRequest.Builder().build();
            mInterstitialAd = new InterstitialAd(ViewActivity.this);
            mInterstitialAd.setAdUnitId("ca-app-pub-8216298059462584/6875831473");
            mInterstitialAd.loadAd(adRequest1);
            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }
            });

        }
    }
}
