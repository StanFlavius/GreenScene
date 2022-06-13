package com.example.greenscene.Functionalities.FavouriteConcerts;

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
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.greenscene.Functionalities.PastConcerts.PastConcertsAdapter;
import com.example.greenscene.Functionalities.PastConcerts.PastConcertsFragmentDirections;
import com.example.greenscene.Functionalities.PastConcerts.PastConcertsViewModel;
import com.example.greenscene.Models.PredictHQApi.Event;
import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.example.greenscene.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FavouriteConcertsFragment extends Fragment {
    private List<Event> events;
    private List<Event> futureEventsList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FavouriteConcertsAdapter adapter;
    private FavouriteConcertsAdapter.OnItemClickListener listener;
    private FirebaseAuth fAuth;

    private NavController navController;
    private FavouriteConcertsViewModel mViewModel;

    public static FavouriteConcertsFragment newInstance() {
        return new FavouriteConcertsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favourite_concerts_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavouriteConcertsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        navController = Navigation.findNavController(view);
        FloatingActionButton button = view.findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_favouriteConcertsFragment2_to_concertsMapFragment);
            }
        });

//        BottomNavigationItemView menuItem1 = view.findViewById(R.id.favouriteConcertsFragment2);
//        menuItem1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navController.navigate(R.id.action_favouriteConcertsFragment2_to_pastConcertsFragment2);
//            }
//        });


        BottomNavigationItemView menuItem3 = view.findViewById(R.id.settingsFragment2);
        menuItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_favouriteConcertsFragment2_to_settingsFragment2);
            }
        });

        BottomNavigationItemView menuItem4 = view.findViewById(R.id.pastConcertsFragment2);
        menuItem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_favouriteConcertsFragment2_to_pastConcertsFragment2);
            }
        });

        fAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.future_favourite_recycler);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FavouriteConcertsAdapter(new ArrayList<Event>(),getContext());
        recyclerView.setAdapter(adapter);

        mViewModel = ViewModelProviders.of(this).get(FavouriteConcertsViewModel.class);
        String currentUserId = fAuth.getUid();
        System.out.println(fAuth.getCurrentUser().getUid());
        System.out.println(currentUserId);
        mViewModel.getEventIds(currentUserId);

        mViewModel.getEventIds().observe((LifecycleOwner) requireContext(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                String idQuery = "";
                List<String> userEventIds = mViewModel.getEventIds().getValue();
                System.out.println(userEventIds);
                for(int i=0;i<=userEventIds.size()-1;i++){
                    idQuery += userEventIds.get(i) + ",";
                }
//                if(userEventIds.size()>0){
//                    idQuery += userEventIds.get(0);
//                }

                System.out.println(currentUserId);
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAA");
                System.out.println(idQuery);

                mViewModel.getFavorites(idQuery);

                mViewModel.getFutureEvents().observe((LifecycleOwner) requireContext(), new Observer<PredictHQResult>() {
                    @Override
                    public void onChanged(PredictHQResult predictHQResult) {
                        List<Event> listOfEvents = predictHQResult.getEvents();
                        listener = new FavouriteConcertsAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                FavouriteConcertsFragmentDirections.ActionFavouriteConcertsFragment2ToFavouriteConcertsDetailsFragment action =
                                        FavouriteConcertsFragmentDirections.actionFavouriteConcertsFragment2ToFavouriteConcertsDetailsFragment(
                                                listOfEvents.get(position).getId()
                                        );

                                navController.navigate(action);
                            }
                        };
                        adapter.updateData(listOfEvents, listener);
                    }
                });
            }
        });
    }
}