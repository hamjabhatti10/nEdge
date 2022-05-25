package com.app.nEdge.ui.activities.RegistrationActivtiy

import com.app.nEdge.customData.enums.EducationType
import com.app.nEdge.customData.enums.UserType
import com.app.nEdge.ui.base.BaseRegistrationViewModel

class RegisterActivityViewModel : BaseRegistrationViewModel() {
    var userType = UserType.Student
    var educationType = EducationType.School_College
}