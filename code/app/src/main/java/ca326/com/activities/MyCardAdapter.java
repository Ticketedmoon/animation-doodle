package ca326.com.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.HashMap;
import java.util.List;

public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.ViewHolder> {

    private Context context;

    //List for videos
    List<Video> videos;

    private Bitmap bitmap;

    private boolean isPlaying = true;

    public MyCardAdapter(List<Video> videos, Context context){
        super();
        this.videos = videos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        final ViewHolder holder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        //get the video for the right position
        Video video =  videos.get(position);

        //MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //give YourVideoUrl below
        //retriever.setDataSource(video.getVideoUrl(), new HashMap<String, String>());
// this gets frame at 2nd second
        //Bitmap image = retriever.getFrameAtTime(2000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//use this bitmap image


        String url = video.getVideoUrl();
        //bitmap = retriveVideoFrameFromVideo(url);
        //BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        //Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail( url, MediaStore.Video.Thumbnails.MINI_KIND );

        Uri videoUri = Uri.parse(url);
        System.out.println(videoUri);
        //holder.videoView.setBackground(bitmapDrawable);
        holder.videoView.setVideoURI(videoUri);
        holder.videoView.setMinimumWidth(1000);
        holder.textViewName.setText(video.getName());
        holder.textViewDescription.setText(video.getDescription());
        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("second click");
                if (isPlaying){
                    //holder.videoView.setBackground(null);
                    holder.videoView.start();
                    isPlaying = false;
                }
                else{
                    holder.videoView.pause();
                    isPlaying = true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views

        //will be changing NetworkImageView to videoView later on
        public VideoView videoView;
        public ImageView image;
        public TextView textViewName;
        public TextView textViewDescription;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            videoView = (VideoView) itemView.findViewById(R.id.videoViews);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
        }
    }
    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
             {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        return bitmap;
    }
}