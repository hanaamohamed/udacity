package movieApp.com.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MoviesOpenHelper extends SQLiteOpenHelper {

    public static String DatabaseName = "database.db";
    public static int DatabaseVersion = 4;

    public MoviesOpenHelper(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String TableCreate = "CREATE TABLE " + Contract.Movies.TableName + "(" +
                Contract.Movies.ID + " INTEGER PRIMARY KEY," +
                Contract.Movies.MOVIE_iD + " INTEGER  ," +
                Contract.Movies.MOVIE_TITLE + " TEXT NOT NULL," +
                Contract.Movies.MOVIE_DATE + " INTEGER  ," +
                Contract.Movies.MOVIE_OVERVIEW + " TEXT ," +
                Contract.Movies.MOVIE_RATE + " FLOAT ," +
                Contract.Movies.MOVIE_POSTER_PATH + " TEXT ," +
                Contract.Movies.FAVOURITE + " INT );";
        db.execSQL(TableCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + Contract.Movies.TableName);
        onCreate(db);
    }
}
