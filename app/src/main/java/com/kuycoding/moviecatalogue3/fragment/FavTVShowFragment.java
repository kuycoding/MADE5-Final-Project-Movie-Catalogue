package com.kuycoding.moviecatalogue3.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
public class FavTVShowFragment extends Fragment {
    private TvShowAdapter adapter;
    private ProgressBar progressBar;
    private TvShowViewModel tvShowViewModel;
    private boolean isForFavActivity, shouldRefreshOnResume = false;


    public FavTVShowFragment() {
        // Required empty public constructor
    }

    public FavTVShowFragment(boolean isForFavActivity) {
        this.isForFavActivity = isForFavActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
        showLoading(true);

        if (savedInstanceState != null) {
            isForFavActivity = savedInstanceState.getBoolean(EXTRA_STATE_FOR_FAV);
        }

        tvShowViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TvShowViewModel.class);
        tvShowViewModel.setTvShowFav(getActivity());

        adapter = new TvShowAdapter();
        adapter.notifyDataSetChanged();

        RecyclerView recyclerView = view.findViewById(R.id.rv_movie);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBar);

        tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        tvShowViewModel.getListTvShows().observe(this, getTvShow);
        tvShowViewModel.setTvShowFav(getActivity());
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
