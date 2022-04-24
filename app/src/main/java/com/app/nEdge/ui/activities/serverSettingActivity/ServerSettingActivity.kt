package com.app.nEdge.ui.activities.serverSettingActivity

import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import com.app.nEdge.R
import com.app.nEdge.constant.Constants
import com.app.nEdge.databinding.ActivityServerSettingBinding
import com.app.nEdge.helper.RemoteConfigrations.RemoteConfigSharePrefrence
import com.app.nEdge.ui.base.BaseActivity
import com.app.nEdge.utils.Utilities

class ServerSettingActivity : BaseActivity() {
    private var remoteConfigSharedPreferences: RemoteConfigSharePrefrence? = null
    private lateinit var mBinding: ActivityServerSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityServerSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setUpActionBar(mBinding.activityToolbar.toolbar, "", false, isShowHome = true)
        mBinding.activityToolbar.toolbarTitle.visibility = View.VISIBLE
        mBinding.activityToolbar.toolbarTitle.text = resources.getString(R.string.server_setting)
        mBinding.activityToolbar.toolbar.setNavigationOnClickListener {
            this.finish()
        }
        initializeComponents()
        crashingImplementation()
        getAndSetBuildVersion()
        setUrlText()
        setMinimumVersion()
    }

    private fun setMinimumVersion() {
        val appInfo: ApplicationInfo =
            packageManager.getApplicationInfo(this.applicationInfo.packageName, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBinding.textMinimumVersion.text =
                getString(R.string.minimun_version).plus(" = ")
                    .plus(appInfo.minSdkVersion.toString()).plus(" (")
                    .plus(Utilities.getAndroidVersion(appInfo.minSdkVersion)).plus(")")
        } else {
            mBinding.textMinimumVersion.text =
                getString(R.string.minimun_version).plus(" = 16 (4.1.0)")
        }
    }

    private fun crashingImplementation() {

        mBinding.buttonCrash.setOnClickListener {
            Constants.STAGING.toInt()
        }

    }

    private fun getAndSetBuildVersion() {
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(packageName, 0).longVersionCode
        } else {
            packageManager.getPackageInfo(packageName, 0).versionCode
        }
        val versionName = packageManager
            .getPackageInfo(packageName, 0).versionName
        val buildVersion =
            "Build Number:$versionCode\nVersion Name :$versionName"
        mBinding.textViewBuildVersion.text = buildVersion
    }

    private fun setUrlText() {
        //mBinding.textServerName.text = BuildConfig.FLAVOR.toUpperCase()
        mBinding.textServerName.visibility = View.VISIBLE
    }

    private fun initializeComponents() {
        remoteConfigSharedPreferences = RemoteConfigSharePrefrence(this)
    }
}
