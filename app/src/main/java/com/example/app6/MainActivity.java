package com.example.app6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    boolean showJson = true;
    boolean showXml = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button xmlBtn = findViewById(R.id.parseXmlBtn);
        Button jsonBtn = findViewById(R.id.parseJsonBtn);

        xmlBtn.setOnClickListener(v -> {
            TextView tvCity, tvLatitude, tvLongitude, tvTemperature, tvHumidity;
            tvCity = findViewById(R.id.citynameval1);
            tvLatitude = findViewById(R.id.latitudeVal1);
            tvLongitude = findViewById(R.id.longitudeVal1);
            tvTemperature = findViewById(R.id.temperatureVal1);
            tvHumidity = findViewById(R.id.humidityVal1);

            if (showXml) {
                try {
                    InputStream is = getAssets().open("city.xml");

                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(is);

                    Element rootTag = doc.getDocumentElement();
                    rootTag.normalize();
                    NodeList nodeList = doc.getElementsByTagName("citydetails");

                    Node node = nodeList.item(0);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element everyTagUnderRoot = (Element) node;
                        tvCity.setText(String.format(" City Name: %s", getValue("cityname", everyTagUnderRoot)));
                        tvLatitude.setText(String.format(" Latitude: %s", getValue("latitude", everyTagUnderRoot)));
                        tvLongitude.setText(String.format(" Longitude: %s", getValue("longitude", everyTagUnderRoot)));
                        tvTemperature.setText(String.format(" Temperature: %s", getValue("temperature", everyTagUnderRoot)));
                        tvHumidity.setText(String.format(" Humidity: %s", getValue("humidity", everyTagUnderRoot)));
                    }
                    showXml = false;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                tvCity.setText(" ");
                tvLatitude.setText(" ");
                tvLongitude.setText(" ");
                tvTemperature.setText(" ");
                tvHumidity.setText(" ");
                showXml = true;

            }


        });

        jsonBtn.setOnClickListener(v -> {
            TextView tvCity, tvLatitude, tvLongitude, tvTemperature, tvHumidity;
            tvCity = findViewById(R.id.citynameval2);
            tvLatitude = findViewById(R.id.latitudeVal2);
            tvLongitude = findViewById(R.id.longitudeVal2);
            tvTemperature = findViewById(R.id.temperatureVal2);
            tvHumidity = findViewById(R.id.humidityVal2);
            if (showJson) {
                try {
                    JSONObject jsonObject = new JSONObject(readJson());
                    JSONObject cityDetails = jsonObject.getJSONObject("citydetails");

                    String city, latitude, longitude, temperature, humidity;

                    city = cityDetails.getString("cityname");
                    latitude = cityDetails.getString("latitude");
                    longitude = cityDetails.getString("longitude");
                    temperature = cityDetails.getString("temperature");
                    humidity = cityDetails.getString("humidity");

                    tvCity.setText(String.format(" City Name: %s", city));
                    tvLatitude.setText(String.format(" Latitude: %s", latitude));
                    tvLongitude.setText(String.format(" Longitude: %s", longitude));
                    tvTemperature.setText(String.format(" Temperature: %s", temperature));
                    tvHumidity.setText(String.format(" Humidity: %s", humidity));
                    showJson = false;

                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }

            } else {
                tvCity.setText(" ");
                tvLatitude.setText(" ");
                tvLongitude.setText(" ");
                tvTemperature.setText(" ");
                tvHumidity.setText(" ");
                showJson = true;
            }

        });
    }

    private String readJson() {
        String json;
        try {
            InputStream inputStream = getAssets().open("city.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
}