package movieApp.com.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;


public class MoviesProvider extends ContentProvider {

    /*selection from most popular*/
    private static final int mAllMoviesMostPop = 1;
    private static final int mMostPopMoviesByID = 2;
    private static final int mMostPopMoviesByFav = 3;

    /*selection from most rated*/
    private static final int mAllMoviesMostRated = 4;
    private static final int mMostRatedMoviesByID = 5;
    private static final int mMostRatedMoviesByFav = 6;

    /*favourites*/
    private static final int mAllFavourites = 7;
    private static final int mFavouriteById = 8;


    private static final String TAG = "ContentProvider";


    SQLiteDatabase dbHelper;
    MoviesOpenHelper moviesOpenHelper;
    UriMatcher uriMatcher = buildUriMatcher();

    /*most popular*/
    public static String SelectByIDFromMostPop = Contract.MoviesMostPop.TableName + "." + Contract.MoviesMostPop.MOVIE_iD + "=?";
    String SelectByTitleFromMostPop = Contract.MoviesMostPop.TableName + "." + Contract.MoviesMostPop.MOVIE_TITLE + "= ?";
    String SelectFavFromMostPop = Contract.MoviesMostPop.TableName + "." + Contract.MoviesMostPop.FAVOURITE + "=1";

    /*most rated*/
    public static String SelectByIDFromMostRat = Contract.MoviesHighestRated.TableName + "." + Contract.MoviesHighestRated.MOVIE_iD + "=?";
    String SelectByTitleFromMostRat = Contract.MoviesHighestRated.TableName + "." + Contract.MoviesHighestRated.MOVIE_TITLE + "= ?";
    String SelectFavFromMostRat = Contract.MoviesHighestRated.TableName + "." + Contract.MoviesHighestRated.FAVOURITE + " =1";


