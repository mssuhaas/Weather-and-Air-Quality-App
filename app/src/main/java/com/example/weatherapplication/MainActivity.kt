package com.example.weatherapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

//Reference URL for API
//https://api.openweathermap.org/data/2.5/weather?lat=17.385&lon=78.486&appid=33215ee997a870d8d81b4d7ca4d1944e


class MainActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    //Basic data for API calls
    //For openweathermap API
    val latit = "17.385"
    val longi = "78.486"
    val apiKey = "33215ee997a870d8d81b4d7ca4d1944e"
    //For ThingSpeak API
    private val key = "7L1DLRKCUBWFS6M3"
    private val channel = "1957363"

    private fun getCurrentLocation() {
        // checking location permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE);
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // getting the last known or current location
                latitude = location.latitude
                longitude = location.longitude
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed on getting current location",
                    Toast.LENGTH_SHORT).show()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        weatherTask().execute()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



        val airQL = findViewById<RelativeLayout>(R.id.airQualityLayout)
        val value = airQL.setOnClickListener{
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }


    }
    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?lat=$latit&lon=$longi&appid=$apiKey").readText(Charsets.UTF_8)

            }catch (e: Exception){
                response = null

            }
            return response
        }
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val temp = (main.getLong("temp") - 273).toString() + "°C"
                val tempMin = (main.getLong("temp_min")-273).toString() + "°C"
                val tempMax = (main.getLong("temp_max")-273).toString() + "°C"
                val feelLike = (main.getLong("feels_like")-273).toString() + "°C"
                val tempData = " $tempMax / $tempMin Feels like $feelLike"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity") + "%"

                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description").capitalize()
                findViewById<TextView>(R.id.stateVal).text = weatherDescription
                findViewById<TextView>(R.id.temperature).text = temp
                findViewById<TextView>(R.id.sunRiseTime).text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                findViewById<TextView>(R.id.sunSetTime).text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
                findViewById<TextView>(R.id.windVal).text = windSpeed
                findViewById<TextView>(R.id.pressureVal).text = pressure
                findViewById<TextView>(R.id.humidityVal).text = humidity
                findViewById<TextView>(R.id.feelslikeView).text = tempData


                val bg = resources.getDrawable(
                    when (weatherDescription) {
                        "Sunny" -> R.drawable.sunny_gradient
                        "Cloudy", "Haze","Few clouds","Mist","Scattered clouds" -> R.drawable.cloudy_gradient
                        else -> R.drawable.rainy_gradient
                    }
                )
                findViewById<RelativeLayout>(R.id.mainLayout).background = bg

                val imgSete: ImageView = findViewById<ImageView>(R.id.imgView)
                if (weatherDescription == "Sunny") {
                    imgSete.setImageResource(R.drawable._02_sun)
                } else if (weatherDescription == "Cloudy"||weatherDescription=="Haze"||weatherDescription=="Mist"||weatherDescription=="Few clouds"||weatherDescription=="Scattered clouds") {
                    imgSete.setImageResource(R.drawable._03_cloud)
                }
                else{
                    imgSete.setImageResource(R.drawable._01_rain)
                }

            }catch (e: Exception) {
                println(e)
            }
        }
    }












}
