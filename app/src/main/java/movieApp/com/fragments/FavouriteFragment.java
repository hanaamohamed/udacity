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
import android.widget.GridView;

import java.util.ArrayList;

import activity.com.movietesttwo.movieApp.com.R;
import movieApp.com.UI.ResponseAdapter;
import movieApp.com.UI.cursorAdapter;
import movieApp.com.classes.Response;
import movieApp.com.database.Contract;
import movieApp.com.database.DatabaseSource;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FavouriteFragment extends Fragment implements LoaderCallbacks<Cursor> {
    private static final int ID = 0;
    ArrayList<Response.ResultsEntity> resultsEntities;
    GridView gridView;
    private cursorAdapter mCursorAdapter;


    public FavouriteFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_favourite, container, false);
        gridView = (GridView) root.findViewById(R.id.movies);
        // retrieveFavourites();
       // gridView.setAdapter(mCursorAdapter);
        return  root;
    }
   void retrieveFavourites(){
       resultsEntities = new ArrayList<>();
       ArrayList<String> Posters = new ArrayList<>();
       DatabaseSource source = new DatabaseSource(getActivity());
       resultsEntities = source.retrieveFav();
       for (Response.ResultsEntity entity: resultsEntities) {
            Posters.add(entity.getPoster_path());
        }
        ResponseAdapter adapter = new ResponseAdapter(getActivity(),R.layout.movie_item,Posters);
        gridView.setAdapter(adapter);
    }
    @Override
    public void onResume() {
        super.onResume();
       // retrieveFavourites();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ID,savedInstanceState,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Contract.Movies.CONTENT_MOVIES_URI.buildUpon().appendPath("0").appendPath("1").build();
        return new CursorLoader(getActivity(),uri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter = new cursorAdapter(getActivity(),data);
        mCursorAdapter.swapCursor(data);
        gridView.setAdapter(mCursorAdapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
