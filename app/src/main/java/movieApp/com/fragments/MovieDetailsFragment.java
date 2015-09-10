package movieApp.com.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import movieApp.com.UI.MovieDetailsAdapter;
import activity.com.movietesttwo.movieApp.com.R;
import movieApp.com.classes.AsyncParserResponse;
import movieApp.com.classes.AsyncResponse;
import movieApp.com.classes.ConnectionTask;
import movieApp.com.classes.ParserTask;
import movieApp.com.classes.Response;
import movieApp.com.classes.Review;
import movieApp.com.classes.TaskParams;
import movieApp.com.classes.Video;
import movieApp.com.classes.staticObjects;
import movieApp.com.database.DatabaseSource;


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

    Response.ResultsEntity resultsEntity;

    public MovieDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_movie_details, container, false);
        header = inflater.inflate(R.layout.header_movie_list, container, false);
        init();
        //Bundle bundle = getActivity().getIntent().getExtras();
        // resultsEntity = bundle.getParcelable("object");
        final DatabaseSource source = new DatabaseSource(getActivity());
        final int check = source.isFavourite(resultsEntity.getId());
        if (check == 1)
            favourite.setBackgroundColor(Color.rgb(198, 226, 255));
        else
            favourite.setBackgroundColor(Color.LTGRAY);

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check == 1) {
                    Toast.makeText(getActivity(), source.favourite(resultsEntity.getId(), 0) + "", Toast.LENGTH_SHORT).show();
                    favourite.setBackgroundColor(Color.LTGRAY);
                } else {
                    Toast.makeText(getActivity(), source.favourite(resultsEntity.getId(), 1) + "", Toast.LENGTH_SHORT).show();
                    favourite.setBackgroundColor(Color.rgb(198, 226, 255));
                }
            }
        });
        try {
            setData(staticObjects.resultsEntity);
        } catch (ParseException e) {
            Log.e("DATE", "error", e);
        }
        list.addHeaderView(header);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) instanceof Video.ResultsEntity){
                    Video.ResultsEntity video = (Video.ResultsEntity) parent.getItemAtPosition(position);
                    Uri uri = Uri.parse("http://www."+video.getSite()+".com/watch?v="+video.getKey());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }

            }
        });

        return root;
    }

    private void setData(Response.ResultsEntity obj) throws ParseException {
        title.setText(obj.getOriginal_title());
        description.setText(obj.getOverview());
        rate.setText(obj.getVote_average() + "/10");
        Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w185" + obj.getPoster_path()).resize(150, 200).into(poster);
        if (!obj.getRelease_date().isEmpty()) {
            String date_obj = obj.getRelease_date() + " 00:00:00.0";
            int date = formatDate(date_obj);
            year.setText(date + "");
            resultsEntity.setRelease_date(date + "");
        } else
            year.setText("N/A");
        retrieveData(obj.getId());
    }

    private void retrieveData(int id) {
        ConnectionTask task = new ConnectionTask(new AsyncResponse() {
            @Override
            public void connectionTask(String output) {
                String videos = null, reviews = null;
                if (output.contains("@/")) {
                    String[] strings = output.split("@/");
                    videos = strings[0];
                    reviews = strings[1];
                }
                ParserTask parserTask = new ParserTask(getActivity(), pb, 2, new AsyncParserResponse() {
                    @Override
                    public void parserTask(HashMap output) {
                        updateDisplay(output);
                    }
                });
                parserTask.execute(new TaskParams(null, reviews, videos));
            }

        }
        );
        String urlVideos  = "http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=04db6a4e0e321dd1bec24ff22c995709";
        String urlReviews = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=04db6a4e0e321dd1bec24ff22c995709";
        task.execute(urlVideos, urlReviews);
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
        resultsEntity = staticObjects.resultsEntity;

    }

    private void updateDisplay(HashMap hashMap) {
        if (hashMap.size() > 0) {
            MovieDetailsAdapter movieDetailsAdapter = new MovieDetailsAdapter(getActivity());
            movieDetailsAdapter.addSection("Trailers");
            List<Video.ResultsEntity> videos = (List<Video.ResultsEntity>) hashMap.get("videos");
            if (videos != null) {
                for (Video.ResultsEntity video : videos)
                    movieDetailsAdapter.addItem(video);
            }
            movieDetailsAdapter.addSection("Reviews");
            List<Review.ResultsEntity> reviews = (List<Review.ResultsEntity>) hashMap.get("reviews");
            if (reviews != null) {
                for (Review.ResultsEntity review : reviews)
                    movieDetailsAdapter.addItem(review);
            }
            list.setAdapter(movieDetailsAdapter);
        }
    }
}
