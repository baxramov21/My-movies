package com.sheyx.mymovies.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.sheyx.mymovies.pojos.Movie;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    @TypeConverter
    public String fromListToString(List<Movie> movies) {
        return new Gson().toJson(movies);
    }

    @TypeConverter
    public List<Movie> fromStringToList(String jsonObject) {
        Gson gson = new Gson();
        ArrayList objects = gson.fromJson(jsonObject, ArrayList.class);
        ArrayList<Movie> movies = new ArrayList<>();
        for (Object object : objects) {
            movies.add(gson.fromJson(object.toString(), Movie.class));
        }
        return movies;
    }
}