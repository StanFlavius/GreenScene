package com.example.greenscene.Functionalities.MapRouteFav;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.greenscene.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MapRouteFavFragment extends Fragment implements OnMapReadyCallback {

    private MapRouteFavViewModel mViewModel;
    private Double latitude;
    private Double longitude;
    private TextView textView1;
    private TextView textView2;
    private FusedLocationProviderClient client;
    private SupportMapFragment supportMapFragment;
    private GoogleMap map;
    private MarkerOptions place1;
    private MarkerOptions place2;
    private LatLng origin;
    private LatLng destination;
    private LatLng destination1;
    private TextView txt;
    private Polyline currentPolyline;
    private Integer REQUEST_CODE = 111;
    private ArrayList<LatLng> directionPositionList;

    public static MapRouteFavFragment newInstance() {
        return new MapRouteFavFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_route_fav_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MapRouteFavViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null) {

            MapRouteFavFragmentArgs args = MapRouteFavFragmentArgs.fromBundle(getArguments());
            this.latitude = Double.parseDouble(args.getLatitude());
            this.longitude = Double.parseDouble(args.getLongitude());
        }

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapViewForRoute);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
            getCurrentLocation();
        }
        else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        supportMapFragment.getMapAsync((OnMapReadyCallback) this);

        destination = new LatLng(latitude, longitude);
        //requestDirection();

    }



    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>(){
            @Override
            public void onSuccess(Location location) {
                System.out.println(location);
                if (location != null){
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    origin = latLng;

                    //origin = new LatLng(44.3992093,26.096089);

                    GoogleDirection.withServerKey("AIzaSyBJ2Qk5X2MeLtUD0vDFqYxXKg4ZHi9-rbQ")
                            .from(origin)
                            .to(destination)
                            .transportMode(TransportMode.DRIVING)
                            .execute(new DirectionCallback() {
                                @Override
                                public void onDirectionSuccess(@Nullable Direction direction) {
                                    System.out.println("SUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
                                    System.out.println(origin);
                                    map.addMarker(new MarkerOptions().position(origin));
                                    map.addMarker(new MarkerOptions().position(destination));
                                    if(direction.isOK()){
                                        Route route = direction.getRouteList().get(0);
                                        directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                        System.out.println(directionPositionList);
                                        map.addPolyline(DirectionConverter.createPolyline(getContext(), directionPositionList, 5, Color.RED));
                                        setCameraWithCoordinatesBounds(route);
                                    }
                                }

                                @Override
                                public void onDirectionFailure(@NonNull @NotNull Throwable t) {
                                    Toast.makeText(getActivity(), "Esec", Toast.LENGTH_SHORT).show();
                                }
                            });

                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);

                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

                    map.addMarker(markerOptions).showInfoWindow();

                    TileProvider tileProvider = new UrlTileProvider(256, 256) {
                        @Nullable
                        @Override
                        public URL getTileUrl(int x, int y, int zoom) {
                            String s = String.format("https://tiles.breezometer.com/v1/air-quality/breezometer-aqi/current-conditions/%d/%d/%d.png?key=a052f3bc0a9b4d06b4555466e526bc2d&breezometer_aqi_color=indiper", zoom, x, y);

                            System.out.println(s);
                            if (!checkTileExists(x, y, zoom)) {
                                return null;
                            }
                            try {
                                return new URL(s);
                            } catch (MalformedURLException e) {
                                throw new AssertionError(e);
                            }
                        }

                        private boolean checkTileExists(int x, int y, int zoom) {
                            int minZoom = 1;
                            int maxZoom = 100;

                            return (zoom >= minZoom && zoom <= maxZoom);
                        }
                    };

                    TileOverlay tileOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider).transparency(0.5f));

                }
                else{
                    Toast.makeText(getActivity(), "Activate location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }else{
            Toast.makeText(getActivity(), "no permission", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestDirection(){
        GoogleDirection.withServerKey("AIzaSyBJ2Qk5X2MeLtUD0vDFqYxXKg4ZHi9-rbQ")
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(@Nullable Direction direction) {
                        System.out.println("SUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
                        System.out.println(origin);
                        map.addMarker(new MarkerOptions().position(origin));
                        map.addMarker(new MarkerOptions().position(destination));
                        if(direction.isOK()){
                            Route route = direction.getRouteList().get(0);
                            directionPositionList = route.getLegList().get(0).getDirectionPoint();
                            System.out.println(directionPositionList);
                            map.addPolyline(DirectionConverter.createPolyline(getContext(), directionPositionList, 5, Color.RED));
                            setCameraWithCoordinatesBounds(route);
                        }
                    }

                    @Override
                    public void onDirectionFailure(@NonNull @NotNull Throwable t) {
                        Toast.makeText(getActivity(), "Esec", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setCameraWithCoordinatesBounds(Route route){
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        map = googleMap;
    }

}