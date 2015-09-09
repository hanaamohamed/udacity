package movieApp.com.classes;

import android.os.AsyncTask;

import java.io.IOException;

public class ConnectionTask extends AsyncTask<String, Void, String> {

    AsyncResponse asyncResponse = null;

    public ConnectionTask(AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String Info;
        try {
            Info = httpClient.GetData(strings[0]);

            if (strings.length>1) {
                Info += "@/";
                Info += httpClient.GetData(strings[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return Info;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        asyncResponse.connectionTask(s);
    }

}
