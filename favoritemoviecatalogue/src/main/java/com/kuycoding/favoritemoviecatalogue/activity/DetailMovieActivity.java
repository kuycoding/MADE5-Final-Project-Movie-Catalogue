package com.kuycoding.favoritemoviecatalogue.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kuycoding.favoritemoviecatalogue.R;
import com.kuycoding.favoritemoviecatalogue.model.Movies;

public class DetailMovieActivity extends AppCompatActivity {
    private TextView textViewTitle;
    private TextView textViewUserScore;
    private TextView textViewDate;
    private TextView textViewVote;
    private TextView textViewPopular;
    private TextView textViewLang;
    private TextView textViewOverview;
    private ImageView imgPoster, imgCover;
    public static final String EXTRA_DATA = "extra_data";

    private Movies movies;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

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
                        textViewOverview.setText(movies.getOverview());

                        progressBar.setVisibility(View.INVISIBLE);
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
}
