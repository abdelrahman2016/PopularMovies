package com.example.abdelrahman.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.abdelrahman.popularmovies.data.MovieContract;
import com.example.abdelrahman.popularmovies.data.MovieDB;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailedMovieFragment extends Fragment {
    private Intent intent;
    private String rate;
    private String release_date;
    private String descr;
    private String vote;
    private String image_pos;
    private String title_mov;
    private View rootview;
    private final String LOG_TAG = getClass().getSimpleName();
    private MovieDB movieDB;
    private ViewHolder holder;
    private final String LINK="http://image.tmdb.org/t/p/w780";
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(rate, rate);
        outState.putString(release_date, release_date);
        outState.putString(descr, descr);
        outState.putString(vote, vote);
        outState.putString(image_pos, image_pos);
        outState.putString(title_mov, title_mov);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null) {
        rate = savedInstanceState.getString(rate);
            descr = savedInstanceState.getString(descr);
        vote = savedInstanceState.getString(vote);
        image_pos = savedInstanceState.getString(image_pos);
        title_mov = savedInstanceState.getString(title_mov);
        holder.votes.setText("Votes: " +vote);
        holder.overview.setText("Description:  " +descr);
        holder.avg.setText("Rate:  "+rate+"/10");
        holder.title.setText(title_mov);
        holder.release.setText("Release date:  "+release_date);
        Picasso.with(getContext()).load(LINK+image_pos).into(holder.image);
    }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arg=getArguments();
        if(arg !=null){
            ArrayList<String> data=arg.getStringArrayList("data");
            Boolean isFav=arg.getBoolean("favorite");

            /*       Log.v(LOG_TAG,""+data.size());
     *//*
            if(data==null){
                return null;
            }*/
            Log.v(LOG_TAG,"mesh null");
        rootview = inflater.inflate(R.layout.fragment_detailed_movie, container, false);
        holder=new ViewHolder(rootview);
        Typeface typeFace= Typeface.createFromAsset(getActivity().getAssets(),"gothic_0.TTF");
        holder.avg.setTypeface(typeFace);
        holder.title.setTypeface(typeFace);
        holder.release.setTypeface(typeFace);
        holder.overview.setTypeface(typeFace);
        holder.votes.setTypeface(typeFace);
     /*intent = getActivity().getIntent();
               Boolean checkIfFavorite=intent.getBooleanExtra("checkIfFavorite",false);
     */           /*Log.v(LOG_TAG,"check if favorite"+checkIfFavorite);*/
/*
        if (intent != null&&intent.hasExtra("id") && intent.hasExtra("movie_title") && intent.hasExtra("release_date") && intent.hasExtra("vote_avg") && intent.hasExtra("poster") && intent.hasExtra("over_view")) {
*/
/*
            title_mov = intent.getStringExtra("movie_title"); //2
            image_pos = intent.getStringExtra("poster"); //5
                rate =  intent.getStringExtra("vote_avg");
            release_date =  intent.getStringExtra("release_date") ;
                descr =  intent.getStringExtra("over_view");;
*/

            image_pos = data.get(2);
            title_mov = data.get(0);
            rate = data.get(6);
            release_date =data.get(5);
            descr = data.get(1);

            Picasso.with(getContext()).load(LINK+image_pos).into(holder.image);
/*
                vote = intent.getStringExtra("vote");
*/
            vote =data.get(4);

            holder.votes.setText("Votes: " +vote);
            holder.overview.setText("Description:  " +descr);
            holder.avg.setText("Rate:  "+rate+"/10");
            holder.title.setText(title_mov);
            holder.release.setText("Release date:  "+release_date);

            movieDB=new MovieDB(getContext());

           SQLiteDatabase db=movieDB.getReadableDatabase();
/*
            final String id=intent.getStringExtra("id");
*/
            final String id=data.get(3);

            final ImageButton button =(ImageButton) rootview.findViewById(R.id.favorite);
            Cursor cursor=db.query(MovieContract.TABLE_NAME,new String[]{MovieContract.COLUMN_MOVIE_ID},
                    MovieContract.COLUMN_MOVIE_ID+" = ?",new String[]{id},null,null,null);
            if (cursor.moveToFirst()){
                button.setImageResource(R.drawable.full_favorite);
            }
            cursor.close();
            db.close();



            button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLiteDatabase db=movieDB.getWritableDatabase();
                        Cursor cursor1=db.query(MovieContract.TABLE_NAME,new String[]{MovieContract.COLUMN_MOVIE_ID},
                                MovieContract.COLUMN_MOVIE_ID+" = ?",new String[]{id},null,null,null);
                        if (cursor1.moveToFirst()){
                           int b= db.delete(MovieContract.TABLE_NAME,MovieContract.COLUMN_MOVIE_ID+" LIKE ?",new String[]{id});
                            button.setImageResource(R.drawable.empty_favorite);
                        /*    Log.v(LOG_TAG,"hadelete aho "+b);
                        */}else{
                        ContentValues content=new ContentValues();

                        content.put(MovieContract.COLUMN_MOVIE_ID,id);
                        content.put(MovieContract.COLUMN_MOVIE_TITLE,title_mov);
                        content.put(MovieContract.COLUMN_MOVIE_VOTE,vote);
                        content.put(MovieContract.COLUMN_MOVIE_VOTE_AVG,rate);
                        content.put(MovieContract.COLUMN_OVERVIEW,descr);
                        content.put(MovieContract.COLUMN_RELEASE_Date,release_date);
                        /*    Log.v(LOG_TAG,image_pos);
                        */content.put(MovieContract.COLUMN_POSTER,image_pos);
                        db.insert(MovieContract.TABLE_NAME,null,content);

                            button.setImageResource(R.drawable.full_favorite);}
                            cursor1.close();
                            db.close();
   /*                     }
   */
                    }
                });



                LinearLayout linear = (LinearLayout) rootview.findViewById(R.id.trailer);
                linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(getActivity(), Trailers.class);
                        intent1.putExtra("id",id/* intent.getStringExtra("id")*/);
                        startActivity(intent1);

                    }
                });
                LinearLayout linear2 = (LinearLayout) rootview.findViewById(R.id.reviews);
                linear2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent2 = new Intent(getActivity(), Reviews.class);
                        intent2.putExtra("id",id /*intent.getStringExtra("id")*/);
                        startActivity(intent2);
                    }
                });
            /*}*/

    /*}
    */
            return rootview;
        }
        return null;
    }
public static class ViewHolder{
    TextView avg;
    TextView title;
    TextView release;
    ImageView image;
    TextView overview;
    TextView votes;
    public ViewHolder(View view){
    avg = (TextView) view.findViewById(R.id.avg_rate);
   title = (TextView) view.findViewById(R.id.title);
   release = (TextView) view.findViewById(R.id.date);
    overview = (TextView) view.findViewById(R.id.descr);
    image = (ImageView) view.findViewById(R.id.image);
     votes = (TextView) view.findViewById(R.id.votes);
}
}
}