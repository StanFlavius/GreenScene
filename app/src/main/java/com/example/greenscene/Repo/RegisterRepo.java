package com.example.greenscene.Repo;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
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
}
