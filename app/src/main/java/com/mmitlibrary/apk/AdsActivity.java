package com.mmitlibrary.apk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class AdsActivity extends AppCompatActivity {
ImageView imgView;
Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        imgView=findViewById(R.id.imgview);
        btn=(Button)findViewById(R.id.btn);

        Intent intent = getIntent();
        String link = intent.getStringExtra("link");

        Picasso.get().load(link).into(imgView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
