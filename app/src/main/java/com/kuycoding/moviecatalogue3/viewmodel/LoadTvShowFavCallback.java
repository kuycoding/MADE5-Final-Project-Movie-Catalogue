package com.kuycoding.moviecatalogue3.viewmodel;

import com.kuycoding.moviecatalogue3.model.TvShow;

import java.util.ArrayList;

public interface LoadTvShowFavCallback {
    void preExecute();

    void postExecute(ArrayList<TvShow> tvShows);
}
