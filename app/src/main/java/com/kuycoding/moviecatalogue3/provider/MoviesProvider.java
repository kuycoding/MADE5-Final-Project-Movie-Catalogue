package com.kuycoding.moviecatalogue3.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.kuycoding.moviecatalogue3.db.MovieHelper;
import com.kuycoding.moviecatalogue3.fragment.FavMoviesFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.kuycoding.moviecatalogue3.db.DatabaseContract.AUTHORITY;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.CONTENT_URI;
import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TABLE_FAV_MOVIE;

public class MoviesProvider extends ContentProvider {
        private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private MovieHelper movieHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_FAV_MOVIE, MOVIE);
        sUriMatcher.addURI(AUTHORITY, TABLE_FAV_MOVIE + "/#", MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        movieHelper = MovieHelper.getInstance(getContext());
        movieHelper.open();
        return true;
    }

    @Override
    public Cursor query(@NotNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        movieHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = movieHelper.queryProviderAll();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public String getType(@NotNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NotNull Uri uri, ContentValues contentValues) {
        movieHelper.open();
        long added;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = movieHelper.insertProviderMovie(contentValues);
                break;
            default:
                added = 0;
                break;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, new FavMoviesFragment.DataObserver(new Handler(), getContext()));

        //getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" +added);
    }

    @Override
    public int update(@NotNull Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        movieHelper.open();
        int updated;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                updated = movieHelper.updateProviderMovie(uri.getLastPathSegment(), contentValues);
                break;
            default:
                updated = 0;
                break;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, new FavMoviesFragment.DataObserver(new Handler(), getContext()));
        //getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }

    @Override
    public int delete(@NotNull Uri uri, String selection, String[] selectionArgs) {
        movieHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = movieHelper.deleteProviderMovie(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, new FavMoviesFragment.DataObserver(new Handler(), getContext()));
        //getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return deleted;
    }
}
