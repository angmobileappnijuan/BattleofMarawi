package com.gmail.angmobileappnijuan.battleofmarawi.c_main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;
import com.gmail.angmobileappnijuan.battleofmarawi.R;
import com.gmail.angmobileappnijuan.battleofmarawi.d_drawer_items.D_A_Messages;
import com.gmail.angmobileappnijuan.battleofmarawi.d_drawer_items.D_B_Web_Viewer;
import com.gmail.angmobileappnijuan.battleofmarawi.d_drawer_items.D_D_Video;
import com.gmail.angmobileappnijuan.battleofmarawi.h_data.ImportantData;
import com.gmail.angmobileappnijuan.battleofmarawi.i_adapter.BookAdapter;
import com.gmail.angmobileappnijuan.battleofmarawi.z_model.BookCover;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.vending.expansion.downloader.Helpers;

import java.util.ArrayList;

public class C_A_MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AdView mAdView;
    private static ArrayList<BookCover> listOfBooks;
    private GridView gridView;

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__a__main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activity = this;

        if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(ImportantData.MY_PREF_FIRST_TIME_VIDEO, true)) {
            Intent intent = new Intent(getBaseContext(), D_D_Video.class);
            startActivity(intent);
        }


        ProfilePictureView profilePicture = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
        profilePicture.setProfileId(ImportantData.facebookID);
        TextView name = (TextView) findViewById(R.id.facebookName);
        name.setText(ImportantData.userProfile.getName());

        gridView = (GridView) findViewById(R.id.gridview);
        BookAdapter adapter = new BookAdapter(getBaseContext(), getBookList());
        gridView.setAdapter(adapter);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        //TODO MobileAds.initialize(this, "ca-app-pub-2633959536645156~5825790607");
        mAdView = findViewById(R.id.adView);
        //TODO mAdView.setAdUnitId("ca-app-pub-2633959536645156/5849841336");
        mAdView.setVisibility(View.GONE);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mAdView.setVisibility(View.GONE);
            }

        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.c__a__main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.messageOfCGPA) {
            ImportantData.messageToRead = "CGPA Message";
            Intent intent = new Intent(getBaseContext(), D_A_Messages.class);
            startActivity(intent);
        } else if (id == R.id.messageOfDirector) {
            ImportantData.messageToRead = "Director Message";
            Intent intent = new Intent(getBaseContext(), D_A_Messages.class);
            startActivity(intent);

        } else if (id == R.id.visitFacebook) {
            this.startActivity(getOpenFacebookIntent(getBaseContext()));

        } else if (id == R.id.nav_contactUs) {
            sendMessage();

        } else if (id == R.id.video) {
            Intent intent = new Intent(getBaseContext(), D_D_Video.class);
            startActivity(intent);
        }else if (id == R.id.about_orc) {
            ImportantData.webToOpen = "Operations Research Center";
            Intent intent = new Intent(getBaseContext(), D_B_Web_Viewer.class);
            startActivity(intent);
        }else if (id == R.id.about_developer) {
            ImportantData.webToOpen = "angmobileappnijuan";
            Intent intent = new Intent(getBaseContext(), D_B_Web_Viewer.class);
            startActivity(intent);
        } else if (id == R.id.disclaimer) {
            ImportantData.webToOpen = "disclaimer";
            Intent intent = new Intent(getBaseContext(), D_B_Web_Viewer.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ArrayList<BookCover> getBookList() {


        if (listOfBooks == null) {
            listOfBooks = new ArrayList<BookCover>();
            listOfBooks.add(new BookCover("Marawi and Beyond", "Mother Book", R.drawable.motherbookcovericon));
            listOfBooks.add(new BookCover("Operations", "Book 1", R.drawable.book_cover_1));
            listOfBooks.add(new BookCover("Intelligence", "Book 2", R.drawable.book_cover_2));
            listOfBooks.add(new BookCover("IO", "Book 3", R.drawable.book_cover_3));
            listOfBooks.add(new BookCover("Combined Arms", "Book 4", R.drawable.book_cover_4));
            listOfBooks.add(new BookCover("Combat Engineers", "Book 5", R.drawable.book_cover_5));
            listOfBooks.add(new BookCover("Armor Operations", "Book 6", R.drawable.book_cover_6));
            listOfBooks.add(new BookCover("Waterborne Operations", "Book 7", R.drawable.book_cover_7));
            listOfBooks.add(new BookCover("SOF", "Book 8", R.drawable.book_cover_8));
            listOfBooks.add(new BookCover("Fire Support", "Book 9", R.drawable.book_cover_9));
            listOfBooks.add(new BookCover("CAS", "Book 10", R.drawable.book_cover_10));
            listOfBooks.add(new BookCover("CMOCC", "Book 11", R.drawable.book_cover_11));
            listOfBooks.add(new BookCover("Public Information", "Book 12", R.drawable.book_cover_12));
            listOfBooks.add(new BookCover("Stakeholder Engagement", "Book 13", R.drawable.book_cover_13));
            listOfBooks.add(new BookCover("PysOps", "Book 14", R.drawable.book_cover_14));
            listOfBooks.add(new BookCover("Social Media", "Book 15", R.drawable.book_cover_15));
            listOfBooks.add(new BookCover("P/CVE", "Book 16", R.drawable.book_cover_16));
            listOfBooks.add(new BookCover("Female Warrior", "Book 17", R.drawable.book_cover_17));
            listOfBooks.add(new BookCover("Sustainment", "Book 18", R.drawable.book_cover_18));
            listOfBooks.add(new BookCover("Signal", "Book 19", R.drawable.book_cover_19));
            listOfBooks.add(new BookCover("Leadership", "Book 20", R.drawable.book_cover_20));
            listOfBooks.add(new BookCover("Honoring the Sacrifices", "Book 21", R.drawable.book_cover_21));

        } else {

        }

        return listOfBooks;
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + ImportantData.FACEBOOK_PAGE_ID));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + ImportantData.FACEBOOK_PAGE_ID));
        }
    }

    private void sendMessage() {
        Uri uri = Uri.parse("fb-messenger://user/" + ImportantData.FACEBOOK_PAGE_ID);

        Intent toMessenger = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(toMessenger);
        try {
            startActivity(toMessenger);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getBaseContext(), "Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
        }
    }



}
