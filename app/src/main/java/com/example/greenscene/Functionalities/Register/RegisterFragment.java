package com.example.greenscene.Functionalities.Register;

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

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.greenscene.Models.Authentication.User;
import com.example.greenscene.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        NavController navController = Navigation.findNavController(view);

        Button button = view.findViewById(R.id.buttonTestBack);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_registerFragment2_to_loginFragment2);
            }
        });

        EditText email = view.findViewById(R.id.emailRegister);
        EditText password = view.findViewById(R.id.passwordRegister);
        EditText firstName = view.findViewById(R.id.firstNameRegister);
        EditText lastName = view.findViewById(R.id.lastNameRegister);

        Button registerButton = view.findViewById(R.id.buttonToRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailData = email.getText().toString().trim();
                String passwordData = password.getText().toString().trim();
                String firstNameData = firstName.getText().toString().trim();
                String lastNameData = lastName.getText().toString().trim();

                System.out.println(emailData + passwordData + firstNameData + lastNameData);
                Log.d("d", emailData + passwordData + firstNameData + lastNameData);


                if (emailData.equals("") || passwordData.equals("")){
                    Toast.makeText(getActivity(), "Registration failed! No empty box allowed!", Toast.LENGTH_SHORT).show();
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(emailData).matches()){
                    Toast.makeText(getActivity(), "Registration failed! Email format incorrect!", Toast.LENGTH_SHORT).show();
                }

                mViewModel.init();
                mViewModel.getEmails().observe((LifecycleOwner) requireContext(), new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> emails) {
                        Boolean checkEmail = false;

                        if (emails != null){
                            for (String e : emails) {
                                if (e.equals(emailData)) {
                                    checkEmail = true;
                                    break;
                                }
                            }
                        }

                        if (! checkEmail){
                            mAuth.createUserWithEmailAndPassword(emailData, passwordData).addOnCompleteListener((OnCompleteListener<AuthResult>) task ->{
                                if (task.isSuccessful()){
                                    String userToken = UUID.randomUUID().toString();
                                    User user = new User(userToken, emailData, firstNameData, lastNameData, passwordData);

                                    db.collection("Users")
                                            .document(userToken)
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("ADDED", "User was added");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    Log.d("FAILED", e.getLocalizedMessage());
                                                }
                                            });

                                    navController.navigate(R.id.action_registerFragment2_to_loginFragment2);
                                }
                                else{

                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            Toast.makeText(getActivity(), "Registration failed! Email already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}