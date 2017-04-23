package com.sunilkumar.alterego.myapplication;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Locale;

import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.ui.AIButton;

public class MainActivity extends AppCompatActivity {
    private TextView resultText;
    String description;
    LocationManager locationManager;
    private AIButton aiButton;
    private Switch loginout;
    private Button clear, play;
    private TextToSpeech t1;
    private String speech;
    private String weather_geo_city;
    private String cab_geo_city1;
    private String cab_geo_city2;
    private final String finalCabReply = "Great! wait till we verify the details with cab api";
    private final String finalWeatherReply = "Okay let me check.";
    private final String youtube = "Okey";
    String mprovider;
    DecimalFormat dtime;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultText = (TextView) findViewById(R.id.TVResult);
        aiButton = (AIButton) findViewById(R.id.micButton);
////        loginout = (Switch) findViewById(R.id.loginout);
//        loginout.setOnCheckedChangeListener(this);
        clear = (Button) findViewById(R.id.clear);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        mprovider = locationManager.getBestProvider(criteria, false);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultText.setText("Tap mike icon to speak");
            }
        });

        final AIConfiguration config = new AIConfiguration("fe95b46f50f9447382ec6a779a01ed7b",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                    t1.setPitch(1.3f);
                    t1.setSpeechRate(1f);
                }
            }
        });

        aiButton.initialize(config);
        aiButton.setResultsListener(new AIButton.AIButtonListener() {
            @Override
            public void onResult(final AIResponse result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("ApiAi", "onResult");
                        speech = result.getResult().getFulfillment().getSpeech().toString();
                        if (speech.equals(finalCabReply)) {
                            cab_geo_city1 = result.getResult().getParameters().get("geo-city").toString();
                            cab_geo_city2 = result.getResult().getParameters().get("geo-city1").toString();
                            resultText.setText(cab_geo_city1 + " " + cab_geo_city2);

                        } else if (speech.equals(finalWeatherReply)) {
                            weather_geo_city = result.getResult().getParameters().toString();
                            WeatherData mWeatherData = new WeatherData();
                            String url;
                            if (weather_geo_city.contains("geo-city")) {
                                weather_geo_city = result.getResult().getParameters().get("geo-city").toString();
                                url = "http://api.openweathermap.org/data/2.5/weather?q=" + weather_geo_city + ",in&appid=39e430ff0fa367b17f8225b66fae0a55";
                            } else {
                                url = "http://api.openweathermap.org/data/2.5/weather?q=Hubli,in&appid=39e430ff0fa367b17f8225b66fae0a55";
                            }
                            mWeatherData.execute(url);
                        } else if (speech.equals(youtube)) {
                            t1.speak(speech, TextToSpeech.QUEUE_FLUSH, null, null);
                            speech = result.getResult().getResolvedQuery().toString();
                            Intent intent = new Intent(Intent.ACTION_SEARCH);
                            intent.setPackage("com.google.android.youtube");
                            intent.putExtra("query", speech);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            resultText.setText(speech);
                            t1.speak(speech, TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                        Log.i("Weather", speech);
                        //t1.speak(speech,TextToSpeech.QUEUE_FLUSH,null,null);
                    }
                });
            }
            @Override
            public void onError(final AIError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("ApiAi", "onError");
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled() {

            }
        });
    }

    public class WeatherData extends AsyncTask<String, String, String> {
        String city;
        double temp;

        @Override
        protected String doInBackground(String... url) {

//            try{
//                //Create HTTPClient
//                OkHttpClient client = new OkHttpClient();
//
//                //Define request body sent to the server
//                RequestBody mRequestBody = new FormBody.Builder()
//                        .add("type","json")
//                        .build();
//
//                Request mRequest = new Request.Builder()
//                        .url(url[0])
//                        .post(mRequestBody)
//                        .build();
//
//                //Transform the request and wait for response to process next
//                Response mResponse = client.newCall(mRequest).execute();
//                String result = mResponse.body().toString();
//                return result;
//            }catch (Exception e){
//                return null;
//            }

            HTTPHandler httpHandler = new HTTPHandler();
            String json = httpHandler.makeServiceCall(url[0]);
            Log.i("Response from url", json);
            if (!json.isEmpty()) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray weather = jsonObject.getJSONArray("weather");
                    JSONObject weatherDetails = weather.getJSONObject(0);
                    description = weatherDetails.getString("description");
                    city = jsonObject.getString("name");
                    JSONObject main = jsonObject.getJSONObject("main");
                    temp = Double.parseDouble(main.getString("temp"));
                    temp -= 273.15;
                    temp = Double.parseDouble(new DecimalFormat("##.###").format(temp));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            speech = "Its " + description + " with " + temp + "degree celsius";
                            String display = "Its " + description + " with " + temp + " \u00b0C";
                            resultText.setText(display);
                            t1.speak(speech, TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                    });
                } catch (final Exception e) {
                }
            } else {
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
        }
    }


//    public void promptSpeechInput(){
//        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say something!");
//        try {
//            startActivityForResult(i, 100);
//        }catch (ActivityNotFoundException e){
//            Toast.makeText(MainActivity.this,"Sorry your device doesn't support speech recording.",Toast.LENGTH_LONG).show();
//        }
//    }
//    public void onActivityResult(int request_code , int result_code , Intent i) {
//        super.onActivityResult(request_code,result_code,i);
//        switch (request_code){
//            case 100:
//                if(result_code == RESULT_OK && i != null){
//                    ArrayList<String> result =i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    resultText.setText(result.get(0));
//                }
//                break;
//        }
//    }
}
