package com.kuycoding.moviecatalogue3.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TvShow implements Parcelable {
    private int id;
    private String posterPath;
    private String backdrop_path;
    private String name;
    private String firstAirDate;
    private double popularity;
    private ArrayList<String> originCountry;
    private int voteCount;
    private double voteAverage;
    private String originalLanguage;
    private ArrayList<Integer> genre;
    private ArrayList<String> listGenreString;
    private String overview;

    public TvShow(JSONObject object) {
        try {
            int id = object.getInt("id");
            String posterPath = "https://image.tmdb.org/t/p/w185/" + object.getString("poster_path");
            String backdrop_path = "https://image.tmdb.org/t/p/w780/" + object.getString("backdrop_path");
            String name = object.getString("name");
            String firstAirDate = object.getString("first_air_date");
            double popularity = object.getDouble("popularity");

            ArrayList<String> originCountry = new ArrayList<>();
            JSONArray arrayOriginCountry = object.getJSONArray("origin_country");
            for (int i = 0; i < arrayOriginCountry.length(); i++) {
                String item = arrayOriginCountry.getString(i);
                originCountry.add(item);
            }

            int voteCount = object.getInt("vote_count");
            double voteAverage = object.getDouble("vote_average");
            String originalLanguage = object.getString("original_language");

            final ArrayList<String> listGenreString = new ArrayList<>();

            JSONArray arrayGenreIds = object.getJSONArray("genre_ids");
            for (int i = 0; i < arrayGenreIds.length(); i++) {
                String item = arrayGenreIds.getString(i);
                listGenreString.add(item);
            }

            String overview = object.getString("overview");

            this.id = id;
            this.posterPath = posterPath;
            this.backdrop_path = backdrop_path;
            this.name = name;
            this.firstAirDate = firstAirDate;
            this.popularity = popularity;
            this.originCountry = originCountry;
            this.voteCount = voteCount;
            this.voteAverage = voteAverage;
            this.originalLanguage = originalLanguage;
            this.listGenreString = listGenreString;
            this.overview = overview;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getListGenreString() {
        return listGenreString;
    }

    public void setListGenreString(ArrayList<String> listGenreString) {
        this.listGenreString = listGenreString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public ArrayList<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(ArrayList<String> originCountry) {
        this.originCountry = originCountry;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public ArrayList<Integer> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<Integer> genre) {
        this.genre = genre;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }


    public TvShow() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdrop_path);
        dest.writeString(this.name);
        dest.writeString(this.firstAirDate);
        dest.writeDouble(this.popularity);
        dest.writeStringList(this.originCountry);
        dest.writeInt(this.voteCount);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.originalLanguage);
        dest.writeList(this.genre);
        dest.writeStringList(this.listGenreString);
        dest.writeString(this.overview);
    }

    private TvShow(Parcel in) {
        this.id = in.readInt();
        this.posterPath = in.readString();
        this.backdrop_path = in.readString();
        this.name = in.readString();
        this.firstAirDate = in.readString();
        this.popularity = in.readDouble();
        this.originCountry = in.createStringArrayList();
        this.voteCount = in.readInt();
        this.voteAverage = in.readDouble();
        this.originalLanguage = in.readString();
        this.genre = new ArrayList<>();
        in.readList(this.genre, Integer.class.getClassLoader());
        this.listGenreString = in.createStringArrayList();
        this.overview = in.readString();
    }

    public static final Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel source) {
            return new TvShow(source);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };
}
