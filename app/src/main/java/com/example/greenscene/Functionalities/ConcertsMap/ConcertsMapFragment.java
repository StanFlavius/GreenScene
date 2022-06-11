package com.example.greenscene.Functionalities.ConcertsMap;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenscene.Models.PredictHQApi.Event;
import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.example.greenscene.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConcertsMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private ConcertsMapViewModel mViewModel;
    private GoogleMap gMap;

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

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView concertTextView;
        mViewModel = ViewModelProviders.of(this).get(ConcertsMapViewModel.class);
        mViewModel.init();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        supportMapFragment.getMapAsync(this);

        mViewModel.getEvents().observe((LifecycleOwner) requireContext(), new Observer<PredictHQResult>() {
            @Override
            public void onChanged(PredictHQResult res) {
                /*for(int i=0;i<10;i++){
                    System.out.println(res.getEvents().get(i).getTitle());
                }*/

            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
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

                } else {
                    Toast.makeText(getActivity(), "Activate location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }
}