package com.app.nEdge.ui.activities.SignUpActivity

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.app.nEdge.CommonKeys.CommonKeys
import com.app.nEdge.R
import com.app.nEdge.application.nEdgeApplication
import com.app.nEdge.customData.enums.NetworkStatus
import com.app.nEdge.databinding.ActivitySignupBinding
import com.app.nEdge.models.UserBuilder
import com.app.nEdge.ui.activities.RegistrationActivity.RegistrationActivity
import com.app.nEdge.ui.base.BaseActivity
import com.app.nEdge.ui.customWidgets.ProgressHUD
import com.app.nEdge.utils.ActivityUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class SignUpActivity : BaseActivity() {
    private lateinit var mBinding: ActivitySignupBinding
    private lateinit var viewModel: SignUpActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[SignUpActivityViewModel::class.java]
        setListeners()
        setObserver()
    }

    private fun setObserver() {
        viewModel.registrationListeners.observe(this) {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    ProgressHUD.show(this)
                }
                NetworkStatus.SUCCESS -> {
                    ProgressHUD.removeView()
                    val bundle = Bundle().apply {
                        putSerializable(CommonKeys.KEY_DATA, createUserModel())
                        putString(CommonKeys.KEY_USER_NODE, it.t as String)
                    }
                    ActivityUtils.startNewActivity(this, RegistrationActivity::class.java, bundle)
                    finish()
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

    private fun setListeners() {
        mBinding.buttonRegister.setOnClickListener { view: View? -> validation() }
    }



    private fun validation() {
        if (TextUtils.isEmpty(mBinding.editTextName.text.toString())) {
            mBinding.editTextName.error = getString(R.string.require_field)
        } else if (TextUtils.isEmpty(mBinding.editTextEmail.text.toString()) ||
            !Patterns.EMAIL_ADDRESS.matcher(mBinding.editTextEmail.text.toString()).matches()
        ) {
            mBinding.editTextEmail.error = getString(R.string.invalid_email)
        } else if (TextUtils.isEmpty(mBinding.editTextPassword.text.toString())) {
            mBinding.editTextPassword.error = getString(R.string.require_field)
        } else if (TextUtils.isEmpty(mBinding.editTextPhoneNumber.text.toString())) {
            mBinding.editTextPhoneNumber.error = getString(R.string.require_field)
        } else if (mBinding.editTextPassword.text.toString().length < 6) {
            mBinding.editTextPassword.error = getString(R.string.passwordLengthValidation)
        } else {
            registerUser(mBinding.editTextPassword.text.toString(), createUserModel())
        }
    }

    private fun registerUser(password: String, user: UserBuilder) {
        user.email?.let {
            ProgressHUD.show(this)
            nEdgeApplication.getFirebaseAuth().createUserWithEmailAndPassword(it, password)
                .addOnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {

                            viewModel.addUserDataToFirebase(UserBuilder.build(createUserModel()))


                    } else {
                        Toast.makeText(this, R.string.someThingWentWrong, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun createUserModel(): UserBuilder {
        val userBuilder = UserBuilder()
        userBuilder.userId = nEdgeApplication.getFirebaseAuth().currentUser?.uid
        userBuilder.name = mBinding.editTextName.text.toString()
        userBuilder.email = mBinding.editTextEmail.text.toString()
        userBuilder.phoneNumber = mBinding.textCountryCode.text.toString()
            .plus(mBinding.editTextPhoneNumber.text.toString())
        return userBuilder
    }


}