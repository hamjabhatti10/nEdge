package com.app.nEdge.ui.fragments.usertypeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.nEdge.customData.enums.UserType
import com.app.nEdge.databinding.FragmentUserTypeBinding
import com.app.nEdge.ui.activities.RegistrationActivity.RegistrationImp
import com.app.nEdge.ui.base.BaseFragment

class userTypeFragment : BaseFragment() {

    companion object {
        fun newInstance() = userTypeFragment()
    }

    private lateinit var viewModel: UserTypeViewModel
    private lateinit var mBinding: FragmentUserTypeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentUserTypeBinding.inflate(
            inflater,
            container,
            false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserTypeViewModel::class.java)
        setListeners()
    }

    private fun setListeners() {
        mBinding.textExpert.setOnClickListener {
            (mActivityListener as RegistrationImp).onUserType(UserType.Expert)
        }
        mBinding.textStudent.setOnClickListener {
            (mActivityListener as RegistrationImp).onUserType(UserType.Student)
        }
    }
}