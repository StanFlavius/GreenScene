package com.example.greenscene.Functionalities.FavouriteConcertsDetails;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.greenscene.Functionalities.FavouriteConcerts.FavouriteConcertsFragmentDirections;
import com.example.greenscene.Functionalities.PastEventDetails.PastEventDetailsFragmentArgs;
import com.example.greenscene.Functionalities.PastEventDetails.PastEventDetailsViewModel;
import com.example.greenscene.Functionalities.Utils.Utils;
import com.example.greenscene.Models.PredictHQApi.Event;
import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.example.greenscene.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavouriteConcertsDetailsFragment extends Fragment {

    private FavouriteConcertsDetailsViewModel mViewModel;
    private String currentEventId;
    private FirebaseAuth fAuth;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView startTextView;
    private Button buttonToMap;
    private Button buttonShare;
    private NavController navController;

    public static FavouriteConcertsDetailsFragment newInstance() {
        return new FavouriteConcertsDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favourite_concerts_details_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavouriteConcertsDetailsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null) {
            //PastEventDetailsFragmentArgs args = PastEventDetailsFragmentArgs.fromBundle(getArguments());
            FavouriteConcertsDetailsFragmentArgs args = FavouriteConcertsDetailsFragmentArgs.fromBundle(getArguments());
            this.currentEventId = args.getEventId();
        }

        buttonShare = view.findViewById(R.id.buttonShare);
        navController = Navigation.findNavController(view);

        fAuth = FirebaseAuth.getInstance();

        FloatingActionButton button = view.findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_favouriteConcertsDetailsFragment_to_concertsMapFragment);
            }
        });


        BottomNavigationItemView menuItem1 = view.findViewById(R.id.favouriteConcertsFragment2);
        menuItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_favouriteConcertsDetailsFragment_to_favouriteConcertsFragment2);
            }
        });

        BottomNavigationItemView menuItem2 = view.findViewById(R.id.settingsFragment2);
        menuItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_favouriteConcertsDetailsFragment_to_settingsFragment2);
            }
        });

        BottomNavigationItemView menuItem3 = view.findViewById(R.id.pastConcertsFragment2);
        menuItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_favouriteConcertsDetailsFragment_to_pastConcertsFragment2);
            }
        });

        BottomNavigationItemView menuItem4 = view.findViewById(R.id.logout);
        menuItem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                navController.navigate(R.id.action_favouriteConcertsDetailsFragment_to_startScreenFragment);
            }
        });

        mViewModel = ViewModelProviders.of(this).get(FavouriteConcertsDetailsViewModel.class);
        mViewModel.init(currentEventId);

        mViewModel.getCurrentEvent().observe((LifecycleOwner) getActivity(), new Observer<PredictHQResult>() {
            @Override
            public void onChanged(PredictHQResult predictHQResult) {
                Event currentEvent = predictHQResult.getEvents().get(0);

                titleTextView = view.findViewById(R.id.favElemTitle);
                titleTextView.setText(currentEvent.getTitle());

                descriptionTextView = view.findViewById(R.id.favElemDescription);
                startTextView = view.findViewById(R.id.favElemStart);

                if(currentEvent.getDescription().length()>3){
                    descriptionTextView.setText(currentEvent.getDescription());
                } else {
                    descriptionTextView.setText("There is no desciption available for this event.");
                }

                startTextView.setText(Utils.prettyFormatDate(currentEvent.getStart()));

                buttonToMap = view.findViewById(R.id.buttonToMap);
                buttonToMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FavouriteConcertsDetailsFragmentDirections.ActionFavouriteConcertsDetailsFragmentToMapRouteFavFragment action =
                                FavouriteConcertsDetailsFragmentDirections.actionFavouriteConcertsDetailsFragmentToMapRouteFavFragment(
                                        currentEvent.getLocation().get(1).toString(),
                                        currentEvent.getLocation().get(0).toString()
                                );

                        navController.navigate(action);
                    }
                });

                buttonShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        //String eventData = currentEvent.getTitle() + " @ " + Utils.prettyFormatDate(currentEvent.getStart());
                        String eventData = "http://greenscene.ro/futureEvent?eventId=" + currentEventId;
                        sendIntent.putExtra(Intent.EXTRA_TEXT,eventData);
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, "Share Event!"));
                    }
                });
            }
        });

    }
}