package com.example.abdelrahman.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by abdelrahman on 28/08/16.
 */
public class MovieContract implements BaseColumns {
        public static final String TABLE_NAME = "Movie";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_VOTE = "vote";
        public static final String COLUMN_MOVIE_VOTE_AVG = "avg_vote";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_RELEASE_Date = "release_date";




}
