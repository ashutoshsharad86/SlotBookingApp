package com.ashutosh.slotbookingapp;


import retrofit2.Call;
import retrofit2.http.GET;
public interface HealthifyMeInterface {
    @GET("/api/v1/booking/slots/all?username=alok%40x.coz&api_key=a4aeb4e27f27b5786828f6cdf00d8d2cb44fe6d7&vc=276&expert_username=neha%40healthifyme.com&format=json")
    Call<SlotTimingItems> loadQuestions();
}

