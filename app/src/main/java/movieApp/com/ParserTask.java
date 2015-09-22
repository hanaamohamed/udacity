package movieApp.com;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;


import java.util.HashMap;
import java.util.List;

import movieApp.com.classes.AsyncParserResponse;
import movieApp.com.classes.Response;


public class ParserTask extends AsyncTask<String, Void, HashMap> {

    Context context;
    ProgressBar pb;
    int typeParser;
    AsyncParserResponse asyncParserResponse;

    public ParserTask(Context context) {
        this.context = context;
    }

    public void setAsyncParserResponse(AsyncParserResponse asyncParserResponse) {
        this.asyncParserResponse = asyncParserResponse;
    }

    public void setTypeParser(int typeParser) {
        this.typeParser = typeParser;
    }

    public void setPb(ProgressBar pb) {
        this.pb = pb;
    }

    @Override
    protected void onPreExecute() {
        pb.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected HashMap doInBackground(String... params) {
        String[] keys = new String[]{"videos", "reviews", "movies"};
        HashMap<String, List> hashMap = new HashMap<>();
        switch (typeParser) {
            case 1:
                List<Response.ResultsEntity> resultMovies;
                resultMovies = Parser.getData(params[0]);
                hashMap.put(keys[2], resultMovies);
                return hashMap;
            case 2:
                List resultVideos;
                resultVideos = Parser.getVideos(params[0]);
                hashMap.put(keys[0], resultVideos);
                List resultReviews;
                resultReviews = Parser.getReviews(params[1]);
                hashMap.put(keys[1], resultReviews);
                return hashMap;
            default:
                return null;
        }

    }

    @Override
    protected void onPostExecute(HashMap s) {
        super.onPostExecute(s);
        pb.setVisibility(View.GONE);
        asyncParserResponse.parserTask(s);
    }

}
