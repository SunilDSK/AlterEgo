package com.sunilkumar.alterego.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Sunilkumar on 17-02-2017.
 */

public interface WeatherApi {
    @GET("/data/2.5/weather")
    Call<WeatherData> getCityWeatherFromApi(
            @Query("q") String cityName,
            @Query("appid") String appId);

    Call<WeatherData> getLocalWeatherFromApi(
            @Query("lat") float latitude,
            @Query("lon") float longitude,
            @Query("appid") String appId);
}
