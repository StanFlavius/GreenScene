package com.example.greenscene.APIs.PredictHQ;

import com.example.greenscene.Models.PredictHQApi.PredictHQResult;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiPredictHQInterface {

    @GET("events")
    Single<PredictHQResult> getConcertList(@Header("Authorization") String authToken,
                                           @Header("Accept") String accept,
                                           @Query("category") String category);

    @GET("events")
    Single<PredictHQResult> getAreaConcertList(@Header("Authorization") String authToken,
                                               @Header("Accept") String accept,
                                               @Query("category") String category,
                                               @Query("within") String within,
                                               @Query("limit") Integer limit);

    @GET("events")
    Single<PredictHQResult> getEventById(@Header("Authorization") String authToken,
                                         @Header("Accept") String accept,
                                         @Query("category") String category,
                                         @Query("id") String id);

    @GET("events")
    Single<PredictHQResult> getPastEventsByIds(@Header("Authorization") String authToken,
                                               @Header("Accept") String accept,
                                               @Query("category") String category,
                                               @Query("id") String listOfIds,
                                               @Query("start.lte") String start,
                                               @Query("limit") Integer limit);

    @GET("events")
    Single<PredictHQResult> getFutureEventsByIds(@Header("Authorization") String authToken,
                                               @Header("Accept") String accept,
                                               @Query("category") String category,
                                               @Query("id") String listOfIds,
                                               @Query("start.gte") String end,
                                               @Query("limit") Integer limit);
}
