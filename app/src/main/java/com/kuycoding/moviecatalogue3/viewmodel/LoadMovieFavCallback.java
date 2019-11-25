package com.kuycoding.moviecatalogue3.viewmodel;

import android.database.Cursor;

import com.kuycoding.moviecatalogue3.model.Movies;

import java.util.ArrayList;

public interface LoadMovieFavCallback {
    void postExecute(ArrayList<Movies> movies);

    void postExecute(Cursor movies);

    void preExecute();
}
