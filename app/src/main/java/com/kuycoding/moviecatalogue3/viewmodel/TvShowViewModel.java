package com.kuycoding.moviecatalogue3.viewmodel;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kuycoding.moviecatalogue3.db.TvShowHelper;
import com.kuycoding.moviecatalogue3.model.GenreModel;
import com.kuycoding.moviecatalogue3.model.TvShow;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class TvShowViewModel extends ViewModel implements LoadTvShowFavCallback {
    private static final String API_KEY = "b123a5d9a362f614b34df7b54814fd36";
    private MutableLiveData<ArrayList<TvShow>> listTvShows = new MutableLiveData<>();


    public void setTvShows() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShow> listItems = new ArrayList<>();
        final ArrayList<GenreModel> listGenre = new ArrayList<>();

        String urlGenre = "http://api.themoviedb.org/3/genre/tv/list?api_key=" + API_KEY;
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=en-US";


        client.get(urlGenre, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray genres = responseObject.getJSONArray("genres");
                    for (int i = 0; i < genres.length(); i++) {
                        JSONObject genre = genres.getJSONObject(i);
                        GenreModel genreModel = new GenreModel(genre);
                        listGenre.add(genreModel);
                    }

                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Exception", Objects.requireNonNull(error.getMessage()));
            }
        });

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray results = responseObject.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject tvShow = results.getJSONObject(i);

                        JSONArray arrayGenreIds = tvShow.getJSONArray("genre_ids");
                        ArrayList<String> nameGenre = new ArrayList<>();
                        for (int j = 0; j < arrayGenreIds.length(); j++) {
                            int item = arrayGenreIds.getInt(j);

                            for (int n = 0; n < listGenre.size(); n++) {
                                if (listGenre.get(n).getId() == item) {
                                    nameGenre.add(listGenre.get(n).getName());

                                    String namaGenreItem = listGenre.get(n).getName();
                                    arrayGenreIds.put(j, namaGenreItem);
                                }
                            }
                        }

                        Log.d("nameGenre", String.valueOf(nameGenre));
                        Log.d("arrayGenreIds", String.valueOf(arrayGenreIds));

                        TvShow tvShowModel = new TvShow(tvShow);
                        listItems.add(tvShowModel);
                    }
                    listTvShows.postValue(listItems);

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

    public void setSearch(String keyword) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShow> listItems = new ArrayList<>();
        final ArrayList<GenreModel> listGenre = new ArrayList<>();

        String urlSearch = "https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY +"&language=en-US&query=" +keyword;
        String urlGenre = "http://api.themoviedb.org/3/genre/movie/list?api_key=" + API_KEY;
        client.get(urlSearch, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    Log.d("SEARCH", result);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject tv = list.getJSONObject(i);

                        JSONArray arrayGenreIds = tv.getJSONArray("genre_ids");
                        ArrayList<String> nameGenre = new ArrayList<>();
                        for (int j = 0; j < arrayGenreIds.length(); j++) {
                            int item = arrayGenreIds.getInt(j);

                            for (int n = 0; n < listGenre.size(); n++) {
                                if (listGenre.get(n).getId() == item) {
                                    nameGenre.add(listGenre.get(n).getName());

                                    String namaGenreItem = listGenre.get(n).getName();
                                    arrayGenreIds.put(j, namaGenreItem);
                                }
                            }
                        }

                        Log.d("nameGenre", String.valueOf(nameGenre));
                        Log.d("arrayGenreIds", String.valueOf(arrayGenreIds));

                        TvShow tvItems = new TvShow(tv);
                        listItems.add(tvItems);
                    }

                    listTvShows.postValue(listItems);
                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", Objects.requireNonNull(error.getMessage()));
            }
        });

        client.get(urlGenre, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray genres = responseObject.getJSONArray("genres");
                    for (int i = 0; i < genres.length(); i++) {
                        JSONObject genre = genres.getJSONObject(i);
                        GenreModel genreModel = new GenreModel(genre);
                        listGenre.add(genreModel);
                    }

                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Exception", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    public void setTvShowFav(Context context) {
        TvShowHelper tvShowHelper = TvShowHelper.getInstance(context);
        tvShowHelper.open();
        new LoadTvShowFavAsync(tvShowHelper, this).execute();
    }

    public LiveData<ArrayList<TvShow>> getListTvShows() {
        return listTvShows;
    }


    @Override
    public void preExecute() {
        Log.d("TvShowViewModel", "preExecute: ");
    }

    @Override
    public void postExecute(ArrayList<TvShow> tvShows) {
        listTvShows.postValue(tvShows);
        Log.d("MovieViewModel", "postExecute" + tvShows.toString());
    }


    private static class LoadTvShowFavAsync extends AsyncTask<Void, Void, ArrayList<TvShow>> {
        private final WeakReference<TvShowHelper> weakTvShowHelper;
        private final WeakReference<LoadTvShowFavCallback> weakCallback;

        private LoadTvShowFavAsync(TvShowHelper tvShowHelper, LoadTvShowFavCallback callback) {
            weakTvShowHelper = new WeakReference<>(tvShowHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<TvShow> doInBackground(Void... voids) {
            return weakTvShowHelper.get().getAllTvShowsFav();
        }

        @Override
        protected void onPostExecute(ArrayList<TvShow> tvShows) {
            super.onPostExecute(tvShows);
            weakCallback.get().postExecute(tvShows);
        }
    }
}

