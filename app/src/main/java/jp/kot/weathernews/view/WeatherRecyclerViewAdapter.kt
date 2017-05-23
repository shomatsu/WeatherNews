package jp.kot.weathernews.view

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import jp.kot.weathernews.R
import jp.kot.weathernews.entity.Weather

class WeatherRecyclerViewAdapter(private var weatherList: List<Weather>) : RecyclerView.Adapter<WeatherRecyclerViewAdapter.WeatherRecyclerViewHolder>() {

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WeatherRecyclerViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(viewType, null)
        return WeatherRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherRecyclerViewHolder?, position: Int) {
        holder?.setData(weatherList.get(position))
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return R.layout.weather_item_row_top
        } else {
            val weather = weatherList.get(position)
            val weatherBef = weatherList.get(position - 1)
            if (TextUtils.equals(weather.date, weatherBef.date)) {
                return R.layout.weather_item_row
            } else {
                return R.layout.weather_item_row_top
            }
        }
    }

    class WeatherRecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var date: TextView? = null
        private var temp: TextView? = null
        private var icon: ImageView? = null
        private var dateTime: TextView? = null
        init {
            val dateTemp = itemView.findViewById(R.id.date)
            if (dateTemp != null) {
                date = dateTemp as TextView
            }
            icon = itemView.findViewById(R.id.icon) as ImageView
            dateTime = itemView.findViewById(R.id.date_time) as TextView
            temp = itemView.findViewById(R.id.temperature) as TextView
        }

        fun setData(weather: Weather) {
            val context = temp?.context
            date?.text = weather.date
            Picasso.with(context).load(weather.icon).into(icon)
            dateTime?.text = weather.dateTime
            temp?.text = context?.getString(R.string.temperature, weather.tempMax.toString(), weather.tempMin.toString())
        }
    }
}