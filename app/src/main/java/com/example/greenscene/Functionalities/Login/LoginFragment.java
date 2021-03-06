package com.example.greenscene.Functionalities.Login;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenscene.MainActivity;
import com.example.greenscene.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.SQLOutput;
import java.util.concurrent.Executor;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private NavController navController;
    private FirebaseAuth mAuth;
    private LoginViewModel mViewModel;
    private GoogleSignInOptions googleSignInOptions;
    //private GoogleSignInClient googleSignInClient;
    private final static int RC_SIGN_IN = 111;
    private ProgressBar progressBar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public MainActivity mainActivity;
    private GoogleSignInClient googleSignInClient;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        navController = Navigation.findNavController(view);

        EditText email = view.findViewById(R.id.emailLogin);
        EditText password = view.findViewById(R.id.passwordLogin);

        Button buttonLogin = view.findViewById(R.id.buttonToLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailData = email.getText().toString().trim();
                String passwordData = password.getText().toString().trim();

                if (emailData.equals("") || passwordData.equals("")){
                    Toast.makeText(getActivity(), "Login failed! No empty box allowed!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(emailData, passwordData).addOnCompleteListener((OnCompleteListener<AuthResult>) task -> {
                        if (task.isSuccessful()) {
                            //if (mAuth.getCurrentUser().isEmailVerified()){
                                navController.navigate(R.id.action_loginFragment2_to_concertsMapFragment);
                            }
//                            else{
//                                Toast.makeText(getActivity(), "Please confirm your email!", Toast.LENGTH_SHORT).show();
//                            }
                        //}
                        else {
                            Toast.makeText(getActivity(), "Could not log in! Email or password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        TextView buttonBack = view.findViewById(R.id.buttonTestBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_loginFragment2_to_registerFragment2);
            }
        });

        googleSignInClient = ((MainActivity)getActivity()).googleSignInClientAct;

        ImageView buttonGoogle = view.findViewById(R.id.buttonToLoginGoogle);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);
        //((MainActivity)getActivity()).setGoogleSignInClientAct(GoogleSignIn.getClient(getActivity(), googleSignInOptions));
        //mainActivity.setGoogleSignInClientAct(((MainActivity)getActivity()).googleSignInClientAct);

        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

//        Button button = view.findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((MainActivity)getActivity()).getGoogleSignInClientAct().signOut();
//            }
//        });

        TextView textChangePass = view.findViewById(R.id.changePass);
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
                            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getActivity(), "Check your email", Toast.LENGTH_LONG).show();
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

    private void signIn(){
        Intent intent = googleSignInClient.getSignInIntent();
        ((MainActivity)getActivity()).googleSignInClientAct= googleSignInClient;
        googleSignInClient.signOut();
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
                navController.navigate(R.id.action_loginFragment2_to_concertsMapFragment);
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
                            System.out.println(user.getDisplayName());

                        }
                        else{
                            Toast.makeText(getActivity(), "Could not log in! Email or password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}