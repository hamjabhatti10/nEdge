package com.app.nEdge.ui.activities.loginActivity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.app.nEdge.CommonKeys.CommonKeys
import com.app.nEdge.R
import com.app.nEdge.application.nEdgeApplication
import com.app.nEdge.customData.enums.NetworkStatus
import com.app.nEdge.databinding.ActivityLoginBinding
import com.app.nEdge.models.User
import com.app.nEdge.models.UserBuilder
import com.app.nEdge.source.local.prefrance.PrefUtils
import com.app.nEdge.ui.activities.RegistrationActivity.RegistrationActivity
import com.app.nEdge.ui.activities.SignUpActivity.SignUpActivity
import com.app.nEdge.ui.base.BaseActivity
import com.app.nEdge.ui.customWidgets.ProgressHUD
import com.app.nEdge.utils.ActivityUtils
import com.app.nEdge.utils.toDataClass
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentSnapshot

class LoginActivity : BaseActivity() {
    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[LoginViewModel::class.java]
        checkSession()
        setListeners()
        setObserver()
    }

    private fun checkSession() {
        if(nEdgeApplication.getFirebaseAuth().currentUser!=null) {
            if (PrefUtils.getString(this, CommonKeys.KEY_USER_TYPE) == null) {
                nEdgeApplication.getFirebaseAuth().currentUser?.uid?.let {
                    viewModel.getUserData(
                        it
                    )
                }
            } else
                decideMainActivity()
        }
    }

    private fun setObserver() {
        viewModel.usersData.observe(this) {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    ProgressHUD.show(this)
                }
                NetworkStatus.SUCCESS -> {
                    ProgressHUD.removeView()
                    val userMap = (it.t as DocumentSnapshot).data
                    val user = userMap?.toDataClass<User>()
                    if (user?.userType == null) {
                        val bundle = Bundle().apply {
                            putSerializable(CommonKeys.KEY_DATA, createUserModel(user))
                            putString(CommonKeys.KEY_USER_NODE, (it.t as DocumentSnapshot).id)
                        }
                        ActivityUtils.startNewActivity(
                            this,
                            RegistrationActivity::class.java,
                            bundle
                        )
                        finish()
                    } else {
                        PrefUtils.setString(this, CommonKeys.KEY_USER_TYPE, user.userType)
                        ProgressHUD.removeView()
                        decideMainActivity()
                    }


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
        mBinding.buttonLogin.setOnClickListener { view: View? -> validation() }
        mBinding.textViewRegistration.setOnClickListener { view: View? ->
            ActivityUtils.startNewActivity(this, SignUpActivity::class.java)
        }
    }

    private fun validation() {
        if (TextUtils.isEmpty(mBinding.editTextEmailPhone.text.toString())) {
            mBinding.editTextEmailPhone.error = getString(R.string.require_field)
        } else if (TextUtils.isEmpty(mBinding.editTextPassword.text.toString())) {
            mBinding.editTextPassword.error = getString(R.string.require_field)
        } else {
            signInWithFirebase()
        }
    }

    private fun signInWithFirebase() {
        val email = mBinding.editTextEmailPhone.text.toString()
        val password = mBinding.editTextPassword.text.toString()
        ProgressHUD.show(this)
        nEdgeApplication.getFirebaseAuth().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    nEdgeApplication.getFirebaseAuth().currentUser?.uid?.let {
                        viewModel.getUserData(
                            it
                        )
                    }

                } else {
                    ProgressHUD.removeView()
                    Toast.makeText(
                        this@LoginActivity,
                        R.string.someThingWentWrong,
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }

    private fun createUserModel(user: User?): UserBuilder {
        val userBuilder = UserBuilder()
        userBuilder.userId = nEdgeApplication.getFirebaseAuth().currentUser?.uid
        userBuilder.name = user?.name
        userBuilder.email = user?.email
        userBuilder.phoneNumber = user?.phoneNumber
        return userBuilder
    }

}