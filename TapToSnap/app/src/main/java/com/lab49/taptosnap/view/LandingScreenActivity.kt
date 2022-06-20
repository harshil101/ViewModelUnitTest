package com.lab49.taptosnap.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lab49.taptosnap.R
import com.lab49.taptosnap.databinding.ActivityLandingScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.quizFragment)
        fragment?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}