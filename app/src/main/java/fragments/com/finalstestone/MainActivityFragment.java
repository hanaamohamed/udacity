package fragments.com.finalstestone;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.List;

import UI.com.finalstestone.ResponseAdapter;
import activity.com.movietesttwo.MovieDetails;
import activity.com.movietesttwo.R;
import activity.com.movietesttwo.SettingsActivity;
import classes.com.finalstestone.Parser;
import classes.com.finalstestone.Response;
import classes.com.finalstestone.httpClient;
import classes.com.finalstestone.staticObjects;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    GridView lv;
    ProgressBar pb;
    SharedPreferences preferences;

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
        requestData();
        lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                staticObjects.resultsEntity = (Response.ResultsEntity) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), MovieDetails.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void requestData() {

        Task task = new Task();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = preferences.getString(getString(R.string.sorting), getString(R.string.most_popular));
        if (sortBy.equals(getString(R.string.highestRated)))
            task.execute("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=04db6a4e0e321dd1bec24ff22c995709");
        if (sortBy.equals(getString(R.string.most_popular)))
            task.execute("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=04db6a4e0e321dd1bec24ff22c995709");
    }


    class Task extends AsyncTask<String, Void, List<Response.ResultsEntity>> {

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected List<Response.ResultsEntity> doInBackground(String... strings) {
            List<Response.ResultsEntity> result;
            try {
                String tweetsInfo = httpClient.GetData(strings[0]);
                result = Parser.getData(tweetsInfo);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<Response.ResultsEntity> s) {
            updateDisplay(s);
            pb.setVisibility(View.INVISIBLE);
            super.onPostExecute(s);
        }
    }

    private void updateDisplay(List<Response.ResultsEntity> s) {
        //  Toast.makeText(getActivity(), s.length, Toast.LENGTH_LONG).show();
        ResponseAdapter adapter = new ResponseAdapter(getActivity(), R.layout.movie_item, s);
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
