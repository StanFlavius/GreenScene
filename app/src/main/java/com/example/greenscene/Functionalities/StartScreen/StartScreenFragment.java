package com.example.greenscene.Functionalities.StartScreen;

import androidx.lifecycle.ViewModelProvider;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.example.greenscene.R;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StartScreenFragment extends Fragment {

    private ObjectAnimator objectAnimator;
    private StartScreenViewModel mViewModel;
    Button buttonLogin;
    Button buttonRegister;

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

    public void setAlpha(float color){

        buttonLogin.setAlpha(color);
        buttonRegister.setAlpha(color);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        buttonLogin = view.findViewById(R.id.buttonTestLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_startScreenFragment_to_loginFragment2);
            }
        });

        buttonRegister = view.findViewById(R.id.buttonTestRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_startScreenFragment_to_registerFragment2);
            }
        });

        ImageView imageView = view.findViewById(R.id.ic_logo_mus);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        objectAnimator.setDuration(5000);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                objectAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        objectAnimator.start();

        ImageView imageView2 = view.findViewById(R.id.ic_logo_mus2);
        if(imageView2 != null){
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(imageView2, "rotation", 0f, 360f);
            objectAnimator2.setDuration(5000);
            objectAnimator2.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    objectAnimator2.start();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            objectAnimator2.start();
        }

    }
}