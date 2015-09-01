package classes.com.finalstestone;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;


import java.util.List;


public class ParserTask extends AsyncTask<String, Void, List> {

    Context context;
    ProgressBar pb;
    List resultsEntities;
    int typeParser ;
    AsyncParserResponse asyncParserResponse;

    public ParserTask(Context context, ProgressBar pb, int typeParser,AsyncParserResponse asyncResponse) {
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
    protected List<Response.ResultsEntity> doInBackground(String... strings) {
        switch (typeParser){
            case 1:
                List<Response.ResultsEntity> resultMovies;
                resultMovies = Parser.getData(strings[0]);
                return resultMovies;
            case 2:
                List resultVideos;
                resultVideos = Parser.getVideos(strings[0]);
                return resultVideos;
            default:
                return null;
        }

    }

    @Override
    protected void onPostExecute(List s) {
        super.onPostExecute(s);
        pb.setVisibility(View.INVISIBLE);
        resultsEntities =s;
        asyncParserResponse.parserTask(s);
    }

    public List getResultsEntities() {
        return resultsEntities;
    }
}
