package com.example.kurt.ugood.calendar;

import java.util.ArrayList;

public class HomeCollection {
    public String Date="";
    public String Title="";
    public String Content="";
    public String Mood="";

    public static ArrayList<HomeCollection> date_collection_arr;

    public HomeCollection(){}

    public HomeCollection(String date, String title, String description, String mood){

        this.Date=date;
        this.Title=title;
        this.Content= description;
        this.Mood=mood;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMood() {
        return Mood;
    }

    public void setMood(String mood) {
        Mood = mood;
    }
}