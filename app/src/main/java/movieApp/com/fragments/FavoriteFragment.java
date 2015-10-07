package movieApp.com.fragments;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import activity.com.movietesttwo.movieApp.com.R;
import movieApp.com.UI.cursorAdapter;
import movieApp.com.classes.Response;
import movieApp.com.database.Contract;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FavoriteFragment extends Fragment implements LoaderCallbacks<Cursor> {
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


    public FavoriteFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);
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
        getLoaderManager().restartLoader(LoaderID, null, this);
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
        mUri = Contract.Favorite.CONTENT_MOVIES_URI;
        return new CursorLoader(getActivity(), mUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        mCursorAdapter = new cursorAdapter(getActivity(), data, mSortBy);
        mCursorAdapter.swapCursor(data);
        gridView.setAdapter(mCursorAdapter);

    }

    Response.ResultsEntity cursorToObject(Cursor data) {
        Response.ResultsEntity object = new Response.ResultsEntity();
        object.setId(data.getInt(data.getColumnIndex(Contract.Favorite.MOVIE_iD)));
        object.setOriginal_title(data.getString(data.getColumnIndex(Contract.Favorite.MOVIE_TITLE)));
        object.setRelease_date(data.getString(data.getColumnIndex(Contract.Favorite.MOVIE_DATE)));
        object.setOverview(data.getString(data.getColumnIndex(Contract.Favorite.MOVIE_OVERVIEW)));
        object.setPoster_path(data.getString(data.getColumnIndex(Contract.Favorite.MOVIE_POSTER_PATH)));
        return object;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }


    public interface CallBackFavourite {
        void getFirstFavouriteItem(Response.ResultsEntity firstMovie);

        void getSelectedFavouriteItem(Response.ResultsEntity selectedMovie);
    }

}
