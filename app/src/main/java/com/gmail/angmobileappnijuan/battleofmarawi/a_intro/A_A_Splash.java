package com.gmail.angmobileappnijuan.battleofmarawi.a_intro;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Messenger;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.gmail.angmobileappnijuan.battleofmarawi.BuildConfig;
import com.gmail.angmobileappnijuan.battleofmarawi.R;
import com.gmail.angmobileappnijuan.battleofmarawi.b_log_in.B_A_Facebook_Log_In;
import com.gmail.angmobileappnijuan.battleofmarawi.b_log_in.B_B_Update;
import com.gmail.angmobileappnijuan.battleofmarawi.b_log_in.B_C_Maintenance;
import com.gmail.angmobileappnijuan.battleofmarawi.f_helper.DownloaderService;
import com.gmail.angmobileappnijuan.battleofmarawi.h_data.ImportantData;
import com.google.android.vending.expansion.downloader.Constants;
import com.google.android.vending.expansion.downloader.DownloadProgressInfo;
import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;
import com.google.android.vending.expansion.downloader.DownloaderServiceMarshaller;
import com.google.android.vending.expansion.downloader.Helpers;
import com.google.android.vending.expansion.downloader.IDownloaderClient;
import com.google.android.vending.expansion.downloader.IDownloaderService;
import com.google.android.vending.expansion.downloader.IStub;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.DataInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

public class A_A_Splash extends AppCompatActivity implements IDownloaderClient {

    private static TextView text;
    private static RelativeLayout mDownloadViewGroup;
    private static ProgressBar mDownloadProgressBar;
    private static TextView mProgressPercentTextView;
    private static RelativeLayout mainSplash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__a__splash);
//        getHashKey();
        mDownloadViewGroup = (RelativeLayout)findViewById(R.id.downloadViewGroup);
        mDownloadProgressBar = (ProgressBar)findViewById(R.id.downloadProgressBar);
        mProgressPercentTextView = (TextView) findViewById(R.id.downloadProgressPercentTextView);
        mainSplash = (RelativeLayout)findViewById(R.id.splash_relativelayout);
        text = (TextView)findViewById(R.id.statustext);
        text.setVisibility(View.INVISIBLE);

        isGetMaintananceValue();

