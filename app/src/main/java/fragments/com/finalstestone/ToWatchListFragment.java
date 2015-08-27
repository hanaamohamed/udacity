package fragments.com.finalstestone;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import activity.com.movietesttwo.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ToWatchListFragment extends Fragment {

    public ToWatchListFragment() {
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_to_watch_list, container, false);
    }
}
