package com.example.cfahrney.fanmango;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.cfahrney.fanmango.Data.Results.Model;
import com.example.cfahrney.fanmango.Data.MoviesAPI;
import com.example.cfahrney.fanmango.Data.Results.Result;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar_activity);
    }

    public void sendSearchQuery(View view) throws IOException {
        EditText editText = (EditText) findViewById(R.id.userSearch);
        String searchQuery = editText.getText().toString();
        if (!searchQuery.equals(null) && !searchQuery.isEmpty()) {
            MoviesAPI myMovies = MoviesAPI.retrofit.create(MoviesAPI.class);
            final Call<Model> call = myMovies.getMovies(searchQuery);
            call.enqueue(new Callback<Model>() {
                @Override
                public void onResponse(Call<Model> call, Response<Model> response) {
                    sendIntent(response);
                }

                @Override
                public void onFailure(Call<Model> call, Throwable t) {
                    Log.d("fanmango", "Something went wrong: " + t.getMessage());
                }
            });

        } else {
            Snackbar.make(view, "Search bar empty.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } //If the user's search is not empty then perform the search; else tell them to insert something before clicking send
    }

    private void sendIntent(Response<Model> response) {
        Intent intent = new Intent(this, ResultsActivity.class);
        List<Result> myResults =  response.body().getResults();
        String temp = new Gson().toJson(myResults);
        intent.putExtra("myResults", temp);
        startActivity(intent);
    }
}
