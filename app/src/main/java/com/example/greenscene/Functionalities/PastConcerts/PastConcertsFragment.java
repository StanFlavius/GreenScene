package com.example.greenscene.Functionalities.PastConcerts;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.greenscene.R;

public class PastConcertsFragment extends Fragment {

    private PastConcertsViewModel mViewModel;

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

}