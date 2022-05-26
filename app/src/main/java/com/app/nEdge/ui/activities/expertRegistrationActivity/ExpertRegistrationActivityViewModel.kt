package com.app.nEdge.ui.activities.expertRegistrationActivity

import com.app.nEdge.models.UserBuilder
import com.app.nEdge.ui.base.BaseViewModel

class ExpertRegistrationActivityViewModel : BaseViewModel() {
    var isStartTimeSelected = true
    var userBuilder = UserBuilder()
    var selectedDaysArray = ArrayList<String>()
}