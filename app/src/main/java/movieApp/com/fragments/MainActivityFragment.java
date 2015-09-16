package movieApp.com.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import movieApp.com.classes.AsyncParserResponse;
import movieApp.com.database.DatabaseSource;
import movieApp.com.activity.MovieDetails;
import activity.com.movietesttwo.movieApp.com.R;
import movieApp.com.activity.SettingsActivity;
import movieApp.com.UI.ResponseAdapter;
import movieApp.com.classes.AsyncResponse;
import movieApp.com.classes.ConnectionTask;
import movieApp.com.classes.ParserTask;
import movieApp.com.classes.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    GridView lv;
    ProgressBar pb;
    SharedPreferences preferences;
    List<Response.ResultsEntity> resultsEntities;


    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        pb = (ProgressBar) rootView.findViewById(R.id.progressBar);
        lv = (GridView) rootView.findViewById(R.id.list);
        lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                for (Response.ResultsEntity results : resultsEntities) {
                    if (results.getPoster_path() == parent.getItemAtPosition(position)) {
                        Intent intent = new Intent(getActivity(), MovieDetails.class);
                        intent.putExtra("movieApp.com.classes.Response.ResultsEntity",results);
                        startActivity(intent);
                    }
                }

            }
        });
        requestData();
        return rootView;
    }

    private void requestData() {
        ConnectionTask task = new ConnectionTask(new AsyncResponse() {
            @Override
            public void connectionTask(HashMap output) {
                ParserTask parserTask = new ParserTask(getActivity(), pb, 1, new AsyncParserResponse() {
                    @Override
                    public void parserTask(HashMap output) {
                        updateDisplay(output);
                    }
                });
                String connection = (String) output.get("firstConnection");
                parserTask.execute(connection);
            }

        }
        );
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = preferences.getString(getString(R.string.sorting), getString(R.string.most_popular));
        if (sortBy.equals(getString(R.string.highestRated)))
            task.execute("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=04db6a4e0e321dd1bec24ff22c995709");
        if (sortBy.equals(getString(R.string.most_popular)))
            task.execute("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=04db6a4e0e321dd1bec24ff22c995709");
    }


    private void updateDisplay(HashMap hashMap) {
        List<Response.ResultsEntity> s = (List<Response.ResultsEntity>) hashMap.get("movies");
        DatabaseSource databaseSource = new DatabaseSource(getActivity());
        if (s == null) {
            Toast.makeText(getActivity(), "check your connection.", Toast.LENGTH_LONG).show();
            s = databaseSource.allMovies();
        }
        resultsEntities = s;
        ArrayList<String> imgList = new ArrayList<>();
        for (int i = 0; i < s.size(); i++) imgList.add(s.get(i).getPoster_path());
        databaseSource.fillContentValues(s);
        ResponseAdapter adapter = new ResponseAdapter(getActivity(), R.layout.movie_item, imgList);
        lv.setAdapter(adapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }
}
