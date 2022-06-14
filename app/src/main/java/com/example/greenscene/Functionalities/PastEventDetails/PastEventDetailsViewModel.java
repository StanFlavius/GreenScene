package com.example.greenscene.Functionalities.PastEventDetails;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.example.greenscene.Repo.ConcertsMapRepo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class PastEventDetailsViewModel extends ViewModel {
    private ConcertsMapRepo cRepo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<PredictHQResult> currentEvent;
    private MutableLiveData<List<String>> imageURLs;

    @Inject
    public PastEventDetailsViewModel(ConcertsMapRepo cRepo) {
        this.cRepo = cRepo;
    }

    public void init(String eventId) {
        currentEvent = new MutableLiveData<>();
        cRepo.getEventById(eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<PredictHQResult>() {
                    @Override
                    public void onSuccess(PredictHQResult predictHQResult) {
                        currentEvent.postValue(predictHQResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.getMessage());
                    }
                });
    }

    public void initImagesURLs(String eventId){
        imageURLs = new MutableLiveData<>();
        db.collection("Pictures")
                .whereEqualTo("eventId", eventId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<String> taskResult = new ArrayList<String>();
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot snap : task.getResult()) {
                                taskResult.add(snap.getString("pictureId"));
                            }
                            imageURLs.postValue(taskResult);
                        }
                    }
                });
    }

    public LiveData<PredictHQResult> getCurrentEvent() {
        return currentEvent;
    }
    public LiveData<List<String>> getImageURLs() {
        return imageURLs;
    }
}