package com.example.greenscene.Models.PredictHQApi;

import com.squareup.moshi.Json;

import java.util.List;

public class PredictHQResult {
    @Json(name="count")
    private Integer count;

    @Json(name="overflow")
    private boolean overflow;

    @Json(name="previous")
    private String previous;

    @Json(name="next")
    private String next;

    @Json(name="results")
    private List<Event> events;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
