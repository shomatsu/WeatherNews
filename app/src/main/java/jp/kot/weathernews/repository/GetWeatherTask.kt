package jp.kot.weathernews.repository

import android.content.Context
import android.os.AsyncTask
import jp.kot.weathernews.R
import jp.kot.weathernews.constant.PrefConst
import jp.kot.weathernews.entity.OrmaDatabase
import jp.kot.weathernews.entity.Weather
import jp.kot.weathernews.event.GetWeatherFinishEvent
import jp.kot.weathernews.util.PrefUtil
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class GetWeatherTask(private val context: Context): AsyncTask<Void, Void, List<Weather>>() {

    override fun doInBackground(vararg params: Void?): List<Weather> {
        val lat = PrefUtil.getString(context, PrefConst.KEY_LATITUDE, "0")
        val lon = PrefUtil.getString(context, PrefConst.KEY_LONGITUDE, "0")

        val urlBuilder = HttpUrl.parse(context.getString(R.string.weather_url))
                .newBuilder()
                .addQueryParameter("APPID", context.getString(R.string.weather_api_key))
                .addQueryParameter("lat", lat)
                .addQueryParameter("lon", lon)

        val request = Request.Builder()
                .url(urlBuilder.build())
                .build()

        val response = OkHttpClient().newCall(request).execute()
        val result = response.body().string()

        val jsonObject = JSONObject(result)
        val jsonArray = jsonObject.getJSONArray("list")
        val baseFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPAN)
        val convertDateFormat = SimpleDateFormat("M/d", Locale.JAPAN)
        val convertDateTimeFormat = SimpleDateFormat("HH:mm", Locale.JAPAN)
        val orma = OrmaDatabase.builder(context).build()
        orma.deleteAll()
        val weatherList = ArrayList<Weather>()
        for (i in 0..jsonArray.length() - 1) {
            try {
                val weather = Weather()
                val json = jsonArray.getJSONObject(i)
                val climateJson = json.getJSONObject("main")
                weather.tempMax = Math.ceil(climateJson.getDouble("temp_max") - 273.15).toInt()
                weather.tempMin = Math.ceil(climateJson.getDouble("temp_min") - 273.15).toInt()
                weather.humidity = climateJson.getInt("humidity")

                val weatherJson = json.getJSONArray("weather")
                val valueJson = weatherJson.getJSONObject(0)
                weather.weather = valueJson.getString("main")
                val icon = valueJson.getString("icon")
                weather.icon = context.getString(R.string.weather_icon_url, icon)
                val date = json.getString("dt_txt")
                val baseDate = baseFormat.parse(date)
                weather.date = convertDateFormat.format(baseDate)
                weather.dateTime = convertDateTimeFormat.format(baseDate)
                orma.insertIntoWeather(weather)
                weatherList.add(weather)
            } catch (e: Exception) {
                continue
            }
        }

        return weatherList
    }

    override fun onPostExecute(result: List<Weather>) {
        super.onPostExecute(result)
        EventBus.getDefault().post(GetWeatherFinishEvent(result))
    }
}