package com.gmail.angmobileappnijuan.battleofmarawi.i_adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.facebook.login.LoginManager;
import com.gmail.angmobileappnijuan.battleofmarawi.b_log_in.B_A_Facebook_Log_In;
import com.gmail.angmobileappnijuan.battleofmarawi.c_main.C_A_MainActivity;
import com.gmail.angmobileappnijuan.battleofmarawi.d_drawer_items.D_D_Video;
import com.gmail.angmobileappnijuan.battleofmarawi.e_book.E_Book;
import com.gmail.angmobileappnijuan.battleofmarawi.f_helper.ZipHelper;
import com.gmail.angmobileappnijuan.battleofmarawi.h_data.ImportantData;
import com.gmail.angmobileappnijuan.battleofmarawi.z_model.BookCover;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import com.gmail.angmobileappnijuan.battleofmarawi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Developer on 4/29/2018.
 */

public class BookAdapter extends BaseAdapter {


    private final Context mContext;
    private final ArrayList<BookCover> bookCovers;

    private static AlertDialog alertDialog;



    // 1
    public BookAdapter(Context context, ArrayList<BookCover> bookCovers) {
        this.mContext = context;
        this.bookCovers = bookCovers;
    }

    // 2
    @Override
    public int getCount() {
        return bookCovers.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.adapter_book_cover, null);
        }

        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_cover_art);
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_book_name);
        final TextView bookNumberTextView = (TextView)convertView.findViewById(R.id.textview_book_number);

        imageView.setImageResource(bookCovers.get(position).getResValue());
        nameTextView.setText(bookCovers.get(position).getBookTitle());
        bookNumberTextView.setText(bookCovers.get(position).getBookNumber());
        final int x = position;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials(x);
            }
        });


        return convertView;

    }

    private void checkCredentials(final int position){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(C_A_MainActivity.activity);
        builder1.setMessage("Checking Credentials... Pls wait..");
        builder1.setCancelable(false);
        alertDialog = builder1.create();
        alertDialog.show();
        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).child(ImportantData.firebaseUID).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s = dataSnapshot.getValue().toString();
                int status = Integer.parseInt(s);
                if (status==0){
                    //Pending
                    alertDialog.dismiss();
                    Toast.makeText(mContext, "Please Validate your Account", Toast.LENGTH_SHORT).show();
                }else if(status==1){
//                    try {
//                        ZipResourceFile expansionFile = APKExpansionSupport
//                                .getAPKExpansionZipFile(C_A_MainActivity.activity, 1, 0);
//
//                        ZipResourceFile.ZipEntryRO[] zip = expansionFile.getAllEntries();
//                        Log.e("", "zip[0].isUncompressed() : " + zip[0].isUncompressed());
//                        Log.e("",
//                                "mFile.getAbsolutePath() : "
//                                        + zip[0].mFile.getAbsolutePath());
//                        Log.e("", "mFileName : " + zip[0].mFileName);
//                        Log.e("", "mZipFileName : " + zip[0].mZipFileName);
//                        Log.e("", "mCompressedLength : " + zip[0].mCompressedLength);
//
//                        File file = new File(C_A_MainActivity.activity.getFilesDir().getAbsolutePath() );
//                        ZipHelper.unzip(zip[0].mZipFileName, file);
//
//                        if (file.exists()) {
//                            Log.e("", "unzipped : " + file.getAbsolutePath());
//                        }
//
//                        ImportantData.bookToOpen = bookCovers.get(position);
//                        alertDialog.dismiss();
//                        Intent intent = new Intent(C_A_MainActivity.activity, E_Book.class);
//                        C_A_MainActivity.activity.startActivity(intent);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }



                    try {
                        ZipResourceFile expansionFile = APKExpansionSupport.getAPKExpansionZipFile(C_A_MainActivity.activity, 1, 0);
                        InputStream input = expansionFile.getInputStream(bookCovers.get(position).getBookNumber()+".pdf");
                        byte[] buffer = new byte[input.available()];
                        input.read(buffer);
                        File targetFile = new File(C_A_MainActivity.activity.getFilesDir().getAbsolutePath()+"temp.pdf");
                        OutputStream outStream = new FileOutputStream(targetFile);
                        outStream.write(buffer);
                        ImportantData.bookToOpen = bookCovers.get(position);
                        alertDialog.dismiss();
                        Intent intent = new Intent(C_A_MainActivity.activity, E_Book.class);
                        C_A_MainActivity.activity.startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }else if(status==2){
                    //Active

                }else if(status==3){
                    //No status 3
                    alertDialog.dismiss();
                    Toast.makeText(mContext, "Database Error: Error 301", Toast.LENGTH_SHORT).show();
                }else if(status==4){
                    alertDialog.dismiss();
                    Toast.makeText(mContext, "Account Disabled. Pls contact us thru the 'Contact Us' found in the app's menu", Toast.LENGTH_SHORT).show();
                }else{
                    alertDialog.dismiss();
                    Toast.makeText(mContext, "Database Error: Error 401", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(mContext, "Database Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }


        });

    }





}
