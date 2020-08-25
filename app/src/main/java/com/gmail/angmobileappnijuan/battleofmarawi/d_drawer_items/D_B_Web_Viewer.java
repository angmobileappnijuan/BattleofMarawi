package com.gmail.angmobileappnijuan.battleofmarawi.d_drawer_items;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.gmail.angmobileappnijuan.battleofmarawi.R;
import com.gmail.angmobileappnijuan.battleofmarawi.h_data.ImportantData;

public class D_B_Web_Viewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d__b__web__viewer);

        WebView wv;
        wv = (WebView) findViewById(R.id.webView);
        wv.loadUrl("file:///android_asset/"+ImportantData.webToOpen+".htm");
    }
}
