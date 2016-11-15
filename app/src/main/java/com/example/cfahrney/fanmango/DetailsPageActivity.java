package com.example.cfahrney.fanmango;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cfahrney.fanmango.Data.Details.Details;
import com.example.cfahrney.fanmango.Data.DownloadImageTask;
import com.example.cfahrney.fanmango.Data.MoviesAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_page_activity);
        Intent intent = getIntent();
        int id = intent.getExtras().getInt("id");
        MoviesAPI myMovies = MoviesAPI.retrofit.create(MoviesAPI.class);
        final Call<Details> call = myMovies.getSpecificMovieData(id);
        call.enqueue(new Callback<Details>() {
            @Override
            public void onResponse(Call<Details> call, Response<Details> response) {
                addMetadata(response);
            } //if the call is successful then send the response to addMetaData() to be displayed

            @Override
            public void onFailure(Call<Details> call, Throwable t) {
                Log.d("fanmango", "Something went wrong: " + t.getMessage());
            } //if it fails catch it
        });
    }

    private void addMetadata(Response<Details> response) {
        View scrollSection = findViewById(R.id.scrollingDetails);
        LinearLayout enc = (LinearLayout) scrollSection.findViewById(R.id.encompassingLayoutDetails);
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        RelativeLayout details = (RelativeLayout) inflater.inflate(R.layout.details_page_singular_result, enc, false);
        TextView title = (TextView) details.findViewById(R.id.titleSection);
        TextView description = (TextView) details.findViewById(R.id.descriptionSection);
        ImageView poster = (ImageView) details.findViewById(R.id.posterSection);
        title.setText(response.body().getOriginalTitle());
        description.setText(response.body().getOverview());
        new DownloadImageTask(poster)
                .execute("http://image.tmdb.org/t/p/w500"+response.body().getPosterPath());
        enc.addView(details);
    }
}
