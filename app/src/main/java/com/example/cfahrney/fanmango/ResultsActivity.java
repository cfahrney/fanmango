package com.example.cfahrney.fanmango;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cfahrney.fanmango.Data.Results.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ResultsActivity extends AppCompatActivity {
    public List<Result> _results;

    public void setResults(List<Result> res) {
        _results = res;
    }

    public List<Result> getResults() {
        return _results;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_page_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.returnButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Return", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        String temp = getIntent().getStringExtra("myResults");
        setResults((List<Result>) new Gson().fromJson(temp, new TypeToken<List<Result>>(){}.getType()));
        displayResults();
    }



    private void displayResults() {
        List<Result> results = getResults();
        View scrollSection = findViewById(R.id.scrolling);
        LinearLayout enc = (LinearLayout) scrollSection.findViewById(R.id.encompassingLayout);
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        if (results.size() > 0) {
            for (int i = 0; i < results.size(); i++) {
                LinearLayout curResult = (LinearLayout) inflater.inflate(R.layout.results_page_singular_result, enc, false);
                TextView title = (TextView) curResult.findViewById(R.id.movietitle);
                title.setText(results.get(i).getTitle());
                TextView description = (TextView) curResult.findViewById(R.id.description);

                boolean detailsPageExists = dealWithDescription(description, results.get(i).getOverview());
                TextView seeMoreButton = (TextView) curResult.findViewById(R.id.seemore);
                if (detailsPageExists) {
                    seeMoreButton.setVisibility(View.VISIBLE);
                    seeMoreButton.setId(i);
                }
                else
                    seeMoreButton.setVisibility(View.GONE);
                enc.addView(curResult);
            } //if there is a description for the current result, display it. If not, tell the user there is no description
        } else {
            TextView noResults = new TextView(getApplicationContext());
            noResults.setText(R.string.noResults);
            enc.addView(noResults);
        } //if there were any results, display them accordingly. If not, tell the user there are no results

    }

    private boolean dealWithDescription(TextView description, String overview) {
        String string_description = "";
        if (!overview.isEmpty() && !overview.equals(null)) {
            if (overview.length() > 80)
                for (int j = 0; j < 40; j++)
                    string_description += overview.substring(j, j+1);
            string_description += "...";
            description.setText(string_description);
            return true;
        } else {
            description.setText(R.string.noDesc);
            return false;
        } //no available overview for search result
    }


    public void createDetailsPage(View view) {
        Intent intent = new Intent(this, DetailsPageActivity.class);
        intent.putExtra("id", getResults().get(view.getId()).getId()); //gets the result based on button's ID and then gets the shows ID for http request
        startActivity(intent);
    } //createDetailsPage() initializes and starts the activity if the user clicks on a 'see more' from a 'see more' enabled search result
}
