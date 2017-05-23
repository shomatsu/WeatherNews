package jp.kot.weathernews.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.IBinder
import android.widget.RemoteViews
import jp.kot.weathernews.R
import jp.kot.weathernews.constant.IntentConst
import jp.kot.weathernews.entity.OrmaDatabase
import java.net.URL

class WeatherWidgetService: Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val appWidgetId = intent.getIntExtra(IntentConst.APP_WIDGET_ID, 0)

        val remoteViews = RemoteViews(packageName, R.layout.widget_24_hours)
        val orma = OrmaDatabase.builder(this).build()
        val weatherList = orma.selectFromWeather().toList()
        for (num in 0..6) {
            val weather = weatherList.get(num)
            val iconId = resources.getIdentifier("icon_" + num, "id", packageName)
            val url = URL(weather.icon)
            val drawable = Drawable.createFromStream(url.openStream(), "src")
            val bitmap = (drawable as BitmapDrawable).bitmap
            remoteViews.setImageViewBitmap(iconId, bitmap)
            val dateId = resources.getIdentifier("date_" + num, "id", packageName)
            remoteViews.setTextViewText(dateId, weather.date)
            val dateTimeId = resources.getIdentifier("date_time_" + num, "id", packageName)
            remoteViews.setTextViewText(dateTimeId, weather.dateTime)
            val tempId = resources.getIdentifier("temperature_" + num, "id", packageName)
            remoteViews.setTextViewText(tempId, weather.tempMax.toString() + "℃/" + weather.tempMin.toString() + "℃")
        }
        val manager = AppWidgetManager.getInstance(this)
        manager.updateAppWidget(appWidgetId, remoteViews)

        return super.onStartCommand(intent, flags, startId)
    }
}