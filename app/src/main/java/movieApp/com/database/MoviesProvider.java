package movieApp.com.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by hanaa mohamed on 02-Sep-15.
 */
public class MoviesProvider extends ContentProvider {

    private static final int AllMovies = 1;
    private static final int MoviesByID = 2;
    private static final int FavouriteMovies = 3;
    private static final String TAG = "ContentProvider";


    SQLiteDatabase dbHelper;
    MoviesOpenHelper moviesOpenHelper;
    UriMatcher uriMatcher = buildUriMatcher();


    public static String SelectByID = Contract.Movies.TableName + "." + Contract.Movies.MOVIE_iD + "=?";
    String SelectByTitle = Contract.Movies.TableName + "." + Contract.Movies.MOVIE_TITLE + "= ?";
    String SelectFav = Contract.Movies.TableName + "." + Contract.Movies.FAVOURITE + "=1";


    @Override
    public boolean onCreate() {
        moviesOpenHelper = new MoviesOpenHelper(getContext());
        dbHelper = moviesOpenHelper.getWritableDatabase();
        if (dbHelper != null)
            return true;
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        int matcher = uriMatcher.match(uri);
        switch (matcher) {
            case AllMovies:
                dbHelper = moviesOpenHelper.getReadableDatabase();
                cursor = dbHelper.query(Contract.Movies.TableName, projection, selection, null, null, null, sortOrder);
                break;
            case MoviesByID:
                dbHelper = moviesOpenHelper.getReadableDatabase();
                //SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
                //queryBuilder.setTables(Contract.Movies.TableName);
                String ID = uri.getPathSegments().get(1);
                //queryBuilder.appendWhere(Contract.Movies.MOVIE_iD+"="+ID);
                cursor = dbHelper.query(Contract.Movies.TableName, projection, SelectByID, new String[]{ID}, null, null, sortOrder);
                break;
            case FavouriteMovies:
                dbHelper = moviesOpenHelper.getReadableDatabase();
                cursor = dbHelper.query(Contract.Movies.TableName, projection, SelectFav, null, null, null, sortOrder);
                break;
        }
        if (cursor != null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Override
    public String getType(Uri uri) {
        int type = uriMatcher.match(uri);
        switch (type) {
            case AllMovies:
                return Contract.Movies.MOVIE_DIR;
            case MoviesByID:
                return Contract.Movies.MOVIE_ITEM;
            case FavouriteMovies:
                return Contract.Movies.MOVIE_DIR;
            default:
                return "wrong uri";
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        dbHelper = moviesOpenHelper.getWritableDatabase();
        Uri rUri = null;
        try {
            long id = dbHelper.insert(Contract.Movies.TableName, Contract.Movies.ID, values);
            if (id > 0) {
                rUri = Contract.Movies.buildMovieID(id);
                getContext().getContentResolver().notifyChange(uri, null);
                Log.i(TAG, "inserted");
            } else
                Log.i(TAG, "Not inserted");


        } catch (Exception e) {
            Log.e(TAG, "error ", e);
        }
        return rUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        dbHelper = moviesOpenHelper.getWritableDatabase();
        int id = 0;
        try {
            id = dbHelper.delete(Contract.Movies.TableName, selection, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        }
        if (id > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updated = -1;
        updated = dbHelper.update(Contract.Movies.TableName, values, SelectByID, selectionArgs);
        if (updated > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return updated;

    }

    private UriMatcher buildUriMatcher() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.CONTENT_BASE, Contract.MOVIES_PATH, AllMovies);
        uriMatcher.addURI(Contract.CONTENT_BASE, Contract.MOVIES_PATH + "/#/", MoviesByID);
        uriMatcher.addURI(Contract.CONTENT_BASE, Contract.MOVIES_PATH + "/#/#", FavouriteMovies);
        return uriMatcher;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int id = -1;
        try {
            dbHelper.beginTransaction();
            for (ContentValues value : values) {
                if (value != null) {
                    id = (int) dbHelper.insert(Contract.Movies.TableName, Contract.Movies.ID, value);
                    if (id > 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                        Log.i(TAG, "inserted");
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "error", e);
        } finally {
            dbHelper.endTransaction();
        }
        return id;
    }
}
