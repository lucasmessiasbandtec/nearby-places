package com.example.locaisproximos

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.locaisproximos.Common.Common
import com.example.locaisproximos.Model.PlaceDetail
import com.example.locaisproximos.remote.IGoogleAPIService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_view_place.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class ViewPlace : AppCompatActivity() {

    internal lateinit var mService:IGoogleAPIService
    var mPlace:PlaceDetail?=null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_place)

        //Init service
        mService = Common.googleApiService

        //Set empty for all text view
        place_name.text=""
        place_address.text=""
        place_open_hour.text=""

        btn_add_place.setOnClickListener {
            //Botão para adicionar ao itinerário

        }

        //Load photo of place
        if(Common.currentResult!!.photos != null && Common.currentResult!!.photos!!.size > 0)
            Picasso.with(this)
                .load(getPhotoOfPlace(Common.currentResult!!.photos!![0].photo_reference!!, 1000))
                .into(photo)

        //Load rating
        if(Common.currentResult!!.rating != null)
            rating_bar.rating = Common.currentResult!!.rating.toFloat()
        else
            rating_bar.visibility = View.GONE

        //Load open hours
        if(Common.currentResult!!.opening_hours != null)
            place_open_hour.text="Open Now : "+Common.currentResult!!.opening_hours!!.open_now
        else
            place_open_hour.visibility = View.GONE

        //Use service to fetch Address and Name
        mService.getDetailPlace(getDetailPlaceUrl(Common.currentResult!!.place_id!!))
            .enqueue(object : Callback<PlaceDetail> {
                override fun onResponse(call: Call<PlaceDetail>, response: Response<PlaceDetail>) {
                    mPlace = response!!.body()

                    place_address.text = mPlace!!.result!!.formatted_address
                    place_name.text = mPlace!!.result!!.name
                }

                override fun onFailure(call: Call<PlaceDetail>, t: Throwable) {
                    Toast.makeText(baseContext,""+t!!.message,Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun getDetailPlaceUrl(place_id: String): String {

        val url = StringBuilder("https://maps.googleapis.com/maps/api/place/details/json")
        url.append("?place_id=$place_id")
        url.append("&key=AIzaSyBk7kR9-NKJ2XeaIp9-BTEB-_ecgCO4pcU")
        return url.toString()

    }

    private fun getPhotoOfPlace(photo_reference: String, maxWidth: Int): String {

        val url = StringBuilder("https://maps.googleapis.com/maps/api/place/photo")
        url.append("?maxwidth=$maxWidth")
        url.append("&photoreference=$photo_reference")
        url.append("&key=AIzaSyBk7kR9-NKJ2XeaIp9-BTEB-_ecgCO4pcU")
        return url.toString()

    }
}