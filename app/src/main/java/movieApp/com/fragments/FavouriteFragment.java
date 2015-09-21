package movieApp.com.fragments;

import android.annotation.TargetApi;
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


    public FavouriteFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favourite, container, false);
        gridView = (GridView) root.findViewById(R.id.movies);
        if (mCursor!=null && mCursor.moveToFirst())
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

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LoaderID, savedInstanceState, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Contract.Movies.CONTENT_MOVIES_URI.buildUpon().appendPath("0").appendPath("1").build();
        return new CursorLoader(getActivity(), uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        mCursorAdapter = new cursorAdapter(getActivity(), data);
        mCursorAdapter.swapCursor(data);
        gridView.setAdapter(mCursorAdapter);

    }

    Response.ResultsEntity cursorToObject(Cursor data) {
        Response.ResultsEntity object = new Response.ResultsEntity();
        object.setId(data.getInt(data.getColumnIndex(Contract.Movies.MOVIE_iD)));
        object.setPoster_path(data.getString(data.getColumnIndex(Contract.Movies.MOVIE_POSTER_PATH)));
        object.setOverview(data.getString(data.getColumnIndex(Contract.Movies.MOVIE_OVERVIEW)));
        object.setRelease_date(data.getString(data.getColumnIndex(Contract.Movies.MOVIE_DATE)));
        object.setOriginal_title(data.getString(data.getColumnIndex(Contract.Movies.MOVIE_TITLE)));
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
