package com.kuycoding.moviecatalogue3.fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kuycoding.moviecatalogue3.R;
import com.kuycoding.moviecatalogue3.activity.DetailMovieActivity;
import com.kuycoding.moviecatalogue3.adapter.MovieAdapter;
import com.kuycoding.moviecatalogue3.model.Movies;
import com.kuycoding.moviecatalogue3.viewmodel.LoadMovieFavCallback;
import com.kuycoding.moviecatalogue3.viewmodel.MovieViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static com.kuycoding.moviecatalogue3.db.DatabaseContract.CONTENT_URI;
import static com.kuycoding.moviecatalogue3.fragment.MovieFragment.EXTRA_STATE_FOR_FAV;
import static com.kuycoding.moviecatalogue3.helper.MappingHelper.mapCursorToArrayList;

public class FavMoviesFragment extends Fragment implements LoadMovieFavCallback {
    private MutableLiveData<ArrayList<Movies>> listMovies = new MutableLiveData<>();
    private MovieAdapter adapter;
    private ProgressBar progressBar;
    private MovieViewModel movieViewModel;
    private boolean isForFavActivity, shouldRefreshOnResume = false;

    public FavMoviesFragment() {

    }

    public FavMoviesFragment(boolean isForFavActivity) {
        this.isForFavActivity = isForFavActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @SuppressLint("Recycle")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
        showLoading(false);

        if (savedInstanceState != null) {
            isForFavActivity = savedInstanceState.getBoolean(EXTRA_STATE_FOR_FAV);
        }
        movieViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MovieViewModel.class);
        movieViewModel.setMovieFav(getContext());

        adapter = new MovieAdapter();
        adapter.notifyDataSetChanged();

        RecyclerView recyclerView = view.findViewById(R.id.rv_movie);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBar);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, getMovie);
        movieViewModel.setMovieFav(getActivity());
        adapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movies movies) {
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_text) + " " + movies.getTitle(), Toast.LENGTH_SHORT).show();
                Intent moveWithObjectIntent = new Intent(getContext(), DetailMovieActivity.class);
                moveWithObjectIntent.putExtra(DetailMovieActivity.EXTRA_DATA, movies);
                startActivity(moveWithObjectIntent);
            }
        });

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver myObserver = new DataObserver(handler, getContext());
        Objects.requireNonNull(getContext()).getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);
        getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
        new LoadMovieFavAsync(getContext(), this).execute();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_STATE_FOR_FAV, isForFavActivity);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shouldRefreshOnResume) {
            if (isForFavActivity) {
                movieViewModel.setMovieFav(getActivity());
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    private Observer<ArrayList<Movies>> getMovie = new Observer<ArrayList<Movies>>() {
        @Override
        public void onChanged(ArrayList<Movies> movies) {
            if (movies != null) {
                adapter.setmData(movies);
            }
            showLoading(false);
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void postExecute(ArrayList<Movies> movies) {
        Log.d("MovieViewModel", "postExecute" + movies.toString());
        if (movies.size() > 0){
            listMovies.postValue(movies);
        } else {
            adapter.setmData(new ArrayList<Movies>());
            Toast.makeText(getContext(), "Data gak ada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void postExecute(Cursor movies) {
        ArrayList<Movies> listMovies = mapCursorToArrayList(movies);
        if (listMovies.size() > 0) {
            adapter.setmData(listMovies);
        } else {
            adapter.setmData(new ArrayList<Movies>());
            Toast.makeText(getContext(), "Data gak ada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void preExecute() {

    }

    public static class LoadMovieFavAsync extends AsyncTask<Void, Void, ArrayList<Movies>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMovieFavCallback> weakCallback;

        LoadMovieFavAsync(Context movieHelper, LoadMovieFavCallback callback) {
            weakContext = new WeakReference<>(movieHelper);
            weakCallback = new WeakReference<>(callback);
        }
      /*  @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }*/

        @Override
        protected ArrayList<Movies> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
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

        public DataObserver(Handler handler, Context context) {
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
