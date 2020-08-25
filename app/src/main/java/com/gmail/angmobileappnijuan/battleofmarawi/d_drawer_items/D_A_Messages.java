package com.gmail.angmobileappnijuan.battleofmarawi.d_drawer_items;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.gmail.angmobileappnijuan.battleofmarawi.R;
import com.gmail.angmobileappnijuan.battleofmarawi.h_data.ImportantData;

public class D_A_Messages extends AppCompatActivity {

    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d__a__messages);


        pdfView = (PDFView) findViewById(R.id.pdfViewMessages);
        pdfView.fromAsset(ImportantData.messageToRead + ".pdf")
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(0)
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(getBaseContext(), "PDF Error", Toast.LENGTH_SHORT).show();
                    }
                })
                .pageFitPolicy(FitPolicy.HEIGHT)
                .scrollHandle(null)
                .enableAntialiasing(true)
                .load();
    }
}
