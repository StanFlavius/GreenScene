package com.example.greenscene.Functionalities.Settings;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenscene.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    private NavController navController;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        BottomNavigationItemView menuItem1 = view.findViewById(R.id.settingsFragment2);
        menuItem1.setPressed(true);
        FloatingActionButton button = view.findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_settingsFragment2_to_concertsMapFragment);
            }
        });

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.pastConcertsFragment2:
                    item.setChecked(true);
                    navController.navigate(R.id.action_settingsFragment2_to_pastConcertsFragment2);
                    break;
                case R.id.favouriteConcertsFragment2:
                    item.setChecked(true);
                    navController.navigate(R.id.action_settingsFragment2_to_favouriteConcertsFragment2);
                    break;
                case R.id.logout:
                    item.setChecked(true);
                    firebaseAuth.signOut();
                    navController.navigate(R.id.action_settingsFragment2_to_startScreenFragment);
                    break;
            }
            return false;
        });
//        BottomNavigationItemView menuItem1 = view.findViewById(R.id.favouriteConcertsFragment2);
//        menuItem1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navController.navigate(R.id.action_settingsFragment2_to_favouriteConcertsFragment2);
//            }
//        });
//
//        BottomNavigationItemView menuItem3 = view.findViewById(R.id.pastConcertsFragment2);
//        menuItem3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navController.navigate(R.id.action_settingsFragment2_to_pastConcertsFragment2);
//            }
//        });
//
//        BottomNavigationItemView menuItem4 = view.findViewById(R.id.logout);
//        menuItem4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firebaseAuth.signOut();
//                navController.navigate(R.id.action_settingsFragment2_to_startScreenFragment);
//            }
//        });

        TextView textChangePass = view.findViewById(R.id.changePassSett);
        textChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText emailToReset = new EditText(view.getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Reset password?");
                builder.setMessage("Enter email to send password reset link");
                builder.setView(emailToReset);

                builder.setPositiveButton("Yes", (dialog, which) -> {
                    String email = emailToReset.getText().toString();
                    if (email.isEmpty()){
                        emailToReset.setError("Email is required");
                        emailToReset.requestFocus();
                        return;
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        emailToReset.setError("Please provide a valid email");
                        emailToReset.requestFocus();
                        return;
                    }
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity(), "Check your email", Toast.LENGTH_LONG).show();
                                firebaseAuth.signOut();
                                navController.navigate(R.id.action_settingsFragment2_to_startScreenFragment);
                            }
                            else{
                                Toast.makeText(getActivity(), "Something went wrong. Try again later!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                });

                builder.setNegativeButton("No", (dialog, which) -> {

                });

                builder.create().show();
            }
        });
    }
}