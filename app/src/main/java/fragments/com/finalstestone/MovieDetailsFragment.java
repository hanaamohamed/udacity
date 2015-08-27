package fragments.com.finalstestone;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import activity.com.movietesttwo.R;
import classes.com.finalstestone.Response;
import classes.com.finalstestone.staticObjects;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

    ImageView poster;
    TextView title;
    TextView year;
    TextView min;
    TextView rate,description;
    Button favourite;
    View root;

    public MovieDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_movie_details, container, false);
        init();
        try {
            setData(staticObjects.resultsEntity);
        } catch (ParseException e) {
            Log.e("DATE","error",e);
        }
        return root;
    }

    private void setData(Response.ResultsEntity obj) throws ParseException {
        title.setText(obj.getOriginal_title());
        description.setText(obj.getOverview());
        rate.setText(obj.getVote_average() + "/10");
        Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w185" + obj.getPoster_path()).resize(150,200).into(poster);
        String date_obj = obj.getRelease_date()+" 00:00:00.0";
        year.setText(formatDate(date_obj) + "");
    }
    private int formatDate(String s) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        Date date = dateFormat.parse(s);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
    private void init() {
        poster = (ImageView) root.findViewById(R.id.poster);
        title = (TextView) root.findViewById(R.id.title);
        year = (TextView) root.findViewById(R.id.year);
        min = (TextView) root.findViewById(R.id.min);
        rate = (TextView) root.findViewById(R.id.rate);
        description = (TextView) root.findViewById(R.id.des);
        favourite = (Button) root.findViewById(R.id.fav);

    }
}
