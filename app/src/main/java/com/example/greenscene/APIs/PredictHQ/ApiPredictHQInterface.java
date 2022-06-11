package com.example.greenscene.APIs.PredictHQ;

import com.example.greenscene.Models.PredictHQApi.PredictHQResult;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiPredictHQInterface {

    @GET("events")
    Single<PredictHQResult> getConcertList(@Header("Authorization") String authToken,
                                           @Header("Accept") String accept,
                                           @Query("category") String category);
}
