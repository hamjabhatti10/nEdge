package com.app.nEdge.source.remote.retrofit

import com.app.nEdge.R
import com.app.nEdge.application.nEdgeApplication
import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String
        get() = nEdgeApplication.getContext().getString(R.string.noInternet)
}