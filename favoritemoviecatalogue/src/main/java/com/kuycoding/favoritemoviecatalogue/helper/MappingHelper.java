package com.kuycoding.favoritemoviecatalogue.helper;

import android.database.Cursor;

import com.kuycoding.favoritemoviecatalogue.model.Movies;
import com.kuycoding.favoritemoviecatalogue.db.DatabaseContract;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<Movies> mapCursorToArrayList(Cursor movieCursor) {
        ArrayList<Movies> moviesArrayList = new ArrayList<>();

        while (movieCursor.moveToNext()) {
            int id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavColumns._ID));
            int id_movie = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavColumns.ID_MOVIE));
            String title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavColumns.TITLE));
            String overview = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavColumns.OVERVIEW));
            double vote_average = movieCursor.getDouble(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavColumns.VOTE_AVERAGE));
            int vote_count = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavColumns.VOTE_COUNT));
            String release_date = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavColumns.RELEASE_DATE));
            String poster_path = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavColumns.POSTER_PATH));
            String backdrop_path = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavColumns.BACKDROP_PATH));
            double popular = movieCursor.getDouble(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavColumns.POPULARITY));
            String genre = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavColumns.GENRE));
            final ArrayList<String> listGenreString = new ArrayList<>();

            moviesArrayList.add(new Movies(id, id_movie, title, overview, vote_average, vote_count, release_date, poster_path, backdrop_path, popular, genre));
        }
        return moviesArrayList;
    }
}
