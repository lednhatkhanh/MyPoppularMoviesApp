package com.leonzk.mypoppularmoviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    String movieJsonString;
    MovieAdapter mMovieAdapter;
    private final String LOG_TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displayGridView();
        new FetchMovieData().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayGridView(){
        final GridView mMovieGridView = (GridView) findViewById(R.id.gridview_movie);
        mMovieAdapter = new MovieAdapter(this,new ArrayList<Movie>());

        if(mMovieGridView != null){
            mMovieGridView.setAdapter(mMovieAdapter);
            mMovieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(getApplicationContext(),DetailActivity.class)
                            .putExtra("Movie",(Parcelable) mMovieGridView.getItemAtPosition(i));
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart() {
        super.onStart();
        refreshData();
    }

    private void refreshData(){
        new FetchMovieData().execute();
    }

    private class FetchMovieData extends AsyncTask<Void, Void, ArrayList<Movie>>{
        private final String LOG_TAG = this.getClass().getSimpleName();

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            movieJsonString = null;

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String sort_by = sharedPreferences.getString(getString(R.string.pref_sort_key),getString(R.string.pref_sort_popularity));

            try {

                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";

                final String SORT_BY = "sort_by";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM,BuildConfig.THE_MOVIE_DB_API_KEY)
                        .appendQueryParameter(SORT_BY,sort_by).build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonString = buffer.toString();
                return getMovieFromJSON(movieJsonString);
            } catch (IOException e) {

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);

            if(movies != null){
                mMovieAdapter.clear();
                mMovieAdapter.addAll(movies);
            }
        }

        private ArrayList<Movie> getMovieFromJSON(String movieJsonStr) throws JSONException, MalformedURLException {

            final String TMDB_RESULTS = "results";
            final String TMDB_POSTER_PATH = "poster_path";
            final String TMDB_ORIGINAL_TITLE = "original_title";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_VOTE_AVERAGE = "vote_average";
            final String TMDB_RELEASE_DATE = "release_date";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(TMDB_RESULTS);

            ArrayList<Movie> resultArr = new ArrayList<>();

            for(int i=0;i<movieArray.length();i++){
                String poster_path;
                String original_title;
                String overview;
                Double vote_average;
                String release_date;

                JSONObject movie = movieArray.getJSONObject(i);

                poster_path = movie.getString(TMDB_POSTER_PATH).substring(1);
                original_title = movie.getString(TMDB_ORIGINAL_TITLE);
                overview = movie.getString(TMDB_OVERVIEW);
                vote_average = movie.getDouble(TMDB_VOTE_AVERAGE);
                release_date = movie.getString(TMDB_RELEASE_DATE);


                Movie mMovie = new Movie(original_title,convertImageURL(poster_path),overview,vote_average,new GregorianCalendar(
                        Integer.parseInt(getReleaseDate(release_date)[0]),Integer.parseInt(getReleaseDate(release_date)[1]),Integer.parseInt(getReleaseDate(release_date)[2])));

                resultArr.add(mMovie);
            }
            return resultArr;
        }

        private String convertImageURL(String posterURL) throws MalformedURLException {

            final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";

            Uri builtUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                    .appendPath("w185")
                    .appendPath(posterURL).build();
            URL imageURL = new URL(builtUri.toString());
            return imageURL.toString();
        }

        private String[] getReleaseDate(String releaseDate) throws MalformedURLException {
            return releaseDate.split("-");
        }
    }
}
