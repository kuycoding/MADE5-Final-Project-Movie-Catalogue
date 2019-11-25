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
import com.kuycoding.moviecatalogue3.db.TvShowHelper;
import com.kuycoding.moviecatalogue3.model.TvShow;

public class DetailTvShowActivity extends AppCompatActivity {
    private TextView textViewTitle;
    private TextView textViewUserScore;
    private TextView textViewDate;
    private TextView textViewVote;
    private TextView textViewPopular;
    private TextView textViewLang;
    private TextView textViewOverview;
    private TextView textViewGenre;
    private TextView textViewCountry;
    private ImageView imgPoster, imgCover;
    public static final String EXTRA_DATA = "extra_data";

    private boolean isFavorite = false;
    private TvShow tvShow;
    private TvShowHelper tvShowHelper;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tvshow);

        imgPoster = findViewById(R.id.img_poster);
        imgCover = findViewById(R.id.img_cover);
        textViewTitle = findViewById(R.id.tv_title);
        textViewDate = findViewById(R.id.tv_date);
        textViewPopular = findViewById(R.id.txt_populer);
        textViewCountry = findViewById(R.id.txt_country);
        textViewVote = findViewById(R.id.txt_vote);
        textViewUserScore = findViewById(R.id.tv_userscore);
        textViewLang = findViewById(R.id.txt_lang);
        textViewGenre = findViewById(R.id.txt_genre);
        textViewOverview = findViewById(R.id.txt_overview);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        tvShow = getIntent().getParcelableExtra(EXTRA_DATA);
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (Exception ignored) { }
                handler.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        tvShow = getIntent().getParcelableExtra(EXTRA_DATA);
                        Glide.with(DetailTvShowActivity.this)
                                .load(tvShow.getPosterPath())
                                .apply(new RequestOptions().override(350, 550))
                                .placeholder(R.color.colorAccent)
                                .into(imgPoster);
                        Glide.with(DetailTvShowActivity.this)
                                .load(tvShow.getBackdrop_path())
                                .apply(new RequestOptions().override(350, 550))
                                .placeholder(R.color.colorAccent)
                                .into(imgCover);
                        textViewTitle.setText(tvShow.getName());
                        textViewDate.setText(tvShow.getFirstAirDate());
                        textViewPopular.setText(Double.toString(tvShow.getPopularity()));
                        textViewCountry.setText(tvShow.getOriginCountry().toString().replaceAll("[\\[\\]]", ""));
                        textViewVote.setText(Integer.toString(tvShow.getVoteCount()));
                        textViewUserScore.setText(Double.toString(tvShow.getVoteAverage()));
                        textViewLang.setText(tvShow.getOriginalLanguage());
                        textViewGenre.setText(tvShow.getListGenreString().toString().replaceAll("[\\[\\]]", ""));
                        textViewOverview.setText(tvShow.getOverview());

                        progressBar.setVisibility(View.INVISIBLE);
                        tvShowHelper = TvShowHelper.getInstance(getApplicationContext());
                        tvShowHelper.open();
                        if (tvShowHelper.checkTvShowFav(tvShow.getId())) isFavorite = true;
                    }
                });
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            if (!tvShowHelper.checkTvShowFav(tvShow.getId())) {
                long result;
                result = tvShowHelper.insertTvShowFav(tvShow);
                if (result > 0) {
                    item.setIcon(R.drawable.ic_tab_favorit_gold);
                    Toast.makeText(DetailTvShowActivity.this, getResources().getString(R.string.success_add_fav), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(DetailTvShowActivity.this, getResources().getString(R.string.faild_add_fav), Toast.LENGTH_SHORT).show();
            }  else {
                if (tvShowHelper.checkTvShowFav(tvShow.getId())) {
                    long result = tvShowHelper.deleteTvShowFav(tvShow.getId());
                    if (result > 0) {
                        item.setIcon(R.drawable.ic_tab_favorit_gold);
                        Toast.makeText(DetailTvShowActivity.this, getResources().getString(R.string.success_remove_fav), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(DetailTvShowActivity.this, getResources().getString(R.string.faild_remove_fav), Toast.LENGTH_SHORT).show();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tvShowHelper !=null) tvShowHelper.close();
    }
}
