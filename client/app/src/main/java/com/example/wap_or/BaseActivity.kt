package com.example.wap_or
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setupBottomNavigation() {
        val bottomNavigation: View? = findViewById(R.id.bottom_navigation)
        bottomNavigation?.findViewById<View>(R.id.account_button)?.setOnClickListener {
            navigateToAccount()
        }
        bottomNavigation?.findViewById<View>(R.id.home_button)?.setOnClickListener {
            navigateToHome()
        }
    }

    private fun navigateToAccount() {
        // AccountActivity로 이동
        val intent = Intent(this, AccountActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun navigateToHome() {
        // PaylogActivity로 이동
        val intent = Intent(this, PaylogActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}
