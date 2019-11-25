package com.kuycoding.moviecatalogue3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kuycoding.moviecatalogue3.model.TvShow;

import java.util.ArrayList;
import java.util.Arrays;

import static android.provider.BaseColumns._ID;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TABLE_FAV_TV_SHOW;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TvShowFavColumns.BACKDROP_PATH;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TvShowFavColumns.FIRST_AIR_DATE;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TvShowFavColumns.GENRE;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TvShowFavColumns.ID_TV_SHOW;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TvShowFavColumns.NAME;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TvShowFavColumns.ORIGINAL_LANGUAGE;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TvShowFavColumns.ORIGIN_COUNTRY;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TvShowFavColumns.OVERVIEW;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TvShowFavColumns.POPULARITY;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TvShowFavColumns.POSTER_PATH;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TvShowFavColumns.VOTE_AVERAGE;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TvShowFavColumns.VOTE_COUNT;

public class TvShowHelper {
    private static final String TAG = TvShowHelper.class.getSimpleName();
    private static final String DATABASE_TABLE = TABLE_FAV_TV_SHOW;
    private static DatabaseHelper databaseHelper;
    private static TvShowHelper INSTANCE;

    private static SQLiteDatabase database;

    private TvShowHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static TvShowHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null)
                    INSTANCE = new TvShowHelper(context);
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

    public ArrayList<TvShow> getAllTvShowsFav() {
        ArrayList<TvShow> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " asc",
                null);
        cursor.moveToFirst();
        TvShow tvShow;
        if (cursor.getCount() > 0) {
            do {
                tvShow = new TvShow();
                tvShow.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID_TV_SHOW)));
                tvShow.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                tvShow.setBackdrop_path(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP_PATH)));
                tvShow.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
                tvShow.setFirstAirDate(cursor.getString(cursor.getColumnIndexOrThrow(FIRST_AIR_DATE)));
                tvShow.setPopularity(cursor.getDouble(cursor.getColumnIndexOrThrow(POPULARITY)));

                String originCountry = cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_LANGUAGE));
                tvShow.setOriginCountry(convertStringToArraylist(originCountry));

                tvShow.setVoteCount(cursor.getInt(cursor.getColumnIndexOrThrow(VOTE_COUNT)));
                tvShow.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                tvShow.setOriginalLanguage(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_LANGUAGE)));

                String genre = cursor.getString(cursor.getColumnIndexOrThrow(GENRE));
                tvShow.setListGenreString(convertStringToArraylist(genre));

                tvShow.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                arrayList.add(tvShow);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }


    public long insertTvShowFav(TvShow tvShow) {
        ContentValues args = new ContentValues();
        args.put(ID_TV_SHOW, tvShow.getId());
        args.put(POSTER_PATH, tvShow.getPosterPath());
        args.put(BACKDROP_PATH, tvShow.getBackdrop_path());
        args.put(NAME, tvShow.getName());
        args.put(FIRST_AIR_DATE, tvShow.getFirstAirDate());
        args.put(POPULARITY, tvShow.getPopularity());

        ArrayList<String> listOriginCountry = tvShow.getListGenreString();
        args.put(ORIGIN_COUNTRY, convertArraylistToString(listOriginCountry));

        args.put(VOTE_COUNT, tvShow.getVoteCount());
        args.put(VOTE_AVERAGE, tvShow.getVoteAverage());
        args.put(ORIGINAL_LANGUAGE, tvShow.getOriginalLanguage());

        ArrayList<String> listGenre = tvShow.getListGenreString();
        args.put(GENRE, convertArraylistToString(listGenre));

        args.put(OVERVIEW, tvShow.getOverview());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteTvShowFav(int id) {
        return database.delete(DATABASE_TABLE, ID_TV_SHOW + "= '" + id + "'", null);
    }

    public boolean checkTvShowFav(int id) {
        String selectString = "SELECT * FROM " + TABLE_FAV_TV_SHOW + " WHERE " + ID_TV_SHOW + "=?";


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


    private ArrayList<String> convertStringToArraylist(String str) {
        return new ArrayList<>(Arrays.asList(str.split(",")));
    }

    private String convertArraylistToString(ArrayList<String> arrayList) {
        return arrayList.toString().replaceAll("[\\[\\]]", "");
    }

    public Cursor queryByIdProviderTv(String id) {
        return database.query(DATABASE_TABLE, null
                , ID_TV_SHOW + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProviderTv() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , ID_TV_SHOW + " ASC");
    }

    public long insertProviderTv(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProviderTv(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, ID_TV_SHOW + " = ?", new String[]{id});
    }

    public int deleteProviderTv(String id) {
        return database.delete(DATABASE_TABLE, ID_TV_SHOW + " = ?", new String[]{id});
    }

}
