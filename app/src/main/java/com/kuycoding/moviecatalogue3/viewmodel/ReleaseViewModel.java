package com.kuycoding.moviecatalogue3.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kuycoding.moviecatalogue3.model.Movies;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class ReleaseViewModel {
    private static final String API_KEY = "b123a5d9a362f614b34df7b54814fd36";
    private MutableLiveData<ArrayList<Movies>> listMovie = new MutableLiveData<>();

    public void setReleaseDate(String date) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movies> listItem = new ArrayList<>();
        String NEW_MOVIES_TODAY = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&primary_release_date.gte=" + TODAY_DATE() + "&primary_release_date.lte=" + TODAY_DATE();

        client.get(NEW_MOVIES_TODAY, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("result");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject jsonObject = list.getJSONObject(i);
                        Movies movies = new Movies(jsonObject);
                        listItem.add(movies);
                    }
                    listMovie.postValue(listItem);
                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
            }
        });
    }
    private static String TODAY_DATE() {
        Date nowDate = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(nowDate);
    }
    public LiveData<ArrayList<Movies>> getTodayMovie () {
        return listMovie;
    }
}
