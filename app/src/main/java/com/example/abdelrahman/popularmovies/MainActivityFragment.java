package com.example.abdelrahman.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.abdelrahman.popularmovies.data.MovieContract;
import com.example.abdelrahman.popularmovies.data.MovieDB;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements DataFetch {
    private GridView grid;
    private ImageAdapter image;
    private final String MOVIE_TITLE="original_title";
    private final String MOVIE_OVERVIEW="overview";
    private final String MOVIE_RELEASE_DATE="release_date";
    private final String MOVIE_VOTE_AVG="vote_average";
    private final String POSTER="poster_path";
    private final String ID="id";
    private final String VOTES="vote_count";

    private int count=1;
    private boolean checkForDelay=true; //a delay for the grid view to load the next page
    private boolean checkifFavorite;
    private Background background;
    private  String settingSaved;
    private MovieDB movieDB;
    private String movietitle="";
    private String over_view="";
    private String release_date="";
   private String vote_avg="";
    private String poster="";
    private String id="";
    private String pop="";

    private final String LOG_TAG=MainActivityFragment.class.getSimpleName();

    public interface Callback{
        public void dataToBeSent(ArrayList data,Boolean fav);
    }
    @Override
    public void onStart() {
        updateData(count);
        super.onStart();
    }

    @Override
    public void updateAdapter(String post) {
        image.updateResult(post);
    }


    public void updateData(int x){
        SharedPreferences shared= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String channel = (shared.getString(getString(R.string.sort_movies),getString(R.string.default_movie)));
        if(channel.equals("favorite")){
            checkifFavorite=true;
            updateAdapter(null);
        }
        else {
            checkifFavorite=false;
            if (!channel.equals(settingSaved)) {
                count = 1;
                x = 1;
                settingSaved = channel;
            }

            background = new Background(getActivity()/*,image,null,null*/, (DataFetch) this);
            background.execute(channel, "" + x);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {





        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_main, container, false);
         grid=(GridView) rootView.findViewById(R.id.gridView);

        SharedPreferences shared= PreferenceManager.getDefaultSharedPreferences(getActivity());//this two lines to save the settings value in the settingssaved string
        settingSaved = (shared.getString(getString(R.string.sort_movies),getString(R.string.default_movie)));
        movieDB=new MovieDB(getContext());



        image=new ImageAdapter(getActivity());
        grid.setAdapter(image);
        grid.setOnItemClickListener(new GridView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               if (checkifFavorite) {
                   movietitle=image.dataTitle.get(i);
                   over_view=image.dataOverView.get(i);
                   release_date=image.dataReleaseDate.get(i);
                   vote_avg=image.dataVoteAvg.get(i);
                   poster=image.dataPoster.get(i);
                   pop=image.dataVotes.get(i);
                   id=image.dataId.get(i);
               }else{
                try {
                   JSONObject obj=image.data.getJSONObject(i);
                    movietitle=obj.getString(MOVIE_TITLE);
                    over_view=obj.getString(MOVIE_OVERVIEW);
                    release_date=obj.getString(MOVIE_RELEASE_DATE);
                    vote_avg=obj.getString(MOVIE_VOTE_AVG);
                  poster= obj.getString(POSTER);
                    id=obj.getString(ID);
                    pop=obj.getString(VOTES);
                } catch (JSONException e) {
                    e.printStackTrace();
                }}
                ArrayList<String> data=new ArrayList<String>(7);
                data.add(movietitle);
                data.add(over_view);
                data.add(poster);
                data.add(id);
                data.add(pop);
                data.add(release_date);
                data.add(vote_avg);
                ((Callback)getActivity()).dataToBeSent(data,checkifFavorite);
               /* Intent intent=new Intent(getActivity(),DetailedMovie.class);
               */         /*.putExtra("movie_title",movietitle).putExtra("over_view",over_view)
                      .putExtra("poster",poster).putExtra("id",id).putExtra("vote",pop)
                        .putExtra("release_date",release_date).putExtra("vote_avg",vote_avg).putExtra("checkIfFavorite",checkifFavorite);
               */
                /*startActivity(intent);
*/
            }
        });


        grid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }
            @Override
            public void onScroll(AbsListView absListView, int firstItem, int visibleItems, int itemsCount) {

                if(firstItem!=0&&firstItem+visibleItems>=itemsCount&&checkForDelay==true){
                    checkForDelay=false;
                   Timer buttonTimer = new Timer();
                    buttonTimer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    updateData(++count);
                                    checkForDelay=true;
                                }
                            });
                        }
                    }, 2000);
                }
            }
        });





        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.refresh_settings){
            count=1;
            updateData(count);
            return true;
        }
        else{
            if(item.getItemId()==R.id.action_settings){
                Intent intent=new Intent(getActivity(),SettingActivty.class);
                startActivity(intent);
            return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class ImageAdapter extends BaseAdapter{
    private Context mContext;
        private LayoutInflater inflater=null;
        /*w342
        */
        private final String LINK="http://image.tmdb.org/t/p/w342";
        private JSONArray data;
        private ArrayList<String> dataPoster;
        private ArrayList<String> dataTitle;
        private ArrayList<String> dataOverView;
        private ArrayList<String> dataReleaseDate;
        private ArrayList<String> dataVoteAvg;
        private ArrayList<String> dataVotes;
        private ArrayList<String> dataId;
        private final String[] MOVIE_COLUMNS={
                MovieContract.COLUMN_MOVIE_TITLE,
                MovieContract.COLUMN_MOVIE_VOTE,
                MovieContract.COLUMN_MOVIE_VOTE_AVG,
                MovieContract.COLUMN_OVERVIEW,
                MovieContract.COLUMN_POSTER,
                MovieContract.COLUMN_MOVIE_ID,
                MovieContract.COLUMN_RELEASE_Date
        };
        private final int COL_MOVIE_TITLE=0;
        private final int COL_MOVIE_VOTE=1;
        private final int COL_MOVIE_VOTE_AVG=2;
        private final int COL_MOVIE_OVERVIEW=3;
        private final int COL_MOVIE_POSTER=4;
        private final int COL_MOVIE_MOVIE_ID=5;
        private final int COL_MOVIE_RELEASE_DATE=6;



        private final String POSTER="poster_path";

        public void updateResult(String x){
            if(x==null){ //which means that it is the favorites
                dataPoster=new ArrayList<String>();
                dataId=new ArrayList<String>();
                dataOverView=new ArrayList<String>();
                dataVoteAvg=new ArrayList<String>();
                dataTitle=new ArrayList<String>();
                dataReleaseDate=new ArrayList<String>();
                dataVotes=new ArrayList<String>();
            SQLiteDatabase db =movieDB.getReadableDatabase();
            Cursor cursor=db.query(MovieContract.TABLE_NAME,MOVIE_COLUMNS,null,null,null,null,null);
            if(cursor.moveToFirst()){
                dataPoster.add(cursor.getString(COL_MOVIE_POSTER));
                dataOverView.add(cursor.getString(COL_MOVIE_OVERVIEW));
                dataId.add(cursor.getString(COL_MOVIE_MOVIE_ID));
                dataVoteAvg.add(cursor.getString(COL_MOVIE_VOTE_AVG));
                dataTitle.add(cursor.getString(COL_MOVIE_TITLE));
                dataReleaseDate.add(cursor.getString(COL_MOVIE_RELEASE_DATE));
                dataVotes.add(cursor.getString(COL_MOVIE_VOTE));
                while (cursor.moveToNext()){
                    dataPoster.add(cursor.getString(COL_MOVIE_POSTER));
                    dataOverView.add(cursor.getString(COL_MOVIE_OVERVIEW));
                    dataId.add(cursor.getString(COL_MOVIE_MOVIE_ID));
                    dataVoteAvg.add(cursor.getString(COL_MOVIE_VOTE_AVG));
                    dataTitle.add(cursor.getString(COL_MOVIE_TITLE));
                    dataReleaseDate.add(cursor.getString(COL_MOVIE_RELEASE_DATE));
                    dataVotes.add(cursor.getString(COL_MOVIE_VOTE));
                }
            }
            cursor.close();
                db.close();
            }
            else{


            if(count==1){
            try {

                 data=new JSONArray(x);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
            else{
                String y=","+x.substring(1, x.length()-1);
                String temp=data.toString();
                String newData=temp.substring(0,temp.length()-1)+y+"]";
                try {
                    data=new JSONArray(newData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
            notifyDataSetChanged();

        }
        public ImageAdapter(Context c){
            mContext=c;
            inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        public int getCount(){
            if (checkifFavorite){
            return dataPoster.size();
            }else {
                if (data != null) {
                    return data.length();
                }
                return 0;
            }
        }
        public Object getItem(int position) {
            if (checkifFavorite){
              return dataPoster.get(position);
            }
            else{
            try {
                if (data!=null) {
                    return data.getJSONObject(position);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            }
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }


        public View getView(int position,View convertView,ViewGroup parent){
             ImageView view;
            String link=null;
            if(convertView==null){
                view =(ImageView) inflater.inflate(R.layout.grid_item_view,null);
           }
            else{
                view=(ImageView)convertView;

            }



            if (checkifFavorite){
              link=dataPoster.get(position);
            }else{

            try {

                    link = data.getJSONObject(position).getString(POSTER);

                } catch (JSONException e) {
                e.printStackTrace();
            }}

            if(link!=null){

                Picasso.with(mContext).load(LINK+link).into(view);
            }
            return view;


        }


    }

}
