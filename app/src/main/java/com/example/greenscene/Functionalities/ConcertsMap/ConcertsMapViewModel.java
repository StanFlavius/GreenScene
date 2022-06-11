package com.example.greenscene.Functionalities.ConcertsMap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.greenscene.Models.PredictHQApi.Event;
import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.example.greenscene.Repo.ConcertsMapRepo;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ConcertsMapViewModel extends ViewModel {
    private MutableLiveData<PredictHQResult> result;
    private ConcertsMapRepo cRepo;

    public void init(){
        result = new MutableLiveData<PredictHQResult>();
        cRepo = ConcertsMapRepo.getInstance();
        cRepo.getConcertList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<PredictHQResult>() {
                    @Override
                    public void onSuccess(PredictHQResult res) {
                        result.postValue(res);
                    }
                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.getMessage());
                    }
                });
    }

    public LiveData<PredictHQResult> getEvents() {
        return result;
    }
}