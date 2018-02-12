package ca326.com.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.ViewHolder> {

    //load image for now
    private ImageLoader loadImage;
    private Context context;

    //List for videos
    List<Video> videos;

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
    public void onBindViewHolder(ViewHolder holder, int position) {

        //get the video for the right position
        Video video =  videos.get(position);

        //load image from url which is stored in the database
        loadImage = MyVolleyRequest.getInstance(context).getImageLoader();
        //set temporary image i.e R.drawable.play
        loadImage.get(video.getVideoUrl(), ImageLoader.getImageListener(holder.imageView, R.drawable.play, android.R.drawable.ic_dialog_alert));

        //Showing data on the views
        holder.imageView.setImageUrl(video.getVideoUrl(), loadImage);
        holder.textViewName.setText(video.getName());
        holder.textViewDescription.setText(video.getDescription());

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views

        //will be changing NetworkImageView to videoView later on
        public NetworkImageView imageView;
        public TextView textViewName;
        public TextView textViewDescription;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewHero);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
        }
    }
}
