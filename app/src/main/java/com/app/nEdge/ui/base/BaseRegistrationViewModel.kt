package com.app.nEdge.ui.base

import androidx.lifecycle.MutableLiveData
import com.app.nEdge.CommonKeys.FireBaseCommonKeys
import com.app.nEdge.application.nEdgeApplication
import com.app.nEdge.models.ApiResponse
import com.app.nEdge.models.User
import com.app.nEdge.utils.serializeToMap
import com.google.android.gms.tasks.Task

open class BaseRegistrationViewModel : BaseViewModel() {
    var registrationListeners: MutableLiveData<ApiResponse> = MutableLiveData()

    fun addUserDataToFirebase(user: User) {
        registrationListeners.value = ApiResponse.loading()
        val userMap: Map<String, Any> = user.serializeToMap()
        val docRef = nEdgeApplication.getFirebaseFirestore().collection(FireBaseCommonKeys.KEY_USER)
        val docId = docRef.id
        docRef.document().set(userMap).addOnCompleteListener { task1: Task<Void?>? ->
            if (task1?.isSuccessful == true) {
                registrationListeners.value = ApiResponse.success(docId)
            } else {
                registrationListeners.value = ApiResponse.error(null)
            }
        }
    }

    fun updateUserDataToFirebase(user: User, userNode: String) {
        registrationListeners.value = ApiResponse.loading()
        val userMap: Map<String, Any> = user.serializeToMap()
        val docRef = nEdgeApplication.getFirebaseFirestore().collection(FireBaseCommonKeys.KEY_USER)
        docRef.document(userNode).update(userMap).addOnCompleteListener { task1: Task<Void?>? ->
            if (task1?.isSuccessful == true) {
                registrationListeners.value = ApiResponse.success(null)
            } else {
                registrationListeners.value = ApiResponse.error(null)
            }
        }
    }

}