package com.gmail.angmobileappnijuan.battleofmarawi.e_book;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.gmail.angmobileappnijuan.battleofmarawi.R;
import com.gmail.angmobileappnijuan.battleofmarawi.c_main.C_A_MainActivity;
import com.gmail.angmobileappnijuan.battleofmarawi.f_helper.ZipContentProvider;
import com.gmail.angmobileappnijuan.battleofmarawi.h_data.ImportantData;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class E_Book extends AppCompatActivity {

    private PDFView pdfView;
    private static int pageNumberToSave;
    private static int totalPages;

    private InterstitialAd mInterstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_e__book);

        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_PADDING).child(ImportantData.bookToOpen.getBookNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s = dataSnapshot.getValue().toString();
                if (ImportantData.bookToOpen.getBookNumber().equals(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(ImportantData.LAST_BOOK_READ, "none"))) {
                    int pageToRead = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(ImportantData.LAST_BOOK_READ_PAGE, 0);
                    Toast.makeText(getBaseContext(), Integer.toString(pageToRead), Toast.LENGTH_SHORT).show();
                    loadPDF(s, pageToRead);
                } else {
                    loadPDF(s, 0);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });



        //TODO MobileAds.initialize(this, "ca-app-pub-2633959536645156~5825790607");
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        pageNumberToSave = 0;
        totalPages=0;

        Toast.makeText(getBaseContext(), "Decrypting file...", Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_PADDING).child(ImportantData.bookToOpen.getBookNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s = dataSnapshot.getValue().toString();
                if (ImportantData.bookToOpen.getBookNumber().equals(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(ImportantData.LAST_BOOK_READ, "none"))) {
                    int pageToRead = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(ImportantData.LAST_BOOK_READ_PAGE, 0);
                    loadPDF(s, pageToRead);
                    Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                } else {
                    loadPDF(s, 0);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void loadPDF(String s, int page) {
        pdfView = (PDFView)findViewById(R.id.pdfView);
        File file = new File(getBaseContext().getFilesDir().getAbsolutePath()+"temp.pdf");

        pdfView.fromFile(file)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(page)
                .password(s)
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        showAd(page, pageCount);
                        pageNumberToSave = page;
                        totalPages=pageCount;
                    }
                })
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(getBaseContext(), "PDF Error: " + t.getMessage()+"<<--", Toast.LENGTH_SHORT).show();

                    }
                })
                .pageFitPolicy(FitPolicy.BOTH)
                //.password(s)
                .scrollHandle(null)
                .enableAntialiasing(true)
                .load();



    }

    private void showAd(int currentPage, int pageCount) {
        if (currentPage == pageCount&&currentPage!=0) {
            mInterstitialAd = new InterstitialAd(this);
            //TODO mInterstitialAd.setAdUnitId("ca-app-pub-2633959536645156/6732528225");
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    mInterstitialAd.show();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when the ad is displayed.
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when when the interstitial ad is closed.
                }
            });
        } else if (currentPage % 25 == 0) {
            mInterstitialAd = new InterstitialAd(this);
            //TODO mInterstitialAd.setAdUnitId("ca-app-pub-2633959536645156/6732528225");
            mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                    mInterstitialAd.show();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when the ad is displayed.
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when when the interstitial ad is closed.
                }
            });

        }

    }

    @Override
    public void onBackPressed() {
        if(isEndOfTheBook(pageNumberToSave, totalPages)){
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt(ImportantData.LAST_BOOK_READ_PAGE, 0).apply();
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(ImportantData.LAST_BOOK_READ, "none").apply();
        }else{
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt(ImportantData.LAST_BOOK_READ_PAGE, pageNumberToSave).apply();
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(ImportantData.LAST_BOOK_READ, ImportantData.bookToOpen.getBookNumber()).apply();
        }
        super.onBackPressed();
    }

    private boolean isEndOfTheBook(int current, int allPages){
        if(current==(allPages-1)){
            return true;
        }else{
            return  false;
        }

    }


}
