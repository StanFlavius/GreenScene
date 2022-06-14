package com.example.greenscene.Functionalities.ConcertsMap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.greenscene.Models.Database.FavouriteElement;
import com.example.greenscene.Models.PredictHQApi.Event;
import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.example.greenscene.Repo.ConcertsMapRepo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.UUID;

import javax.inject.Inject;

@HiltViewModel
public class ConcertsMapViewModel extends ViewModel {
    private MutableLiveData<PredictHQResult> concertList;
    private MutableLiveData<PredictHQResult> event;
    private ConcertsMapRepo cRepo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Inject
    public ConcertsMapViewModel(ConcertsMapRepo cRepo) {
        this.cRepo = cRepo;
    }

    public void init(){
        concertList = new MutableLiveData<PredictHQResult>();
        cRepo = ConcertsMapRepo.getInstance();
        cRepo.getAreaConcertList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<PredictHQResult>() {
                    @Override
                    public void onSuccess(PredictHQResult res) {
                        concertList.postValue(res);
                    }
                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.getMessage());
                    }
                });
    }

    public void init(String id){
        event = new MutableLiveData<PredictHQResult>();
        cRepo = ConcertsMapRepo.getInstance();
        cRepo.getEventById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<PredictHQResult>() {
                    @Override
                    public void onSuccess(@NotNull PredictHQResult predictHQResult) {
                        event.postValue(predictHQResult);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                    }
                });
    }

    public void addFav(FavouriteElement favouriteElement){
        db.collection("Favourites")
                .document(UUID.randomUUID().toString())
                .set(favouriteElement)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        //Log.d("FAILED", e.getLocalizedMessage());
                    }
                });
    }

    public LiveData<PredictHQResult> getEvent() {return event;}

    public LiveData<PredictHQResult> getEvents() {
        return concertList;
    }
}