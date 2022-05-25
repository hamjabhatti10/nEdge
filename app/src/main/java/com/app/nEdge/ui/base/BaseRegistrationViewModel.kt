package com.app.nEdge.ui.base

import androidx.lifecycle.MutableLiveData
import com.app.nEdge.CommonKeys.FireBaseCommonKeys
import com.app.nEdge.application.nEdgeApplication
import com.app.nEdge.models.ApiResponse
import com.app.nEdge.models.User
import com.app.nEdge.models.UserBuilder
import com.app.nEdge.utils.serializeToMap
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

open class BaseRegistrationViewModel : BaseViewModel() {
    var registrationListeners: MutableLiveData<ApiResponse> = MutableLiveData()
    fun registerUser(password: String, user: UserBuilder) {
        user.email?.let {
            registrationListeners.value = ApiResponse.loading()
            nEdgeApplication.getFirebaseAuth().createUserWithEmailAndPassword(it, password)
                .addOnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        user.userId = nEdgeApplication.getFirebaseAuth().currentUser?.uid
                        addUserDataToFirebase(UserBuilder.build(user))
                    } else {
                        registrationListeners.value = ApiResponse.error(null)
                    }
                }
        }
    }

    private fun addUserDataToFirebase(user: User) {

        val userMap: Map<String, Any> = user.serializeToMap()
        val docRef = nEdgeApplication.getFirebaseFirestore().collection(FireBaseCommonKeys.KEY_USER)
        docRef.document().set(userMap).addOnCompleteListener { task1: Task<Void?>? ->
            if (task1?.isSuccessful == true) {
                registrationListeners.value = ApiResponse.success(null)
            } else {
                registrationListeners.value = ApiResponse.error(null)
            }
        }
    }
}