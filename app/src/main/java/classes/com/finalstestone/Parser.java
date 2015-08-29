package classes.com.finalstestone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
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


}
