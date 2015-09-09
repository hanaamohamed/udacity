package movieApp.com.UI;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import movieApp.com.classes.Review;

/**
 * Created by hanaa mohamed on 05-Sep-15.
 */
public class reviewAdapter extends ArrayAdapter{
    Context context;
    List<Review.ResultsEntity> reviews;
    public reviewAdapter(Context context, int resource, List<Review.ResultsEntity> reviews) {
        super(context, resource, reviews);
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
