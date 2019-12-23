package com.example.booktobook.Model;


import java.util.List;

public class User {
    private String id;
    private String password;
    private int point;
    private int uploaded_book_count;
    private int borrowed_book_count;
    private List<Alert> alert;
    private boolean flag;

    public User() {
    }

    public User(String id, String password) {
        this.id = id;
        this.password = password;
        this.point = 20;
        this.uploaded_book_count = 0;
        this.borrowed_book_count = 0;
        this.flag=false;
    }

    public User(String id, String password, int point, int uploaded_book_count, int borrowed_book_count) {
        this.id = id;
        this.password = password;
        this.point = point;
        this.uploaded_book_count = uploaded_book_count;
        this.borrowed_book_count = borrowed_book_count;
        this.flag=false;
    }

    public Boolean getFlag() {return flag;}
    public void setFlag(Boolean flag){this.flag=flag;}
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getUploaded_book_count() {
        return uploaded_book_count;
    }

    public void setUploaded_book_count(int uploaded_book_count) {
        this.uploaded_book_count = uploaded_book_count;
    }

    public int getBorrowed_book_count() {
        return borrowed_book_count;
    }

    public void setBorrowed_book_count(int borrowed_book_count) {
        this.borrowed_book_count = borrowed_book_count;
    }

    public List<Alert> getAlert() {
        return alert;
    }

    public void setAlert(List<Alert> alert) {
        this.alert = alert;
    }
}