package com.kuycoding.favoritemoviecatalogue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kuycoding.favoritemoviecatalogue.activity.DetailMovieActivity;
import com.kuycoding.favoritemoviecatalogue.adapter.MovieAdapter;
import com.kuycoding.favoritemoviecatalogue.model.Movies;
import com.kuycoding.favoritemoviecatalogue.viewmodel.LoadMovieFavCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.kuycoding.favoritemoviecatalogue.db.DatabaseContract.CONTENT_URI;
import static com.kuycoding.favoritemoviecatalogue.helper.MappingHelper.mapCursorToArrayList;

public class MainActivity extends AppCompatActivity implements LoadMovieFavCallback {
    private MutableLiveData<ArrayList<Movies>> listMovies = new MutableLiveData<>();
    private MovieAdapter adapter;

    @SuppressLint("Recycle")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new MovieAdapter();
        RecyclerView recyclerView = findViewById(R.id.rv_movie);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);
        Cursor cursor = getContentResolver().query(CONTENT_URI, null, null,null, null);
        ArrayList<Movies> moviesArrayList = new ArrayList<>();
        assert cursor != null;
        while (cursor.moveToNext()) {
            moviesArrayList.add(
                    new Movies(
                            cursor.getInt(0),
                            cursor.getString(3),
                            cursor.getString(2),
                            cursor.getString(5),
                            cursor.getString(4),
                            cursor.getDouble(8),
                            cursor.getDouble(6),
                            cursor.getInt(7),
                            cursor.getString(9),
                            cursor.getString(11)
                    )
            );
        }
        adapter.setmData(moviesArrayList);
        adapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movies movies) {
                Intent moveWithObjectIntent = new Intent(MainActivity.this, DetailMovieActivity.class);
                moveWithObjectIntent.putExtra(DetailMovieActivity.EXTRA_DATA, movies);
                startActivity(moveWithObjectIntent);
            }
        });
        new LoadMovieFavAsync(this, this).execute();
    }
    @Override
    public void onResume() {
        super.onResume();
        new LoadMovieFavAsync(getApplicationContext(),this).execute();
    }

    @Override
    public void postExecute(ArrayList<Movies> movies) {
        Log.d("MovieViewModel", "postExecute" + movies.toString());
        if (movies.size() > 0){
            listMovies.postValue(movies);
        } else {
            adapter.setmData(new ArrayList<Movies>());
            Toast.makeText(this, "Data belum ada", Toast.LENGTH_SHORT).show();
        }
    }

    public static class LoadMovieFavAsync extends AsyncTask<Void, Void, ArrayList<Movies>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMovieFavCallback> weakCallback;

        LoadMovieFavAsync(Context movieHelper, LoadMovieFavCallback callback) {
            weakContext = new WeakReference<>(movieHelper);
            weakCallback = new WeakReference<>(callback);
        }
       @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movies> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
            assert dataCursor != null;
            return mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
        }
    }

    public static class DataObserver extends ContentObserver {

        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadMovieFavAsync(context, (LoadMovieFavCallback) context).execute();
        }
    }
}