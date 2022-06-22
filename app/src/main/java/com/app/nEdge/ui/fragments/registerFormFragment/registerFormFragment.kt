package com.app.nEdge.ui.fragments.registerFormFragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.app.nEdge.CommonKeys.CommonKeys.Companion.KEY_DATA
import com.app.nEdge.CommonKeys.FireBaseCommonKeys
import com.app.nEdge.R
import com.app.nEdge.constant.Constants.expertTimeFormat
import com.app.nEdge.customData.adapter.MyAdapter
import com.app.nEdge.customData.enums.NetworkStatus
import com.app.nEdge.customData.enums.UserType
import com.app.nEdge.databinding.FragmentRegistrationFormBinding
import com.app.nEdge.models.SubjectSpinnerModel
import com.app.nEdge.models.UserBuilder
import com.app.nEdge.ui.activities.RegistrationActivity.RegistrationImp
import com.app.nEdge.ui.base.BaseFragment
import com.app.nEdge.ui.customWidgets.ProgressHUD
import com.app.nEdge.utils.Utilities
import com.google.firebase.firestore.DocumentSnapshot
import java.text.SimpleDateFormat
import java.util.*

class registerFormFragment : BaseFragment() {

    companion object {
        fun newInstance(bundle: Bundle) = registerFormFragment().apply {
            arguments = bundle
        }
    }

    private lateinit var viewModel: RegisterFormViewModel
    private lateinit var mBinding: FragmentRegistrationFormBinding
    private lateinit var myAdapter: MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentRegistrationFormBinding.inflate(
            inflater,
            container,
            false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RegisterFormViewModel::class.java]
        viewModel.userType = arguments?.getString(KEY_DATA) ?: UserType.Student.toString()
        adjustViewsWithUserType()
        setListeners()
    }

    private fun adjustViewsWithUserType() {
        if (viewModel.userType == UserType.Student.toString()) {
            mBinding.layoutExpertRegistration.visibility = View.GONE
            mBinding.textUserType.text = getString(R.string.iWantToLearn)
        } else {
            mBinding.layoutExpertRegistration.visibility = View.VISIBLE
            mBinding.textUserType.text = getString(R.string.iAmExpert)
            setCurrentTime()
            setObservers()
            viewModel.getSubjectsData()
            createDaysSpinner()

        }
    }

    private fun setObservers() {
        viewModel.subjectsLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    ProgressHUD.show(requireContext())
                }
                NetworkStatus.SUCCESS -> {
                    ProgressHUD.removeView()
                    val subjectsArray: ArrayList<String> =
                        (it.t as DocumentSnapshot).get(FireBaseCommonKeys.KEY_SUBJECTS_ARRAY) as ArrayList<String>
                    createSubjectsSpinner(subjectsArray)
                }
                NetworkStatus.ERROR -> {
                    ProgressHUD.removeView()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.someThingWentWrong),
                        Toast.LENGTH_SHORT
                    )
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
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, subjects)
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
            requireContext(), 0,
            listVOs
        )
        mBinding.spinnerDays.adapter = myAdapter
    }

    private fun setCurrentTime() {
        mBinding.textEndTime.text = Utilities.getCurrentDate(expertTimeFormat)
        mBinding.textStartTime.text = Utilities.getCurrentDate(expertTimeFormat)

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
            requireContext(),
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
        mBinding.btnCssProfessinal.setOnClickListener {
            viewModel.educationType = mBinding.btnCssProfessinal.text.toString()
        }
        mBinding.btnSchoolClg.setOnClickListener {
            viewModel.educationType = mBinding.btnSchoolClg.text.toString()
        }
        mBinding.btnUniversity.setOnClickListener {
            viewModel.educationType = mBinding.btnUniversity.text.toString()
        }
    }

    private fun validations() {
        if (TextUtils.isEmpty(viewModel.educationType)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.please_select_education_type),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (viewModel.userType == UserType.Expert.toString()) {
            if (TextUtils.isEmpty(mBinding.etRatePerAns.text.toString())) {
                mBinding.etRatePerAns.error = getString(R.string.require_field)
            } else if (TextUtils.isEmpty(mBinding.etRatePerLec.text.toString())) {
                mBinding.etRatePerLec.error = getString(R.string.require_field)
            } else if (mBinding.spinnerSubjects.selectedItemPosition == 0) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.subjectValidation),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (checkDayValidation()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.daysValidation),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                createUserObject()
                (mActivityListener as RegistrationImp).onGettingUserInfo(viewModel.userBuilder)
            }
        } else {
            createUserObject()
            (mActivityListener as RegistrationImp).onGettingUserInfo(viewModel.userBuilder)

        }
    }

    private fun createUserObject() {
        viewModel.userBuilder = UserBuilder()
        viewModel.userBuilder.educationType = viewModel.educationType
        if (viewModel.userType == UserType.Expert.toString()) {
            viewModel.userBuilder.ratePerLecture(mBinding.etRatePerLec.text.toString())
            viewModel.userBuilder.ratePerMarkingAnswer(mBinding.etRatePerAns.text.toString())
            viewModel.userBuilder.availabiltyDays(viewModel.selectedDaysArray)
            viewModel.userBuilder.availabiltyStartTime(mBinding.textStartTime.text.toString())
            viewModel.userBuilder.availabiltyEndTime(mBinding.textEndTime.text.toString())
        }

    }

    private fun checkDayValidation(): Boolean {
        viewModel.selectedDaysArray.clear()
        val array = myAdapter.list
        for (spinnerModel in array) {
            if (spinnerModel.isSelected()) {
                spinnerModel.getTitle()?.let {
                    viewModel.selectedDaysArray.add(it)
                }
            }
        }
        return viewModel.selectedDaysArray.isEmpty()
    }
}