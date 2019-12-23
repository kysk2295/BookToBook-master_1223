package com.example.booktobook.Model;

public class BookData{
    public String book_image;
    public String title;
    public String author;
    public String publisher;
    public String haver;
    public boolean abled;


    public BookData(){

    }

    public BookData(String book_image, String title, String author, String publisher, String haver) {
        this.book_image = book_image;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.haver = haver;
        this.abled = true;
    }



    public String getBook_image() {
        return book_image;
    }

    public void setBook_image(String book_image) {
        this.book_image = book_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getHaver() {
        return haver;
    }

    public void setHaver(String haver) {
        this.haver = haver;
    }

    public boolean isAbled() {
        return abled;
    }

    public void setAbled(boolean abled) {
        this.abled = abled;
    }
}
