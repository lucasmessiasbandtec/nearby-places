package com.example.locaisproximos.Common

import com.example.locaisproximos.Model.Results
import com.example.locaisproximos.remote.IGoogleAPIService
import com.example.locaisproximos.remote.RetrofitClient

object Common {

    private val GOOGLE_API_URL = "https://maps.googleapis.com/"

    var currentResult:Results?=null

    val googleApiService:IGoogleAPIService
        get()=RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)

}