package com.webservicepoc;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.webservicepoc.Model.Result;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private List<Result> searchResults = new ArrayList<Result>();
    private ListView resultsListView;
    private EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultsListView = (ListView) findViewById(R.id.results_listview);
        searchBox = (EditText) findViewById(R.id.search_box);
    }

    //Method to be called onClick on button
    public void search(View view) throws Exception{
        String searchTerm = URLEncoder.encode(searchBox.getText().toString(), "UTF-8");
        new Search().execute(searchTerm);
    }

    private void populateSearchResultsFromJSON(JSONObject json) throws Exception {
        //Populate ArrayList of Results
        searchResults.clear();
        JSONArray array = json.getJSONArray("results");
        for(int x = 0; x < array.length(); x++) {
            JSONObject value = array.getJSONObject(x);
            searchResults.add(new Result(value.get("name").toString(), value.get("formatted_address").toString()));
        }
    }

    private void populateListView() {
        SearchResultArrayAdapter adapter = new SearchResultArrayAdapter(this, R.layout.result_row, searchResults);
        resultsListView.setAdapter(adapter);
    }

    //Network calls must be made on a separate thread - AsyncTask does this for us
    private class Search extends AsyncTask<String, Void, Void> {

        //Abstract away this API key later
        private String ENDPOINT = "https://maps.googleapis.com/maps/api/place/textsearch/json?key=AIzaSyCMWhWJMcqu4JT9j65O29Qpzu9BYb6q2LU&query=";

        @Override
        protected Void doInBackground(String... searchTerm) {
            try {
                //Append searchTerm to the query string
                ENDPOINT = ENDPOINT + searchTerm[0];

                //Make web request
                URL url = new URL(ENDPOINT);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                //Create json string
                StringBuffer jsonStringBuilder = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonStringBuilder.append(line).append("\n");
                }

                //Create json object from string
                JSONObject jsonObject = new JSONObject(jsonStringBuilder.toString());

                populateSearchResultsFromJSON(jsonObject);
                Log.i("JSON", jsonStringBuilder.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void onPostExecute(Void x) {
            populateListView();
        }
    }
}
