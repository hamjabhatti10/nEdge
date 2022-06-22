package com.app.nEdge.ui.fragments.registerFormFragment

import com.app.nEdge.customData.enums.UserType
import com.app.nEdge.models.UserBuilder
import com.app.nEdge.ui.base.BaseViewModel

class RegisterFormViewModel : BaseViewModel() {
    var educationType: String? = ""
    val selectedDaysArray = ArrayList<String>()
    var userBuilder = UserBuilder()
    var isStartTimeSelected: Boolean = true
    var userType: String = UserType.Student.toString()
}