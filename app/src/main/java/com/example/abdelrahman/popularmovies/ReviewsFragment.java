package com.example.abdelrahman.popularmovies;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class ReviewsFragment extends Fragment implements DataFetch {
    ArrayAdapter<String> adapter;
    ListView view;
    private final String LOG_TAG=getClass().getSimpleName();
    public ReviewsFragment() {
    }


    @Override
    public void updateAdapter(String x) {
        try {
            JSONArray arr=new JSONArray(x);
            int length=arr.length();
            String[] data=new String[length];
            for(int i=0;i<length;i++){
                JSONObject obj=arr.getJSONObject(i);

                data[i]="A movie review by "+obj.getString("author")+"\n\n" +obj.getString("content")+"\n";
            };
            adapter.clear();
            adapter.addAll(data);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_reviews, container, false);
        view=(ListView) rootView.findViewById(R.id.list_view_review);

        Log.v(LOG_TAG,""+view);


        adapter=new ArrayAdapter<String>(getActivity(),R.layout.list_view_review_item,R.id.list_view_review_textview,new ArrayList<String>());
        Log.v(LOG_TAG,""+adapter);
        view.setAdapter(adapter);
        return rootView ;
    }

    public void onStart() {
        new Background(getActivity(),/*null,adapter,view,*/(DataFetch)this).execute(getActivity().getIntent().getStringExtra("id"),"reviews");
        super.onStart();
    }




}
