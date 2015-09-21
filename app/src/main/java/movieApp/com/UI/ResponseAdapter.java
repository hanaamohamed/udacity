package movieApp.com.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import activity.com.movietesttwo.movieApp.com.R;

public class ResponseAdapter extends ArrayAdapter<String> {
    List<String> resultsEntity;
    Context context;

    public ResponseAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        resultsEntity = objects;
        this.context = context;
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
        int  dimens = (int) context.getResources().getDimension(R.dimen.image_movie_width);
        if (resultsEntity.get(position) != null)
            Picasso.with(context).load("https://image.tmdb.org/t/p/w185" + resultsEntity.get(position)).
                    resize(dimens, dimens+100).into(viewHolder.imageView);
        return convertView;
    }


    private class ViewHolderMovies {
        ImageView imageView;

        public ViewHolderMovies(View view) {
            imageView = (ImageView) view.findViewById(R.id.movie);
        }
    }
}
