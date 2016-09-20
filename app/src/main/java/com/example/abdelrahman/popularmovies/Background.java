package com.example.abdelrahman.popularmovies;
import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;

import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Created by abdelrahman on 12/08/16.
 */
public class Background  extends AsyncTask<String,Void,String>{
     private Activity activity;
    private final String LOG_TAG=Background.class.getSimpleName();
   private DataFetch dataFetch;

    public Background(Activity act,DataFetch x){
        activity=act;
        dataFetch=x;
    }
    @Override
    protected void onPostExecute(String s) {
        if(s!=null) {
            dataFetch.updateAdapter(s);
         super.onPostExecute(s);
        }
        else{
            Toast.makeText(activity.getApplicationContext(),"The internet connection is not stable",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection=null;
        BufferedReader bufferedReader=null;
        String movies=null;
        Uri.Builder uri=new Uri.Builder();
        uri.scheme("http");
        uri.authority("api.themoviedb.org");
        uri.appendPath("3");
        uri.appendPath("movie");
        if(strings[0].equals("popular")||strings[0].equals("top_rated")){
        uri.appendPath(strings[0]);
        uri.appendQueryParameter("api_key","Enter your API key here");//Enter your API key here
        uri.appendQueryParameter("page",strings[1]);
        }
        else{
            if(strings[1]=="videos"){
                uri.appendPath(strings[0]);
                uri.appendPath(strings[1]);
                uri.appendQueryParameter("api_key","Enter your API key here");//Enter your API key here

            }
            else{
                uri.appendPath(strings[0]);
                uri.appendPath(strings[1]);
                uri.appendQueryParameter("api_key","Enter your API key here");//Enter your API key here
            }
        }
        try {
            URL url=new URL(uri.build().toString());
              connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStreamReader input=new InputStreamReader(connection.getInputStream());
            if(input==null)
                return null;
            bufferedReader=new BufferedReader(input);
            StringBuffer buffer=new StringBuffer();
            String line;
            while((line=bufferedReader.readLine())!=null){
                buffer.append(line+"\n");
            }
            movies=buffer.toString();
            return toJsonArray(movies);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }  finally {
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                if (connection!=null){
                    connection.disconnect();
                }
            }
        }
        return null;
    }
    private String toJsonArray(String json) throws JSONException {
        JSONObject obj=new JSONObject(json);
        JSONArray arr=obj.getJSONArray("results");
        return arr.toString();
    }
}