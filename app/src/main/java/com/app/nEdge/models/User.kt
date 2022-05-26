package com.app.nEdge.models

import com.google.firebase.firestore.PropertyName

data class User(
    @PropertyName("userId")
    var userId: String?,
    @PropertyName("name")
    var name: String?,
    @PropertyName("email")
    var email: String?,
    @PropertyName("phoneNumber")
    var phoneNumber: String?,
    @PropertyName("userType")
    var userType: String?,
    @PropertyName("educationType")
    var educationType: String?
)
