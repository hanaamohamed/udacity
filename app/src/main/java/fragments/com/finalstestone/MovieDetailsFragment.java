package fragments.com.finalstestone;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import UI.com.finalstestone.VideosAdapter;
import activity.com.movietesttwo.R;
import classes.com.finalstestone.AsyncParserResponse;
import classes.com.finalstestone.AsyncResponse;
import classes.com.finalstestone.ConnectionTask;
import classes.com.finalstestone.Parser;
import classes.com.finalstestone.ParserTask;
import classes.com.finalstestone.Response;
import classes.com.finalstestone.Video;
import classes.com.finalstestone.httpClient;
import classes.com.finalstestone.staticObjects;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

    ImageView poster;
    TextView title;
    TextView year;
    TextView min;
    TextView rate, description;
    Button favourite;
    View root, header;
    ListView list;
    ProgressBar pb;
    String url;
    boolean check = false;
    Response.ResultsEntity resultsEntity;

    public MovieDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_movie_details, container, false);
        header = inflater.inflate(R.layout.header_videos, container, false);
        init();
        resultsEntity = staticObjects.resultsEntity;
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check) {
                    favourite.setBackgroundColor(Color.DKGRAY);
                    check = true;
                }
                else {
                    favourite.setBackgroundColor(Color.LTGRAY);
                    check = false;
                }
            }
        });
        list.addHeaderView(header);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        try {
            setData(staticObjects.resultsEntity);
        } catch (ParseException e) {
            Log.e("DATE", "error", e);
        }
        return root;
    }

    private void setData(Response.ResultsEntity obj) throws ParseException {
        title.setText(obj.getOriginal_title());
        description.setText(obj.getOverview());
        rate.setText(obj.getVote_average() + "/10");
        Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w185" + obj.getPoster_path()).resize(150, 200).into(poster);
        if (obj.getRelease_date()!=null) {
            String date_obj = obj.getRelease_date() + " 00:00:00.0";
            int date = formatDate(date_obj);
            year.setText(date + "");
            resultsEntity.setRelease_date(date+"");
        }else
            year.setText("N/A");
        int id = obj.getId();

        ConnectionTask task = new ConnectionTask(new AsyncResponse() {
            @Override
            public void connectionTask(String output) {
                ParserTask parserTask = new ParserTask(getActivity(), pb, 2, new AsyncParserResponse() {
                    @Override
                    public void parserTask(List output) {
                        updateDisplay(output);
                    }
                });
                parserTask.execute(output);
            }

        }
        );
        url = "http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=04db6a4e0e321dd1bec24ff22c995709";
        task.execute(url);

    }

    private int formatDate(String s) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        Date date = dateFormat.parse(s);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    private void init() {
        poster = (ImageView) header.findViewById(R.id.poster);
        title = (TextView) header.findViewById(R.id.title);
        year = (TextView) header.findViewById(R.id.year);
        min = (TextView) header.findViewById(R.id.min);
        rate = (TextView) header.findViewById(R.id.rate);
        description = (TextView) header.findViewById(R.id.des);
        favourite = (Button) header.findViewById(R.id.fav);
        list = (ListView) root.findViewById(R.id.trailerList);
        pb = (ProgressBar) root.findViewById(R.id.progressBar);
    }

    private void updateDisplay(List<Video.ResultsEntity> resultsEntities) {
        List<String> types = new ArrayList<>();
        if (resultsEntities == null)
            Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
        else {
            for (Video.ResultsEntity resultsEntity : resultsEntities)
                types.add(resultsEntity.getName());
            list.setAdapter(new VideosAdapter(getActivity(), R.layout.video_item, types));
        }
    }
}
