package com.example.abdelrahman.popularmovies;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class TrailerFragment extends Fragment implements DataFetch {
private ArrayAdapter<String>adapter;
private     ListView view;
    private ShareActionProvider mShare;
    private Background background;
    private String firstUri;
    private final String LOG_TAG=TrailerFragment.class.getSimpleName();

    @Override
    public void updateAdapter(String x) {
        try {
            JSONArray arr=new JSONArray(x);
            int length=arr.length();
            String[] data=new String[length];
            final String[] keys=new String[arr.length()] ;
            for(int i=0;i<length;i++){
                JSONObject obj=arr.getJSONObject(i);
                data[i]=(obj.getString("type")+ ": "+obj.getString("name"));
                keys[i]=(obj.getString("key"));
            }
            firstUri="http://www.youtube.com/watch?v="+keys[0];
            Log.v(LOG_TAG,""+data.length);

            view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    startActivity((new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+keys[i]))));
                }
            });


            adapter.clear();

            adapter.addAll(data);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share,menu);
        MenuItem item=menu.findItem(R.id.share);
        mShare=(ShareActionProvider) MenuItemCompat.getActionProvider(item);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        if (id==R.id.share){
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.putExtra(Intent.EXTRA_TEXT,firstUri);
            intent.setType("text/plain");
            if(intent.resolveActivity(getActivity().getPackageManager())!=null){
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_trailer, container, false);
        view=(ListView) rootview.findViewById(R.id.list_view_videos);

        int layout=R.layout.list_view_video_item;
        adapter=new ArrayAdapter<String>(getActivity(),layout,R.id.list_view_video_textview,new ArrayList<String>());
        view.setAdapter(adapter);


        return rootview ;
    }



    @Override
    public void onStart() {
         background=new Background(getActivity()/*,null,adapter,view*/,this);
                background.execute(getActivity().getIntent().getStringExtra("id"),"videos");
        super.onStart();
    }

}
