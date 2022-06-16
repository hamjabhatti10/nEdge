package com.app.nEdge.models

import java.io.Serializable

class UserBuilder : Serializable {
    var userId: String? = null
    var name: String? = null
    var email: String? = null
    var phoneNumber: String? = null
    var userType: String? = null
    var educationType: String? = null
    var availabiltyDays:ArrayList<String>?= null
    var availabiltyEndTime:String?= null
    var availabiltyStartTime:String?= null
    var ratePerLecture:String?= null
    var ratePerMarkingAnswer:String?= null
    fun availabiltyDays(availabiltyDays: ArrayList<String>?) =
        apply {
            this.availabiltyDays = availabiltyDays
        } fun availabiltyEndTime(availabiltyEndTime: String?) =
        apply {
            this.availabiltyEndTime = availabiltyEndTime
        } fun availabiltyStartTime(availabiltyStartTime: String?) =
        apply {
            this.availabiltyStartTime = availabiltyStartTime
        } fun ratePerLecture(ratePerLecture: String?) =
        apply {
            this.ratePerLecture = ratePerLecture
        } fun ratePerMarkingAnswer(ratePerMarkingAnswer: String?) =
        apply {
            this.ratePerMarkingAnswer = ratePerMarkingAnswer
        }
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
            builder.educationType,
            builder.availabiltyDays,
            builder.availabiltyEndTime,
            builder.availabiltyStartTime,
            builder.ratePerLecture,
            builder.ratePerMarkingAnswer
        )
    }
}