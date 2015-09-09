package movieApp.com.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import activity.com.movietesttwo.movieApp.com.R;
import movieApp.com.UI.ResponseAdapter;
import movieApp.com.classes.Response;
import movieApp.com.database.DatabaseSource;


/**
 * A placeholder fragment containing a simple view.
 */
public class FavouriteFragment extends Fragment {
    ArrayList<Response.ResultsEntity> resultsEntities;
    GridView gridView;
    public FavouriteFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_favourite, container, false);
        gridView = (GridView) root.findViewById(R.id.movies);
        retrieveFavourites();
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
        retrieveFavourites();
    }
}
