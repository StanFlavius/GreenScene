package com.example.greenscene.Repo;

import android.net.Uri;

import com.example.greenscene.Models.Database.PhotoElement;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class ImageRepo {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private static ImageRepo instance;

    public static ImageRepo getInstance(){
        if (instance == null) {
            instance = new ImageRepo();
        }
        return instance;
    }

    private ImageRepo() {}

    public void uploadImage(Uri imageUri, String eventId, String userId, String fileExtension) {

        StorageReference fileRef = storageReference.child(System.currentTimeMillis()+"-"+userId+"."+fileExtension);
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageId = uri.toString();
                        String imageUri = uri.toString();
                        PhotoElement elem = new PhotoElement(eventId, imageId, userId);

                        db.collection("Pictures")
                                .document(UUID.randomUUID().toString())
                                .set(elem);
                    }
                });
            }
        });
    }
}
