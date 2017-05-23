package jp.kot.weathernews.repository

import jp.kot.weathernews.entity.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by cmlab on 2017/05/12.
 */
interface WeatherApiService {
    @GET("data/2.5/forecast/")
    fun getWeather(@Query("id") id: String, @Query("APPID") appid: String): Call<List<Weather>>
}