package com.example.greenscene.Functionalities.FavouriteConcerts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenscene.Models.PredictHQApi.Event;
import com.example.greenscene.R;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FavouriteConcertsAdapter extends RecyclerView.Adapter<FavouriteConcertsAdapter.MyViewHolder> {
    private List<Event> futureEventsList;
    private Context context;
    private FavouriteConcertsAdapter.OnItemClickListener onItemClickListener;

    public FavouriteConcertsAdapter(List<Event> futureEventsList, Context context) {
        this.futureEventsList = futureEventsList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public FavouriteConcertsAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favourite_concerts_item,parent,false);
        return new FavouriteConcertsAdapter.MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FavouriteConcertsAdapter.MyViewHolder holders, int position) {
        final FavouriteConcertsAdapter.MyViewHolder holder = holders;
        Event model = futureEventsList.get(position);

        holder.title.setText(model.getTitle());

        String rawDate = model.getStart();
        String formatedDate = rawDate.split("T")[0];
        String formatedHour = rawDate.split("T")[1].substring(0,6);

        holder.start.setText(formatedHour + " / " + formatedDate);

        String descString;
        if(model.getDescription().length() > 3) {
            descString = model.getDescription();
        } else {
            descString = "Sorry, there is no description available for this event.";
        }
        holder.description.setText(descString);
    }

    @Override
    public int getItemCount() {
        return futureEventsList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView description;
        TextView start;
        Button removeButton;
        FavouriteConcertsAdapter.OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView, FavouriteConcertsAdapter.OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);
            //TODO:
            title = itemView.findViewById(R.id.titleFutureItem);
            description = itemView.findViewById(R.id.descriptionFutureItem);
            start = itemView.findViewById(R.id.startFutureItem);

            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void updateData(List<Event> futureEvents, FavouriteConcertsAdapter.OnItemClickListener onItemClickListener){
        this.futureEventsList = futureEvents;
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }
}
