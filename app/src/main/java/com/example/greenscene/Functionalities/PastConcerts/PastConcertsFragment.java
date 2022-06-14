package com.example.greenscene.Functionalities.PastConcerts;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.greenscene.Models.PredictHQApi.Event;
import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.example.greenscene.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PastConcertsFragment extends Fragment {
    private PastConcertsViewModel mViewModel;
    private NavController navController;
    private List<Event> events;
    private List<Event> pastEventsList;
    private List<String> eventIds;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PastConcertsAdapter adapter;
    private PastConcertsAdapter.OnItemClickListener listener;
    private FirebaseAuth fAuth;

    private EditText searchBox;
    private Button searchButton;

    public static PastConcertsFragment newInstance() {
        return new PastConcertsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.past_concerts_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PastConcertsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        BottomNavigationItemView menuItem1 = view.findViewById(R.id.pastConcertsFragment2);
        menuItem1.setPressed(true);
        FloatingActionButton button = view.findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_pastConcertsFragment2_to_concertsMapFragment);
            }
        });

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.settingsFragment2:
                    item.setChecked(true);
                    navController.navigate(R.id.action_pastConcertsFragment2_to_settingsFragment2);
                    break;
                case R.id.favouriteConcertsFragment2:
                    item.setChecked(true);
                    navController.navigate(R.id.action_pastConcertsFragment2_to_favouriteConcertsFragment2);
                    break;
                case R.id.logout:
                    item.setChecked(true);
                    fAuth.signOut();
                    navController.navigate(R.id.action_pastConcertsFragment2_to_startScreenFragment);
                    break;
            }
            return false;
        });
//        BottomNavigationItemView menuItem1 = view.findViewById(R.id.favouriteConcertsFragment2);
//        menuItem1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navController.navigate(R.id.action_pastConcertsFragment2_to_favouriteConcertsFragment2);
//            }
//        });
//
//        BottomNavigationItemView menuItem3 = view.findViewById(R.id.settingsFragment2);
//        menuItem3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navController.navigate(R.id.action_pastConcertsFragment2_to_settingsFragment2);
//            }
//        });
//
//        BottomNavigationItemView menuItem4 = view.findViewById(R.id.logout);
//        menuItem4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fAuth.signOut();
//                navController.navigate(R.id.action_pastConcertsFragment2_to_startScreenFragment);
//            }
//        });

        fAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.past_favorites_recycler);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PastConcertsAdapter(new ArrayList<Event>(),getContext());
        recyclerView.setAdapter(adapter);

        mViewModel = ViewModelProviders.of(this).get(PastConcertsViewModel.class);

        String currentUserId = fAuth.getUid();
        mViewModel.getEventIds(currentUserId);

        mViewModel.getEventIds().observe((LifecycleOwner) getActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                List<String> userEventIds = mViewModel.getEventIds().getValue();
                String idQuery = createStringQuery(userEventIds);

                eventIds = strings;

                mViewModel.getFavorites(idQuery);

                mViewModel.getPastEvents().observe((LifecycleOwner) getActivity(), new Observer<PredictHQResult>() {
                    @Override
                    public void onChanged(PredictHQResult predictHQResult) {
                        List<Event> listOfEvents = predictHQResult.getEvents();
                        listener = new PastConcertsAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                PastConcertsFragmentDirections.ActionPastConcertsFragment2ToPastEventDetailsFragment action =
                                        PastConcertsFragmentDirections.actionPastConcertsFragment2ToPastEventDetailsFragment(
                                          listOfEvents.get(position).getId()
                                        );

                                navController.navigate(action);
                            }
                        };
                        pastEventsList = listOfEvents;
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