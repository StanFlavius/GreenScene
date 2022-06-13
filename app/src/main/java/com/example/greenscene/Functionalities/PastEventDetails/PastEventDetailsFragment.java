package com.example.greenscene.Functionalities.PastEventDetails;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.greenscene.Models.PredictHQApi.Event;
import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.example.greenscene.R;
import com.google.firebase.auth.FirebaseAuth;

public class PastEventDetailsFragment extends Fragment {
    private String currentEventId;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView startTextView;
    private RecyclerView recyclerView;

    private PastEventDetailsViewModel mViewModel;
    private FirebaseAuth fAuth;

    public static PastEventDetailsFragment newInstance() {
        return new PastEventDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.past_event_details_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PastEventDetailsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null) {
            PastEventDetailsFragmentArgs args = PastEventDetailsFragmentArgs.fromBundle(getArguments());
            this.currentEventId = args.getEventId();
        }

        fAuth = FirebaseAuth.getInstance();

        titleTextView = view.findViewById(R.id.pastDetailsTitle);
        descriptionTextView = view.findViewById(R.id.pastDetailsDescription);
        startTextView = view.findViewById(R.id.pastDetailsStart);
        recyclerView = view.findViewById(R.id.pastDetailsGallery);

        mViewModel = ViewModelProviders.of(this).get(PastEventDetailsViewModel.class);
        mViewModel.init(currentEventId);

        mViewModel.getCurrentEvent().observe((LifecycleOwner) requireContext(), new Observer<PredictHQResult>() {
            @Override
            public void onChanged(PredictHQResult predictHQResult) {
                Event currentEvent = predictHQResult.getEvents().get(0);
                titleTextView.setText(currentEvent.getTitle());
                if(currentEvent.getDescription().length()>3){
                    descriptionTextView.setText(currentEvent.getDescription());
                } else {
                    descriptionTextView.setText("There is no description available for this event.");
                }

                String rawDate = currentEvent.getStart();
                String formatedDate = rawDate.split("T")[0];
                String formatedHour = rawDate.split("T")[1].substring(0,6);

                startTextView.setText(formatedHour + " / " + formatedDate);

                //TODO: update recyclerview
            }
        });
    }
}