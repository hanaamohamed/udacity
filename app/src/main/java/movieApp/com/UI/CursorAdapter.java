package movieApp.com.UI;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.Objects;

import activity.com.movietesttwo.movieApp.com.R;
import movieApp.com.database.Contract;


public class cursorAdapter extends android.widget.CursorAdapter {

    String mTableName;

    public cursorAdapter(Context context, Cursor c, String tableName) {
        super(context, c);
        mTableName = tableName;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.movie_item, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView iv = (ImageView) view.findViewById(R.id.movie);
        String poster;
        if (Objects.equals(mTableName, "most popular")) {
            poster = cursor.getString(cursor.getColumnIndex(Contract.MoviesMostPop.MOVIE_POSTER_PATH));
        } else
            poster = cursor.getString(cursor.getColumnIndex(Contract.MoviesHighestRated.MOVIE_POSTER_PATH));

        int dimens = (int) context.getResources().getDimension(R.dimen.image_movie_width);
        Picasso.with(context).load("https://image.tmdb.org/t/p/w185" + poster).
                resize(dimens, dimens + 100).into(iv);
    }
}
