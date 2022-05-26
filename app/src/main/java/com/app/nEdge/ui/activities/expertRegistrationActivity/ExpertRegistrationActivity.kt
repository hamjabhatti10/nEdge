package com.app.nEdge.ui.activities.expertRegistrationActivity

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.app.nEdge.CommonKeys.FireBaseCommonKeys
import com.app.nEdge.R
import com.app.nEdge.constant.Constants.expertTimeFormat
import com.app.nEdge.customData.adapter.MyAdapter
import com.app.nEdge.customData.enums.NetworkStatus
import com.app.nEdge.databinding.ActivityExpertRegistrationBinding
import com.app.nEdge.models.SubjectSpinnerModel
import com.app.nEdge.ui.base.BaseActivity
import com.app.nEdge.ui.customWidgets.ProgressHUD
import com.app.nEdge.utils.Utilities
import com.google.firebase.firestore.DocumentSnapshot
import java.text.SimpleDateFormat
import java.util.*


class ExpertRegistrationActivity : BaseActivity() {
    private lateinit var mBinding: ActivityExpertRegistrationBinding
    private lateinit var viewModel: ExpertRegistrationActivityViewModel
    private lateinit var myAdapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityExpertRegistrationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initViewModel()
        getDataFromBundle()
        setObservers()
        viewModel.getSubjectsData()
        setListeners()
        setCurrentTime()
        createDaysSpinner()

    }

    private fun getDataFromBundle() {
//        viewModel.userBuilder = intent.extras?.getSerializable(CommonKeys.KEY_DATA) as UserBuilder
    }

    private fun setObservers() {
        viewModel.subjectsLiveData.observe(this) {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    ProgressHUD.show(this)
                }
                NetworkStatus.SUCCESS -> {
                    ProgressHUD.removeView()
                    val subjectsArray: ArrayList<String> =
                        (it.t as DocumentSnapshot).get(FireBaseCommonKeys.KEY_SUBJECTS_ARRAY) as ArrayList<String>
                    createSubjectsSpinner(subjectsArray)
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

    private fun createSubjectsSpinner(subjectsArray: ArrayList<String>) {
        val subjects = ArrayList<String>()
        subjects.addAll(subjectsArray)
        subjects.add(0, getString(R.string.subject))
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, subjects)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBinding.spinnerSubjects.adapter = adapter
    }

    private fun createDaysSpinner() {

        val listVOs: ArrayList<SubjectSpinnerModel> = ArrayList()

        for (i in resources.getStringArray(R.array.days)) {
            val subjectSpinnerModel = SubjectSpinnerModel()
            subjectSpinnerModel.setTitle(i)
            subjectSpinnerModel.setSelected(false)
            listVOs.add(subjectSpinnerModel)
        }
        myAdapter = MyAdapter(
            this, 0,
            listVOs
        )
        mBinding.spinnerDays.adapter = myAdapter
    }

    private fun setCurrentTime() {
        mBinding.textEndTime.text = Utilities.getCurrentDate(expertTimeFormat)
        mBinding.textStartTime.text = Utilities.getCurrentDate(expertTimeFormat)

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ExpertRegistrationActivityViewModel::class.java]
    }

    private fun createTimePickerDialog() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            if (viewModel.isStartTimeSelected) {
                mBinding.textStartTime.text = SimpleDateFormat(expertTimeFormat).format(cal.time)
            } else {
                mBinding.textEndTime.text = SimpleDateFormat(expertTimeFormat).format(cal.time)
            }
        }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            false
        ).show()

    }

    private fun setListeners() {
        mBinding.textStartTime.setOnClickListener {
            viewModel.isStartTimeSelected = true
            createTimePickerDialog()
        }
        mBinding.textEndTime.setOnClickListener {
            viewModel.isStartTimeSelected = false
            createTimePickerDialog()
        }
        mBinding.btnDone.setOnClickListener {
            validations()
        }
    }

    private fun validations() {
        if (TextUtils.isEmpty(mBinding.etRatePerAns.text.toString())) {
            mBinding.etRatePerAns.error = getString(R.string.require_field)
        } else if (TextUtils.isEmpty(mBinding.etRatePerLec.text.toString())) {
            mBinding.etRatePerLec.error = getString(R.string.require_field)
        } else if (mBinding.spinnerSubjects.selectedItemPosition == 0) {
            Toast.makeText(this, getString(R.string.subjectValidation), Toast.LENGTH_SHORT).show()
        } else if (checkDayValidation()) {
            Toast.makeText(this, getString(R.string.daysValidation), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

        }
    }

    private fun checkDayValidation(): Boolean {
        viewModel.selectedDaysArray.clear()
        val array = myAdapter.list
        for (spinnerModel in array) {
            if (spinnerModel.isSelected()) {
                spinnerModel.getTitle()?.let { viewModel.selectedDaysArray.add(it) }
            }
        }
        return viewModel.selectedDaysArray.isEmpty()
    }
}