package org.example;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

    public class YandexWeather {
        public static void main(String[] args) {

            String apiKey = "814c8567-1231-45cf-9e29-6fe57ceaaf84";

            double latitude = 55.75;
            double longitude = 37.62;

            int limit = 7;

            try {

                String urlString = String.format("https://api.weather.yandex.ru/v2/forecast?lat="+latitude+"&lon="+longitude, limit);

                URL url = new URL(urlString);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Yandex-Weather-Key", apiKey);
                connection.setRequestProperty("Accept", "application/json");

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());

                    System.out.println("Answer:");
                    System.out.println(jsonResponse.toString(2));

                    JSONObject fact = jsonResponse.getJSONObject("fact");
                    int currentTemp = fact.getInt("temp");
                    System.out.println("\nCurrent temperature: " + currentTemp + " C ");

                    JSONArray forecasts = jsonResponse.getJSONArray("forecasts");
                    double totalTemp = 0;
                    int count = 0;
                    for (int i = 0; i < forecasts.length(); i++) {
                        JSONObject dayParts = forecasts.getJSONObject(i).getJSONObject("parts").getJSONObject("day");
                        if (dayParts.has("temp_avg")) {
                            totalTemp += dayParts.getInt("temp_avg");
                            count++;
                        }
                    }
                    if (count > 0) {
                        double averageTemp = totalTemp / count;
                        System.out.println("\nAverage temperature of " + count + " days is " + averageTemp + " C ");
                    } else {
                        System.out.println("\nFailed to calculate average temperature.");
                    }
                } else {
                    System.out.println("\n" + "Error: " + responseCode);
                }

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//https://github.com/AntonVO1991/Weatheryandex.git
