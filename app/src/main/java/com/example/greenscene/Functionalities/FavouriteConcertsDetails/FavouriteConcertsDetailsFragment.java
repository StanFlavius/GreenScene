package com.example.greenscene.Functionalities.FavouriteConcertsDetails;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.greenscene.Models.PredictHQApi.Event;
import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.example.greenscene.R;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class FavouriteConcertsDetailsFragment extends Fragment {

    private FavouriteConcertsDetailsViewModel mViewModel;
    private String currentEventId;
    private FirebaseAuth fAuth;
    private TextView textView;
    private Button buttonToMap;
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

        navController = Navigation.findNavController(view);

        fAuth = FirebaseAuth.getInstance();

        mViewModel = ViewModelProviders.of(this).get(FavouriteConcertsDetailsViewModel.class);
        mViewModel.init(currentEventId);

        mViewModel.getCurrentEvent().observe((LifecycleOwner) requireContext(), new Observer<PredictHQResult>() {
            @Override
            public void onChanged(PredictHQResult predictHQResult) {
                Event currentEvent = predictHQResult.getEvents().get(0);

                textView = view.findViewById(R.id.favElemTitle);
                textView.setText(currentEvent.getTitle());

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
            }
        });

    }
}