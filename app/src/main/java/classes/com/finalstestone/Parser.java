package classes.com.finalstestone;

import com.google.gson.Gson;
import java.util.List;


public class Parser {
    public static List<Response.ResultsEntity> getData(String jsonStr){
        if (jsonStr!=null) {
            Gson gson = new Gson();
            Response response = gson.fromJson(jsonStr, Response.class);
            return response.getResults();
        }else
            return null;
    }
    public static List getVideos(String jsonStr){
        if (jsonStr!=null) {
            Gson gson = new Gson();
            Video response = gson.fromJson(jsonStr, Video.class);
            return response.getResults();
        }else
            return null;

    }


}
