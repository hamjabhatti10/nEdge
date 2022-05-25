package com.app.nEdge.ui.activities.expertRegistrationActivity

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.app.nEdge.R
import com.app.nEdge.customData.adapter.MyAdapter
import com.app.nEdge.databinding.ActivityExpertRegistrationBinding
import com.app.nEdge.models.SubjectSpinnerModel
import com.app.nEdge.ui.base.BaseActivity
import com.app.nEdge.utils.Utilities
import java.text.SimpleDateFormat
import java.util.*


class ExpertRegistrationActivity : BaseActivity() {
    private lateinit var mBinding: ActivityExpertRegistrationBinding
    private lateinit var mViewModel: ExpertRegistrationActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityExpertRegistrationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initViewModel()
        setListeners()
        setCurrentTime()
        createSubjectSpinner()

    }

    private fun createSubjectSpinner() {
        val select_qualification = resources.getStringArray(R.array.days)

        val listVOs: ArrayList<SubjectSpinnerModel> = ArrayList()

        for (i in select_qualification) {
            val subjectSpinnerModel = SubjectSpinnerModel()
            subjectSpinnerModel.setTitle(i)
            subjectSpinnerModel.setSelected(false)
            listVOs.add(subjectSpinnerModel)
        }
        val myAdapter = MyAdapter(
            this, 0,
            listVOs
        )
        mBinding.spinnerDays.adapter = myAdapter
    }

    private fun setCurrentTime() {
        mBinding.textEndTime.text = Utilities.getCurrentDate("hh:mm aa")
        mBinding.textStartTime.text = Utilities.getCurrentDate("hh:mm aa")

    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ExpertRegistrationActivityViewModel::class.java]
    }

    private fun createTimePickerDialog() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            if (mViewModel.isStartTimeSelected) {
                mBinding.textStartTime.text = SimpleDateFormat("hh:mm aa").format(cal.time)
            } else {
                mBinding.textEndTime.text = SimpleDateFormat("hh:mm aa").format(cal.time)
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
            mViewModel.isStartTimeSelected = true
            createTimePickerDialog()
        }
        mBinding.textEndTime.setOnClickListener {
            mViewModel.isStartTimeSelected = false
            createTimePickerDialog()
        }
    }
}