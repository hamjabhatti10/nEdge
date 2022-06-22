package com.app.nEdge.ui.activities.RegistrationActivity

import com.app.nEdge.customData.enums.UserType
import com.app.nEdge.models.UserBuilder

interface RegistrationImp {
    fun onUserType(userType: UserType)
    fun onGettingUserInfo(userBuilder: UserBuilder)
}