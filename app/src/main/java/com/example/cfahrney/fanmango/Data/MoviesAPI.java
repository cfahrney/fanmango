package com.example.cfahrney.fanmango.Data;


import com.example.cfahrney.fanmango.Data.Details.Details;
import com.example.cfahrney.fanmango.Data.Results.Model;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by cfahrney on 8/5/2016.
 */


//http://api.themoviedb.org/3/search/movie?api_key=b18f10eed1608bdb27af5ab6994fc417&query=movie


public interface MoviesAPI {
    @GET("search/movie?api_key=b18f10eed1608bdb27af5ab6994fc417")
    Call<Model> getMovies(@Query("query") String query);

    @GET("movie/{id}?api_key=b18f10eed1608bdb27af5ab6994fc417")
            Call<Details> getSpecificMovieData(@Path("id") int id);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
