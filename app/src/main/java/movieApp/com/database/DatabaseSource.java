package movieApp.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import activity.com.movietesttwo.movieApp.com.R;
import movieApp.com.classes.Response;

public class DatabaseSource {
    Context context;
    String mTableName;
    private static String MOSTPOP;
    private static String HIGHESTRATED;

    public DatabaseSource(Context context, String TableName) {
        this.context = context;
        mTableName = TableName;
        MOSTPOP = context.getString(R.string.most_popular);
        HIGHESTRATED = context.getString(R.string.highestRated);
    }

    public int Delete(Response.ResultsEntity resultsEntity) {
        if (Objects.equals(mTableName, MOSTPOP))
            return context.getContentResolver().delete(Contract.MoviesMostPop.CONTENT_MOVIES_URI,
                    null,
                    new String[]{resultsEntity.getId() + ""});
        if (Objects.equals(mTableName, HIGHESTRATED))
            return context.getContentResolver().delete(Contract.MoviesHighestRated.CONTENT_MOVIES_URI,
                    null,
                    new String[]{resultsEntity.getId() + ""});
        else
            return 0;


    }


    public boolean insertAll(List<Response.ResultsEntity> resultsEntities, String TableName) {
        boolean check = false;
        ContentValues[] contentValues = new ContentValues[resultsEntities.size()];
        int i = 0;
        if (Objects.equals(mTableName, MOSTPOP)) {
            for (Response.ResultsEntity resultsEntity : resultsEntities) {
                if (isExist(resultsEntity.getId()) == -1) {
                    ContentValues values = new ContentValues();
                    values.put(Contract.MoviesMostPop.MOVIE_iD, resultsEntity.getId());
                    values.put(Contract.MoviesMostPop.MOVIE_TITLE, resultsEntity.getTitle());
                    values.put(Contract.MoviesMostPop.MOVIE_DATE, resultsEntity.getRelease_date());
                    values.put(Contract.MoviesMostPop.MOVIE_OVERVIEW, resultsEntity.getOverview());
                    values.put(Contract.MoviesMostPop.FAVOURITE, 0);
                    values.put(Contract.MoviesMostPop.MOVIE_RATE, resultsEntity.getVote_average());
                    values.put(Contract.MoviesMostPop.MOVIE_POSTER_PATH, resultsEntity.getPoster_path());
                    contentValues[i++] = values;
                }
            }
            long id = context.getContentResolver().bulkInsert(Contract.MoviesMostPop.CONTENT_MOVIES_URI, contentValues);
            if (id > 0)
                check = true;
        }
        if (Objects.equals(mTableName, HIGHESTRATED)) {
            for (Response.ResultsEntity resultsEntity : resultsEntities) {
                if (isExist(resultsEntity.getId()) == -1) {
                    ContentValues values = new ContentValues();
                    values.put(Contract.MoviesHighestRated.MOVIE_iD, resultsEntity.getId());
                    values.put(Contract.MoviesHighestRated.MOVIE_TITLE, resultsEntity.getTitle());
                    values.put(Contract.MoviesHighestRated.MOVIE_DATE, resultsEntity.getRelease_date());
                    values.put(Contract.MoviesHighestRated.MOVIE_OVERVIEW, resultsEntity.getOverview());
                    values.put(Contract.MoviesHighestRated.FAVOURITE, 0);
                    values.put(Contract.MoviesHighestRated.MOVIE_RATE, resultsEntity.getVote_average());
                    values.put(Contract.MoviesHighestRated.MOVIE_POSTER_PATH, resultsEntity.getPoster_path());
                    contentValues[i++] = values;
                }
            }
            long id = context.getContentResolver().bulkInsert(Contract.MoviesHighestRated.CONTENT_MOVIES_URI, contentValues);
            if (id > 0)
                check = true;
        }
        return check;
    }


