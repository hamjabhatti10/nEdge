package com.app.nEdge.ui.base

import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.nEdge.CommonKeys.FireBaseCommonKeys
import com.app.nEdge.application.nEdgeApplication
import com.app.nEdge.models.ApiResponse
import com.app.nEdge.source.remote.rxjava.DisposableManager


abstract class BaseViewModel : ViewModel() {
    var usersData: MutableLiveData<ApiResponse> = MutableLiveData()
    var subjectsLiveData: MutableLiveData<ApiResponse> = MutableLiveData()

    fun setServerName(textView: TextView) {

    }

    fun cancelServerRequest() {
        DisposableManager.dispose()
    }

    fun getUserData(userId: String) {
        usersData.value = ApiResponse.loading()
        nEdgeApplication.getFirebaseFirestore().collection(FireBaseCommonKeys.KEY_USER)
            .whereEqualTo(FireBaseCommonKeys.KEY_USER_ID, userId)
            .get().addOnSuccessListener { snapShot ->
                if (snapShot != null && !snapShot.isEmpty) {
                    val document = snapShot.documents[0]
                    usersData.value = ApiResponse.success(document)
                } else {
                    usersData.value = ApiResponse.error(null)
                }

            }
    }

    fun getSubjectsData() {
        subjectsLiveData.value = ApiResponse.loading()
        nEdgeApplication.getFirebaseFirestore().collection(FireBaseCommonKeys.KEY_SUBJECTS)
            .get().addOnSuccessListener { snapShot ->
                if (snapShot != null && !snapShot.isEmpty) {
                    val document = snapShot.documents[0]
                    subjectsLiveData.value = ApiResponse.success(document)
                } else {
                    subjectsLiveData.value = ApiResponse.error(null)
                }

            }
    }
}