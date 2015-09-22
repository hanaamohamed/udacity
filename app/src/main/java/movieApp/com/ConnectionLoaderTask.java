package movieApp.com;

import android.annotation.TargetApi;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Build;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import movieApp.com.classes.AsyncResponse;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ConnectionLoaderTask extends AsyncTaskLoader<HashMap> {
    List<String> mUri;
    AsyncResponse mResponse;

    public ConnectionLoaderTask(Context context) {
        super(context);
        mUri = new ArrayList<>();
    }

    public void implementResponse(AsyncResponse response) {
        mResponse = response;
    }

    public void addUri(String Uri) {
        mUri.add(Uri);
    }

    @Override
    public HashMap loadInBackground() {
        String Info;
        HashMap<String, String> jsonsMap = new HashMap<>();
        try {
            Info = httpClient.GetData(mUri.get(0));
            jsonsMap.put("firstConnection", Info);

            if (mUri.size() > 1) {
                Info = httpClient.GetData(mUri.get(1));
                jsonsMap.put("secondConnection", Info);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonsMap;
    }

    @Override
    public void deliverResult(HashMap data) {

        if (isStarted()) {
            super.deliverResult(data);
           // mResponse.connectionTask(data);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
