package com.kuycoding.favoritemoviecatalogue.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kuycoding.favoritemoviecatalogue.R;
import com.kuycoding.favoritemoviecatalogue.model.Movies;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private ArrayList<Movies> mData = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public ArrayList<Movies> getmData() {
        return mData;
    }

    public void setmData(ArrayList<Movies> data) {
        mData.clear();
        mData.addAll(data);
        Log.i("SETMOVIES", mData.toString());
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview_movies, viewGroup, false);
        return new MovieViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder movieViewHolder, int position) {
        movieViewHolder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvTitle, tvUserScore, tvGenre, tvDate;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_item_poster_fav);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvUserScore = itemView.findViewById(R.id.tv_item_userscore);
            tvGenre = itemView.findViewById(R.id.tv_item_genre);
            tvDate = itemView.findViewById(R.id.tv_item_date);
        }

        @SuppressLint("SetTextI18n")
        void bind(final Movies movies) {
            tvTitle.setText(String.valueOf(movies.getTitle()));
            tvUserScore.setText(Double.toString(movies.getVoteAverage()));
//            tvGenre.setText(movies.getListGenreString().toString().replaceAll("[\\[\\]]", ""));
            tvDate.setText(movies.getReleaseDate());

            if (movies.getPosterPath() != null) {
                Glide.with(itemView.getContext())
                        .load(movies.getPosterPath())
                        .placeholder(R.color.colorAccent)
                        .apply(new RequestOptions().override(350, 550))
                        .into(imgPoster);
            }
            assert movies.getPosterPath() != null;
            Log.d("poster :", movies.getPosterPath());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickCallback.onItemClicked(mData.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movies movies);
    }
}
