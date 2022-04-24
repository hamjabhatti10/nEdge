package com.app.nEdge.DataManager

import com.app.nEdge.googleMapHelper.DirectionObject
import com.app.nEdge.models.GoogleResponseModel
import com.app.nEdge.models.ResponseMain
import io.reactivex.Observable
import retrofit2.Response

interface DataManagerImp {
    //Remote calls
    fun getDirectionRoutes(url: String): Observable<DirectionObject>

    fun getGoogleAccessToken(serverAuthCode: String): Observable<Response<GoogleResponseModel>>

    fun sendOTP(email: String): Observable<Response<ResponseMain>>

}