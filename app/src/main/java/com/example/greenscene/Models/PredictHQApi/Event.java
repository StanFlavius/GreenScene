package com.example.greenscene.Models.PredictHQApi;

import com.squareup.moshi.Json;

import java.util.List;

public class Event {
    @Json(name="id")
    private String id;

    @Json(name="title")
    private String title;

    @Json(name="description")
    private String description;

    @Json(name="category")
    private String category;

    @Json(name="start")
    private String start;

    @Json(name="end")
    private String end;

    @Json(name="predicted_end")
    private String predictedEnd;

    @Json(name="updated")
    private String updated;

    @Json(name="first_seen")
    private String firstSeen;

    @Json(name="timezone")
    private String timezone;

    @Json(name="duration")
    private Integer duration;

    @Json(name="rank")
    private Integer rank;

    @Json(name="local_rank")
    private Integer localRank;

    @Json(name="aviation_rank")
    private Integer aviationRank;

    @Json(name="country")
    private String country;

    @Json(name="location")
    private List<Double> location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getPredictedEnd() {
        return predictedEnd;
    }

    public void setPredictedEnd(String predictedEnd) {
        this.predictedEnd = predictedEnd;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getFirstSeen() {
        return firstSeen;
    }

    public void setFirstSeen(String firstSeen) {
        this.firstSeen = firstSeen;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getLocalRank() {
        return localRank;
    }

    public void setLocalRank(Integer localRank) {
        this.localRank = localRank;
    }

    public Integer getAviationRank() {
        return aviationRank;
    }

    public void setAviationRank(Integer aviationRank) {
        this.aviationRank = aviationRank;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }
}

