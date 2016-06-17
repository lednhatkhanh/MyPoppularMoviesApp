package com.leonzk.mypoppularmoviesapp;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Created by lednh on 17/06/2016.
 */
public class Movie {

    private String title;
    private String posterURL;
    private String overview;
    private Double userRating;
    private GregorianCalendar releaseDate;

    public Movie(){
        title = posterURL = overview = "";
        userRating = 0d;
        releaseDate = null;
    }

    public Movie(String title,String posterURL,String overview,Double userRating,GregorianCalendar releaseDate){
        this.title = title;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.posterURL = posterURL;
        this.overview = overview;
    }

    @Override
    public String toString(){
        String str = "Title: " + title + "\n"
                + "Poster URL: " + posterURL + "\n"
                + "Overview: " + overview + "\n"
                + "User Rating: " + userRating + " \n"
                + "Release Date: " + formatDate();
        return str;
    }

    private String formatDate(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setCalendar(releaseDate);
        return format.format(releaseDate.getTime());
    }
}
