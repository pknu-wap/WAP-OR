package com.example.wap_or

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_WAP_OR)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 앱이 처음 실행되면 LoginActivity로 이동
        val loginIntent = Intent(this, LoginTestActivity::class.java)
        startActivity(loginIntent)
        finish()
    }
}

