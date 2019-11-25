package com.kuycoding.favoritemoviecatalogue.viewmodel;

import com.kuycoding.favoritemoviecatalogue.model.Movies;

import java.util.ArrayList;

public interface LoadMovieFavCallback {

    void postExecute(ArrayList<Movies> movies);

}
