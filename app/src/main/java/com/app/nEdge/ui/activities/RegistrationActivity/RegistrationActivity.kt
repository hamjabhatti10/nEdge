package com.app.nEdge.ui.activities.RegistrationActivity

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.nEdge.CommonKeys.CommonKeys
import com.app.nEdge.CommonKeys.CommonKeys.Companion.KEY_DATA
import com.app.nEdge.R
import com.app.nEdge.customData.enums.NetworkStatus
import com.app.nEdge.customData.enums.UserType
import com.app.nEdge.databinding.ActivityRegistrationBinding
import com.app.nEdge.models.UserBuilder
import com.app.nEdge.source.local.prefrance.PrefUtils
import com.app.nEdge.ui.base.BaseActivity
import com.app.nEdge.ui.customWidgets.ProgressHUD
import com.app.nEdge.ui.fragments.registerFormFragment.registerFormFragment
import com.app.nEdge.ui.fragments.usertypeFragment.userTypeFragment


class RegistrationActivity : BaseActivity(), RegistrationImp {
    private lateinit var mBinding: ActivityRegistrationBinding
    private lateinit var viewModel: RegistrationActivityViewModel
    private lateinit var mFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[RegistrationActivityViewModel::class.java]

        setListeners()
        getDataFromBundle()
        changeFragment(userTypeFragment.newInstance())
        setObserver()
    }

    override fun onUserType(userType: UserType) {
        viewModel.userBuilder.userType = userType.toString()
        changeFragment(registerFormFragment.newInstance(Bundle().apply {
            putString(
                KEY_DATA,
                userType.toString()
            )
        }))

    }

    override fun onGettingUserInfo(userBuilder: UserBuilder) {
        viewModel.userBuilder.educationType = userBuilder.educationType
        viewModel.userBuilder.ratePerLecture = userBuilder.ratePerLecture
        viewModel.userBuilder.availabiltyDays = userBuilder.availabiltyDays
        viewModel.userBuilder.availabiltyStartTime = userBuilder.availabiltyStartTime
        viewModel.userBuilder.availabiltyEndTime = userBuilder.availabiltyEndTime
        viewModel.userBuilder.ratePerMarkingAnswer = userBuilder.ratePerMarkingAnswer
        viewModel.updateUserDataToFirebase(
            UserBuilder.build(viewModel.userBuilder),
            viewModel.userNode
        )
    }

    private fun setListeners() {
        mBinding.layoutBack.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        when {
            getEntryCount() <= 1 -> {
                this.finish()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    private fun getDataFromBundle() {
        viewModel.userBuilder = intent.getBundleExtra(KEY_DATA)?.get(KEY_DATA) as UserBuilder
        viewModel.userNode =
            intent.getBundleExtra(KEY_DATA)?.getString(CommonKeys.KEY_USER_NODE) ?: ""
    }

    private fun changeFragment(fragment: Fragment) {
        mFragment = fragment
        replaceFragment(
            mBinding.registrationContainer.id,
            mFragment
        )
    }


    private fun setObserver() {
        viewModel.registrationListeners.observe(this) {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    ProgressHUD.show(this)
                }
                NetworkStatus.SUCCESS -> {
                    ProgressHUD.removeView()
                    PrefUtils.setString(
                        this@RegistrationActivity,
                        CommonKeys.KEY_USER_TYPE,
                        viewModel.userBuilder.userType
                    )
                    decideMainActivity()

                }
                NetworkStatus.ERROR -> {
                    ProgressHUD.removeView()
                    Toast.makeText(this, getString(R.string.someThingWentWrong), Toast.LENGTH_SHORT)
                        .show()
                }
                NetworkStatus.COMPLETED -> {

                }

            }
        }
    }


}