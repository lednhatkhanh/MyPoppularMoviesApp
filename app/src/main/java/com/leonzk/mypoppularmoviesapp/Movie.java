package com.leonzk.mypoppularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Created by lednh on 17/06/2016.
 */
public class Movie implements Parcelable {

    private String title;
    private String posterURL;
    private String overview;
    private Double userRating;
    private GregorianCalendar releaseDate;

    public GregorianCalendar getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(GregorianCalendar releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

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

    protected Movie(Parcel in) {
        title = in.readString();
        posterURL = in.readString();
        overview = in.readString();
        userRating = in.readByte() == 0x00 ? null : in.readDouble();
        releaseDate = (GregorianCalendar) in.readValue(GregorianCalendar.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(posterURL);
        dest.writeString(overview);
        if (userRating == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(userRating);
        }
        dest.writeValue(releaseDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}