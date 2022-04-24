package com.app.nEdge.ui.base

import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.app.nEdge.source.remote.rxjava.DisposableManager


abstract class BaseViewModel : ViewModel() {
    fun setServerName(textView: TextView) {

    }

    fun cancelServerRequest() {
        DisposableManager.dispose()
    }
}