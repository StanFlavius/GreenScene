package com.example.greenscene.Functionalities.Register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.greenscene.Repo.RegisterRepo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<List<String>> emails;
    private RegisterRepo registerRepo;

    public void init(){
        emails = new MutableLiveData<>();
        registerRepo = RegisterRepo.getInstance();
        registerRepo.getAllEmails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<String>>() {
                    @Override
                    public void onSuccess(@NotNull List<String> emailsList) {
                        emails.postValue(emailsList);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

                    }
                });
    }

    public LiveData<List<String>> getEmails() {
        return emails;
    }
}