    @Override
    public boolean onCreate() {
        moviesOpenHelper = new MoviesOpenHelper(getContext());
        dbHelper = moviesOpenHelper.getWritableDatabase();
        return dbHelper != null;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        int matcher = uriMatcher.match(uri);
        switch (matcher) {
            case mAllMoviesMostPop:
                dbHelper = moviesOpenHelper.getReadableDatabase();
                cursor = dbHelper.query(Contract.MoviesMostPop.TableName, projection, selection, null, null, null, sortOrder);
                break;
            case mMostPopMoviesByID:
                dbHelper = moviesOpenHelper.getReadableDatabase();
                String ID = uri.getPathSegments().get(1);
                cursor = dbHelper.query(Contract.MoviesMostPop.TableName, projection, SelectByIDFromMostPop, new String[]{ID}, null, null, sortOrder);
                break;
            case mMostPopMoviesByFav:
                dbHelper = moviesOpenHelper.getReadableDatabase();
                cursor = dbHelper.query(Contract.MoviesMostPop.TableName, projection, SelectFavFromMostPop, null, null, null, sortOrder);
                break;

            case mAllMoviesMostRated:
                dbHelper = moviesOpenHelper.getReadableDatabase();
                cursor = dbHelper.query(Contract.MoviesHighestRated.TableName, projection, selection, null, null, null, sortOrder);
                break;
            case mMostRatedMoviesByID:
                dbHelper = moviesOpenHelper.getReadableDatabase();
                String ID1 = uri.getPathSegments().get(1);
                cursor = dbHelper.query(Contract.MoviesHighestRated.TableName, projection, SelectByIDFromMostRat, new String[]{ID1}, null, null, sortOrder);
                break;
            case mMostRatedMoviesByFav:
                dbHelper = moviesOpenHelper.getReadableDatabase();
                cursor = dbHelper.query(Contract.MoviesHighestRated.TableName, projection, SelectFavFromMostRat, null, null, null, sortOrder);
                break;
            case mAllFavourites:
                dbHelper = moviesOpenHelper.getReadableDatabase();
                cursor = dbHelper.query(Contract.Favorite.TableName, projection, null, null, null, null, sortOrder);
                break;
            case mFavouriteById:
                dbHelper = moviesOpenHelper.getReadableDatabase();
                String id = uri.getLastPathSegment();
                cursor = dbHelper.query(Contract.Favorite.TableName, new String[]{Contract.Favorite.MOVIE_iD},
                        Contract.Favorite.MOVIE_iD + "=?", new String[]{id}, null, null, sortOrder);
                break;
        }
        if (cursor != null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Override
    public String getType(@NonNull Uri uri) {
        int type = uriMatcher.match(uri);
        switch (type) {
            case mAllMoviesMostPop:
                return Contract.MoviesMostPop.MOVIE_DIR;
            case mMostPopMoviesByID:
                return Contract.MoviesMostPop.MOVIE_ITEM;
            case mMostPopMoviesByFav:
                return Contract.MoviesMostPop.MOVIE_DIR;

            case mAllMoviesMostRated:
                return Contract.MoviesHighestRated.MOVIE_DIR;
            case mMostRatedMoviesByID:
                return Contract.MoviesHighestRated.MOVIE_ITEM;
            case mMostRatedMoviesByFav:
                return Contract.MoviesHighestRated.MOVIE_DIR;
            default:
                return "wrong uri";
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        dbHelper = moviesOpenHelper.getWritableDatabase();
        Uri rUri = null;
        long id = -1;
        switch (uriMatcher.match(uri)) {
            case mAllMoviesMostRated:
                id = dbHelper.insert(Contract.MoviesHighestRated.TableName, Contract.MoviesHighestRated.ID, values);
                if (id > -1)
                    rUri = Contract.MoviesHighestRated.buildMovieID(id);
                break;
            case mAllMoviesMostPop:
                id = dbHelper.insert(Contract.MoviesMostPop.TableName, Contract.MoviesMostPop.ID, values);
                if (id > -1)
                    rUri = Contract.MoviesMostPop.buildMovieID(id);
            case mAllFavourites:
                id = dbHelper.insert(Contract.Favorite.TableName, Contract.Favorite.ID, values);
                if (id > -1)
                    rUri = Contract.Favorite.buildMovieID(id);
        }
        if (id > -1) {
            getContext().getContentResolver().notifyChange(uri, null);
            Log.i(TAG, "inserted");
        } else
            Log.i(TAG, "Not inserted");

        return rUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        dbHelper = moviesOpenHelper.getWritableDatabase();
        int id = 0;
        switch (uriMatcher.match(uri)) {
            case mAllMoviesMostRated:
                id = dbHelper.delete(Contract.MoviesHighestRated.TableName, Contract.MoviesHighestRated.MOVIE_iD + "=?", selectionArgs);
                break;
            case mAllMoviesMostPop:
                id = dbHelper.delete(Contract.MoviesMostPop.TableName, Contract.MoviesMostPop.MOVIE_iD + "=?", selectionArgs);
                break;
            case mFavouriteById:
                String deletedID = uri.getLastPathSegment();
                id = dbHelper.delete(Contract.Favorite.TableName, Contract.Favorite.MOVIE_iD + "=?", new String[]{deletedID});
                break;

        }
        if (id > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updated = -1;
        switch (uriMatcher.match(uri)) {
            case mAllMoviesMostRated:
                updated = dbHelper.update(Contract.MoviesHighestRated.TableName, values, SelectByIDFromMostRat, selectionArgs);
                break;
            case mAllMoviesMostPop:
                updated = dbHelper.update(Contract.MoviesMostPop.TableName, values, SelectByIDFromMostPop, selectionArgs);

        }
        if (updated > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return updated;

    }

    private UriMatcher buildUriMatcher() {

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

       /* most rated uris.*/
        uriMatcher.addURI(Contract.CONTENT_BASE, Contract.HighestRated_MOVIES_PATH, mAllMoviesMostRated);
        uriMatcher.addURI(Contract.CONTENT_BASE, Contract.MoviesHighestRated.TableName + "/#/", mMostRatedMoviesByID);
        uriMatcher.addURI(Contract.CONTENT_BASE, Contract.MoviesHighestRated.TableName + "/#/#", mMostRatedMoviesByFav);

       /* most popular uris.*/
        uriMatcher.addURI(Contract.CONTENT_BASE, Contract.MostPop_MOVIES_PATH, mAllMoviesMostPop);
        uriMatcher.addURI(Contract.CONTENT_BASE, Contract.MoviesMostPop.TableName + "/#/", mMostPopMoviesByID);
        uriMatcher.addURI(Contract.CONTENT_BASE, Contract.MoviesMostPop.TableName + "/#/#", mMostPopMoviesByFav);

        /*favourites*/
        uriMatcher.addURI(Contract.CONTENT_BASE, Contract.FavouritePAth, mAllFavourites);
        uriMatcher.addURI(Contract.CONTENT_BASE, Contract.FavouritePAth + "/#", mFavouriteById);


        return uriMatcher;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int id = -1;
        try {
            dbHelper.beginTransaction();
            for (ContentValues value : values) {
                if (value != null) {
                    switch (uriMatcher.match(uri)) {
                        case mAllMoviesMostRated:
                            id = (int) dbHelper.insert(Contract.MoviesHighestRated.TableName, Contract.MoviesHighestRated.ID, value);
                            break;
                        case mAllMoviesMostPop:
                            id = (int) dbHelper.insert(Contract.MoviesMostPop.TableName, Contract.MoviesMostPop.ID, value);
                            break;

                    }
//                    id = (int) dbHelper.insert(Contract.Movies.TableName, Contract.Movies.ID, value);
                    if (id > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                        Log.i(TAG, "inserted");
                    }
                }
            }
            dbHelper.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        } finally {
            dbHelper.endTransaction();
        }
        return id;
    }
}
