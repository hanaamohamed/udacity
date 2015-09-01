package classes.com.finalstestone;

import android.os.AsyncTask;

import java.io.IOException;

public class ConnectionTask extends AsyncTask<String, Void, String> {

    static String inputStream;
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
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return Info;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.inputStream = s;
        asyncResponse.connectionTask(s);
    }

    public String getInputStream() {
        return inputStream;
    }
}
