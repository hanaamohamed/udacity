package UI.com.finalstestone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import activity.com.movietesttwo.R;
import classes.com.finalstestone.Video;

public class VideosAdapter extends ArrayAdapter<String> {

    Context context;
    List<String> videos;

    public VideosAdapter(Context context, int resource,List<String> videos) {
        super(context, resource, videos);
        this.context = context;
        this.videos = videos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolderVideos holderVideos;
        if (convertView== null){
            convertView = inflater.inflate(R.layout.video_item,parent,false);
            holderVideos = new ViewHolderVideos(convertView);
            convertView.setTag(holderVideos);
        }else
            holderVideos = (ViewHolderVideos) convertView.getTag();
        holderVideos.type.setText(videos.get(position));
        return convertView;
    }

    private class ViewHolderVideos {
        TextView type;
        ImageView iv;
        public ViewHolderVideos(View convertView) {
            type = (TextView) convertView.findViewById(R.id.type);
            iv   = (ImageView) convertView.findViewById(R.id.playImg);
        }
    }
}
