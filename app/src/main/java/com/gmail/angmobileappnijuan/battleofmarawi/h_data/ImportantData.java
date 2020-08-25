package com.gmail.angmobileappnijuan.battleofmarawi.h_data;

import com.facebook.Profile;
import com.gmail.angmobileappnijuan.battleofmarawi.z_model.BookCover;
import com.gmail.angmobileappnijuan.battleofmarawi.z_model.User;

public class ImportantData {

    public static Profile userProfile;
    public static String firebaseUID;
    public static String facebookID;

    public static String messageToRead;
    public static String webToOpen;

    public static BookCover bookToOpen;

    public static User user;


    //FIREBASE Database Reference
    public static final String FIREBASE_ALL_USERS ="ALL_USERS";
    public static final String FIREBASE_REFERENCE ="REFERENCE";
    public static final String FIREBASE_ISMAINTANANCE ="ISMAINTANANCE";
    public static final String FIREBASE_CURRENT_APP_VERSION ="APP_VERSION";
    public static final String FIREBASE_PADDING ="PADDING";

    public static final String MY_PIN ="MY_PIN";
    public static final String MY_PIN_TEXT ="MY_PIN_TEXT";
    public static final String MY_PREF_ACCESS_TOKEN ="MY_PREF_ACCESS_TOKEN";
    public static final String MY_PREF_FIRST_TIME_VIDEO ="MY_PREF_FIRST_TIME_VIDEO";
    public static final String MY_PREF_FAIL_ATTEMPT ="MY_PREF_FAIL_ATTEMPT";

    public static final String LAST_BOOK_READ ="LAST_BOOK_READ";
    public static final String LAST_BOOK_READ_PAGE ="LAST_BOOK_READ_PAGE";

    public static final String FACEBOOK_PAGE_ID ="156277931757594";


}
