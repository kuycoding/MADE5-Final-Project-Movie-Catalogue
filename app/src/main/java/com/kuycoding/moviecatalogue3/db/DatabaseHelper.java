package com.kuycoding.moviecatalogue3.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "dbfavorite";
    private static final int DATABASE_VERSION = 3;

    private static final String SQL_CREATE_TABLE_FAV_MOVIE = String.format(
            "CREATE TABLE %s" + //nama tabel
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT," + //id tabel
                    "%s INTEGER NOT NULL," + //ID_MOVIE
                    "%s TEXT NOT NULL," + //POSTER_PATH
                    "%s TEXT NOT NULL," + //BACDROP_PATH
                    "%s TEXT NOT NULL," + //TITLE
                    "%s TEXT NOT NULL," + //RELEASE_DATE
                    "%s REAL NOT NULL," + //POPULARITY
                    "%s INTEGER NOT NULL," + //VOTE_COUNT
                    "%s REAL NOT NULL," + //VOTE_AVERAGE
                    "%s TEXT NOT NULL," + //ORIGINAL_LANGUAGE
                    "%s TEXT NOT NULL," + //GENRE
                    "%s TEXT NOT NULL)", //OVERVIEW
            DatabaseContract.TABLE_FAV_MOVIE,
            DatabaseContract.MovieFavColumns._ID,
            DatabaseContract.MovieFavColumns.ID_MOVIE,
            DatabaseContract.MovieFavColumns.POSTER_PATH,
            DatabaseContract.MovieFavColumns.BACKDROP_PATH,
            DatabaseContract.MovieFavColumns.TITLE,
            DatabaseContract.MovieFavColumns.RELEASE_DATE,
            DatabaseContract.MovieFavColumns.POPULARITY,
            DatabaseContract.MovieFavColumns.VOTE_COUNT,
            DatabaseContract.MovieFavColumns.VOTE_AVERAGE,
            DatabaseContract.MovieFavColumns.ORIGINAL_LANGUAGE,
            DatabaseContract.MovieFavColumns.GENRE,
            DatabaseContract.MovieFavColumns.OVERVIEW
    );

    private static final String SQL_CREATE_TABLE_FAV_TV_SHOW = String.format(
            "CREATE TABLE %s" + //nama tabel
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT," + //id tabel

                    "%s INTEGER NOT NULL," + //ID_TV_SHOW
                    "%s TEXT NOT NULL," + //POSTER_PATH
                    "%s TEXT NOT NULL," + //BACKDORP
                    "%s TEXT NOT NULL," + //NAME
                    "%s TEXT NOT NULL," + //FIRST_AIR_DATE
                    "%s REAL NOT NULL," + //POPULARITY
                    "%s TEXT NOT NULL," + //ORIGIN_COUNTRY
                    "%s INTEGER NOT NULL," + //VOTE_COUNT
                    "%s REAL NOT NULL," + //VOTE_AVERAGE
                    "%s TEXT NOT NULL," + //ORIGINAL_LANGUAGE
                    "%s TEXT NOT NULL," + //GENRE
                    "%s TEXT NOT NULL)", //OVERVIEW
            DatabaseContract.TABLE_FAV_TV_SHOW,
            DatabaseContract.TvShowFavColumns._ID,
            DatabaseContract.TvShowFavColumns.ID_TV_SHOW,
            DatabaseContract.TvShowFavColumns.POSTER_PATH,
            DatabaseContract.TvShowFavColumns.BACKDROP_PATH,
            DatabaseContract.TvShowFavColumns.NAME,
            DatabaseContract.TvShowFavColumns.FIRST_AIR_DATE,
            DatabaseContract.TvShowFavColumns.POPULARITY,
            DatabaseContract.TvShowFavColumns.ORIGIN_COUNTRY,
            DatabaseContract.TvShowFavColumns.VOTE_COUNT,
            DatabaseContract.TvShowFavColumns.VOTE_AVERAGE,
            DatabaseContract.TvShowFavColumns.ORIGINAL_LANGUAGE,
            DatabaseContract.TvShowFavColumns.GENRE,
            DatabaseContract.TvShowFavColumns.OVERVIEW
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAV_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_FAV_TV_SHOW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_FAV_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_FAV_TV_SHOW);
        onCreate(db);
    }
}
