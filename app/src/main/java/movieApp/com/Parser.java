package movieApp.com;

import com.google.gson.Gson;
import java.util.List;

import movieApp.com.classes.Response;
import movieApp.com.classes.Review;
import movieApp.com.classes.Video;


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
    public  static List getReviews(String jsonStr) {
        if (jsonStr != null) {
            Gson gson = new Gson();
            Review review = gson.fromJson(jsonStr, Review.class);
            return review.getResults();
        } else
            return null;
    }

}
