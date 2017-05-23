package jp.kot.weathernews.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.RemoteViews
import jp.kot.weathernews.R
import jp.kot.weathernews.entity.OrmaDatabase
import java.net.URL


class WeatherWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            // todo
//            val intent = Intent(context, WeatherWidgetService::class.java)
//            intent.putExtra(IntentConst.APP_WIDGET_ID, appWidgetId)
//            context.startService(intent)
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
    }

    fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_24_hours)
        val orma = OrmaDatabase.builder(context).build()
        val weatherList = orma.selectFromWeather().toList()
        for (num in 0..6) {
            val weather = weatherList.get(num)
            val iconId = context.resources.getIdentifier("icon_" + num, "id", context.packageName)
            val url = URL(weather.icon)
            val drawable = Drawable.createFromStream(url.openStream(), "src")
            val bitmap = (drawable as BitmapDrawable).bitmap
            remoteViews.setImageViewBitmap(iconId, bitmap)
            val dateId = context.resources.getIdentifier("date_" + num, "id", context.packageName)
            remoteViews.setTextViewText(dateId, weather.date)
            val dateTimeId = context.resources.getIdentifier("date_time_" + num, "id", context.packageName)
            remoteViews.setTextViewText(dateTimeId, weather.dateTime)
            val tempId = context.resources.getIdentifier("temperature_" + num, "id", context.packageName)
            remoteViews.setTextViewText(tempId, weather.tempMax.toString() + "℃/" + weather.tempMin.toString() + "℃")
        }

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }
}

