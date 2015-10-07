package movieApp.com.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MoviesOpenHelper extends SQLiteOpenHelper {

    public static String DatabaseName = "db";
    public static int DatabaseVersion = 11;

    public MoviesOpenHelper(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String moviesMostPopTable = "CREATE TABLE " + Contract.MoviesMostPop.TableName + "(" +
                Contract.MoviesMostPop.ID + " INTEGER PRIMARY KEY," +
                Contract.MoviesMostPop.MOVIE_iD + " INTEGER  ," +
                Contract.MoviesMostPop.MOVIE_TITLE + " TEXT NOT NULL," +
                Contract.MoviesMostPop.MOVIE_DATE + " TEXT  ," +
                Contract.MoviesMostPop.MOVIE_OVERVIEW + " TEXT ," +
                Contract.MoviesMostPop.MOVIE_RATE + " FLOAT ," +
                Contract.MoviesMostPop.MOVIE_POSTER_PATH + " TEXT ," +
                Contract.MoviesMostPop.FAVOURITE + " INTEGER );";

        String moviesMostRatedTable = "CREATE TABLE " + Contract.MoviesHighestRated.TableName + "(" +
                Contract.MoviesHighestRated.ID + " INTEGER PRIMARY KEY," +
                Contract.MoviesHighestRated.MOVIE_iD + " INTEGER  ," +
                Contract.MoviesHighestRated.MOVIE_TITLE + " TEXT NOT NULL," +
                Contract.MoviesHighestRated.MOVIE_DATE + " TEXT  ," +
                Contract.MoviesHighestRated.MOVIE_OVERVIEW + " TEXT ," +
                Contract.MoviesHighestRated.MOVIE_RATE + " FLOAT ," +
                Contract.MoviesHighestRated.MOVIE_POSTER_PATH + " TEXT ," +
                Contract.MoviesHighestRated.FAVOURITE + " INTEGER );";
        String mFavoriteTable = "CREATE TABLE " + Contract.Favorite.TableName + "(" +
                Contract.MoviesHighestRated.ID + " INTEGER PRIMARY KEY," +
                Contract.MoviesHighestRated.MOVIE_iD + " INTEGER  ," +
                Contract.MoviesHighestRated.MOVIE_TITLE + " TEXT NOT NULL," +
                Contract.MoviesHighestRated.MOVIE_DATE + " TEXT  ," +
                Contract.MoviesHighestRated.MOVIE_OVERVIEW + " TEXT ," +
                Contract.MoviesHighestRated.MOVIE_RATE + " FLOAT ," +
                Contract.MoviesHighestRated.MOVIE_POSTER_PATH + " TEXT );";

        db.execSQL(moviesMostPopTable);
        db.execSQL(moviesMostRatedTable);
        db.execSQL(mFavoriteTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.MoviesMostPop.TableName);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.MoviesHighestRated.TableName);
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Favorite.TableName);
        onCreate(db);
    }
}
