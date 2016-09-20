package com.example.abdelrahman.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private boolean is2Pane;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    setIntent(intent);
    }

    @Override
    public void dataToBeSent(ArrayList data,Boolean fav) {
        if (is2Pane){
        Bundle args=new Bundle();
            args.putStringArrayList("data",data);
            args.putBoolean("favorite",fav);
            DetailedMovieFragment fragment=new DetailedMovieFragment();

            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.detailed_movie_container,fragment).commit();

        }
        else{

            Intent intent=new Intent(this,DetailedMovie.class);

            intent.putExtra("favorite",fav);
            intent.putExtra("data",(ArrayList<String>)data);
            startActivity(intent);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       if (findViewById(R.id.detailed_movie_container)!=null){
           is2Pane=true;
           if (savedInstanceState==null){
               getSupportFragmentManager().beginTransaction().replace(R.id.detailed_movie_container,
                       new DetailedMovieFragment()).commit();
           }
       }
        else{
           is2Pane=false;
       }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.refresh,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}
