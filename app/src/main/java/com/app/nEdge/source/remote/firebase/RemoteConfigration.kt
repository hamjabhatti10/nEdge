package com.app.nEdge.source.remote.firebase

import android.content.Context
import com.app.nEdge.helper.RemoteConfigrations.RemoteConfigSharePrefrence
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class RemoteConfigration(private val context: Context) {
    private var isSettingDisplayed: Boolean? = null

    fun fetchRemoteValues() {
        val remoteConfigSharePrefrence = RemoteConfigSharePrefrence(context)
        val map = HashMap<String, Any>()
        map["isSettingDisplayed"] = false
        val firebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseRemoteConfig.setDefaultsAsync(map)
        val configSettings =
            FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0)
                .build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                isSettingDisplayed =
                    java.lang.Boolean.valueOf(firebaseRemoteConfig.getString("isSettingDisplayed"))
                remoteConfigSharePrefrence.setIsSettingFromRemote(isSettingDisplayed)
            } else {
                task.exception.toString()
            }
        }
    }

}
