package com.app.nEdge.constant


object Constants {

    const val STAGING = "staging"
    const val PRODUCTION = "production"
    const val ACCEPTANCE = "acceptance"
    const val DEVELOPMENT = "development"
    const val STAGING_SERVER_URL = "https://staging.base.com/api/v1/"
    const val PRODUCTION_SERVER_URL = "https://base.com/api/v1/"
    const val ACCEPTANCE_SERVER_URL = "https://acceptance.base.com/api/v1/"
    const val DEVELOPMENT_SERVER_URL = "https://development.base.com/api/v1/"

    val NO_INTERNET_TITLE = "NO INTERNET"
    val NO_INTERNET_MESSAGE = "Please connect to a stable internet"
    val LOCATION_PERMISSION_TITLE = "Location permission denied!"
    val LOCATION_PERMISSION_MESSAGE = "Location permission requires to continue"
    val DIRECTION_API = "https://maps.googleapis.com/maps/api/directions/json?origin="


    const val LOCATION_UPDATE_TIME = 0 //in miliSeconds like miliSeconds X seconds X mint X hours
    const val LOCATION_UPDATE_DISTANCE = 20 //in meter

    const val PREF_NAME = "_Data_Store_Pref_"

    const val STRIPE_KEY = "abc"
    const val SPLASHDELAY = 1000 * 3 * 1 // in milliSeconds
    const val KEY_REQUEST_PLACE: Int = 1000
    const val GOOGLE_REQUEST_CODE: Int = 1001
    const val EARTHRADIUS = 6366198.0

    const val DEFAULT_DATE_FORMATE = "dd MMMM,yyyy"

    const val FCM_ANDROID_TOPIC = "Android_Users"

    const val CPP_LIBRARY_NAME = "keys"

    const val IMAGE_PICKER_REQUEST_CODE = 1234

    const val CAMERA_PICKER_REQUEST_CODE = 12345

    const val VIDEO_GALLERY_PICKER_REQUEST_CODE = 2234

    const val VIDEO_CAMERA_PICKER_REQUEST_CODE = 22345
}