//        if (isNetworkAvailable()) {
//            if (!expansionFilesDelivered()) {
//                try {
//                    Intent launchIntent = A_A_Splash.this.getIntent();
//                    Intent intentToLaunchThisActivityFromNotification = new Intent(A_A_Splash.this, A_A_Splash.this.getClass());
//                    intentToLaunchThisActivityFromNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intentToLaunchThisActivityFromNotification.setAction(launchIntent.getAction());
//
//                    if (launchIntent.getCategories() != null) {
//                        for (String category : launchIntent.getCategories()) {
//                            intentToLaunchThisActivityFromNotification.addCategory(category);
//                        }
//                    }
//
//                    // Build PendingIntent used to open this activity from
//                    // Notification
//                    PendingIntent pendingIntent = PendingIntent.getActivity(A_A_Splash.this, 0, intentToLaunchThisActivityFromNotification, PendingIntent.FLAG_UPDATE_CURRENT);
//                    // Request to start the download
//                    int startResult = DownloaderClientMarshaller.startDownloadServiceIfRequired(this, pendingIntent, DownloaderService.class);
//
//                    if (startResult != DownloaderClientMarshaller.NO_DOWNLOAD_REQUIRED) {
//                        // The DownloaderService has started downloading the files, show progress
//                        initializeDownloadUI();
//                        mainSplash.setVisibility(View.GONE);
//                        return;
//                    } // otherwise, download not needed so we fall through to the app
//                } catch (PackageManager.NameNotFoundException e) {
//                    Log.e("PackError", "Cannot find package!", e);
//                }
//
//            } else {
//                validateXAPKZipFiles();
//            }
//        } else {
//            Intent intent = new Intent(getBaseContext(), A_B_No_Network.class);
//            startActivity(intent);
//            finish();
//        }


    }

    private void initializeDownloadUI(){
        mDownloadViewGroup.setVisibility(View.VISIBLE);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.gmail.angmobileappnijuan.battleofmarawistory", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Toast.makeText(getBaseContext(), Base64.encodeToString(md.digest(), Base64.DEFAULT), Toast.LENGTH_SHORT).show();
                Log.v("KeyHashX:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    private void isGetMaintananceValue() {
        text.setVisibility(View.VISIBLE);
        text.setText("Initializing...");
        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_REFERENCE).child(ImportantData.FIREBASE_ISMAINTANANCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                text.setVisibility(View.VISIBLE);
                text.setText("1/3");
                boolean isMain = (boolean) dataSnapshot.getValue();
                isMaintananceInEffect(isMain);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "Cannot connect to Server 1", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), A_B_No_Network.class);
                finish();
                startActivity(intent);

            }


        });


    }

    private void isMaintananceInEffect(boolean valueFromDatabase) {
        if (valueFromDatabase) {
            Intent intent = new Intent(getBaseContext(), B_C_Maintenance.class);
            finish();
            startActivity(intent);
        } else {
            isGetAppVersion();

        }
    }

    private void isGetAppVersion() {

        FirebaseDatabase.getInstance().getReference().child(ImportantData.FIREBASE_REFERENCE).child(ImportantData.FIREBASE_CURRENT_APP_VERSION).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                text.setVisibility(View.VISIBLE);
                text.setText("2/3");
                String s = dataSnapshot.getValue().toString();
                int currentVersion = Integer.parseInt(s);
                isUpdatedVersion(currentVersion);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "Error in Contacting Server 2", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), A_B_No_Network.class);
                finish();
                startActivity(intent);

            }


        });


    }

    private void isUpdatedVersion(int supportedVersion) {
        if (supportedVersion > BuildConfig.VERSION_CODE) {
            text.setVisibility(View.VISIBLE);
            text.setText("Success");
            Intent intent = new Intent(getBaseContext(), B_B_Update.class);
            finish();
            startActivity(intent);
        } else {
            Intent intent = new Intent(getBaseContext(), B_A_Facebook_Log_In.class);
            startActivity(intent);
            finish();
        }

    }



    //APK EXPANSION
    private IDownloaderService mRemoteService;
    private IStub mDownloaderClientStub;
    private int mState;
    private boolean mCancelValidation;

    // region Expansion Downloader
    @Override
    public void onServiceConnected(Messenger m) {
        mRemoteService = DownloaderServiceMarshaller.CreateProxy(m);
        mRemoteService.onClientUpdated(mDownloaderClientStub.getMessenger());
    }

    /**
     * The download state should trigger changes in the UI --- it may be useful
     * to show the state as being indeterminate at times. This sample can be
     * considered a guideline.
     */
    @Override
    public void onDownloadStateChanged(int newState) {
        setState(newState);
        boolean showDashboard = true;
        boolean showCellMessage = false;
        boolean paused;
        boolean indeterminate;
        switch (newState) {
            case IDownloaderClient.STATE_IDLE:
                // STATE_IDLE means the service is listening, so it's
                // safe to start making calls via mRemoteService.
                paused = false;
                indeterminate = true;
                break;
            case IDownloaderClient.STATE_CONNECTING:
            case IDownloaderClient.STATE_FETCHING_URL:
                showDashboard = true;
                paused = false;
                indeterminate = true;
                break;
            case IDownloaderClient.STATE_DOWNLOADING:
                paused = false;
                showDashboard = true;
                indeterminate = false;
                break;

            case IDownloaderClient.STATE_FAILED_CANCELED:
            case IDownloaderClient.STATE_FAILED:
            case IDownloaderClient.STATE_FAILED_FETCHING_URL:
            case IDownloaderClient.STATE_FAILED_UNLICENSED:
                paused = true;
                showDashboard = false;
                indeterminate = false;
                break;
            case IDownloaderClient.STATE_PAUSED_NEED_CELLULAR_PERMISSION:
            case IDownloaderClient.STATE_PAUSED_WIFI_DISABLED_NEED_CELLULAR_PERMISSION:
                showDashboard = false;
                paused = true;
                indeterminate = false;
                showCellMessage = true;
                break;

            case IDownloaderClient.STATE_PAUSED_BY_REQUEST:
                paused = true;
                indeterminate = false;
                break;
            case IDownloaderClient.STATE_PAUSED_ROAMING:
            case IDownloaderClient.STATE_PAUSED_SDCARD_UNAVAILABLE:
                paused = true;
                indeterminate = false;
                break;
            case IDownloaderClient.STATE_COMPLETED:
                showDashboard = false;
                paused = false;
                indeterminate = false;
                validateXAPKZipFiles();
                return;
            default:
                paused = true;
                indeterminate = true;
                showDashboard = true;
        }
        int newDashboardVisibility = showDashboard ? View.VISIBLE : View.GONE;
        if (mDownloadViewGroup.getVisibility() != newDashboardVisibility) {
            mDownloadViewGroup.setVisibility(newDashboardVisibility);
        }
        mDownloadProgressBar.setIndeterminate(indeterminate);
    }

    /**
     * Sets the state of the various controls based on the progressinfo object
     * sent from the downloader service.
     */
    @Override
    public void onDownloadProgress(DownloadProgressInfo progress) {
        mDownloadProgressBar.setMax((int) (progress.mOverallTotal >> 8));
        mDownloadProgressBar.setProgress((int) (progress.mOverallProgress >> 8));
        mProgressPercentTextView.setText(Long.toString(progress.mOverallProgress * 100 / progress.mOverallTotal) + "%");
    }


    private static class XAPKFile {
        public final boolean mIsMain;
        public final int mFileVersion;
        public final long mFileSize;

        XAPKFile(boolean isMain, int fileVersion, long fileSize) {
            mIsMain = isMain;
            mFileVersion = fileVersion;
            mFileSize = fileSize;
        }
    }

    private static final XAPKFile[] xAPKS = {
            new XAPKFile(
                    true, // true signifies a main file
                    1, // the version of the APK that the file was uploaded against
                    425904400L // the length of the file in bytes
            )
    };
    static private final float SMOOTHING_FACTOR = 0.005f;

    /**
     * Connect the stub to our service on start.
     */

    @Override
    protected void onStart() {
        if (null != mDownloaderClientStub) {
            mDownloaderClientStub.connect(this);
        }
        super.onStart();
    }

    /**
     * Disconnect the stub from our service on stop
     */
    @Override
    protected void onStop() {
        if (null != mDownloaderClientStub) {
            mDownloaderClientStub.disconnect(this);
        }
        super.onStop();
    }


    void validateXAPKZipFiles() {
        AsyncTask<Object, DownloadProgressInfo, Boolean> validationTask = new AsyncTask<Object, DownloadProgressInfo, Boolean>() {

            @Override
            protected void onPreExecute() {
                mDownloadViewGroup.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Object... params) {
                for (XAPKFile xf : xAPKS) {
                    String fileName = Helpers.getExpansionAPKFileName(A_A_Splash.this, xf.mIsMain, xf.mFileVersion);
                    if (!Helpers.doesFileExist(A_A_Splash.this, fileName, xf.mFileSize, false))
                        return false;
                    fileName = Helpers.generateSaveFileName(A_A_Splash.this, fileName);
                    ZipResourceFile zrf;
                    byte[] buf = new byte[1024 * 256];
                    try {
                        zrf = new ZipResourceFile(fileName);
                        ZipResourceFile.ZipEntryRO[] entries = zrf.getAllEntries();
                        /**
                         * First calculate the total compressed length
                         */
                        long totalCompressedLength = 0;
                        for (ZipResourceFile.ZipEntryRO entry : entries) {
                            totalCompressedLength += entry.mCompressedLength;
                        }
                        float averageVerifySpeed = 0;
                        long totalBytesRemaining = totalCompressedLength;
                        long timeRemaining;
                        /**
                         * Then calculate a CRC for every file in the Zip file,
                         * comparing it to what is stored in the Zip directory.
                         * Note that for compressed Zip files we must extract
                         * the contents to do this comparison.
                         */
                        for (ZipResourceFile.ZipEntryRO entry : entries) {
                            if (-1 != entry.mCRC32) {
                                long length = entry.mUncompressedLength;
                                CRC32 crc = new CRC32();
                                DataInputStream dis = null;
                                try {
                                    dis = new DataInputStream(zrf.getInputStream(entry.mFileName));

                                    long startTime = SystemClock.uptimeMillis();
                                    while (length > 0) {
                                        int seek = (int) (length > buf.length ? buf.length : length);
                                        dis.readFully(buf, 0, seek);
                                        crc.update(buf, 0, seek);
                                        length -= seek;
                                        long currentTime = SystemClock.uptimeMillis();
                                        long timePassed = currentTime - startTime;
                                        if (timePassed > 0) {
                                            float currentSpeedSample = (float) seek / (float) timePassed;
                                            if (0 != averageVerifySpeed) {
                                                averageVerifySpeed = SMOOTHING_FACTOR * currentSpeedSample + (1 - SMOOTHING_FACTOR) * averageVerifySpeed;
                                            } else {
                                                averageVerifySpeed = currentSpeedSample;
                                            }
                                            totalBytesRemaining -= seek;
                                            timeRemaining = (long) (totalBytesRemaining / averageVerifySpeed);
                                            this.publishProgress(new DownloadProgressInfo(totalCompressedLength, totalCompressedLength - totalBytesRemaining, timeRemaining, averageVerifySpeed));
                                        }
                                        startTime = currentTime;
                                        if (mCancelValidation)
                                            return true;
                                    }
                                    if (crc.getValue() != entry.mCRC32) {
                                        Log.e(Constants.TAG, "CRC does not match for entry: " + entry.mFileName);
                                        Log.e(Constants.TAG, "In file: " + entry.getZipFileName());
                                        return false;
                                    }
                                } finally {
                                    if (null != dis) {
                                        dis.close();
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                return true;
            }

            @Override
            protected void onProgressUpdate(DownloadProgressInfo... values) {
                onDownloadProgress(values[0]);
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    mDownloadViewGroup.setVisibility(View.GONE);
                    mainSplash.setVisibility(View.VISIBLE);
                    isGetMaintananceValue();

                } else {
                    mDownloadViewGroup.setVisibility(View.VISIBLE);
                    mainSplash.setVisibility(View.GONE);
                }
                super.onPostExecute(result);
            }

        };
        validationTask.execute(new Object());
    }

    boolean expansionFilesDelivered() {
        for (XAPKFile xf : xAPKS) {
            String fileName = Helpers.getExpansionAPKFileName(this, xf.mIsMain, xf.mFileVersion);
            if (!Helpers.doesFileExist(this, fileName, xf.mFileSize, false))
                return false;
        }
        return true;
    }

    private void setState(int newState) {
        if (mState != newState) {
            mState = newState;
        }
    }


    @Override
    protected void onDestroy() {
        this.mCancelValidation = true;
        super.onDestroy();
    }
}
