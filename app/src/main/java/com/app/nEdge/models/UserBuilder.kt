package com.app.nEdge.models

import java.io.Serializable

class UserBuilder : Serializable {
    var userId: String? = null
    var name: String? = null
    var email: String? = null
    var phoneNumber: String? = null
    var userType: String? = null
    var educationType: String? = null
    fun userId(userId: String?) =
        apply { this.userId = userId }

    fun name(name: String?) =
        apply { this.name = userId }

    fun email(email: String?) =
        apply { this.email = email }

    fun phoneNumber(phoneNumber: String?) =
        apply { this.phoneNumber = phoneNumber }

    fun userType(userType: String?) =
        apply {
            this.userType = userType
        }

    fun educationType(educationType: String?) =
        apply {
            this.educationType = educationType
        }

    companion object {
        fun build(builder: UserBuilder) = User(
            builder.userId,
            builder.name,
            builder.email,
            builder.phoneNumber,
            builder.userType,
            builder.educationType
        )
    }
}