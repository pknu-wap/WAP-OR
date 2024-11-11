package com.example.wap_or
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var wrongInputText: TextView
    private lateinit var CertificationNumEditText: EditText
    private lateinit var submitButton2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        emailEditText = findViewById(R.id.Email)
        submitButton = findViewById(R.id.SubmitButton)
        wrongInputText = findViewById(R.id.wrongInputText)
        CertificationNumEditText = findViewById(R.id.CertificationNumber)
        submitButton2 = findViewById(R.id.SubmitButton2)

        submitButton.setOnClickListener {
            val email = emailEditText.text.toString()
            if (isValidEmail(email)) {
                wrongInputText.visibility = View.GONE
                CertificationNumEditText.visibility = View.VISIBLE
                submitButton2.visibility = View.VISIBLE
                // 이메일 유효 시 처리할 코드 추가 ex) 타이머
            } else {
                wrongInputText.visibility = View.VISIBLE
                CertificationNumEditText.visibility = View.INVISIBLE
                submitButton2.visibility = View.INVISIBLE
            }
        }
    }

    // 이메일 유효성 검사 함수
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.(\\.[a-zA-Z]{2,3})$")
        //^[0-9a-zA-Z] : 이메일의 첫 글자는 반드시 숫자(0-9)나 영문자(a-zA-Z)로 시작해야 합니다.
        //([-_.]?[0-9a-zA-Z])*: 그 다음엔 -, _, .이 있을 수 있으며, 이 후 숫자 또는 영문자가 반복될 수 있습니다.
        //@: @ 문자로 사용자 이름 부분과 도메인 부분을 구분합니다.
        //[-_.]?: -, _, . 중 하나가 있을 수도 있고 없을 수도 있음.
        //\\.[a-zA-Z]{2,3}$: 최상위 도메인은 .으로 시작하며, 영문자(a-zA-Z)로 이루어진 2~3자리의 문자열로 끝나야 합니다.
        //$: 문자열이 끝남을 의미

        return emailPattern.matcher(email).matches()
    }
}