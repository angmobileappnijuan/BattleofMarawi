package com.gmail.angmobileappnijuan.battleofmarawi.f_helper;

public class DownloaderService extends com.google.android.vending.expansion.downloader.impl.DownloaderService {
    public static final String BASE64_PUBLIC_KEY = "<<YOUR PUBLIC KEY HERE>>"; // TODO Add public key
    private static final byte[] SALT = new byte[]{1, 4, -1, -1, 14, 42, -79, -21, 13, 2, -8, -11, 62, 1, -10, -101, -19, 41, -12, 18}; // TODO Replace with random numbers of your choice. (it is just to ensure that the expansion files are encrypted with a unique key from your app)

    @Override
    public String getPublicKey() {
        return BASE64_PUBLIC_KEY;
    }

    @Override
    public byte[] getSALT() {
        return SALT;
    }

    @Override
    public String getAlarmReceiverClassName() {
        return DownloaderServiceBroadcastReceiver.class.getName();
    }
}
