package movieApp.com.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
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

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import activity.com.movietesttwo.movieApp.com.R;
import movieApp.com.ConnectionLoaderTask;
import movieApp.com.ParserTask;
import movieApp.com.UI.MovieDetailsAdapter;
import movieApp.com.classes.AsyncParserResponse;
import movieApp.com.classes.Response;
import movieApp.com.classes.Review;
import movieApp.com.classes.Video;
import movieApp.com.database.DatabaseSource;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<HashMap> {

    ImageView poster;
    TextView title;
    TextView year;
    TextView min;
    TextView rate, description;
    Button favourite;
    View root, header;
    ListView list;
    ProgressBar pb;
    Response.ResultsEntity mResultsEntity;
    public static String mKeyIntent = "movieDetails";
    public static final String DETAILS_TAG = "details";
    private SharedPreferences preferences;
    private int mLoaderID = 1;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_movie_details, container, false);
        header = inflater.inflate(R.layout.header_movie_list, null, false);
        Bundle getArgs = getArguments();
        if (getArgs != null) {
            mResultsEntity = getArgs.getParcelable(mKeyIntent);
        } else {
            Bundle getIntent = getActivity().getIntent().getExtras();
            if (getIntent != null)
                mResultsEntity = getIntent.getParcelable(mKeyIntent);
        }
        init();
        list.addHeaderView(header);
        if (mResultsEntity != null) {
            final DatabaseSource source = new DatabaseSource(getActivity());
            final int check = source.isFavourite(mResultsEntity.getId());
            if (check == 1)
                favourite.setBackgroundColor(Color.rgb(198, 226, 255));
            else
                favourite.setBackgroundColor(Color.LTGRAY);

            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (check == 1) {
                        favourite.setBackgroundColor(Color.LTGRAY);
                        source.favourite(mResultsEntity.getId(), 0);
                    } else {
                        favourite.setBackgroundColor(Color.rgb(198, 226, 255));
                        source.favourite(mResultsEntity.getId(), 1);

                    }
                }
            });

            try {
                setData();
            } catch (ParseException e) {
                Log.e("DATE", "error", e);
            }
            getLoaderManager().initLoader(mLoaderID, null, this);
        } else {
            pb.setVisibility(View.GONE);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) instanceof Video.ResultsEntity) {
                    Video.ResultsEntity video = (Video.ResultsEntity) parent.getItemAtPosition(position);
                    Uri uri = Uri.parse("http://www." + video.getSite() + ".com/watch?v=" + video.getKey());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });


        return root;
    }

    private void setData() throws ParseException {
        title.setText(mResultsEntity.getOriginal_title());
        description.setText(mResultsEntity.getOverview());
        rate.setText(mResultsEntity.getVote_average() + "/10");
        int dimens = (int) getActivity().getResources().getDimension(R.dimen.detailPoster);
        Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w185" + mResultsEntity.getPoster_path())
                .resize(dimens, dimens + 100).into(poster);
        if (!mResultsEntity.getRelease_date().isEmpty()) {
            String date_obj = mResultsEntity.getRelease_date();
            int date = formatDate(date_obj);
            year.setText(date + "");
            mResultsEntity.setRelease_date(date + "");
        } else
            year.setText("N/A");
    }


    private int formatDate(String s) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        pb = (ProgressBar) root.findViewById(R.id.progressBar2);
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

    @Override
    public Loader<HashMap> onCreateLoader(int id, Bundle args) {
        ConnectionLoaderTask connectionLoaderTask = new ConnectionLoaderTask(getActivity());
        int movieId = mResultsEntity.getId();
        connectionLoaderTask.addUri("http://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=04db6a4e0e321dd1bec24ff22c995709");
        connectionLoaderTask.addUri("http://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=04db6a4e0e321dd1bec24ff22c995709");
        return connectionLoaderTask;
    }


    @Override
    public void onLoadFinished(Loader<HashMap> loader, HashMap data) {
        String videos = (String) data.get("firstConnection");
        String reviews = (String) data.get("secondConnection");
        int secondParse = 2;
        ParserTask parserTask = new ParserTask(getActivity());
        parserTask.setPb(pb);
        parserTask.setTypeParser(secondParse);
        parserTask.setAsyncParserResponse(new AsyncParserResponse() {
            @Override
            public void parserTask(HashMap output) {
                updateDisplay(output);
            }
        });
        parserTask.execute(videos, reviews);
    }

    @Override
    public void onLoaderReset(Loader<HashMap> loader) {

    }
}
