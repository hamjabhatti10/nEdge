package com.app.nEdge.source.remote.retrofit

import com.app.nEdge.googleMapHelper.DirectionObject
import com.app.nEdge.models.GoogleAuthModel
import com.app.nEdge.models.GoogleResponseModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url


interface ApiResponse {
    @GET
    fun getDirectionRoutes(@Url url: String): Observable<DirectionObject>

    @POST("token")
    fun getGoogleToken(@Body authModel: GoogleAuthModel): Observable<Response<GoogleResponseModel>>


}

