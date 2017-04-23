package com.sunilkumar.alterego.myapplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunilkumar on 18-02-2017.
 */

public class WeatherRESTAdapter {

    private Retrofit mRetrofitBuilder;
    private WeatherApi mApi;
    private WeatherData mWeatherData;
    static final String WEATHER_URL="http://api.openweathermap.org";
    static final String OPEN_WEATHER_API = "39e430ff0fa367b17f8225b66fae0a55";

    public WeatherRESTAdapter() {
        mRetrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(WEATHER_URL).build();
        mApi = mRetrofitBuilder.create(WeatherApi.class);
    }

    private void testWeatherApi(String city){

        Call<WeatherData> call = mApi.getCityWeatherFromApi(city, OPEN_WEATHER_API);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                mWeatherData = response.body();
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
//                Toast.makeText(MainActivity.class,"",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void testWeatherApi(float lat,float lon){

        Call<WeatherData> call = mApi.getLocalWeatherFromApi(lat,lon, OPEN_WEATHER_API);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                mWeatherData = response.body();
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
//                Toast.makeText(MainActivity.class,"",Toast.LENGTH_LONG).show();
            }
        });
    }

    public String getWeatherData(String city){
        testWeatherApi(city);
        String result = mWeatherData.getDescription();
        return result;
    }
    public String getWeatherData(float lat,float lon){
        testWeatherApi(lat,lon);
        String result = mWeatherData.getDescription();
        return result;
    }
}
