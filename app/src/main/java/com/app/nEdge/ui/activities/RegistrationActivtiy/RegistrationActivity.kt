package com.app.nEdge.ui.activities.RegistrationActivtiy

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.app.nEdge.CommonKeys.CommonKeys
import com.app.nEdge.R
import com.app.nEdge.application.nEdgeApplication
import com.app.nEdge.customData.enums.EducationType
import com.app.nEdge.customData.enums.NetworkStatus
import com.app.nEdge.customData.enums.UserType
import com.app.nEdge.databinding.ActivityRegistrationBinding
import com.app.nEdge.models.UserBuilder
import com.app.nEdge.source.local.prefrance.PrefUtils
import com.app.nEdge.ui.activities.expertRegistrationActivity.ExpertRegistrationActivity
import com.app.nEdge.ui.activities.studentMainActivity.StudentMainActivity
import com.app.nEdge.ui.base.BaseActivity
import com.app.nEdge.ui.customWidgets.ProgressHUD
import com.app.nEdge.utils.ActivityUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class RegistrationActivity : BaseActivity() {
    private lateinit var mBinding: ActivityRegistrationBinding
    private lateinit var viewModel: RegisterActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[RegisterActivityViewModel::class.java]
        setListeners()
        setObserver()
        resetLevelSpinner()
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
                        this, CommonKeys.KEY_USER_TYPE,
                        viewModel.userType.toString()
                    )
                    if (viewModel.userType == UserType.Student)
                        ActivityUtils.startNewActivity(this, StudentMainActivity::class.java)
                    else {
                        val bundle = Bundle().apply {
                            putSerializable(CommonKeys.KEY_DATA, createUserModel())
                        }
                        ActivityUtils.startNewActivity(this, ExpertRegistrationActivity::class.java)
                    }
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
        mBinding.spinnerUserType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (mBinding.spinnerUserType.selectedItemPosition != 0 &&
                        mBinding.spinnerUserType.selectedItem.toString() != viewModel.userType.toString()
                    ) {
                        when (p2) {
                            1 -> {
                                viewModel.userType = UserType.Student
                            }
                            2 -> {
                                viewModel.userType = UserType.Expert
                            }
                        }

                        resetLevelSpinner()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        mBinding.spinnerLevelOptions.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (mBinding.spinnerLevelOptions.selectedItemPosition != 0) {
                        viewModel.educationType = EducationType.valueOf(
                            mBinding.spinnerLevelOptions.selectedItem.toString().replace("/", "_")
                        )

                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }

    }

    private fun resetLevelSpinner() {
        val levelArray: Array<String> = resources.getStringArray(R.array.levelOptios)
        val arrayWithOptions = ArrayList<String>()
        arrayWithOptions.addAll(levelArray)
        arrayWithOptions.add(
            0,
            if (viewModel.userType == UserType.Student) getString(R.string.iWantToLearn) else getString(
                R.string.iAmExpertAt
            )
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayWithOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBinding.spinnerLevelOptions.adapter = adapter
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
        } else if (mBinding.spinnerUserType.selectedItemPosition == 0) {
            Toast.makeText(this, getString(R.string.userTypeValidation), Toast.LENGTH_SHORT).show()
        } else if (mBinding.spinnerLevelOptions.selectedItemPosition == 0) {
            Toast.makeText(this, getString(R.string.levelValidation), Toast.LENGTH_SHORT).show()
        } else {
            registerUser(mBinding.editTextPassword.text.toString(), createUserModel())
        }
    }

    private fun registerUser(password: String, user: UserBuilder) {
        user.email?.let {
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
        nEdgeApplication.getFirebaseAuth().currentUser?.uid
        userBuilder.name = mBinding.editTextName.text.toString()
        userBuilder.email = mBinding.editTextEmail.text.toString()
        userBuilder.phoneNumber = mBinding.editTextPhoneNumber.text.toString()
        userBuilder.userType = viewModel.userType.toString()
        userBuilder.educationType = viewModel.educationType.toString()
        return userBuilder
    }


}