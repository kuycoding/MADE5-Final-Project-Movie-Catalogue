package com.kuycoding.moviecatalogue3.adapter;

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
import com.kuycoding.moviecatalogue3.R;
import com.kuycoding.moviecatalogue3.model.TvShow;

import java.util.ArrayList;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder> {
    private ArrayList<TvShow> mData = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public ArrayList<TvShow> getmData() {
        return mData;
    }

    public void setmData(ArrayList<TvShow> data) {
        mData.clear();
        mData.addAll(data);
        Log.i("SETTV", mData.toString());
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public TvShowAdapter.TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview_tvshows, viewGroup, false);
        return new TvShowAdapter.TvShowViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowAdapter.TvShowViewHolder tvShowViewHolder, int position) {
        tvShowViewHolder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class TvShowViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvTitle, tvUserScore, tvGenre, tvDate;

        TvShowViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_item_poster);
            tvTitle = itemView.findViewById(R.id.tv_item_name);
            tvUserScore = itemView.findViewById(R.id.tv_item_userscore);
            tvGenre = itemView.findViewById(R.id.tv_item_genre);
            tvDate = itemView.findViewById(R.id.tv_item_date);
        }

        @SuppressLint("SetTextI18n")
        void bind(final TvShow tvShow) {
            Glide.with(itemView.getContext())
                    .load(tvShow.getPosterPath())
                    .placeholder(R.color.colorAccent)
                    .apply(new RequestOptions().override(350, 550))
                    .into(imgPoster);
            tvTitle.setText(String.valueOf(tvShow.getName()));
            tvUserScore.setText(Double.toString(tvShow.getVoteAverage()));
            tvGenre.setText(String.valueOf(tvShow.getOverview()));
/*            tvGenre.setText(tvShow.getListGenreString().toString().replaceAll("[\\[\\]]", ""));
            tvDate.setText(tvShow.getFirstAirDate());*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickCallback.onItemClicked(mData.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(TvShow tvShow);
    }
}
