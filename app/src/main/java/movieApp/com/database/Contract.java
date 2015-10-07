package movieApp.com.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class Contract {
    public static String CONTENT_BASE = "activity.com.movietesttwo";
    public static Uri CONTENT_URI = Uri.parse("content://" + CONTENT_BASE);

    public static String HighestRated_MOVIES_PATH = "movies_most_rated";
    public static String MostPop_MOVIES_PATH = "movies_most_pop";
    public static String FavouritePAth = "favourites";

    public static class MoviesHighestRated implements BaseColumns {
        public static Uri CONTENT_MOVIES_URI = CONTENT_URI.buildUpon().appendPath(HighestRated_MOVIES_PATH).build();
        public static String MOVIE_ITEM = ContentResolver.ANY_CURSOR_ITEM_TYPE + CONTENT_BASE + "/" + HighestRated_MOVIES_PATH + "/";
        public static String MOVIE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + CONTENT_BASE + "/" + HighestRated_MOVIES_PATH + "/";

        public static String TableName = "movies_most_rated";
        public static String ID = "_id";
        public static String MOVIE_iD = "movie_id";
        public static String MOVIE_TITLE = "title";
        public static String MOVIE_DATE = "date";
        public static String MOVIE_RATE = "rate";
        public static String MOVIE_POSTER_PATH = "poster_path";
        public static String MOVIE_OVERVIEW = "overview";
        public static String FAVOURITE = "favourite";
        public static String[] AllColumns = new String[]{
                ID,
                MOVIE_iD,
                MOVIE_TITLE,
                MOVIE_OVERVIEW,
                MOVIE_POSTER_PATH,
                MOVIE_DATE,
                MOVIE_RATE
        };

        public static Uri buildMovieID(long id) {
            return ContentUris.withAppendedId(CONTENT_MOVIES_URI, id);
        }

    }
    public static class MoviesMostPop implements BaseColumns {
        public static Uri CONTENT_MOVIES_URI = CONTENT_URI.buildUpon().appendPath(MostPop_MOVIES_PATH).build();
        public static String MOVIE_ITEM = ContentResolver.ANY_CURSOR_ITEM_TYPE + CONTENT_BASE + "/" + MostPop_MOVIES_PATH + "/";
        public static String MOVIE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + CONTENT_BASE + "/" + MostPop_MOVIES_PATH + "/";

        public static String TableName = "movies_most_pop";
        public static String ID = "_id";
        public static String MOVIE_iD = "movie_id";
        public static String MOVIE_TITLE = "title";
        public static String MOVIE_DATE = "date";
        public static String MOVIE_RATE = "rate";
        public static String MOVIE_POSTER_PATH = "poster_path";
        public static String MOVIE_OVERVIEW = "overview";
        public static String FAVOURITE = "favourite";
        public static String[] AllColumns = new String[]{
                ID,
                MOVIE_iD,
                MOVIE_TITLE,
                MOVIE_OVERVIEW,
                MOVIE_POSTER_PATH,
                MOVIE_DATE,
                MOVIE_RATE
        };

        public static Uri buildMovieID(long id) {
            return ContentUris.withAppendedId(CONTENT_MOVIES_URI, id);
        }
    }

        public static class Favorite implements BaseColumns {
            public static Uri CONTENT_MOVIES_URI = CONTENT_URI.buildUpon().appendPath(FavouritePAth).build();
            public static String MOVIE_ITEM = ContentResolver.ANY_CURSOR_ITEM_TYPE + CONTENT_BASE + "/" + MostPop_MOVIES_PATH + "/";
            public static String MOVIE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + CONTENT_BASE + "/" + MostPop_MOVIES_PATH + "/";

            public static String TableName = "favourites";
            public static String ID = "_id";
            public static String MOVIE_iD = "movie_id";
            public static String MOVIE_TITLE = "title";
            public static String MOVIE_DATE = "date";
            public static String MOVIE_RATE = "rate";
            public static String MOVIE_POSTER_PATH = "poster_path";
            public static String MOVIE_OVERVIEW = "overview";
            public static String FAVOURITE = "favourite";
            public static String[] AllColumns = new String[]{
                    ID,
                    MOVIE_iD,
                    MOVIE_TITLE,
                    MOVIE_OVERVIEW,
                    MOVIE_POSTER_PATH,
                    MOVIE_DATE,
                    MOVIE_RATE
            };

            public static Uri buildMovieID(long id) {
                return ContentUris.withAppendedId(CONTENT_MOVIES_URI, id);
            }

    }
}
