package ca326.com.activities;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.toolbox.ImageLoader;
import java.util.List;

public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.VideoViewHolder> {

    private Context context;

    //List for videos
    List<Video> videos;

    public MyCardAdapter(List<Video> videos, Context context){
        super();
        this.videos = videos;
        this.context = context;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list, parent, false);
        VideoViewHolder viewHolder = new VideoViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, int position) {

        //get the video for the right position
        Video video =  videos.get(position);

        String url = video.getVideoUrl();
        Uri videoUri = Uri.parse(url);
        System.out.println(videoUri);
        holder.videoView.setSource(videoUri);
        holder.textViewName.setText(video.getName());
        holder.textViewDescription.setText(video.getDescription());
        holder.videoView.setLooping(true);

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder{
        //Views

        //will be changing NetworkImageView to videoView later on
        public CustomVideoView videoView;
        public TextView textViewName;
        public TextView textViewDescription;

        //Initializing Views
        public VideoViewHolder(View itemView) {
            super(itemView);
            videoView = (CustomVideoView) itemView.findViewById(R.id.videoViews);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
        }
    }
}
