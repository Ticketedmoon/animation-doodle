package ca326.com.activities;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.ViewHolder> {


    public static String videoUrl;


    private Context context;

    private ImageLoader loadImage;

    private ItemClickListener mClickListener;

    public static String rateValue="0.0";

    public static Map<Integer, RatingBar> ratingbar_map = new HashMap<Integer, RatingBar>();



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
    public void onBindViewHolder(final ViewHolder holder, int position) {

        //get the video for the right position
        Video video =  videos.get(position);

        //Get thumbnail image of video for cardview position
        loadImage = MyVolleyRequest.getInstance(context).getImageLoader();
        //set temporary image i.e R.drawable.play
        loadImage.get(video.getImageUrl(), ImageLoader.getImageListener(holder.image, R.drawable.video_play, android.R.drawable.ic_dialog_alert));

        //Showing data on the views

        holder.image.setImageUrl(video.getImageUrl(), loadImage);



        videoUrl = video.getVideoUrl();
        Uri videoUri = Uri.parse(videoUrl);

        //Get the rating of each video stored in the database
        Float rating = video.getRating();
        holder.ratingBar.setRating(rating);

        System.out.println(holder.ratingBar);


        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateValue = String.valueOf(holder.ratingBar.getRating());

            }
        });

        holder.rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemThreeFragment fragment = new ItemThreeFragment();
                Toast.makeText(context, rateValue, Toast.LENGTH_SHORT).show();
                fragment.rating();

            }
        });



        System.out.println(videoUri);
        holder.videoView.setVideoURI(videoUri);
        //holder.textViewName.setText(video.getName());
        if (video.getDescription().equals("null")){}
        else{
        holder.textView.setText(video.getDescription());
        }
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
        public  RatingBar ratingBar;
        public TextView textView;
        public ImageButton rating;
        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            rating = (ImageButton) itemView.findViewById(R.id.submit);
            image = (NetworkImageView) itemView.findViewById(R.id.imageView);
            videoView = (VideoView) itemView.findViewById(R.id.videoViews);
            //textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            textView = (TextView) itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
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


