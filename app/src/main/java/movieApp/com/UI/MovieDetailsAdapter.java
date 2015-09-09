package movieApp.com.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import activity.com.movietesttwo.movieApp.com.R;
import movieApp.com.classes.Review;
import movieApp.com.classes.Video;


public class MovieDetailsAdapter extends BaseAdapter {

    private static final int TRAILER_TYPE = 0;
    private static final int REVIEW_TYPE  = 1;
    private static final int SECTION_TYPE = 2;
    private static final int TYPE_COUNT   = 3;
    List combinedObjects;
    Context context;

    public MovieDetailsAdapter(Context context) {
        this.context = context;
        combinedObjects = new ArrayList();
    }

    @Override
    public int getCount() {
        return combinedObjects.size();
    }


    @Override
    public Object getItem(int position) {
        return combinedObjects.get(position);
    }

    public void addItem(Object item) {
        this.combinedObjects.add(item);
    }

    public void addSection(Object item){
        this.combinedObjects.add(item);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Review.ResultsEntity)
            return REVIEW_TYPE;
        if (getItem(position) instanceof Video.ResultsEntity)
            return TRAILER_TYPE;
        return 2;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder ;
        int type = getItemViewType(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            switch (type) {
                case TRAILER_TYPE:
                    convertView = inflater.inflate(R.layout.video_item, parent, false);
                    viewHolder.textView = (TextView) convertView.findViewById(R.id.type);
                    break;
                case REVIEW_TYPE:
                    convertView = inflater.inflate(R.layout.review_item, parent, false);
                    viewHolder.textView = (TextView) convertView.findViewById(R.id.content);
                    viewHolder.author = (TextView) convertView.findViewById(R.id.author);
                    break;
                case SECTION_TYPE:
                    convertView = inflater.inflate(R.layout.section_item,parent,false);
                    viewHolder.textView = (TextView) convertView.findViewById(R.id.section);
                default:
                    break;
            }
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        if (type == TRAILER_TYPE) {
            Video.ResultsEntity video = (Video.ResultsEntity) combinedObjects.get(position);
            viewHolder.textView.setText(video.getType());
        }
        if (type == REVIEW_TYPE) {
            Review.ResultsEntity reviews = (Review.ResultsEntity) combinedObjects.get(position);
            viewHolder.author.setText(reviews.getAuthor());
            viewHolder.textView.setText(reviews.getContent());
        }
        if (type == SECTION_TYPE) {
            String section = (String) combinedObjects.get(position);
            viewHolder.textView.setText(section);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView textView;
        TextView author;
    }
}
