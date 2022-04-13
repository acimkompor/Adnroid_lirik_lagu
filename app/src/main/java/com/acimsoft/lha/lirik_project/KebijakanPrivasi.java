package com.acimsoft.lha.lirik_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

public class KebijakanPrivasi extends AppCompatActivity {

    WebView webview;
    String url = "file:///android_asset/index.html";
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kebijakan_privasi);

        webview = findViewById(R.id.webPrivacy);
        imgBack = findViewById(R.id.imgBack);
        webview.loadUrl(url);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
