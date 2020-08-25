package com.gmail.angmobileappnijuan.battleofmarawi.b_log_in;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.gmail.angmobileappnijuan.battleofmarawi.R;
import com.gmail.angmobileappnijuan.battleofmarawi.c_main.C_A_MainActivity;
import com.gmail.angmobileappnijuan.battleofmarawi.h_data.ImportantData;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class B_D_Pin_Code extends AppCompatActivity {

    private static boolean isNewPin;
    private static TextView textView;
    private static Button buttonReset;
    private static String restoredText;
    private static EditText editText1;
    private static EditText editText2;
    private static EditText editText3;
    private static EditText editText4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_b__d__pin__code);

        SharedPreferences prefs = getSharedPreferences(ImportantData.MY_PIN, MODE_PRIVATE);
        restoredText = prefs.getString(ImportantData.MY_PIN_TEXT, "defValue");

        textView = (TextView)findViewById(R.id.instruction_text);
        buttonReset = (Button)findViewById(R.id.button_reset);
        editText1 = (EditText)findViewById(R.id.edit1);
        editText2 = (EditText)findViewById(R.id.edit2);
        editText3 = (EditText)findViewById(R.id.edit3);
        editText4 = (EditText)findViewById(R.id.edit4);


        if(restoredText.equals("defValue")){
            //Enter a pin Code
            isNewPin=true;
            textView.setText("Please enter your new PIN.");
            buttonReset.setVisibility(View.INVISIBLE);
        }else{
            //Make a pin Code
            textView.setText("Please enter your PIN.");
            isNewPin=false;
            buttonReset.setVisibility(View.VISIBLE);
        }

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogReset();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNewPin){
                    Snackbar.make(view, "Save Pin Code?", Snackbar.LENGTH_LONG)
                            .setAction("SAVE", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if(editText4.getText().toString().trim().equalsIgnoreCase("")) {
                                        editText4.setError("This field can not be blank");
                                    }else if(editText2.getText().toString().trim().equalsIgnoreCase("")) {
                                        editText2.setError("This field can not be blank");
                                    }else if (editText1.getText().toString().trim().equalsIgnoreCase("")) {
                                        editText1.setError("This field can not be blank");
                                    }else if(editText3.getText().toString().trim().equalsIgnoreCase("")) {
                                        editText3.setError("This field can not be blank");
                                    }else{
                                        SharedPreferences.Editor editor = getSharedPreferences(ImportantData.MY_PIN, MODE_PRIVATE).edit();
                                        editor.putString(ImportantData.MY_PIN_TEXT, editText1.getText().toString()+editText2.getText().toString()+editText3.getText().toString()+editText4.getText().toString());
                                        editor.apply();
                                        Intent intent = new Intent(getBaseContext(), C_A_MainActivity.class);
                                        finish();
                                        startActivity(intent);
                                    }
                                }
                            }).show();
                }else{
                    if(editText4.getText().toString().trim().equalsIgnoreCase("")) {
                        editText4.setError("This field can not be blank");
                    }else if(editText2.getText().toString().trim().equalsIgnoreCase("")) {
                        editText2.setError("This field can not be blank");
                    }else if (editText1.getText().toString().trim().equalsIgnoreCase("")) {
                        editText1.setError("This field can not be blank");
                    }else if(editText3.getText().toString().trim().equalsIgnoreCase("")) {
                        editText3.setError("This field can not be blank");
                    }else if (restoredText.equals(editText1.getText().toString()+editText2.getText().toString()+editText3.getText().toString()+editText4.getText().toString())) {
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt(ImportantData.MY_PREF_FAIL_ATTEMPT,0).commit();
                        Intent intent = new Intent(getBaseContext(), C_A_MainActivity.class);
                        finish();
                        startActivity(intent);
                    }else{
                        threeWrongPin(view);

                    }

                }

            }
        });

    }

    private void openDialogReset() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("You will need to validate your account again to gain access to content if you reset your Pin Code.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Reset Pin",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).child(ImportantData.firebaseUID).child("status").setValue(0, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                B_A_Facebook_Log_In.logOutToFB();
                                Toast.makeText(getBaseContext(), "Re-open App and Log in again", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void threeWrongPin(final View view){
        int x = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(ImportantData.MY_PREF_FAIL_ATTEMPT,0);
        x++;
        if(x==3){
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt(ImportantData.MY_PREF_FAIL_ATTEMPT,0).commit();
            FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).child(ImportantData.firebaseUID).child("status").setValue(0, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    B_A_Facebook_Log_In.logOutToFB();
                    SharedPreferences.Editor editor = getSharedPreferences(ImportantData.MY_PIN,  MODE_PRIVATE).edit();
                    editor.putString(ImportantData.MY_PIN_TEXT, "defValue");
                    editor.commit();
                    Snackbar.make(view, "Error. Wrong Pin Code. 2 Attempts remaining...", Snackbar.LENGTH_LONG)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();

                    Toast.makeText(getBaseContext(), "Maximum tries reached. Removing Access.", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }else if(x==1){
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt(ImportantData.MY_PREF_FAIL_ATTEMPT,x).commit();
            Snackbar.make(view, "Error. Wrong Pin Code. 2 Attempts remaining...", Snackbar.LENGTH_LONG)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
        }else if(x==2){
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt(ImportantData.MY_PREF_FAIL_ATTEMPT,x).commit();
            Snackbar.make(view, "Error. Wrong Pin Code.1 Attempt remaining...", Snackbar.LENGTH_LONG)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
        }else{

        }

    }

}
