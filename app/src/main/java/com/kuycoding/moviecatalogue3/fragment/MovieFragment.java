package com.kuycoding.moviecatalogue3.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kuycoding.moviecatalogue3.MainActivity;
import com.kuycoding.moviecatalogue3.R;
import com.kuycoding.moviecatalogue3.activity.DetailMovieActivity;
import com.kuycoding.moviecatalogue3.adapter.MovieAdapter;
import com.kuycoding.moviecatalogue3.model.Movies;
import com.kuycoding.moviecatalogue3.viewmodel.MovieViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment{
    static final String EXTRA_STATE_FOR_FAV = "extra_state_for_fav";
    private MovieAdapter adapter;
    private ProgressBar progressBar;
    private MovieViewModel movieViewModel;
    private boolean isForFavActivity, shouldRefreshOnResume = false;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private RecyclerView recyclerView;

    public MovieFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            isForFavActivity = savedInstanceState.getBoolean(EXTRA_STATE_FOR_FAV);
        }
        adapter = new MovieAdapter();
        adapter.notifyDataSetChanged();
        RecyclerView recyclerView = view.findViewById(R.id.rv_movie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBar);
        showLoading(true);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, getMovie);
        movieViewModel.setMovie();

        adapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movies movies) {
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_text) + " " + movies.getTitle(), Toast.LENGTH_SHORT).show();
                Intent moveWithObjectIntent = new Intent(getContext(), DetailMovieActivity.class);
                moveWithObjectIntent.putExtra(DetailMovieActivity.EXTRA_DATA, movies);
                startActivity(moveWithObjectIntent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        final MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE));
        if (searchItem != null) searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("textSubmit", query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("textChange", newText);
                    if (newText.length() == 0) {
                        movieViewModel.setMovie();
                    } else {
                        movieViewModel.setSearch(newText);
                        adapter.notifyDataSetChanged();
                    }
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_search == item.getItemId()) {
            return false;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
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
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity()))
                .getSupportActionBar()).setTitle(R.string.movies);
    }


    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    private Observer<ArrayList<Movies>> getMovie = new Observer<ArrayList<Movies>>() {
        @Override
        public void onChanged(@Nullable ArrayList<Movies> movies) {
            if (movies != null) {
                adapter.setmData(movies);
                Log.d("Data list movie", adapter.getmData().toString());
                showLoading(false);
            }
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
