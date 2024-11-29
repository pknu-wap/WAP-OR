package com.example.wap_or
import AuthRequest
import SignUpRequest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wap_or.model.EmailRequest
import com.example.wap_or.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.regex.Pattern


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
    private lateinit var pwSubmitButtom: Button
    private lateinit var timerTextView: TextView
    private var condition1 = false
    private var condition2 = false
    private var timeLeftInMillis = (5 * 60 * 1000).toLong()
    private var timer: CountDownTimer? = null
    interface ApiService {
        @POST("mailSend")
        fun sendEmail(@Body emailRequest: EmailRequest): Call<Void>

        @POST("mailAuthCheck")
        fun checkAuthCode(@Body authRequest: AuthRequest): Call<Void>

        @POST("api/users/register/email")
        fun SignUpCode(@Body signupRequest: SignUpRequest): Call<Void>
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
        pwSubmitButtom = findViewById(R.id.PWSubmitButton)
        signupButton = findViewById(R.id.SignUPButton)
        timerTextView = findViewById(R.id.timer);

        submitButton.setOnClickListener {
            val email = emailEditText.text.toString()
            if (isValidEmail(email)) {
                sendEmailRequest(email)
            } else {
                wrongInputText.visibility = View.VISIBLE
                CertificationNumEditText.visibility = View.INVISIBLE
                submitButton2.visibility = View.INVISIBLE
            }
        }
        submitButton2.setOnClickListener {
            val certificationNum = CertificationNumEditText.text.toString()
            if (certificationNum.isNotEmpty()) {
                checkAuthCodeRequest(emailEditText.text.toString(), certificationNum)
            } else {
                wrongInputText2.text = "인증번호를 입력해주세요."
                wrongInputText2.setTextColor(Color.parseColor("#FF6464"))
                wrongInputText2.visibility = View.VISIBLE
            }
        }
        // passwordEditText에 대한 TextWatcher
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 비밀번호 유효성 검사
                if (!isPasswordValid(s.toString())) {
                    wrongInputTextPw.text = "비밀번호가 조건에 맞지 않습니다."
                    wrongInputTextPw.setTextColor(Color.parseColor("#FF6464"))
                    wrongInputTextPw.visibility = View.VISIBLE
                    lockIcon.setImageResource(R.drawable.lock_icon)
                    pwSubmitButtom.visibility = View.INVISIBLE
                }
                else
                {
                    wrongInputTextPw.text = "사용가능한 비밀번호 입니다."
                    wrongInputTextPw.setTextColor(Color.parseColor("#3E74C4"))
                    wrongInputTextPw.visibility = View.VISIBLE
                    lockIcon.setImageResource(R.drawable.unlock_icon)
                    pwSubmitButtom.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        pwSubmitButtom.setOnClickListener {
            val enteredPassword = passwordEditText.text.toString()
            val reEnteredPassword = passwordReInputEditText.text.toString()
            if (enteredPassword.isEmpty() || reEnteredPassword.isEmpty()) {
                wrongInputTextPw.text = "비밀번호를 입력해주세요."
                wrongInputTextPw.setTextColor(Color.parseColor("#FF6464"))
                wrongInputTextPw.visibility = View.VISIBLE
                lockIcon2.setImageResource(R.drawable.lock_icon)
            } else if (!isPasswordValid(enteredPassword)) {
                wrongInputTextPw.text = "비밀번호가 조건에 맞지 않습니다."
                wrongInputTextPw.setTextColor(Color.parseColor("#FF6464"))
                wrongInputTextPw.visibility = View.VISIBLE
                lockIcon2.setImageResource(R.drawable.lock_icon)
            } else if (arePasswordsMatching(enteredPassword, reEnteredPassword)) {
                wrongInputTextPw.text = "비밀번호가 확인되었습니다."
                wrongInputTextPw.setTextColor(Color.parseColor("#3E74C4"))
                wrongInputTextPw.visibility = View.VISIBLE
                lockIcon2.setImageResource(R.drawable.unlock_icon)
                // 비밀번호 제출 이후 처리
                passwordEditText.isEnabled = false
                passwordReInputEditText.isEnabled = false
                pwSubmitButtom.isEnabled = false
                condition2 = true
                checkConditionsAndShowSignupButton(condition1, condition2)
            } else {
                wrongInputTextPw.text = "비밀번호가 일치하지 않습니다."
                wrongInputTextPw.setTextColor(Color.parseColor("#FF6464"))
                wrongInputTextPw.visibility = View.VISIBLE
                lockIcon2.setImageResource(R.drawable.lock_icon)
            }
        }
        signupButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val signupRequest = SignUpRequest(identifier = email, password = password)

            RetrofitInstance.apiService.SignUpCode(signupRequest).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // 회원가입 성공 처리
                        Log.d("Retrofit", "회원가입 성공!")
                        val intent = Intent(this@SignUpActivity, PaylogActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // 서버에서 반환한 실패 응답을 로그로 표시
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = if (!errorBody.isNullOrEmpty()) {
                            "회원가입 실패: ${response.code()} - $errorBody"
                        } else {
                            "회원가입 실패: ${response.code()} - 오류 메시지 없음"
                        }
                        Log.e("Retrofit", errorMessage)
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // 네트워크 오류를 로그로 표시
                    Log.e("Retrofit", "네트워크 오류: ${t.message}")
                }
            })
        }
    }
    fun checkConditionsAndShowSignupButton(condition1: Boolean, condition2: Boolean) {
        if (condition1 && condition2) {
            signupButton.visibility = View.VISIBLE
        } else {
            signupButton.visibility = View.INVISIBLE
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
    private fun startTimer() {
        object : CountDownTimer(timeLeftInMillis, 1000) {
            // 1초마다 호출
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }
            override fun onFinish() {
                timerTextView.setText("00:00")
            }
        }.start()
    }
    private fun updateTimer() {
        runOnUiThread {
            val minutes = (timeLeftInMillis / 1000).toInt() / 60
            val seconds = (timeLeftInMillis / 1000).toInt() % 60
            timerTextView.text = String.format("%02d:%02d", minutes, seconds)
        }
    }
    private fun sendEmailRequest(email: String) {
        val emailRequest = EmailRequest(identifier = email)

        RetrofitInstance.apiService.sendEmail(emailRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    wrongInputText.visibility = View.GONE
                    CertificationNumEditText.visibility = View.VISIBLE
                    submitButton2.visibility = View.VISIBLE
                    timerTextView.visibility = View.VISIBLE
                    // 타이머 시작
                    startTimer()
                    // 이메일 유효 시 처리할 코드 추가 ex) 타이머
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
    private fun checkAuthCodeRequest(identifier: String, authCode: String) {
        val authRequest = AuthRequest(identifier, authCode)
        RetrofitInstance.apiService.checkAuthCode(authRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // 인증 성공 시 처리
                    wrongInputText2.text = "인증이 완료되었습니다."
                    wrongInputText2.setTextColor(Color.parseColor("#3E74C4"))
                    wrongInputText2.visibility = View.VISIBLE

                    // 이메일 인증 완료 처리
                    emailEditText.isEnabled = false
                    submitButton.isEnabled = false
                    CertificationNumEditText.isEnabled = false
                    submitButton2.isEnabled = false
                    timerTextView.isEnabled = false
                    timer?.cancel()
                    timerTextView.visibility = View.INVISIBLE

                    // 조건 충족 상태 갱신
                    condition1 = true
                    checkConditionsAndShowSignupButton(condition1, condition2)
                } else {
                    // 실패 시 처리
                    wrongInputText2.text = "인증번호가 일치하지 않거나 만료되었습니다."
                    wrongInputText2.setTextColor(Color.parseColor("#FF6464"))
                    wrongInputText2.visibility = View.VISIBLE

                    Log.e("Retrofit", "요청 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // 네트워크 오류 처리
                Log.e("Retrofit", "네트워크 오류: ${t.message}")
                wrongInputText2.text = "네트워크 오류가 발생했습니다."
                wrongInputText2.setTextColor(Color.parseColor("#FF6464"))
                wrongInputText2.visibility = View.VISIBLE
            }
        })
    }
}