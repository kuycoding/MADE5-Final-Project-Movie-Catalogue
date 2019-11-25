package com.kuycoding.moviecatalogue3.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String  AUTHORITY = "com.kuycoding.moviecatalogue3";
    private static final String SCHEME = "content";
    public static String TABLE_FAV_MOVIE = "movie_fav";
    public static String TABLE_FAV_TV_SHOW = "tv_show_fav";

    public static final class MovieFavColumns implements BaseColumns {
        public static String ID_MOVIE = "id_movie";
        public static String POSTER_PATH = "poster_path";
        public static String BACKDROP_PATH = "backdrop_path";
        public static String TITLE = "title";
        public static String RELEASE_DATE = "release_data";
        public static String POPULARITY = "popularity";
        public static String VOTE_COUNT = "vote_count";
        public static String VOTE_AVERAGE = "vote_average";
        public static String ORIGINAL_LANGUAGE = "original_language";
        public static String GENRE = "genre";
        public static String OVERVIEW = "overview";

    }

    static final class TvShowFavColumns implements BaseColumns {
        static String ID_TV_SHOW = "id_tv_show";
        static String POSTER_PATH = "poster_path";
        static String BACKDROP_PATH = "backdrop_path";
        static String NAME = "name";
        static String FIRST_AIR_DATE = "first_air_date";
        static String POPULARITY = "popularity";
        static String ORIGIN_COUNTRY = "origin_country";
        static String VOTE_COUNT = "vote_count";
        static String VOTE_AVERAGE = "vote_average";
        static String ORIGINAL_LANGUAGE = "original_language";
        static String GENRE = "genre";
        static String OVERVIEW = "overview";
    }

    public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_FAV_MOVIE)
            .build();
    public static final Uri CONTENT_URI_TVSHOW = new Uri.Builder()
            .scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_FAV_TV_SHOW)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
