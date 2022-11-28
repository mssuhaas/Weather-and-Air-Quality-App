package com.example.weatherapplication

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.weatherapplication.MainActivity2.SharedData.aq
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import java.lang.reflect.Executable
import java.net.URL

class MainActivity2 : AppCompatActivity() {
    private val key = "7L1DLRKCUBWFS6M3"
    private val channel = "1957363"
    var latit = "17.385"
    var longi = "78.486"
    val apiKey = "33215ee997a870d8d81b4d7ca4d1944e"
    public object SharedData{
        var aq: Int? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main2)
        weatherTask2().execute()
        getAirQuality()

    }

    inner class weatherTask2() : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.thingspeak.com/channels/$channel/feeds.json?api_key=$key&results=1").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("channel")
                val data = jsonObj.getJSONArray("feeds").getJSONObject(0)
                val tempS = "Temp: " + (data.getLong("field1")).toString() + "°C"
                val humidityS ="Hum : " + (data.getInt("field2")).toString() + "%"
//                aq = data.getInt("field3")
                val ppmS = "Air Quality = " + (data.getLong("field3")).toString() + "ppm"
                findViewById<TextView>(R.id.textView7).text =  ppmS
                findViewById<TextView>(R.id.temperatureA2).text = tempS
                findViewById<TextView>(R.id.humidityA2).text = humidityS

//                val temper = findViewById<TextView>(R.id.TextView2)

            } catch (e: Exception) {
                println(e)
            }
        }
    }


//
//result > coord
//       > list []
//         0>main>aqi
//         1>components>pm2_5

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getAirQuality() {

        //Hyderabad
        longi = "17.3850"
        latit = "78.4867"

        var url: URL = URL("http://api.openweathermap.org/data/2.5/air_pollution?lat=$latit&lon=$longi&appid=$apiKey")
        var response: String?
        try {
            val response = url.readText(Charsets.UTF_8)
            val jsonOb = JSONObject(response)
            val main = jsonOb.getJSONArray("list").getJSONObject(0)
            val aqi = main.getInt("aqi")
            val main2 = jsonOb.getJSONArray("list").getJSONObject(1)
            val pm10 = main2.getLong("pm10")
            findViewById<TextView>(R.id.hyderabadAQVal).text = pm10.toString()


//            var comp =
//            val p2_5 = (comp.getInt("pm2_5")).toString() // Fine Particles
//            val p10 = comp.getInt("pm10")  //Coarse Particles

            val bg = resources.getDrawable(when(aqi){
                1 -> R.drawable.aqi1
                2 -> R.drawable.aqi2
                3 -> R.drawable.aqi3
                4 -> R.drawable.aqi4
                else -> R.drawable.aqi5
            })
            findViewById<RelativeLayout>(R.id.HyderabadView).background = bg
        }
        catch (e: Exception){
            println(e)
        }


        //New Delhi
        var longi2 = "28.6139"
        var latit2 = "77.2090"
        var apiKey = "a46cff7bcb7fd591933ccad2c4e70218"


        var url2 = URL("http://api.openweathermap.org/data/2.5/air_pollution?lat=$latit2&lon=$longi2&appid=$apiKey")
        try {
            val response2:String?
            response2 = url2.readText(Charsets.UTF_8)
            val jsonOb = JSONObject(response2)
            val list = jsonOb.getJSONArray("list").getJSONObject(0)
            val main = list.getJSONObject("main")
            val aqi = main.getInt("aqi")
            var comp = JSONArray("list").getJSONObject(1)
            val p2_52 = (comp.getInt("pm2_5")).toString()
//            val p10 = comp.getInt("pm10")

            findViewById<TextView>(R.id.delhiAQI).text = p2_52
            val bg = resources.getDrawable(when(aqi){
                1 -> R.drawable.aqi1
                2 -> R.drawable.aqi2
                3 -> R.drawable.aqi3
                4 -> R.drawable.aqi4
                else -> R.drawable.aqi5
            })
            findViewById<RelativeLayout>(R.id.DelhiView).background = bg

        }
        catch (e: Exception){
            println(e)
        }



        //Mumbai
        var longi3 = "19.0760"
        var latit3 = "72.8777"
        apiKey="a46cff7bcb7fd591933ccad2c4e70218"


    var url3 = URL("http://api.openweathermap.org/data/2.5/air_pollution?lat=$latit3&lon=$longi3&appid=$apiKey")
    try {
        val response3:String?
        response3 = url3.readText(Charsets.UTF_8)
        val jsonOb = JSONObject(response3)
        val list = jsonOb.getJSONArray("list").getJSONObject(0)
        val main = list.getJSONObject("main")
        val aqi = main.getInt("aqi")
        var list2 = jsonOb.getJSONArray("list").getJSONObject(1)
        var comp = list2.getJSONObject("components")

        val p2_53 = (comp.getInt("pm2_5")).toString()
//            val p10 = comp.getInt("pm10")

        findViewById<TextView>(R.id.MumbaiAQI).text = p2_53
        val bg = resources.getDrawable(when(aqi){
            1 -> R.drawable.aqi1
            2 -> R.drawable.aqi2
            3 -> R.drawable.aqi3
            4 -> R.drawable.aqi4
            else -> R.drawable.aqi5
        })
        findViewById<RelativeLayout>(R.id.MumbaiView).background = bg

    }
    catch (e: Exception){
        println(e)
    }

}




}

//Mumbai
//longi = "19.0760"
//latit = "72.8777"

//Kolkata
//longi = "22.5726"
//latit = "88.3639"

//chennai
//longi = "13.0827"
//latit = "80.2707"

//bangalore
//longi = "12.9716"

//latit = "77.5946"

