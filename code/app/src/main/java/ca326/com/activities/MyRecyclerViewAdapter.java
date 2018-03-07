package ca326.com.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.Collections;
import java.util.List;

import static ca326.com.activities.Start_Drawing_Screen.adapterPosition;
import static ca326.com.activities.Start_Drawing_Screen.drawables;
import static ca326.com.activities.Start_Drawing_Screen.myDrawable;
import static ca326.com.activities.Start_Drawing_Screen.set;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Integer> mFrames = Collections.emptyList();
    private List<String> mFrameNum = Collections.emptyList();

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, List<Integer> frames, List<String> frameNo) {
        this.mInflater = LayoutInflater.from(context);
        this.mFrames = frames;
        this.mFrameNum= frameNo;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Integer res = mFrames.get(position);
        String animal = mFrameNum.get(position);
        Log.i("set", "set is " + set);
        if (position != adapterPosition) {
            holder.myView.setBackgroundResource(res);
        }
        else if(adapterPosition == 100){
            holder.myView.setBackgroundResource(res);
        }
        else{
            holder.myView.setBackground(myDrawable);
        }

        holder.myTextView.setText(animal);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mFrameNum.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View myView;
        public TextView myTextView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            myView = itemView.findViewById(R.id.frameView);
            myTextView = itemView.findViewById(R.id.frameNum);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mFrameNum.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}