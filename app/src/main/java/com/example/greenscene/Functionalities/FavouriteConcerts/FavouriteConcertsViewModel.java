package com.example.greenscene.Functionalities.FavouriteConcerts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.example.greenscene.Repo.ConcertsMapRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FavouriteConcertsViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<List<String>> eventIds;
    private MutableLiveData<PredictHQResult> futureEvents;
    private ConcertsMapRepo cRepo;

    public void getEventIds(String userId){
        //facem lista tuturor evenimentelor de la favorite ale unui anumit user
    }

    public LiveData<PredictHQResult> getFutureEvents() {
        return futureEvents;
    }

    public void getFavorites() {
        List<String> hardCoded = new ArrayList<String>();
        futureEvents = new MutableLiveData<>();

        eventIds = new MutableLiveData<>();
        hardCoded.add("5B2HbFAwWgWDnC3rJt");
        eventIds.postValue(hardCoded);
        String idQuery = "";
        for(int i=0;i<hardCoded.size()-1;i++){
            idQuery += hardCoded.get(i) + ",";
        }
        idQuery += hardCoded.get(hardCoded.size()-1);

        // String[] -> list of Ids event
        LocalDateTime now = LocalDateTime.now();
        String timeNow = now.toString();

        cRepo=ConcertsMapRepo.getInstance();

        cRepo.getFutureEventsByIds(idQuery, timeNow)
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