package com.example.myapplication.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.myapplication.db.City;
import com.example.myapplication.db.County;
import com.example.myapplication.db.Province;
import com.example.myapplication.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class Utility {
    private static final String TAG = "Utility";
    /*
    * 解析和处理服务器返回的省级数据
    */
    public static boolean handleProvincesResponse(String response){
        Log.d(TAG, "handleProvincesResponse: " + "handle");
        if(!TextUtils.isEmpty(response)){
            try{
                Log.d(TAG, "handleProvincesResponse: ");
                JSONArray allProvinces = new JSONArray(response);
                for(int i =0; i<allProvinces.length(); i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
                Log.d(TAG, "handleProvincesResponse: ");
            }
        }
        return false;
    }

    /*
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCitiesResponse(String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                for(int i =0; i<allCities.length(); i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    * 解析和处理服务器返回的县级数据
    */
    public static boolean handleCountiesResponse(String response, int cityId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                for(int i=0; i<allCounties.length(); i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }


    /*
    * 将返回的JSON数据解析成Weather实体类
    */
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
