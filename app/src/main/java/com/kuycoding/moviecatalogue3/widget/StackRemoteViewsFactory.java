package com.kuycoding.moviecatalogue3.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kuycoding.moviecatalogue3.R;
import com.kuycoding.moviecatalogue3.db.DatabaseHelper;
import com.kuycoding.moviecatalogue3.model.Movies;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.kuycoding.moviecatalogue3.db.DatabaseContract.TABLE_FAV_MOVIE;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<Movies> movieList = new ArrayList<>();
    private Context context;
    private int mApppWidget;

    StackRemoteViewsFactory(Context applicationContext, Intent intent) {
        context = applicationContext;
        mApppWidget = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        movieList.clear();
        getFavoriteMovies(context);
    }

    private void getFavoriteMovies(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor = database.query(TABLE_FAV_MOVIE
                , null
                , null
                , null
                , null
                , null
                , null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movies movies = new Movies(cursor);
                movieList.add(movies);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        Movies movies = movieList.get(position);
        String poster = movies.getBackdrop_path();
        try {
            Bitmap bitmap = Glide.with(context)
                    .asBitmap()
                    .load(poster)
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
            remoteViews.setImageViewBitmap(R.id.imageView, bitmap);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Bundle extra = new Bundle();
        extra.putInt(FavoriteBannerWidget.EXTRA_ITEM, position);
        Intent fillintent = new Intent();
        fillintent.putExtras(extra);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
