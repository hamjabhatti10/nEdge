package com.app.nEdge.ui.activities.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.nEdge.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}