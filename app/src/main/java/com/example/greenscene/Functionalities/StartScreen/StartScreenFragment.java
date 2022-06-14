package com.example.greenscene.Functionalities.StartScreen;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.greenscene.R;

import org.jetbrains.annotations.NotNull;

public class StartScreenFragment extends Fragment {

    private StartScreenViewModel mViewModel;

    public static StartScreenFragment newInstance() {
        return new StartScreenFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_screen_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StartScreenViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        Button buttonLogin = view.findViewById(R.id.buttonTestLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_startScreenFragment_to_loginFragment2);
            }
        });

        Button buttonRegister = view.findViewById(R.id.buttonTestRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_startScreenFragment_to_registerFragment2);
            }
        });

//        Button buttonTest = view.findViewById(R.id.buttonTestAPI);
//        buttonTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navController.navigate(R.id.action_startScreenFragment_to_concertsMapFragment);
//            }
//        });
    }
}