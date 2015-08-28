package classes.com.finalstestone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Exception;import java.lang.String;import java.lang.StringBuffer;import java.lang.System;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;
import android.widget.Toast;

public class httpClient {
	static String TAG = "CONNECTION";
	public static String GetData(String uri) throws IOException {
		BufferedReader input = null;

		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(uri);

			HttpResponse response = client.execute(request);
			input = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));
			StringBuffer sb = new StringBuffer();
			String l ;
			while ((l = input.readLine()) != null) {
				sb.append(l);
			}
			return sb.toString();

		} catch (Exception e) {
			Log.e(TAG, "Error in connection ", e);
			return null ;
		} finally {

			if (input != null) {
				input.close();
			}

		}

	}
	public static InputStream getStream(String uri) throws IOException {
		InputStream in = null;
		URL url = new URL(uri);
		try{
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.connect();
			in = httpURLConnection.getInputStream();
		} catch (Exception e) {
			Log.e(TAG, "Error in connection "+e, e);
		}
		return in;
	}


}