package com.example.greenscene.Functionalities.PastConcerts;

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

import java.util.List;

public class PastConcertsAdapter extends RecyclerView.Adapter<PastConcertsAdapter.MyViewHolder> {
    private List<Event> pastEventsList;
    private Context context;
    private PastConcertsAdapter.OnItemClickListener onItemClickListener;

    public PastConcertsAdapter(List<Event> pastEventsList, Context context) {
        this.pastEventsList = pastEventsList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public PastConcertsAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.past_concerts_item,parent,false);
        return new PastConcertsAdapter.MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PastConcertsAdapter.MyViewHolder holders, int position) {
        final PastConcertsAdapter.MyViewHolder holder = holders;
        Event model = pastEventsList.get(position);

        holder.title.setText(model.getTitle());
        holder.start.setText(model.getStart());
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
        return pastEventsList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView description;
        TextView start;
        Button removeButton;
        PastConcertsAdapter.OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView, PastConcertsAdapter.OnItemClickListener onItemClickListener) {
            super(itemView);

            itemView.setOnClickListener(this);
            //TODO:
            title = itemView.findViewById(R.id.titlePastItem);
            description = itemView.findViewById(R.id.desciptionPastItem);
            start = itemView.findViewById(R.id.startPastItem);

            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void updateData(List<Event> pastEvents, OnItemClickListener onItemClickListener){
        this.pastEventsList = pastEvents;
        this.onItemClickListener = onItemClickListener;
        notifyDataSetChanged();
    }
}
