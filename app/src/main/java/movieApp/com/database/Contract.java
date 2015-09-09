package movieApp.com.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class Contract {
    public static String CONTENT_BASE = "activity.com.movietesttwo";
    public static Uri CONTENT_URI = Uri.parse("content://" + CONTENT_BASE);

    public static String MOVIES_PATH = "movies";

    public static class Movies implements BaseColumns {
        public static Uri CONTENT_MOVIES_URI = CONTENT_URI.buildUpon().appendPath(MOVIES_PATH).build();
        public static String MOVIE_ITEM = ContentResolver.ANY_CURSOR_ITEM_TYPE + CONTENT_BASE + "/" + MOVIES_PATH + "/";
        public static String MOVIE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + CONTENT_BASE + "/" + MOVIES_PATH + "/";

        public static String TableName = "movies";
        public static String ID = "id";
        public static String MOVIE_iD = "movie_id";
        public static String MOVIE_TITLE = "title";
        public static String MOVIE_DATE = "date";
        public static String MOVIE_RATE = "rate";
        public static String MOVIE_POSTER_PATH = "poster_path";
        public static String MOVIE_OVERVIEW = "overview";
        public static String FAVOURITE = "favourite";
        public static String[] AllColumns = new String[]{
                Contract.Movies.MOVIE_iD,
                Contract.Movies.MOVIE_TITLE,
                Contract.Movies.MOVIE_OVERVIEW,
                Contract.Movies.MOVIE_POSTER_PATH,
                Contract.Movies.MOVIE_DATE,
                Contract.Movies.MOVIE_RATE
        };

        public static Uri buildMovieID(long id) {
            return ContentUris.withAppendedId(CONTENT_MOVIES_URI, id);
        }

    }
}
