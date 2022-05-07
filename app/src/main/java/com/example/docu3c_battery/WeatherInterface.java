package com.example.docu3c_battery;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WeatherInterface {
    @GET("data/2.5/weather?lat=23.0747192&lon=76.8575018&appid=5573f132b7ae785cfb94917005ab3085")
    Call<Gson> getWeather();
}
