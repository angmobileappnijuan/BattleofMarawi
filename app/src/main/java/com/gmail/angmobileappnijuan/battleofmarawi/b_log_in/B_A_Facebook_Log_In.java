package com.gmail.angmobileappnijuan.battleofmarawi.b_log_in;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gmail.angmobileappnijuan.battleofmarawi.R;
import com.gmail.angmobileappnijuan.battleofmarawi.h_data.ImportantData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;


public class B_A_Facebook_Log_In extends AppCompatActivity {

    public static CallbackManager mCallbackManager;
    private ProfileTracker mProfileTracker;
    private AccessTokenTracker accessTokenTracker;

    private LoginButton loginButton;
    private static ProgressDialog progressDialog;

    //Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_b__a__facebook__log__in);

        mCallbackManager = CallbackManager.Factory.create();
        progressDialog = new ProgressDialog(this);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                ImportantData.firebaseUID = firebaseAuth.getCurrentUser().getUid();
            }
        };


        if (isFBLoggedIn()) {
            Toast.makeText(getBaseContext(), "Logging in...Please wait.", Toast.LENGTH_LONG).show();
            loginButton.setVisibility(View.INVISIBLE);
            track();
        } else {
            logInToFaceBookButton();
            loginButton.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    //FOR FACEBOOK LOG IN
    public boolean isFBLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    private void track() {
        if (Profile.getCurrentProfile() == null) {
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                    // profile2 is the new profile
                    ImportantData.userProfile = profile2;
                    mProfileTracker.stopTracking();
                }
            };
            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                    accessTokenTracker.stopTracking();
                }
            };
        } else {
            ImportantData.userProfile = Profile.getCurrentProfile();
        }
        ImportantData.facebookID = AccessToken.getCurrentAccessToken().getUserId();
        handleFacebookAccessToken();
    }
    private void logInToFaceBookButton() {
        loginButton.setReadPermissions("public_profile", "user_posts");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                ImportantData.facebookID = loginResult.getAccessToken().getUserId();
                loginButton.setVisibility(View.INVISIBLE);
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            ImportantData.userProfile = Profile.getCurrentProfile();
                        }
                    };
                } else {
                    ImportantData.userProfile = Profile.getCurrentProfile();
                }
                handleFacebookAccessToken();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getBaseContext(), "Error. Ulitin ulit ang pag-log in.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logOutToFacebook(){
        LoginManager.getInstance().logOut();
        loginButton.setVisibility(View.VISIBLE);
    }

    //FOR FIREBASE LOG IN
    private void handleFacebookAccessToken() {
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            Log.v("XXX","nakapasok sa handleFacebookAccessToken");
            AuthCredential credential = FacebookAuthProvider.getCredential(AccessToken.getCurrentAccessToken().getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.v("XXX","handleFacebookAccessToken success");
                                ImportantData.firebaseUID = mAuth.getCurrentUser().getUid();
                                //TODO
                                //isAlreadyUser2();
                                //makeAnewUser();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.v("XXX","handleFacebookAccessToken fail");
                                Toast.makeText(getBaseContext(), "Sign in failed. Cannot connect to our database", Toast.LENGTH_SHORT).show();
                                logOutToFacebook();
                            }
                        }
                    });
        }else{
            ImportantData.firebaseUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            isAlreadyUser2();

        }


    }

    //USER Queries

    private void isAlreadyUser2() {
        Log.v("XXX","nakapasok sa isAlreadyUser2");
        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(ImportantData.firebaseUID)){
                    Log.v("XXX","nakapasok sa isAlreadyUser2 has child");
                    checkAccessToken();
                }else{
                    //Make new User
                    Log.v("XXX","nakapasok sa isAlreadyUser2 NO CHILD");
                    makeAnewUser();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("XXX","nakapasok sa isAlreadyUser2 ERROR");

            }
        });

    }

    private String getRandomString() {
        int sizeOfRandomString = 10;
        String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private void makeAnewUser() {
        Log.v("XXX","nakapasok sa makeAnewUser");
        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).child(ImportantData.firebaseUID).child("fbID").setValue(ImportantData.facebookID, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.v("XXX","nakapasok sa makeAnewUser fb id written");
                FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).child(ImportantData.firebaseUID).child("status").setValue(0, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Log.v("XXX","nakapasok sa makeAnewUser status written");
                        ArrayList<String> peerList = new ArrayList<String>();
                        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).child(ImportantData.firebaseUID).child("listOfPeers").setValue(peerList, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Log.v("XXX","nakapasok sa makeAnewUser listOfPeers written");
                                FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).child(ImportantData.firebaseUID).child("firstUsedTimeStamp").setValue(ServerValue.TIMESTAMP, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        Log.v("XXX","nakapasok sa makeAnewUser firstUsedTimeStamp written");
                                        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).child(ImportantData.firebaseUID).child("currentTimeStamp").setValue(ServerValue.TIMESTAMP, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                Log.v("XXX","nakapasok sa makeAnewUser currentTimeStamp written");
                                                String accessTokenForApp = getRandomString();
                                                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(ImportantData.MY_PREF_ACCESS_TOKEN, accessTokenForApp).apply();
                                                FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).child(ImportantData.firebaseUID).child("accessTokenForApp").setValue(accessTokenForApp, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                        Log.v("XXX","nakapasok sa makeAnewUser accessTokenForApp written");
                                                        checkAccessToken();
                                                    }
                                                });

                                            }
                                        });

                                    }
                                });

                            }
                        });

                    }
                });

            }
        });
    }

    private void checkAccessToken() {
        Log.v("XXX","nakapasok sa checkAccessToken");
        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).child(ImportantData.firebaseUID).child("accessTokenForApp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s = dataSnapshot.getValue().toString();
                if (s.equals(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(ImportantData.MY_PREF_ACCESS_TOKEN, "default"))) {
                    Log.v("XXX","nakapasok sa checkAccessToken no prob accesstoken");
                    checkTimeStamp();
                } else {
                    Log.v("XXX","nakapasok sa checkAccessToken with prob accesstoken");
                    openDialogForIncorrectAccessToken();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "Cannot connect to Server 2", Toast.LENGTH_SHORT).show();
                logOutToFacebook();

            }


        });
    }

    private void openDialogForIncorrectAccessToken() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("You can access this app only in one Device");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Allow access to this device. (Will delete access to other devices)",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String accessTokenForApp = getRandomString();
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(ImportantData.MY_PREF_ACCESS_TOKEN, accessTokenForApp).apply();
                        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).child(ImportantData.firebaseUID).child("accessTokenForApp").setValue(accessTokenForApp, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                checkAccessToken();
                            }
                        });


                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginManager.getInstance().logOut();
                        loginButton.setVisibility(View.VISIBLE);
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void checkTimeStamp() {
        Log.v("XXX","nakapasok sa checkTimeStamp");
        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).child(ImportantData.firebaseUID).child("currentTimeStamp").setValue(ServerValue.TIMESTAMP, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.v("XXX","nakapasok sa checkTimeStamp nakasulat na ng timestamp");
                getCurrentTimeStamp();
            }
        });

    }

    private void getCurrentTimeStamp(){
        Log.v("XXX","nakapasok sa getCurrentTimeStamp");
        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).child(ImportantData.firebaseUID).child("currentTimeStamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("XXX","nakapasok sa getCurrentTimeStamp nabasa sa database");
                String s = dataSnapshot.getValue().toString();
                long current = new Long(s).longValue();
                getFirstUsedTimeStamp(current);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("XXX","nakapasok sa getCurrentTimeStamp ERROR");
                Toast.makeText(getBaseContext(), "Cannot connect to Server 3", Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logOut();
                loginButton.setVisibility(View.VISIBLE);
            }


        });
    }

    private void getFirstUsedTimeStamp(final long current){
        Log.v("XXX","nakapasok sa getFirstUsedTimeStamp");
        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).child(ImportantData.firebaseUID).child("firstUsedTimeStamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s = dataSnapshot.getValue().toString();
                long started = new Long(s).longValue();
                long numberOfDaysUsed = current-started;
                long month = 2592000000L;
                if(numberOfDaysUsed>month){
                    setUserToPending();
                    Log.v("XXX","nakapasok sa getFirstUsedTimeStamp user to pending");
                }else{
                    openNextActivity();
                    Log.v("XXX","nakapasok sa getFirstUsedTimeStamp open next act");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("XXX","nakapasok sa getFirstUsedTimeStamp ERROR");
                Toast.makeText(getBaseContext(), "Cannot connect to Server 4", Toast.LENGTH_SHORT).show();
                logOutToFacebook();
            }


        });
    }

    private void setUserToPending(){
        Log.v("XXX","nakapasok sa setUserToPending");

        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_ALL_USERS).child(ImportantData.firebaseUID).child("status").setValue(0, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.v("XXX","nakapasok sa setUserToPending open next act");
                openNextActivity();
            }
        });

    }


    private void openNextActivity() {
        Intent intent = new Intent(getBaseContext(), B_D_Pin_Code.class);
        finish();
        startActivity(intent);
    }


    private void kkkk(){
        String s = "v(?.g)[->=B6zZ%?X9k$Bs`E}DZ`B>;4s<m'QYcP'^xKdZ]Ys7zH*eXhusYXPP;b}MT)Yt`>s6G&{_T[&E.Nd5979';Z$WL?h6:?";//book1
        s = "PDp!Qx^nn*Vek?h_{G:rrPeJCTke4$[Ey5LBDq@Zq3}BK@Azr'tWrJJmRh`P>TCpA~CYGX5)R:SS?heE~.xDc,Y!Z-.GzDR*G{`%";//book2
        s = "!4R)7ar>>rLs6jJwy9U?eKvn@5YZgp=E.'b%XXprDJ6.pT`dSR{Fv%-c?+n^LG2hFYA}t5zqe<[Z`NU=5w.(6h5@t2tfZpH75TMnC";//book3
        s = "7.V6Yx**G_7%KdFZU^Uu[P=%eYu4t,#UY*r{dyf7yHa{fUba)+=}{v<RNS9nrWN:@UJjpbS%3BF%y7+]M4Qz+UA6h#JYK4f[R,?hH>";//book4
        s = "fy{pEMQ.R=REfZtt%[=Q7KN?'mKVg@HPK:XXhf^vDRYJ-U;qhSC.uJKf=<6BXRD8(!U}*D!_auV5>cc4X);nWb}&MUT6PeT=5t8;A";//book5
        s = "Emhyg7{<Ta4V;{aLTDnunZNZ}(uK}dj@2<_v4$ExhmQ8(Y6@v23{='U]9&f{zb[wt8p6K$&KH7::vk)J#e}63&Jr7B:L;Uvd8BuJM";//book6
        s = ",XNZ:*]D+b#c3p?cvEg8SKy<+xxxVgpU9Za3/aBEWgwtX+fr?P$/WD$y.8[Sxb;KB=&L&-)sL?7hxxC6@T@J}WGcjeULMz6Y}L7<,Fs";//book7
        s = "Y[M.2q^&AaHt,xxx-`#xx%{>Lu;LD-}E4~~L8V~B?%wmnfQ:;vnD4nLGX6YxN]?r6WX5^xxR?P_9xx7?%qEtp[6^/:Ch7bpbqW2mRF$'";//book8
        s = "SZxxR<?s]axxg9Zn_yr92xj!xx_J)5%wZ$`C{bh7A2J}uuSMjP&H/;$XXmMn->S*2Rm=kxxBhgSCq;G93%R7zaqzVcgsBK}Vh-xx@Y$Uwu";//book9
        s = "y%d3a#qYfAExxnZjW]fK&)xx5a9u)TT`%6`=STCnGRg7?K4vw488W4J}t&a:}%<Yhxx:5Y2hhWz9znsY&[M'}!mZk;DQ4?q4A5fN].s";//book10
        s = "@;:fhWN3W+5grq(z?u<C;%xxG_7*}(Q+.cXQ8C~vkYV)wSHycpKd/k[5JL3q!/a8*xxfwV94EA=yn`:U{}j3^Kf>J,xxTLHA(JhgXFm#";//book11
        s = "m/T3j_pV=Ty4Gs[Laekw-[T/[@cs_e&dxx*:n)t4mz^E<n4xymKdVmE`^,9Qsqr(SU+r.Za9{a7PvSu/c<]*(`ck(EN^j5pCJJpUd";//book12
        s = "fjft{Z_(dyJ(_w9YxxWQx[Bxx?ZL_^N)6HkdeE>K3M8y.'2MGf>+LraJvXAaSxxq,v6`*FJ$k;4:+braE6XN}}_wH(t4K`%3t}hhy_X";//book13
        s = "mRYT!EbxxGsKzxqEz#5?8hg@6VMFj8Nthg%&.{[k-6Vhg*4ee`4b,Dt+UCz6F'VpP!6y?@{r]$~8hgV*zn,j*;xx-pxxr7)?.h}v^:xx#8JT";//book14
        s = "?^k'*V''3y})E*a`T-uXk%43tXBv%7m,&JBEuCQ]<vAFj:CACEub'c3YehgW*:d}v!>%/Vgu_H2vM~,[xxtf:(*mdUX2t*wxKE(jkk";//book15
        s = "s_xx)_a]&m_~Fax}85xnYDmqEq(YZ{-8hg3E%n?<Fxeb*jyzu~>Tzq<%WHJp}X)8{4{8r(j&eZ*v;uZ83cjNcm-<bU`tTsFH,#88P]";//book16
        s = "kC'f~{Se&Kqe2d=+MgV^&[K,`KhgZ~cq#r[>q*asjJNx2)}hU%HsLE^w55xxVWWVGGmn3at5q#4JdRv-r~c[7+Hx5>M7<d`kNZ{-c(";//book17
        s = "w}`3y.5Dhw<2QN,!~^-U,DQK-{&xKdLFe9hgVc+N5Kd;y-s/Ta.gRft^A>3{`tY3exxsT4Xk#.E^D.2+j*BG3aaJ^Qb5R].z$;Qh<~";//book18
        s = "n,Y@D29%*~+S>V2z<,74HJz.dUU]Gp[e?XqBSR_jsEApmd)&Xkn]Gj3*[tFE:G*!eW}?D(+'%Y3.n=Nz;Z3WkRnD':6}9K[]sFM%";//book19
        s = ")$X(4e^xxuDcjHfn$z?bXzp&Y-zW@.}XGV3]myt]%5dNW]wMQ2K.MWrP878Cs{L:N^5rRxx]`H/B2CG=3By@8{_,;S4$}6_{?5?WT#";//book20
        s = "sF&Y4fkDwhyxx&ynM7&/TAYR+`$w$8pp+DxAd%K}yzLZYMatJ3S37Y$a{g/vw[[ZL?'y?4[-y!S,f+e%()DKf}t/h2w;Ta&t'm@D]*E";//book21




    }

    public static void logOutToFB(){
        LoginManager.getInstance().logOut();
    }

}
