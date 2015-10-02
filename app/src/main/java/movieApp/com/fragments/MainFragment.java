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
import java.util.Objects;

import movieApp.com.ConnectionLoaderTask;
import movieApp.com.classes.AsyncParserResponse;
import movieApp.com.database.DatabaseSource;
import activity.com.movietesttwo.movieApp.com.R;
import movieApp.com.activity.SettingsActivity;
import movieApp.com.UI.ResponseAdapter;
import movieApp.com.ParserTask;
import movieApp.com.classes.Response;

/**
 * A placeholder fragment containing a simple view.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<HashMap> {


    private static final int mLoaderID = 0;
    GridView gridView;
    ProgressBar pb;
    SharedPreferences preferences;
    List<Response.ResultsEntity> mResultMovies;
    String mSortBy;

    public MainFragment() {
        setHasOptionsMenu(true);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        pb = (ProgressBar) rootView.findViewById(R.id.progressBar);
        gridView = (GridView) rootView.findViewById(R.id.list);
        gridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                for (Response.ResultsEntity results : mResultMovies) {
                    if (results.getPoster_path() == parent.getItemAtPosition(position)) {
                        ((Callback) getActivity()).onItemSelected(results);
                    }
                }

            }
        });
        //requestData();
        return rootView;
    }


    private void updateDisplay(HashMap hashMap) {
        mResultMovies = (List<Response.ResultsEntity>) hashMap.get("movies");
        DatabaseSource databaseSource = new DatabaseSource(getActivity(), mSortBy);
        if (mResultMovies == null) {
            Toast.makeText(getActivity(), "check your connection.", Toast.LENGTH_LONG).show();
            if (Objects.equals(mSortBy, getString(R.string.most_popular)))
                mResultMovies = databaseSource.allMovies();
            if (Objects.equals(mSortBy, getString(R.string.highestRated)))
                mResultMovies = databaseSource.allMovies();

        } else {
            ((Callback) getActivity()).getTheFirstItem(mResultMovies.get(0));

            if (Objects.equals(mSortBy, getString(R.string.most_popular)))
                Toast.makeText(getActivity(), "" + databaseSource.insertAll(mResultMovies, getString(R.string.most_popular)), Toast.LENGTH_LONG).show();
            if (Objects.equals(mSortBy, getString(R.string.highestRated)))
                databaseSource.insertAll(mResultMovies, getString(R.string.highestRated));

        }
        ArrayList<String> imgList = new ArrayList<>();
        for (int i = 0; i < mResultMovies.size(); i++) {
            imgList.add(mResultMovies.get(i).getPoster_path());
        }
        ResponseAdapter adapter = new ResponseAdapter(getActivity(), R.layout.movie_item, imgList);
        gridView.setAdapter(adapter);
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
        //requestData();
        getLoaderManager().restartLoader(mLoaderID, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(mLoaderID, savedInstanceState, this);
    }


    @Override
    public android.support.v4.content.Loader<HashMap> onCreateLoader(int id, Bundle args) {
        ConnectionLoaderTask connectionLoaderTask = new ConnectionLoaderTask(getActivity());
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSortBy = preferences.getString(getString(R.string.sorting), getString(R.string.most_popular));
        if (mSortBy.equals(getString(R.string.highestRated)))
            connectionLoaderTask.addUri("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=04db6a4e0e321dd1bec24ff22c995709");
        if (mSortBy.equals(getString(R.string.most_popular)))
            connectionLoaderTask.addUri("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=04db6a4e0e321dd1bec24ff22c995709");
        return connectionLoaderTask;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<HashMap> loader, HashMap data) {
        String firstConnectionResult = (String) data.get("firstConnection");
        int mainParse = 1;
        ParserTask parserTask = new ParserTask(getActivity());
        parserTask.setPb(pb);
        parserTask.setTypeParser(mainParse);
        parserTask.setAsyncParserResponse(new AsyncParserResponse() {
            @Override
            public void parserTask(HashMap output) {
                updateDisplay(output);
            }
        });
        parserTask.execute(firstConnectionResult);

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<HashMap> loader) {

    }

    public interface Callback {
        void onItemSelected(Response.ResultsEntity resultsEntity);

        void getTheFirstItem(Response.ResultsEntity firstMovie);
    }


}
