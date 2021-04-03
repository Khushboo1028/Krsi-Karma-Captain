package com.krsikarma.captain.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.krsikarma.captain.R;

public class PrivacyPolicy extends AppCompatActivity {

    public static final String TAG = "PrivacyPolicy";
    String textHeading;

    WebView webView;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        init();

        if(textHeading.equals(getString(R.string.privacy_policy))){
            url = "https://sites.google.com/view/krsi-karma-privacy-policy/home";
        }else{

            //TODO: Change to terms of use url
            url = "https://sites.google.com/view/krsi-karma-privacy-policy/home";
        }

        webView.loadUrl(url);
    }

    private void init(){
        textHeading = getIntent().getStringExtra("textHeading");
        webView = (WebView) findViewById(R.id.webView);
    }


}