package movieApp.com.classes;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ProgressBar;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParserTask extends AsyncTask<TaskParams, Void, HashMap> {

    Context context;
    ProgressBar pb;
    int typeParser;
    AsyncParserResponse asyncParserResponse;

    public ParserTask(Context context, ProgressBar pb, int typeParser, AsyncParserResponse asyncResponse) {
        this.context = context;
        this.pb = pb;
        this.typeParser = typeParser;
        this.asyncParserResponse = asyncResponse;
    }

    @Override
    protected void onPreExecute() {
        pb.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected HashMap doInBackground(TaskParams... params) {
        String[] keys = new String[]{"videos", "reviews", "movies"};
        HashMap<String, List> hashMap = new HashMap<>();
        switch (typeParser) {
            case 1:
                List<Response.ResultsEntity> resultMovies;
                resultMovies = Parser.getData(params[0].urlMovie);
                hashMap.put(keys[2], resultMovies);
                return hashMap;
            case 2:
                List resultVideos;
                resultVideos = Parser.getVideos(params[0].urlVideo);
                hashMap.put(keys[0], resultVideos);
                List resultReviews;
                resultReviews = Parser.getReviews(params[0].urlReview);
                hashMap.put(keys[1], resultReviews);
                return hashMap;
            default:
                return null;
        }

    }

    @Override
    protected void onPostExecute(HashMap s) {
        super.onPostExecute(s);
        pb.setVisibility(View.INVISIBLE);
        asyncParserResponse.parserTask(s);
    }

}
