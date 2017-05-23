package jp.kot.weathernews.activity

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.PermissionChecker
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import jp.kot.weathernews.R
import jp.kot.weathernews.constant.PrefConst
import jp.kot.weathernews.event.GetWeatherFinishEvent
import jp.kot.weathernews.util.PrefUtil
import jp.kot.weathernews.view.WeatherRecyclerViewAdapter
import jp.kot.weathernews.viewmodel.TopViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class TopActivity: AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private var locationManager: LocationManager? = null
    private var swipeLayout: SwipeRefreshLayout? = null
    private var weatherListView: RecyclerView? = null
    private val locationListener = object: LocationListener {
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }

        override fun onLocationChanged(location: Location?) {
            PrefUtil.putString(this@TopActivity, PrefConst.KEY_LATITUDE, location?.latitude.toString())
            PrefUtil.putString(this@TopActivity, PrefConst.KEY_LONGITUDE, location?.longitude.toString())
            Log.d("latitude", location?.latitude.toString())
            Log.d("longitude", location?.longitude.toString())
            locationManager?.removeUpdates(this)
            TopViewModel(this@TopActivity).getWeather()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)
        setTitle(R.string.title)

        swipeLayout = findViewById(R.id.swipe_layout) as SwipeRefreshLayout
        swipeLayout?.setOnRefreshListener(this)
        weatherListView = findViewById(R.id.item_list) as RecyclerView
        weatherListView?.layoutManager = LinearLayoutManager(this)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        getWeather()
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        super.onPause()
        locationManager?.removeUpdates(locationListener)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.size == 1 && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
            }
        }
    }

    override fun onRefresh() {
        swipeLayout?.isRefreshing = true
        getWeather()
    }

    fun getWeather() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {
                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
            } else {
                checkLocationPermission()
            }
        } else {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        }
    }

    @Subscribe
    fun onGetWeatherFinish(event: GetWeatherFinishEvent) {
        weatherListView?.adapter = WeatherRecyclerViewAdapter(event.list)
        swipeLayout?.isRefreshing = false
        Log.d(javaClass.name, "weather draw completed")
    }
}
