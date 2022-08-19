package com.sheyx.mymovies.pojos;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sheyx.mymovies.converters.Converter;

import java.util.List;

@Entity(tableName = "search_result")
@TypeConverters(value = Converter.class)
public class SearchResult {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<Movie> movies = null;
    public SearchResult(Integer page, List<Movie> movies, int id) {
        this.page = page;
        this.movies = movies;
        this.id = id;
    }

    @Ignore
    public SearchResult(List<Movie> movies) {
        this.movies = movies;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
