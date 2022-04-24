package com.app.nEdge.DataManager

import com.app.nEdge.googleMapHelper.DirectionObject
import com.app.nEdge.models.GoogleResponseModel
import com.app.nEdge.models.ResponseMain
import io.reactivex.Observable
import retrofit2.Response


object LocalDataManager : BaseDataManager() {
    override fun getDirectionRoutes(url: String): Observable<DirectionObject> {
        TODO("Not yet implemented")
    }

    override fun getGoogleAccessToken(serverAuthCode: String): Observable<Response<GoogleResponseModel>> {
        TODO("Not yet implemented")
    }

    override fun sendOTP(email: String): Observable<Response<ResponseMain>> {
        TODO("Not yet implemented")
    }

}