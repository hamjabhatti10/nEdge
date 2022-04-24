package com.app.nEdge.DataManager

import com.app.nEdge.DataManager.DummayDataManager.Companion.getResponseDummyData
import com.app.nEdge.googleMapHelper.DirectionObject
import com.app.nEdge.models.GoogleAuthModel
import com.app.nEdge.models.GoogleResponseModel
import com.app.nEdge.models.ResponseMain
import com.app.nEdge.source.remote.retrofit.ApiService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


object RemoteDataManager : BaseDataManager() {

    private lateinit var data: GoogleAuthModel

    // Remote calls
    override fun getDirectionRoutes(url: String): Observable<DirectionObject> {
        return ApiService.getDirectionResponse().getDirectionRoutes(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getGoogleAccessToken(serverAuthCode: String): Observable<Response<GoogleResponseModel>> {

//        when {
//            BuildConfig.FLAVOR.equals(Constants.STAGING) -> {
//                data = GoogleAuthModel(
//                    serverAuthCode,
//                    "authorization_code",
//                    nEdgeApplication.getContext()
//                        .getString(R.string.google_client_id_staging),
//                    nEdgeApplication.getContext()
//                        .getString(R.string.google_client_secret_staging),
//                    nEdgeApplication.getContext()
//                        .getString(R.string.google_client_redirect_uri_staging)
//                )
//            }
//            BuildConfig.FLAVOR.equals(Constants.DEVELOPMENT) -> {
//                data = GoogleAuthModel(
//                    serverAuthCode,
//                    "authorization_code",
//                    nEdgeApplication.getContext()
//                        .getString(R.string.google_client_id_development),
//                    nEdgeApplication.getContext()
//                        .getString(R.string.google_client_secret_development),
//                    nEdgeApplication.getContext()
//                        .getString(R.string.google_client_redirect_uri_development)
//                )
//            }
//            BuildConfig.FLAVOR.equals(Constants.ACCEPTANCE) -> {
//                data = GoogleAuthModel(
//                    serverAuthCode,
//                    "authorization_code",
//                    nEdgeApplication.getContext()
//                        .getString(R.string.google_client_id_acceptance),
//                    nEdgeApplication.getContext()
//                        .getString(R.string.google_client_secret_acceptance),
//                    nEdgeApplication.getContext()
//                        .getString(R.string.google_client_redirect_uri_acceptance)
//                )
//            }
//            else -> {
//                data = GoogleAuthModel(
//                    serverAuthCode,
//                    "authorization_code",
//                    nEdgeApplication.getContext()
//                        .getString(R.string.google_client_id_production),
//                    nEdgeApplication.getContext()
//                        .getString(R.string.google_client_secret_production),
//                    nEdgeApplication.getContext()
//                        .getString(R.string.google_client_redirect_uri_production)
//                )
//            }
//        }
        return ApiService.getGoogleResponse().getGoogleToken(data).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * This is example code for success dummy response now you don't need to pass data directly from doOnSubscribe.
     * After that all apis are called same as this. */
    override fun sendOTP(email: String): Observable<Response<ResponseMain>> {
        return Observable.create { observer ->
            // observer.onError(Throwable("This is dummy thorwable if you want to test failed case."))
            observer.onNext(getResponseDummyData())
            observer.onComplete()
        }
    }

}