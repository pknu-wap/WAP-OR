package com.example.wap_or
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_test) // XML 파일 이름을 설정

        // 버튼을 XML에서 가져오기
        val loginButton: Button = findViewById(R.id.loginButton)

        // 버튼 클릭 리스너 설정
        loginButton.setOnClickListener {
            // PaylogActivity로 이동
            val intent = Intent(this, PaylogActivity::class.java)
            startActivity(intent)
        }
    }
}