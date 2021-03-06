package com.example.greenscene.Functionalities.FavouriteConcerts;

import androidx.annotation.ColorInt;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.greenscene.Functionalities.PastConcerts.PastConcertsAdapter;
import com.example.greenscene.Functionalities.PastConcerts.PastConcertsFragmentDirections;
import com.example.greenscene.Functionalities.PastConcerts.PastConcertsViewModel;
import com.example.greenscene.Models.PredictHQApi.Event;
import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.example.greenscene.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavouriteConcertsFragment extends Fragment {
    private List<Event> events;
    private List<Event> futureEventsList;
    private List<String> eventIds;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FavouriteConcertsAdapter adapter;
    private FavouriteConcertsAdapter.OnItemClickListener listener;
    private FirebaseAuth fAuth;
    private FirebaseMessaging fMessage;

    private NavController navController;
    private FavouriteConcertsViewModel mViewModel;

    private EditText searchBox;
    private Button searchButton;

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

        BottomNavigationItemView menuItem1 = view.findViewById(R.id.favouriteConcertsFragment2);
        menuItem1.setPressed(true);

        navController = Navigation.findNavController(view);
        searchBox = view.findViewById(R.id.searchBox);
        searchButton = view.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> userEventIds = eventIds;
                String idQuery = createStringQuery(userEventIds);

                String searchQuery = searchBox.getText().toString().replace(" ", "+");
                mViewModel.getSearchedFavorites(idQuery, searchQuery);
            }
        });

        FloatingActionButton button = view.findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_favouriteConcertsFragment2_to_concertsMapFragment);
            }
        });


        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.pastConcertsFragment2:
                    item.setChecked(true);
                    navController.navigate(R.id.action_favouriteConcertsFragment2_to_pastConcertsFragment2);
                    break;
                case R.id.settingsFragment2:
                    item.setChecked(true);
                    navController.navigate(R.id.action_favouriteConcertsFragment2_to_settingsFragment2);
                    break;
                case R.id.logout:
                    item.setChecked(true);
                    fAuth.signOut();
                    navController.navigate(R.id.action_favouriteConcertsFragment2_to_startScreenFragment);
                    break;
            }
            return false;
        });
//        BottomNavigationItemView menuItem3 = view.findViewById(R.id.favouriteConcertsFragment2);
//        menuItem3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navController.navigate(R.id.action_favouriteConcertsFragment2_to_settingsFragment2);
//            }
//        });
//
//        BottomNavigationItemView menuItem4 = view.findViewById(R.id.pastConcertsFragment2);
//        menuItem4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navController.navigate(R.id.action_favouriteConcertsFragment2_to_pastConcertsFragment2);
//            }
//        });
//
//        BottomNavigationItemView menuItem1 = view.findViewById(R.id.logout);
//        menuItem1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fAuth.signOut();
//                navController.navigate(R.id.action_favouriteConcertsFragment2_to_startScreenFragment);
//            }
//        });

        fAuth = FirebaseAuth.getInstance();
        fMessage = FirebaseMessaging.getInstance();

        fMessage.getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.d("TOKENIZE", s);
                    }
                });

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

        mViewModel.getEventIds().observe((LifecycleOwner) getActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                List<String> userEventIds = mViewModel.getEventIds().getValue();
                String idQuery = createStringQuery(userEventIds);

                eventIds = strings;

                mViewModel.getFavorites(idQuery);

                mViewModel.getFutureEvents().observe((LifecycleOwner) getActivity(), new Observer<PredictHQResult>() {
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
                        futureEventsList = listOfEvents;
                        adapter.updateData(listOfEvents, listener);
                    }
                });
            }
        });
    }

    private String createStringQuery(List<String> plistIds) {
        String idQuery = "";
        for(int i=0;i<plistIds.size()-1;i++){
            idQuery += plistIds.get(i) + ",";
        }
        if(plistIds.size()>0){
            idQuery += plistIds.get(0);
        }
        return idQuery;
    }
}