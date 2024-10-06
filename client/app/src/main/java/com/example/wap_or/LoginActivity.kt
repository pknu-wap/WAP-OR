package com.example.wap_or
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var kakaoLoginButton: FrameLayout
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var wrongInputTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_WAP_OR)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // 뷰 연결
        loginButton = findViewById(R.id.LoginButton)
        kakaoLoginButton = findViewById(R.id.kakaoLoginButton)
        loginEditText = findViewById(R.id.login)
        passwordEditText = findViewById(R.id.password)
        wrongInputTextView = findViewById(R.id.wrongInputText)

        // 로그인 버튼 클릭 이벤트
        loginButton.setOnClickListener {
            val login = loginEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (login.isEmpty() || password.isEmpty()) {
                wrongInputTextView.visibility = View.VISIBLE
                wrongInputTextView.text = "아이디와 비밀번호를 입력하세요."
            } else {
                wrongInputTextView.visibility = View.GONE
                Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                finish() // LoginActivity 종료
            }
        }

        // 카카오 로그인 버튼 클릭 이벤트
        kakaoLoginButton.setOnClickListener {
            onKakaoLoginClick(it)
        }
    }

    // 카카오 로그인 클릭 처리
    fun onKakaoLoginClick(view: View) {
        Toast.makeText(this, "카카오 로그인", Toast.LENGTH_SHORT).show()
        // 카카오 로그인 로직 추가
    }
}