package com.example.abdelrahman.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

public class DetailedMovie extends AppCompatActivity {
    private final String LOG_TAG=getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie);
        if (savedInstanceState==null){
            Bundle bundle=new Bundle();
           Intent tt= getIntent();
            ArrayList<String> arr=tt.getStringArrayListExtra("data");
                bundle.putBoolean("favorite", tt.getBooleanExtra("favorite", false));
              bundle.putStringArrayList("data", arr);
                DetailedMovieFragment fragment = new DetailedMovieFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.detailed_movie_container,
                        fragment).commit();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
