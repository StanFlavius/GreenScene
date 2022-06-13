package com.example.greenscene.Functionalities.PastEventDetails;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.greenscene.Functionalities.PastConcerts.PastConcertsAdapter;
import com.example.greenscene.Functionalities.Utils.Utils;
import com.example.greenscene.Models.PredictHQApi.Event;
import com.example.greenscene.R;

import java.util.List;

public class PastEventDetailsAdapter extends RecyclerView.Adapter<PastEventDetailsAdapter.MyViewHolder> {
    private List<String> imageURLs;
    private Context context;
    private PastEventDetailsAdapter.OnItemClickListener onItemClickListener;

    public PastEventDetailsAdapter(List<String> imageURLs, Context context) {
        this.imageURLs = imageURLs;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.past_concerts_gallery_item, parent, false);
        return new PastEventDetailsAdapter.MyViewHolder(view, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return imageURLs.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final PastEventDetailsAdapter.MyViewHolder holders = holder;
        String currentURL = imageURLs.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(imageURLs.get(position))
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holders.imageView);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        PastEventDetailsAdapter.OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView, PastEventDetailsAdapter.OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.currImage);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void updateData(List<String> imageUrls, PastEventDetailsAdapter.OnItemClickListener onItemClickListener){
        this.imageURLs = imageUrls;
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }
}
