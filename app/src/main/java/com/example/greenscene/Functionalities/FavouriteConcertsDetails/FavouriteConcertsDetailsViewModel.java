package com.example.greenscene.Functionalities.FavouriteConcertsDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.example.greenscene.Repo.ConcertsMapRepo;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class FavouriteConcertsDetailsViewModel extends ViewModel {
    private ConcertsMapRepo cRepo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<PredictHQResult> currentEvent;

    @Inject
    public FavouriteConcertsDetailsViewModel(ConcertsMapRepo cRepo) {
        this.cRepo = cRepo;
    }

    public void init(String eventId) {
        currentEvent = new MutableLiveData<>();
        cRepo = ConcertsMapRepo.getInstance();

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

    public LiveData<PredictHQResult> getCurrentEvent() {
        return currentEvent;
    }
}