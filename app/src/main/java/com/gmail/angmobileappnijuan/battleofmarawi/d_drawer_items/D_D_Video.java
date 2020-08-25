package com.gmail.angmobileappnijuan.battleofmarawi.d_drawer_items;

import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.gmail.angmobileappnijuan.battleofmarawi.R;
import com.gmail.angmobileappnijuan.battleofmarawi.h_data.ImportantData;

public class D_D_Video extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d__d__video);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        VideoView videoView = (VideoView) findViewById(R.id.videoview);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.video;
        videoView.setVideoURI(Uri.parse(path));
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(ImportantData.MY_PREF_FIRST_TIME_VIDEO, false).apply();

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.start();
    }
}
