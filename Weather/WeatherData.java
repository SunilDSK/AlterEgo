package com.sunilkumar.alterego.myapplication;

/**
 * Created by Sunilkumar on 17-02-2017.
 */
public class WeatherData {
    private Weather weather_info;
    private Main temp_humid;

    public WeatherData(Weather weather_info, Main temp_humid) {
        this.weather_info = weather_info;
        this.temp_humid = temp_humid;
    }

    class Main {
        double temp;
        double pressure;
        int humidity;

        public Main(double temp, int pressure, int humidity) {
            this.temp = temp;
            this.pressure = pressure;
            this.humidity = humidity;
        }

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getPressure() {
            return pressure;
        }

        public void setPressure(double pressure) {
            this.pressure = pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }
    }

    class Weather{
        String main;
        String description;

        public Weather() {
        }

        public Weather(String main, String description) {
            this.main = main;
            this.description = description;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public double getPressure() {
        return temp_humid.getPressure();
    }

    public double getTemp() {
        return temp_humid.getTemp();
    }

    public double getHumidity() {
        return temp_humid.getHumidity();
    }

    public String getDescription() {
        return weather_info.getDescription();
    }

}
