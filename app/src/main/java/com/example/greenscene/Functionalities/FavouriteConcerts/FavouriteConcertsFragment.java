package com.example.greenscene.Functionalities.FavouriteConcerts;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.greenscene.R;

public class FavouriteConcertsFragment extends Fragment {

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

}