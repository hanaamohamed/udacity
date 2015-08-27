package UI.com.finalstestone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import activity.com.movietesttwo.R;
import classes.com.finalstestone.Movie;

public class MoviesAdapter extends ArrayAdapter {

    ArrayList<Movie> movies;
    Context context;

    public MoviesAdapter(Context context, int resource, ArrayList<Movie> objects) {
        super(context, resource, objects);
        this.context = context;
        movies = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderMovies viewHolder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.movie_item, parent, false);
            viewHolder = new ViewHolderMovies(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderMovies) convertView.getTag();
        }
        viewHolder.movieName.setText(movies.get(position).getName());
//        viewHolder.imageView.setImageResource(movies.get(position).getImg());
        return convertView;
    }


    private class ViewHolderMovies {
        TextView movieName;
        ImageView imageView;

        public ViewHolderMovies(View view) {
            imageView = (ImageView) view.findViewById(R.id.movie);


        }
    }
}
