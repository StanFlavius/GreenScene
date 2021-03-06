package com.example.greenscene.Functionalities.FavouriteConcerts;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class FavouriteConcertsViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<List<String>> eventIds;
    private MutableLiveData<PredictHQResult> futureEvents;
    private ConcertsMapRepo cRepo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Inject
    public FavouriteConcertsViewModel(ConcertsMapRepo cRepo) {
        this.cRepo = cRepo;
    }

    public void getEventIds(String userId){
        eventIds = new MutableLiveData<>();
        db.collection("Favourites")
                .whereEqualTo("userId",userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<String> taskResult = new ArrayList<String>();
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot snap : task.getResult()) {
                                taskResult.add(snap.getString("favElementId"));
                            }
                            eventIds.postValue(taskResult);
                        }
                    }
                });
    }

    public LiveData<List<String>> getEventIds(){
        return eventIds;
    }
    public LiveData<PredictHQResult> getFutureEvents() {
        return futureEvents;
    }

    public void getFavorites(String idQuery) {
        futureEvents = new MutableLiveData<>();

        LocalDateTime now = LocalDateTime.now();
        String timeNow = now.toString();

        cRepo.getFutureEventsByIds(idQuery, timeNow)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<PredictHQResult>() {
                    @Override
                    public void onSuccess(PredictHQResult predictHQResult) {
                        System.out.println(predictHQResult.getEvents());
                        futureEvents.postValue(predictHQResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.getMessage());
                    }
                });
    }

    public void getSearchedFavorites(String idQuery, String searchQuery) {
        LocalDateTime now = LocalDateTime.now();
        String timeNow = now.toString();

        cRepo.getFutureEventsByIdsAndQuery(idQuery, searchQuery, timeNow)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<PredictHQResult>() {
                    @Override
                    public void onSuccess(PredictHQResult predictHQResult) {
                        futureEvents.postValue(predictHQResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.getMessage());
                    }
                });
    }
}