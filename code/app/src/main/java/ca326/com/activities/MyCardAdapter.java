package ca326.com.activities;

import android.content.Context;
import android.content.Intent;
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

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.HashMap;
import java.util.List;

public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.ViewHolder> {

    private Top_Rated_Screen top;

    private Context context;

    private ImageLoader loadImage;

    private ItemClickListener mClickListener;


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

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        //get the video for the right position
        Video video =  videos.get(position);

        loadImage = MyVolleyRequest.getInstance(context).getImageLoader();
        //set temporary image i.e R.drawable.play
        loadImage.get(video.getImageUrl(), ImageLoader.getImageListener(holder.image, R.drawable.play, android.R.drawable.ic_dialog_alert));

        //Showing data on the views

        holder.image.setImageUrl(video.getImageUrl(), loadImage);



        String url = video.getVideoUrl();
        Uri videoUri = Uri.parse(url);

        System.out.println(videoUri);
        holder.videoView.setVideoURI(videoUri);
        holder.textViewName.setText(video.getName());
        holder.textViewDescription.setText(video.getDescription());
                /*
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
                */
            }






    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Views

        //will be changing NetworkImageView to videoView later on
        public VideoView videoView;
        public NetworkImageView image;
        public TextView textViewName;
        public TextView textViewDescription;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            image = (NetworkImageView) itemView.findViewById(R.id.imageView);
            videoView = (VideoView) itemView.findViewById(R.id.videoViews);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            System.out.println("thisis clicked");
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}


