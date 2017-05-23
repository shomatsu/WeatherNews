package jp.kot.weathernews.viewmodel

import android.content.Context
import android.os.AsyncTask
import jp.kot.weathernews.repository.GetWeatherTask

/**
 * Created by cmlab on 2017/05/15.
 */

class TopViewModel(private var context: Context) {
    fun getWeather() {
        GetWeatherTask(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }
}
