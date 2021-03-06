package com.example.greenscene.Repo;

import android.webkit.PermissionRequest;

import com.example.greenscene.APIs.PredictHQ.ApiPredictHQ;
import com.example.greenscene.APIs.PredictHQ.ApiPredictHQInterface;
import com.example.greenscene.Models.PredictHQApi.Event;
import com.example.greenscene.Models.PredictHQApi.PredictHQResult;
import com.google.protobuf.Api;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class ConcertsMapRepo {
    public static final String APIKEY_HQ = "Bearer aaf3dqZMYeNrlx91AdPbY1bnFSgHJI2lCkW1H9vy";
    public static final Double BUCHAREST_LATITUDE = 44.435241;
    public static final Double BUCHAREST_LONGITUDE = 26.102803;
    String accept = new String("application/json");


    private static ConcertsMapRepo instance;
    private List<Event> dataSet = new ArrayList<>();

    public static ConcertsMapRepo getInstance(){
        if (instance == null) {
            instance = new ConcertsMapRepo();
        }
        return instance;
    }

    @Inject
    public ConcertsMapRepo() {}

    //Fixed geolocation position
    //TODO: custom latitude and longitude?
    public Single<PredictHQResult> getConcertList(){
        ApiPredictHQInterface apiPredictHQInterface = ApiPredictHQ.getApiPredictHQ().create(ApiPredictHQInterface.class);
        return apiPredictHQInterface.getConcertList(APIKEY_HQ, accept, "concerts");
    }

    public Single<PredictHQResult> getAreaConcertList(){
        ApiPredictHQInterface apiPredictHQInterface = ApiPredictHQ.getApiPredictHQ().create(ApiPredictHQInterface.class);
        Double radius = 200.0;
        String unit = "km";
        String within = radius.toString() + unit + "@" + BUCHAREST_LATITUDE.toString() + "," + BUCHAREST_LONGITUDE.toString();
        Integer limit = 30;
        return apiPredictHQInterface.getAreaConcertList(APIKEY_HQ, accept, "concerts", within, limit);
    }

    public Single<PredictHQResult> getEventById(String eventId){
        ApiPredictHQInterface apiPredictHQInterface = ApiPredictHQ.getApiPredictHQ().create(ApiPredictHQInterface.class);

        return apiPredictHQInterface.getEventById(APIKEY_HQ, accept, "concerts", eventId);
    }

    public Single<PredictHQResult> getPastEventsByIds(String listOfIds, String timeStart) {
        ApiPredictHQInterface apiPredictHQInterface = ApiPredictHQ.getApiPredictHQ().create(ApiPredictHQInterface.class);

        return apiPredictHQInterface.getPastEventsByIds(APIKEY_HQ, accept, "concerts", listOfIds, timeStart, 10);
    }

    public Single<PredictHQResult> getFutureEventsByIds(String listOfIds, String timeEnd) {
        ApiPredictHQInterface apiPredictHQInterface = ApiPredictHQ.getApiPredictHQ().create(ApiPredictHQInterface.class);

        return apiPredictHQInterface.getFutureEventsByIds(APIKEY_HQ, accept, "concerts", listOfIds, timeEnd, 10);
    }

    public Single<PredictHQResult> getPastEventsByIdsAndQuery(String listOfIds, String searchQuery, String timeEnd) {
        ApiPredictHQInterface apiPredictHQInterface = ApiPredictHQ.getApiPredictHQ().create(ApiPredictHQInterface.class);

        return apiPredictHQInterface.getPastEventsByIdsAndQuery(APIKEY_HQ, accept, "concerts", listOfIds, searchQuery, timeEnd, 20);
    }

    public Single<PredictHQResult> getFutureEventsByIdsAndQuery(String listOfIds, String searchQuery, String timeEnd) {
        ApiPredictHQInterface apiPredictHQInterface = ApiPredictHQ.getApiPredictHQ().create(ApiPredictHQInterface.class);

        return apiPredictHQInterface.getFutureEventsByIdsAndQuery(APIKEY_HQ, accept, "concerts", listOfIds, searchQuery, timeEnd, 20);
    }
}
