package com.cudaf.myappportfolio.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ru.noties.storm.anno.Column;
import ru.noties.storm.anno.Table;

@Table("movies")
public class Movie implements Serializable {

    @Column
    private int id;
    @Column
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @Column
    @Expose
    private String overview;
    @Column
    @SerializedName("vote_average")
    @Expose
    private double voteAverage;
    @Column
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @Column
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    private boolean favorite;

    public Movie() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getFullPosterPath() {
        return "http://image.tmdb.org/t/p/w500"
            + posterPath;
    }

}
