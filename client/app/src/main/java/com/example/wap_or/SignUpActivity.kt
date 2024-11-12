package com.example.wap_or
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Pattern
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.content.Intent
import com.example.wap_or.model.EmailRequest
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import android.util.Log
import retrofit2.Callback
import retrofit2.Response
import com.example.wap_or.utils.Constants

class SignUpActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var wrongInputText: TextView
    private lateinit var CertificationNumEditText: EditText
    private lateinit var submitButton2: Button
    private lateinit var wrongInputText2: TextView
    private lateinit var passwordEditText: EditText
    private lateinit var passwordReInputEditText: EditText
    private lateinit var wrongInputTextPw: TextView
    private lateinit var lockIcon: ImageView
    private lateinit var lockIcon2: ImageView
    private lateinit var signupButton: Button
    interface ApiService {
        @POST("mailSend")
        fun sendEmail(@Body emailRequest: EmailRequest): Call<Void>
    }
    object RetrofitInstance {
        private const val BASE_URL = Constants.BASE_URL

        // Retrofit 인스턴스 생성
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        // ApiService 인스턴스 생성
        val apiService: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        emailEditText = findViewById(R.id.Email)
        submitButton = findViewById(R.id.SubmitButton)
        wrongInputText = findViewById(R.id.wrongInputText)
        CertificationNumEditText = findViewById(R.id.CertificationNumber)
        submitButton2 = findViewById(R.id.SubmitButton2)
        wrongInputText2 = findViewById(R.id.wrongInputText2)
        passwordEditText = findViewById(R.id.passwordInput)
        passwordReInputEditText = findViewById(R.id.passwordReInput)
        wrongInputTextPw = findViewById(R.id.wrongInputTextPw)
        lockIcon = findViewById(R.id.lockIcon)
        lockIcon2 = findViewById(R.id.lockIcon2)
        signupButton = findViewById(R.id.SignUPButton)
        var condition1 = false
        var condition2 = false


        submitButton.setOnClickListener {
            val email = emailEditText.text.toString()
            if (isValidEmail(email)) {
                sendEmailRequest(email)
                wrongInputText.visibility = View.GONE
                CertificationNumEditText.visibility = View.VISIBLE
                submitButton2.visibility = View.VISIBLE
                // 이메일 유효 시 처리할 코드 추가 ex) 타이머
                //if()
//             {
//                //이미 가입한 이메일인 경우
//              wrongInputText.visibility = View.VISIBLE
//              wrongInputText.text = "이미 가입한 이메일입니다."
//             }
            } else {
                wrongInputText.visibility = View.VISIBLE
                CertificationNumEditText.visibility = View.INVISIBLE
                submitButton2.visibility = View.INVISIBLE
            }
        }
        submitButton2.setOnClickListener {
            val CertificationNum = CertificationNumEditText.text.toString()
            //인증번호 일치 여부, 만료 여부 api로 검사
            if (CertificationNum == "") {
                //텍스트 수정
                wrongInputText2.text = "인증이 완료되었습니다."
                wrongInputText2.setTextColor(Color.parseColor("#3E74C4"))
                wrongInputText2.visibility = View.VISIBLE
                //타이머 제거

                // 가입하기 조건 1 충족
                condition1 = true;

                //이메일, 인증번호 EditText, button 비활성화
                emailEditText.isEnabled = false
                submitButton.isEnabled = false
                CertificationNumEditText.isEnabled = false
                submitButton2.isEnabled = false
            }
//          else if()
//          {
//                //인증 시간이 만료된 경우
//              wrongInputText2.visibility = View.VISIBLE
//              wrongInputText2.text = "인증 시간이 만료되었습니다."
//          }
            else {
                //인증번호가 일치하지 않는 경우
                wrongInputText2.visibility = View.VISIBLE

            }
        }
        // passwordEditText에 대한 TextWatcher
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 비밀번호 유효성 검사
                if (!isPasswordValid(s.toString())) {
                    wrongInputTextPw.text = "비밀번호가 조건에 맞지 않습니다."
                    wrongInputTextPw.visibility = View.VISIBLE
                    lockIcon.setImageResource(R.drawable.lock_icon)
                }
                else
                {
                    wrongInputTextPw.visibility = View.INVISIBLE
                    lockIcon.setImageResource(R.drawable.unlock_icon)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // passwordReInputEditText에 대한 TextWatcher
        passwordReInputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 두 비밀번호 일치 여부 검사
                if(s.toString().isEmpty())
                {
                    wrongInputTextPw.visibility = View.INVISIBLE
                }
                else if(!isPasswordValid(s.toString()))
                {
                    wrongInputTextPw.text = "비밀번호가 조건에 맞지 않습니다."
                    wrongInputTextPw.visibility = View.VISIBLE
                    lockIcon2.setImageResource(R.drawable.lock_icon)
                }
                else if (arePasswordsMatching(passwordEditText.text.toString(), s.toString()))
                {
                    wrongInputTextPw.text = "비밀번호가 확인되었습니다."
                    wrongInputTextPw.setTextColor(Color.parseColor("#3E74C4"))
                    wrongInputTextPw.visibility = View.VISIBLE
                    lockIcon2.setImageResource(R.drawable.unlock_icon)
                    passwordEditText.isEnabled = false
                    passwordReInputEditText.isEnabled = false
                    condition2 = true;
                }
                else
                {
                    wrongInputTextPw.visibility = View.VISIBLE
                    lockIcon2.setImageResource(R.drawable.lock_icon)
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        if (condition1 && condition2) {
            signupButton.visibility = View.VISIBLE
        }
        signupButton.setOnClickListener{
            val intent = Intent(this, PaylogActivity::class.java)
            startActivity(intent)
            finish()
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
    // 비밀번호 유효성 검사 함수
    private fun isPasswordValid(password: String): Boolean {
        if (password.length < 8 || password.length > 20) {
            return false
        }
        if (!password.any { it.isLetter() }) {
            return false
        }
        if (!password.any { it.isDigit() }) {
            return false
        }
        return true
    }
    // 두 비밀번호가 일치하는지 검사하는 함수
    fun arePasswordsMatching(password: String, rePassword: String): Boolean {
        return password == rePassword
    }
    private fun sendEmailRequest(email: String) {
        val emailRequest = EmailRequest(identifier = email)

        RetrofitInstance.apiService.sendEmail(emailRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) { // 200번대 코드 확인
                    Log.d("Retrofit", "이메일 요청 성공!")
                } else {
                    // 에러 코드와 함께 에러 메시지 로깅
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (!errorBody.isNullOrEmpty()) {
                        "${response.code()} - $errorBody"
                    } else {
                        "${response.code()} - 오류 메시지 없음"
                    }
                    Log.e("Retrofit", "요청 실패: $errorMessage")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Retrofit", "네트워크 오류: ${t.message}")
            }
        })
    }
}