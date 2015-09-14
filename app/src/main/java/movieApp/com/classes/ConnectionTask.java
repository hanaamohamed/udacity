package movieApp.com.classes;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.HashMap;

public class ConnectionTask extends AsyncTask<String, Void, HashMap> {

    AsyncResponse asyncResponse = null;

    public ConnectionTask(AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected HashMap doInBackground(String... strings) {
        String Info;
        HashMap<String,String> hashMap = new HashMap<>();
        try {
            Info = httpClient.GetData(strings[0]);
            hashMap.put("firstConnection",Info);

            if (strings.length>1) {
                Info = httpClient.GetData(strings[1]);
                hashMap.put("secondConnection",Info);

            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return hashMap;
    }

    @Override
    protected void onPostExecute(HashMap s) {
        super.onPostExecute(s);
        asyncResponse.connectionTask(s);
    }

}
