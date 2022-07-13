package com.example.mymovies.converters;

import androidx.room.TypeConverter;

import com.example.mymovies.pojos.Movie;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Converter {
    @TypeConverter
    public String fromListToString(List<Integer> movies) {
        return new Gson().toJson(movies);
    }

    @TypeConverter
    public List<Integer> fromStringToList(String jsonObject) {
        Gson gson = new Gson();
        ArrayList objects = gson.fromJson(jsonObject, ArrayList.class);
        ArrayList<Integer> movies = new ArrayList<>();
        for (Object object : objects) {
            movies.add(gson.fromJson(object.toString(), Integer.class));
        }
        return movies;
    }
}