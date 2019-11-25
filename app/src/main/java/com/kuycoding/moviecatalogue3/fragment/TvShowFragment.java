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
import com.kuycoding.moviecatalogue3.activity.DetailTvShowActivity;
import com.kuycoding.moviecatalogue3.adapter.TvShowAdapter;
import com.kuycoding.moviecatalogue3.model.TvShow;
import com.kuycoding.moviecatalogue3.viewmodel.TvShowViewModel;

import java.util.ArrayList;
import java.util.Objects;

import static com.kuycoding.moviecatalogue3.fragment.MovieFragment.EXTRA_STATE_FOR_FAV;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {
    private TvShowAdapter adapter;
    private ProgressBar progressBar;
    private TvShowViewModel tvShowViewModel;
    private boolean isForFavActivity, shouldRefreshOnResume = false;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    public TvShowFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tvshow, container, false);
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
        adapter = new TvShowAdapter();
        adapter.notifyDataSetChanged();
        RecyclerView recyclerView = view.findViewById(R.id.rv_tvshow);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBar);
        showLoading(true);
        tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        tvShowViewModel.getListTvShows().observe(this, getTvShow);
        tvShowViewModel.setTvShows();

        adapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow tvShow) {
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_text) + " " + tvShow.getName(), Toast.LENGTH_SHORT).show();
                Intent moveWithObjectIntent = new Intent(getContext(), DetailTvShowActivity.class);
                moveWithObjectIntent.putExtra(DetailTvShowActivity.EXTRA_DATA, tvShow);
                startActivity(moveWithObjectIntent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        final TvShowViewModel tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
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
                        tvShowViewModel.setTvShows();
                    } else {
                        tvShowViewModel.setSearch(newText);
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
                tvShowViewModel.setTvShowFav(getActivity());
            }
        }
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity()))
                .getSupportActionBar()).setTitle(R.string.tv_shows);
    }

    @Override
    public void onStop() {
        super.onStop();
        shouldRefreshOnResume = true;
    }

    private Observer<ArrayList<TvShow>> getTvShow = new Observer<ArrayList<TvShow>>() {
        @Override
        public void onChanged(@Nullable ArrayList<TvShow> tvShows) {
            if (tvShows != null) {
                adapter.setmData(tvShows);
                Log.d("Data list tvshow", adapter.getmData().toString());
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
