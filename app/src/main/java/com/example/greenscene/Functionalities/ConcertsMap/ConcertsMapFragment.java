package com.example.greenscene.Functionalities.ConcertsMap;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.greenscene.Models.PredictHQApi.Event;
import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.example.greenscene.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConcertsMapFragment extends Fragment {

    private ConcertsMapViewModel mViewModel;

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
        concertTextView = view.findViewById(R.id.ConcertTrial);
        mViewModel.init();
        mViewModel.getEvents().observe((LifecycleOwner) requireContext(), new Observer<PredictHQResult>() {
            @Override
            public void onChanged(PredictHQResult res) {
                System.out.println(res.getEvents().get(0).getTitle());
                concertTextView.setText(res.getEvents().get(0).getTitle());
            }
        });
    }
}