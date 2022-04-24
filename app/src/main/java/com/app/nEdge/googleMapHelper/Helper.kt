package com.app.nEdge.googleMapHelper

import android.content.Context
import com.app.nEdge.R
import com.app.nEdge.constant.Constants.DIRECTION_API
import com.app.nEdge.utils.Log

class Helper(var pContext: Context) {
    companion object {
        val MY_SOCKET_TIMEOUT_MS = 5000
        private lateinit var _Context: Context
    }

    init {
        _Context = pContext
    }

    fun getURL(clat: Double, clng: Double, dlat: Double, dlng: Double): String {
        Log.d(
            "Helper",
            "getURL: " + DIRECTION_API + clat + "," + clng + "&destination=" + dlat + "," + dlng + "&key=" + _Context.getString(
                R.string.google_maps_key
            )
        )
        return DIRECTION_API + clat + "," + clng + "&destination=" + dlat + "," + dlng + "&key=" + _Context.getString(
            R.string.google_maps_key
        )
    }


}
