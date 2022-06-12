package com.example.greenscene.Functionalities.Register;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    private NavController navController;
    private RegisterViewModel mViewModel;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private final static int RC_SIGN_IN = 123;

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

        navController = Navigation.findNavController(view);
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
                                    FirebaseUser userC = mAuth.getCurrentUser();
                                    User user = new User(userC.getUid(), emailData, firstNameData, lastNameData, passwordData);

                                    db.collection("Users")
                                            .document(userC.getUid())
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

        Button buttonGoogle = view.findViewById(R.id.registerGoogle);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn(){
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                navController.navigate(R.id.action_registerFragment2_to_concertsMapFragment);
            }
            catch (ApiException e){
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();

                            //String userToken = UUID.randomUUID().toString();
                            User user2 = new User(user.getUid(), user.getEmail(), user.getDisplayName().split(" ")[0],
                                    user.getDisplayName().split(" ")[1], "no data");
                            db.collection("Users")
                                    .document(user.getUid())
                                    .set(user2)
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
                        }
                        else{
                            Toast.makeText(getActivity(), "Could not log in! Email or password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}