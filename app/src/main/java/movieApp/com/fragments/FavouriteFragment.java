package movieApp.com.fragments;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import activity.com.movietesttwo.movieApp.com.R;
import movieApp.com.UI.cursorAdapter;
import movieApp.com.classes.Response;
import movieApp.com.database.Contract;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FavouriteFragment extends Fragment implements LoaderCallbacks<Cursor> {
    private static final int LoaderID = 0;
    ArrayList<Response.ResultsEntity> resultsEntities;
    GridView gridView;
    private cursorAdapter mCursorAdapter;
    Cursor mCursor;
    String mSortBy;
    SharedPreferences preferences;
    String[] mAllColumns;
    Uri mUri;
    String[] columns = new String[6];
    String[] AllColumns = new String[0];


    public FavouriteFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favourite, container, false);
        gridView = (GridView) root.findViewById(R.id.movies);
        if (mCursor != null && mCursor.moveToFirst())
            ((CallBackFavourite) getActivity()).getFirstFavouriteItem(cursorToObject(mCursor));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((CallBackFavourite) getActivity()).getSelectedFavouriteItem(cursorToObject(mCursor));

            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LoaderID,null,this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LoaderID, savedInstanceState, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSortBy = preferences.getString(getString(R.string.sorting), getString(R.string.most_popular));
        getPref();
        return  new CursorLoader(getActivity(), mUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        mCursorAdapter = new cursorAdapter(getActivity(), data,mSortBy);
        mCursorAdapter.swapCursor(data);
        gridView.setAdapter(mCursorAdapter);

    }

    Response.ResultsEntity cursorToObject(Cursor data) {
        Response.ResultsEntity object = new Response.ResultsEntity();
        object.setId(data.getInt(data.getColumnIndex(columns[0])));
        object.setOriginal_title(data.getString(data.getColumnIndex(columns[1])));
        object.setRelease_date(data.getString(data.getColumnIndex(columns[2])));
        object.setOverview(data.getString(data.getColumnIndex(columns[3])));
        object.setPoster_path(data.getString(data.getColumnIndex(columns[4])));
        return object;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    public void getPref() {

        if (Objects.equals(mSortBy, getActivity().getString(R.string.most_popular))) {
            mUri = Contract.MoviesMostPop.CONTENT_MOVIES_URI.buildUpon().appendPath("0").appendPath("1").build();
            columns[0] = Contract.MoviesMostPop.MOVIE_iD;
            columns[1] = Contract.MoviesMostPop.MOVIE_TITLE;
            columns[2] = Contract.MoviesMostPop.MOVIE_DATE;
            columns[3] = Contract.MoviesMostPop.MOVIE_OVERVIEW;
            columns[4] = Contract.MoviesMostPop.MOVIE_POSTER_PATH;
            AllColumns = Contract.MoviesMostPop.AllColumns;
        }
        if (Objects.equals(mSortBy, getActivity().getString(R.string.highestRated))) {
            mUri = Contract.MoviesHighestRated.CONTENT_MOVIES_URI.buildUpon().appendPath("0").appendPath("1").build();
            columns[0] = Contract.MoviesHighestRated.MOVIE_iD;
            columns[1] = Contract.MoviesHighestRated.MOVIE_TITLE;
            columns[2] = Contract.MoviesHighestRated.MOVIE_DATE;
            columns[3] = Contract.MoviesHighestRated.MOVIE_OVERVIEW;
            columns[4] = Contract.MoviesHighestRated.MOVIE_POSTER_PATH;
            AllColumns = Contract.MoviesHighestRated.AllColumns;

        }
    }

    public interface CallBackFavourite {
        void getFirstFavouriteItem(Response.ResultsEntity firstMovie);

        void getSelectedFavouriteItem(Response.ResultsEntity selectedMovie);
    }
}
