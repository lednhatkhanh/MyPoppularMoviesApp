package com.leonzk.mypoppularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private final String LOG_TAG = getClass().getSimpleName();
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        movie = intent.getParcelableExtra("Movie");
        displayData();
    }

    private void displayData(){
        ImageView posterImageView = (ImageView) findViewById(R.id.detail_movie_poster_imageview);
        TextView title = (TextView) findViewById(R.id.detail_movie_title);
        TextView releaseDate = (TextView) findViewById(R.id.detail_movie_release_date_textview);
        TextView userRating = (TextView) findViewById(R.id.detail_movie_user_rating_textview);
        TextView overview = (TextView) findViewById(R.id.detail_movie_overview_textview);

        Picasso.with(this).load(movie.getPosterURL()).into(posterImageView);
        Log.e(LOG_TAG,movie.getPosterURL());
        title.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        //String.format(Locale.getDefault(),"%f",movie.getUserRating())+R.string.user_rating
        userRating.setText(String.format(getResources().getString(R.string.user_rating)
                ,movie.getUserRating()));
        overview.setText(movie.getOverview());
    }
}
