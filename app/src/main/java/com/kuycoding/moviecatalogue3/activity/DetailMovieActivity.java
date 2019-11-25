package com.kuycoding.moviecatalogue3.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kuycoding.moviecatalogue3.R;
import com.kuycoding.moviecatalogue3.db.MovieHelper;
import com.kuycoding.moviecatalogue3.model.Movies;

public class DetailMovieActivity extends AppCompatActivity {
    private TextView textViewTitle;
    private TextView textViewUserScore;
    private TextView textViewDate;
    private TextView textViewVote;
    private TextView textViewPopular;
    private TextView textViewLang;
    private TextView textViewOverview;
    private TextView textViewGenre;
    private ImageView imgPoster, imgCover;
    public static final String EXTRA_DATA = "extra_data";

    private boolean isFavorite = false;
    private Movies movies;
    private ProgressBar progressBar;
    private MovieHelper movieHelper;
    private MenuItem add, del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        imgPoster = findViewById(R.id.img_poster);
        imgCover = findViewById(R.id.img_cover);
        textViewTitle = findViewById(R.id.tv_title);
        textViewDate = findViewById(R.id.tv_date);
        textViewPopular = findViewById(R.id.txt_populer);
        textViewVote = findViewById(R.id.txt_vote);
        textViewUserScore = findViewById(R.id.tv_userscore);
        textViewLang = findViewById(R.id.txt_lang);
        textViewGenre = findViewById(R.id.txt_genre);
        textViewOverview = findViewById(R.id.txt_overview);
        movies = getIntent().getParcelableExtra(EXTRA_DATA);
        progressBar.setVisibility(View.INVISIBLE);
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (Exception ignored) {
                }
                handler.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        movies = getIntent().getParcelableExtra(EXTRA_DATA);
                        Glide.with(DetailMovieActivity.this)
                                .load(movies.getPosterPath())
                                .apply(new RequestOptions().override(350, 550))
                                .placeholder(R.color.colorAccent)
                                .into(imgPoster);
                        Glide.with(DetailMovieActivity.this)
                                .load(movies.getBackdrop_path())
                                .apply(new RequestOptions().override(350, 550))
                                .placeholder(R.color.colorAccent)
                                .into(imgCover);
                        textViewTitle.setText(movies.getTitle());
                        textViewDate.setText(movies.getReleaseDate());
                        textViewPopular.setText(Double.toString(movies.getPopularity()));
                        textViewVote.setText(Integer.toString(movies.getVoteCount()));
                        textViewUserScore.setText(Double.toString(movies.getVoteAverage()));
                        textViewLang.setText(movies.getOriginalLanguage());
                        textViewGenre.setText(movies.getListGenreString().toString().replaceAll("[\\[\\]]", ""));
                        textViewOverview.setText(movies.getOverview());

                        movieHelper = MovieHelper.getInstance(getApplicationContext());
                        movieHelper.open();
                        if (movieHelper.checkMovieFav(movies.getId()))
                            isFavorite = true;
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        movieHelper.close();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isFavorite) {
            getMenuInflater().inflate(R.menu.menu_already_favorite, menu); //solid
        } else {
            getMenuInflater().inflate(R.menu.menu_add_favorite, menu); //outline_gold
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_favorite) {
            if (!movieHelper.checkMovieFav(movies.getId())) {
                long result;
                result = movieHelper.insertMovieFav(movies);
                if (result > 0) {
                    item.setIcon(R.drawable.ic_tab_favorit_gold);
                    Toast.makeText(DetailMovieActivity.this, getResources().getString(R.string.success_add_fav), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(DetailMovieActivity.this, getResources().getString(R.string.faild_add_fav), Toast.LENGTH_SHORT).show();
            } else {
                if (movieHelper.checkMovieFav(movies.getId())) {
                    long result = movieHelper.deleteMovieFav(movies.getId());
                    if (result > 0) {
                        item.setIcon(R.drawable.ic_tab_favorit_gold);
                        Toast.makeText(DetailMovieActivity.this, getResources().getString(R.string.success_remove_fav), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(DetailMovieActivity.this, getResources().getString(R.string.faild_remove_fav), Toast.LENGTH_SHORT).show();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (movieHelper != null) movieHelper.close();
    }
}
