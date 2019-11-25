package com.kuycoding.moviecatalogue3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kuycoding.moviecatalogue3.model.Movies;

import java.util.ArrayList;
import java.util.Arrays;

import static android.provider.BaseColumns._ID;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.MovieFavColumns.BACKDROP_PATH;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.MovieFavColumns.GENRE;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.MovieFavColumns.ID_MOVIE;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.MovieFavColumns.ORIGINAL_LANGUAGE;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.MovieFavColumns.OVERVIEW;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.MovieFavColumns.POPULARITY;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.MovieFavColumns.POSTER_PATH;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.MovieFavColumns.RELEASE_DATE;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.MovieFavColumns.TITLE;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.MovieFavColumns.VOTE_AVERAGE;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.MovieFavColumns.VOTE_COUNT;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TABLE_FAV_MOVIE;

public class MovieHelper {
    private static final String TAG = MovieHelper.class.getSimpleName();
    private static final String DATABASE_TABLE = TABLE_FAV_MOVIE;
    private static DatabaseHelper databaseHelper;
    private static MovieHelper INSTANCE;

    private static SQLiteDatabase database;

    private MovieHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null)
                    INSTANCE = new MovieHelper(context);
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public ArrayList<Movies> getAllMoviesFav() {
        ArrayList<Movies> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " asc",
                null);
        cursor.moveToFirst();
        Movies movies;
        if (cursor.getCount() > 0) {
            do {
                movies = new Movies(cursor);
                movies.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID_MOVIE)));
                movies.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                movies.setBackdrop_path(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP_PATH)));
                movies.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movies.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                movies.setPopularity(cursor.getDouble(cursor.getColumnIndexOrThrow(POPULARITY)));
                movies.setVoteCount(cursor.getInt(cursor.getColumnIndexOrThrow(VOTE_COUNT)));
                movies.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                movies.setOriginalLanguage(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_LANGUAGE)));

                String genre = cursor.getString(cursor.getColumnIndexOrThrow(GENRE));
                movies.setListGenreString(convertStringToArraylist(genre));

                movies.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));

                Log.d(TAG, "getAllMoviesFav: " + movies.toString());
                arrayList.add(movies);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public boolean checkMovieFav(int id) {
        String selectString = "SELECT * FROM " + TABLE_FAV_MOVIE + " WHERE " + ID_MOVIE + " =?";
        Cursor cursor = database.rawQuery(selectString, new String[]{String.valueOf(id)});
        boolean checkMovieFav = false;
        if (cursor.moveToFirst()) {
            checkMovieFav = true;
            int count = 0;
            while (cursor.moveToNext())
                count++;
            Log.d(TAG, String.format("%d record found", count));
        }
        cursor.close();
        return checkMovieFav;
    }

    public long insertMovieFav(Movies movies) {
        ContentValues args = new ContentValues();
        args.put(ID_MOVIE, movies.getId());
        args.put(POSTER_PATH, movies.getPosterPath());
        args.put(BACKDROP_PATH, movies.getBackdrop_path());
        args.put(TITLE, movies.getTitle());
        args.put(RELEASE_DATE, movies.getReleaseDate());
        args.put(POPULARITY, movies.getPopularity());
        args.put(VOTE_COUNT, movies.getVoteCount());
        args.put(VOTE_AVERAGE, movies.getVoteAverage());
        args.put(ORIGINAL_LANGUAGE, movies.getOriginalLanguage());

        ArrayList<String> arrayList = movies.getListGenreString();
        args.put(GENRE, convertArraylistToString(arrayList));
        args.put(OVERVIEW, movies.getOverview());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteMovieFav(int id) {
        return database.delete(DATABASE_TABLE, ID_MOVIE + "= '" + id + "'", null);
    }

    private ArrayList<String> convertStringToArraylist(String str) {
        return new ArrayList<>(Arrays.asList(str.split(",")));
    }

    private String convertArraylistToString(ArrayList<String> arrayList) {
        return arrayList.toString().replaceAll("[\\[\\]]", "");
    }

    public Cursor queryById(String id) {
        return database.query(DATABASE_TABLE, null
                , ID_MOVIE + " =?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    /**
     * Ambil data dari semua note yang ada di dalam database
     *
     * @return cursor hasil query
     */
    public Cursor queryProviderAll() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , ID_MOVIE + " ASC");
    }

    public long insertProviderMovie(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProviderMovie(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, ID_MOVIE + " =?", new String[]{id});
    }

    public int deleteProviderMovie(String id) {
        return database.delete(DATABASE_TABLE, ID_MOVIE + " =?", new String[]{id});
    }

}