    public long isExist(int _id) {
        long id;
        Uri uri = null;
        String[] AllColumns = new String[0];
        String column = null;
        if (Objects.equals(mTableName, HIGHESTRATED)) {
            uri = Contract.MoviesHighestRated.CONTENT_MOVIES_URI.buildUpon().appendPath(String.valueOf(_id)).build();
            AllColumns = Contract.MoviesHighestRated.AllColumns;
            column = Contract.MoviesHighestRated.MOVIE_iD;
        }
        if (Objects.equals(mTableName, MOSTPOP)) {
            uri = Contract.MoviesMostPop.CONTENT_MOVIES_URI.buildUpon().appendPath(String.valueOf(_id)).build();
            AllColumns = Contract.MoviesMostPop.AllColumns;
            column = Contract.MoviesMostPop.MOVIE_iD;
        }


        Cursor cursor = null;
        try {
            assert uri != null;
            cursor = context.getContentResolver().query(uri,
                    AllColumns,
                    null,
                    new String[]{"" + _id},
                    null);
            assert cursor != null;
            if (cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndex(column));
            } else
                id = -1;
        } finally {
            assert cursor != null;
            cursor.close();

        }
        return id;
    }

    public int favourite(long id, int fav) {
        Uri uri = null;
        String column = null;
        if (Objects.equals(mTableName, MOSTPOP)) {
            uri = Contract.MoviesMostPop.CONTENT_MOVIES_URI;
            column = Contract.MoviesMostPop.FAVOURITE;
        }
        if (Objects.equals(mTableName, HIGHESTRATED)) {
            uri = Contract.MoviesHighestRated.CONTENT_MOVIES_URI;
            column = Contract.MoviesHighestRated.FAVOURITE;
        }
        ContentValues values = new ContentValues();
        values.put(column, fav);
        assert uri != null;
        return context.getContentResolver().update(uri, values, null, new String[]{String.valueOf(id)});
    }

    public ArrayList<Response.ResultsEntity> retrieveFav() {
        ArrayList<Response.ResultsEntity> resultsEntities = new ArrayList<>();
        Uri uri = null;
        String[] columns = new String[6];

        if (Objects.equals(mTableName, MOSTPOP)) {
            uri = Contract.MoviesMostPop.CONTENT_MOVIES_URI.buildUpon().appendPath("0").appendPath("1").build();
            columns[0] = Contract.MoviesMostPop.MOVIE_iD;
            columns[1] = Contract.MoviesMostPop.MOVIE_TITLE;
            columns[2] = Contract.MoviesMostPop.MOVIE_DATE;
            columns[3] = Contract.MoviesMostPop.MOVIE_OVERVIEW;
            columns[4] = Contract.MoviesMostPop.MOVIE_POSTER_PATH;
            columns[5] = Contract.MoviesMostPop.MOVIE_RATE;
        }
        if (Objects.equals(mTableName, HIGHESTRATED)) {
            uri = Contract.MoviesHighestRated.CONTENT_MOVIES_URI.buildUpon().appendPath("0").appendPath("1").build();
            columns[0] = Contract.MoviesHighestRated.MOVIE_iD;
            columns[1] = Contract.MoviesHighestRated.MOVIE_TITLE;
            columns[2] = Contract.MoviesHighestRated.MOVIE_DATE;
            columns[3] = Contract.MoviesHighestRated.MOVIE_OVERVIEW;
            columns[4] = Contract.MoviesHighestRated.MOVIE_POSTER_PATH;
            columns[5] = Contract.MoviesHighestRated.MOVIE_RATE;
        }
        assert uri != null;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        try {
            assert cursor != null;
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    Response.ResultsEntity entity = new Response.ResultsEntity();
                    entity.setId(cursor.getInt(cursor.getColumnIndex(columns[0])));
                    entity.setTitle(cursor.getString(cursor.getColumnIndex(columns[1])));
                    entity.setRelease_date(cursor.getString(cursor.getColumnIndex(columns[2])));
                    entity.setOverview(cursor.getString(cursor.getColumnIndex(columns[3])));
                    entity.setPoster_path(cursor.getString(cursor.getColumnIndex(columns[4])));
                    entity.setVote_average(cursor.getDouble(cursor.getColumnIndex(columns[5])));
                    resultsEntities.add(entity);
                }
            }
        } finally {
            assert cursor != null;
            cursor.close();
        }

        return resultsEntities;
    }

    public ArrayList<Response.ResultsEntity> allMovies() {
        ArrayList<Response.ResultsEntity> resultsEntities = new ArrayList<>();
        Uri uri = null;
        String[] columns = new String[6];
        String[] AllColumns = new String[0];

        if (Objects.equals(mTableName, MOSTPOP)) {
            uri = Contract.MoviesMostPop.CONTENT_MOVIES_URI;
            columns[0] = Contract.MoviesMostPop.MOVIE_iD;
            columns[1] = Contract.MoviesMostPop.MOVIE_TITLE;
            columns[2] = Contract.MoviesMostPop.MOVIE_DATE;
            columns[3] = Contract.MoviesMostPop.MOVIE_OVERVIEW;
            columns[4] = Contract.MoviesMostPop.MOVIE_POSTER_PATH;
            columns[5] = Contract.MoviesMostPop.MOVIE_RATE;
            AllColumns = Contract.MoviesMostPop.AllColumns;
        }
        if (Objects.equals(mTableName, HIGHESTRATED)) {
            uri = Contract.MoviesHighestRated.CONTENT_MOVIES_URI;
            columns[0] = Contract.MoviesHighestRated.MOVIE_iD;
            columns[1] = Contract.MoviesHighestRated.MOVIE_TITLE;
            columns[2] = Contract.MoviesHighestRated.MOVIE_DATE;
            columns[3] = Contract.MoviesHighestRated.MOVIE_OVERVIEW;
            columns[4] = Contract.MoviesHighestRated.MOVIE_POSTER_PATH;
            columns[5] = Contract.MoviesHighestRated.MOVIE_RATE;
            AllColumns = Contract.MoviesHighestRated.AllColumns;

        }
        assert uri != null;
        Cursor cursor = context.getContentResolver().query(uri, AllColumns, null, null, null);
        try {
            assert cursor != null;
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    Response.ResultsEntity entity = new Response.ResultsEntity();
                    entity.setId(cursor.getInt(cursor.getColumnIndex(columns[0])));
                    entity.setOriginal_title(cursor.getString(cursor.getColumnIndex(columns[1])));
                    entity.setRelease_date(cursor.getString(cursor.getColumnIndex(columns[2])));
                    entity.setOverview(cursor.getString(cursor.getColumnIndex(columns[3])));
                    entity.setPoster_path(cursor.getString(cursor.getColumnIndex(columns[4])));
                    resultsEntities.add(entity);
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return resultsEntities;
    }

    public int isFavourite(long id) {
        int checked = 0;
        Uri uri = null;
        String column = null;
        if (Objects.equals(mTableName, MOSTPOP)) {
            uri = Contract.MoviesMostPop.CONTENT_MOVIES_URI.buildUpon().appendPath(String.valueOf(id)).build();
            column = Contract.MoviesMostPop.FAVOURITE;
        }
        if (Objects.equals(mTableName, HIGHESTRATED)) {
            uri = Contract.MoviesHighestRated.CONTENT_MOVIES_URI.buildUpon().appendPath(String.valueOf(id)).build();
            column = Contract.MoviesHighestRated.FAVOURITE;

        }
        assert uri != null;
        Cursor cursor = context.getContentResolver().query(uri, new String[]{column}, null, new String[]{String.valueOf(id)}, null);

        try {
            assert cursor != null;
            if (cursor.moveToFirst()) {
                checked = cursor.getInt(cursor.getColumnIndex(column));
            }
        } finally {
            assert cursor != null;
            cursor.close();
        }
        return checked;
    }
}
