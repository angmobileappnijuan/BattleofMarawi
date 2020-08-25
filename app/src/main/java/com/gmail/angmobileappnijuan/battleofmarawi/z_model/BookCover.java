package com.gmail.angmobileappnijuan.battleofmarawi.z_model;

/**
 * Created by Developer on 4/29/2018.
 */

public class BookCover {

    private String bookTitle;
    private String bookNumber;
    private int resValue;

    public BookCover(String bookTitle, String bookNumber, int resValue) {
        this.bookTitle = bookTitle;
        this.bookNumber = bookNumber;
        this.resValue = resValue;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    public int getResValue() {
        return resValue;
    }

    public void setResValue(int resValue) {
        this.resValue = resValue;
    }
}
