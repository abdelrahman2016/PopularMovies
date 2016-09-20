package com.example.abdelrahman.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by abdelrahman on 28/08/16.
 */
public class MovieDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    static final String DB_NAME="movies.db";

    public MovieDB(Context context){
        super(context,DB_NAME,null,DATABASE_VERSION);
    }
    @Override

    public void onCreate(SQLiteDatabase db) {

        String createMovieTable="CREATE TABLE "+MovieContract.TABLE_NAME+" ( "+MovieContract._ID+
            " INTEGER AUTO_INCREMENT PRIMARY KEY, " +MovieContract.COLUMN_MOVIE_ID+ " TEXT NOT NULL, "+
            MovieContract.COLUMN_MOVIE_TITLE+" TEXT NOT NULL, "+MovieContract.COLUMN_MOVIE_VOTE+
            " TEXT NOT NULL, "+MovieContract.COLUMN_MOVIE_VOTE_AVG+" TEXT NOT NULL, "+MovieContract.COLUMN_OVERVIEW
            +" TEXT NOT NULL, "+MovieContract.COLUMN_POSTER+" TEXT NOT NULL, "+
                MovieContract.COLUMN_RELEASE_Date+" TEXT NOT NULL , UNIQUE ( "+MovieContract.COLUMN_MOVIE_ID+" ) ON CONFLICT REPLACE );";
        db.execSQL(createMovieTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS "+MovieContract.TABLE_NAME);
        onCreate(db);
    }
}
