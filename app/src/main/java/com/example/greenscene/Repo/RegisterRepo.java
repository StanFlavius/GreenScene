package com.example.greenscene.Repo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.greenscene.Models.Authentication.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import io.reactivex.Single;

public class RegisterRepo {

    private static RegisterRepo instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<String> emails = new ArrayList<>();

    public static RegisterRepo getInstance(){
        if ( instance == null){
            instance = new RegisterRepo();
        }
        return instance;
    }

    public Single<List<String>> getAllEmails(){
        return Single.create(
                subscriber -> {
                    db.collection("Users")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for(QueryDocumentSnapshot doc : task.getResult())
                                            emails.add(doc.getString("email"));
                                        subscriber.onSuccess(emails);
                                    }
                                }
                            });
                }
        );
    }

//    public void addUser(User user){
//        String userToken = UUID.randomUUID().toString();
//        db.collection("Users")
//                .document(userToken)
//                .set(user)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.d("ADDED", "User was added");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull @NotNull Exception e) {
//                        Log.d("FAILED", e.getLocalizedMessage());
//                    }
//                });
//    }
}
