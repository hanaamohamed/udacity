package movieApp.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import movieApp.com.classes.Response;

public class DatabaseSource {
    Context context;

    public DatabaseSource(Context context) {
        this.context = context;
    }

    public int Delete(Response.ResultsEntity resultsEntity) {
        return context.getContentResolver().delete(Contract.Movies.CONTENT_MOVIES_URI,
                MoviesProvider.SelectByID,
                new String[]{resultsEntity.getId() + ""});
    }

    public boolean fillContentValues(List<Response.ResultsEntity> resultsEntities) {
        boolean check = false;
        for (Response.ResultsEntity resultsEntity : resultsEntities) {
            if (isExist(resultsEntity.getId()) == -1) {
                ContentValues values = new ContentValues();
                values.put(Contract.Movies.MOVIE_iD, resultsEntity.getId());
                values.put(Contract.Movies.MOVIE_TITLE, resultsEntity.getTitle());
                values.put(Contract.Movies.MOVIE_DATE, 2002);
                values.put(Contract.Movies.MOVIE_OVERVIEW, resultsEntity.getOverview());
                values.put(Contract.Movies.FAVOURITE, 0);
                values.put(Contract.Movies.MOVIE_RATE, resultsEntity.getVote_average());
                values.put(Contract.Movies.MOVIE_POSTER_PATH, resultsEntity.getPoster_path());
                long id = Long.parseLong(context.getContentResolver().insert(Contract.Movies.CONTENT_MOVIES_URI,values).getPathSegments().get(1));
                if (id>0)
                    check = true;
            } else
                break;
        }
        return check;
    }



    public long isExist(int _id) {
        long id;
        Uri uri = Contract.Movies.CONTENT_MOVIES_URI.buildUpon().appendPath(String.valueOf(_id)).build();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri,
                    Contract.Movies.AllColumns,
                    null,
                    new String[]{"" + _id},
                    null);
            if (cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndex(Contract.Movies.MOVIE_iD));
            } else
                id = -1;
        } finally {
            cursor.close();

        }
        return id;
    }

    public int favourite(long id, int fav) {
        Uri uri = Contract.Movies.CONTENT_MOVIES_URI;
        ContentValues values = new ContentValues();
        values.put(Contract.Movies.FAVOURITE, fav);
        return context.getContentResolver().update(uri, values, null, new String[]{String.valueOf(id)});
    }

   public ArrayList<Response.ResultsEntity> retrieveFav() {
        ArrayList<Response.ResultsEntity> resultsEntities = new ArrayList<>();
        Uri uri = Contract.Movies.CONTENT_MOVIES_URI.buildUpon().appendPath("0").appendPath("1").build();
        Cursor cursor = context.getContentResolver().query(uri,null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    Response.ResultsEntity entity = new Response.ResultsEntity();
                    entity.setId(cursor.getInt(cursor.getColumnIndex(Contract.Movies.MOVIE_iD)));
                    entity.setTitle(cursor.getString(cursor.getColumnIndex(Contract.Movies.MOVIE_TITLE)));
                    entity.setRelease_date(cursor.getString(cursor.getColumnIndex(Contract.Movies.MOVIE_DATE)));
                    entity.setOverview(cursor.getString(cursor.getColumnIndex(Contract.Movies.MOVIE_OVERVIEW)));
                    //entity.setPopularity(cursor.getInt(cursor.getColumnIndex(Contract.Movies.Mov)));
                    entity.setPoster_path(cursor.getString(cursor.getColumnIndex(Contract.Movies.MOVIE_POSTER_PATH)));
                    entity.setVote_average(cursor.getDouble(cursor.getColumnIndex(Contract.Movies.MOVIE_RATE)));
                    resultsEntities.add(entity);
                }
            }
        } finally {
            cursor.close();
        }

        return resultsEntities;
    }

    public ArrayList<Response.ResultsEntity> allMovies() {
        ArrayList<Response.ResultsEntity> resultsEntities = new ArrayList<>();
        Uri uri = Contract.Movies.CONTENT_MOVIES_URI;
        Cursor cursor = context.getContentResolver().query(uri,Contract.Movies.AllColumns, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    Response.ResultsEntity entity = new Response.ResultsEntity();
                    entity.setId(cursor.getInt(cursor.getColumnIndex(Contract.Movies.MOVIE_iD)));
                    entity.setTitle(cursor.getString(cursor.getColumnIndex(Contract.Movies.MOVIE_TITLE)));
                    entity.setRelease_date(cursor.getString(cursor.getColumnIndex(Contract.Movies.MOVIE_DATE)));
                    entity.setOverview(cursor.getString(cursor.getColumnIndex(Contract.Movies.MOVIE_OVERVIEW)));
                    //entity.setPopularity(cursor.getInt(cursor.getColumnIndex(Contract.Movies.Mov)));
                    entity.setPoster_path(cursor.getString(cursor.getColumnIndex(Contract.Movies.MOVIE_POSTER_PATH)));
                    //entity.setVote_average(cursor.getDouble(cursor.getColumnIndex(Contract.Movies.MOVIE_iD)));
                    resultsEntities.add(entity);
                }
            }
        } finally {
            cursor.close();
        }
        return resultsEntities;
    }

    public int isFavourite(long id){
        int checked = 0;
        Uri uri = Contract.Movies.CONTENT_MOVIES_URI.buildUpon().appendPath(String.valueOf(id)).build();
        Cursor cursor = context.getContentResolver().query(uri,new String[]{Contract.Movies.FAVOURITE}, null,new String[]{String.valueOf(id)},null);

       try {
           if (cursor.moveToFirst()){
               checked = cursor.getInt(cursor.getColumnIndex(Contract.Movies.FAVOURITE));
           }
       }finally {
           cursor.close();
       }
        return checked;
    }
}
