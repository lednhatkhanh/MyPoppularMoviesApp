package com.leonzk.mypoppularmoviesapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lednh on 17/06/2016.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    public MovieAdapter(Activity context, List<Movie> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_movie,parent,false);

        }

        TextView title = (TextView) convertView.findViewById(R.id.list_item_movie_textview);
        title.setText(movie.getTitle());

        ImageView posterImage = (ImageView) convertView.findViewById(R.id.list_item_movie_image);
        Picasso.with(getContext()).load(movie.getPosterURL()).into(posterImage);

        return convertView;
    }
}
