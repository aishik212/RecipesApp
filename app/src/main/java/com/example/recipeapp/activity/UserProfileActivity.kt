package com.example.recipeapp.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeapp.databinding.UserProfileActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserProfileActivity : AppCompatActivity() {
    lateinit var inflate: UserProfileActivityBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflate = UserProfileActivityBinding.inflate(layoutInflater)
        setContentView(inflate.root)
        inflate.editBtn.setOnClickListener {
            if (inflate.nameEt.isEnabled) {
                disableAllEts()
            } else {
                enableAllEts()
            }
        }
    }

    private fun enableAllEts() {
        inflate.nameEt.isEnabled = true
        inflate.restrictionsEt.isEnabled = true
        inflate.cuisineEt.isEnabled = true
        inflate.editBtn.text = "SAVE"
        inflate.nameEt.performClick()
        inflate.editBtn.setOnClickListener {
            updateData()
        }
    }

    private fun updateData() {
        if (inflate.nameEt.text.isNullOrEmpty()
            or
            inflate.restrictionsEt.text.isNullOrEmpty()
            or
            inflate.cuisineEt.text.isNullOrEmpty()
        ) {
            Toast.makeText(applicationContext, "Please Enter All Fields", Toast.LENGTH_SHORT).show()
        } else {
            sharedPreferences.edit().putString("name", inflate.nameEt.text.toString()).apply()
            sharedPreferences.edit()
                .putString("restrictions", inflate.restrictionsEt.text.toString())
                .apply()
            sharedPreferences.edit().putString("cuisine", inflate.cuisineEt.text.toString()).apply()
            disableAllEts()
        }
    }

    private fun disableAllEts() {
        inflate.nameEt.isEnabled = false
        inflate.restrictionsEt.isEnabled = false
        inflate.cuisineEt.isEnabled = false
        inflate.editBtn.text = "EDIT"
        inflate.editBtn.setOnClickListener {
            enableAllEts()
        }
    }
}
