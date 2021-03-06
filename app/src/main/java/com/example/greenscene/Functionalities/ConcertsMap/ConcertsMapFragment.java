package com.example.greenscene.Functionalities.ConcertsMap;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenscene.Functionalities.FavouriteConcerts.FavouriteConcertsFragment;
import com.example.greenscene.Functionalities.PastConcerts.PastConcertsFragment;
import com.example.greenscene.Models.Database.FavouriteElement;
import com.example.greenscene.Models.PredictHQApi.Event;
import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.example.greenscene.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ConcertsMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private NavController navController;
    private ConcertsMapViewModel mViewModel;
    private GoogleMap gMap;
    private FirebaseAuth fAuth;
    FloatingActionButton homeButton;
    ObjectAnimator objectAnimator;

    public static ConcertsMapFragment newInstance() {
        return new ConcertsMapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.concerts_map_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ConcertsMapViewModel.class);
        // TODO: Use the ViewModel
    }

    public void setColor(float color){
        homeButton.setBackgroundColor((int) color);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);

        homeButton = view.findViewById(R.id.homeButton);

        objectAnimator = ObjectAnimator.ofFloat(this, String.valueOf(View.ALPHA), 0, 100);
        objectAnimator.setDuration(2000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();



        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.favouriteConcertsFragment2:
                    item.setChecked(true);
                    navController.navigate(R.id.action_concertsMapFragment_to_favouriteConcertsFragment2);
                    break;
                case R.id.pastConcertsFragment2:
                    item.setChecked(true);
                    navController.navigate(R.id.action_concertsMapFragment_to_pastConcertsFragment2);
                    break;
                case R.id.settingsFragment2:
                    item.setChecked(true);
                    navController.navigate(R.id.action_concertsMapFragment_to_settingsFragment2);
                    break;
                case R.id.logout:
                    item.setChecked(true);
                    fAuth.signOut();
                    navController.navigate(R.id.action_concertsMapFragment_to_startScreenFragment);
                    break;
            }
            return false;
        });
//        BottomNavigationItemView menuItem1 = view.findViewById(R.id.favouriteConcertsFragment2);
//        menuItem1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navController.navigate(R.id.action_concertsMapFragment_to_favouriteConcertsFragment2);
//            }
//        });
//
//        BottomNavigationItemView menuItem2 = view.findViewById(R.id.pastConcertsFragment2);
//        menuItem2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navController.navigate(R.id.action_concertsMapFragment_to_pastConcertsFragment2);
//            }
//        });
//
//        BottomNavigationItemView menuItem3 = view.findViewById(R.id.settingsFragment2);
//        menuItem3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navController.navigate(R.id.action_concertsMapFragment_to_settingsFragment2);
//            }
//        });
//
//        BottomNavigationItemView menuItem4 = view.findViewById(R.id.logout);
//        menuItem4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                fAuth.signOut();
//                navController.navigate(R.id.action_concertsMapFragment_to_startScreenFragment);
//            }
//        });

        mViewModel = ViewModelProviders.of(this).get(ConcertsMapViewModel.class);
        mViewModel.init();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        supportMapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        this.gMap.setOnMarkerClickListener(this);
        this.gMap.setOnMapClickListener(this);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        FusedLocationProviderClient client;
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                System.out.println(location);
                if(location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));

                    mViewModel.getEvents().observe((LifecycleOwner) getActivity(), new Observer<PredictHQResult>() {
                        @Override
                        public void onChanged(PredictHQResult res) {
                            for(Event x : res.getEvents()) {
                                String eventTitle = x.getTitle();
                                LatLng latLng = new LatLng(x.getLocation().get(1), x.getLocation().get(0));
                                Marker marker = gMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(eventTitle)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                marker.setTag(x.getId());
                                //System.out.println(marker.getPosition());
                            }
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "Activate location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.concert_popup, null);

        mViewModel = ViewModelProviders.of(this).get(ConcertsMapViewModel.class);
        mViewModel.init(marker.getTag().toString());

        mViewModel.getEvent().observe((LifecycleOwner) getActivity(), new Observer<PredictHQResult>() {
            @Override
            public void onChanged(PredictHQResult predictHQResult) {
                TextView titleTextView = mView.findViewById(R.id.titleTextView);
                TextView descriptionTextView = mView.findViewById(R.id.descriptionTextView);

                titleTextView.setText(predictHQResult.getEvents().get(0).getTitle());
                //descriptionTextView.setText(predictHQResult.getEvents().get(0).getCategory());

                String rawDate = predictHQResult.getEvents().get(0).getStart();
                String formatedDate = rawDate.split("T")[0];
                String formatedHour = rawDate.split("T")[1].substring(0,6);

                descriptionTextView.setText(formatedHour + " / " + formatedDate);

                System.out.println(predictHQResult.getEvents().get(0).getId());
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                System.out.println(predictHQResult.getEvents().get(0).getEnd());

                Button buttonAddFav = mView.findViewById(R.id.buttonAddFav);
                buttonAddFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String favElementId = predictHQResult.getEvents().get(0).getId();
                        String userId = fAuth.getUid();
                        FavouriteElement  favouriteElement = new FavouriteElement(favElementId, userId);
                        mViewModel.addFav(favouriteElement);
                    }
                });
            }
        });

        builder.setView(mView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return false;
    }
}