package com.example.wap_or
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.POST
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.wap_or.model.LoginRequest
import com.example.wap_or.model.LoginResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import android.content.pm.PackageManager
import org.json.JSONException
import org.json.JSONObject

interface ApiService {
    @POST("login/email") // 실제 엔드포인트에 맞게 수정
    //ex http://localhost:8080/api/users/login/email
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
val retrofit = Retrofit.Builder()
    .baseUrl("http://34.64.36.255:8080/api/users/") // 실제 백엔드 URL로 변경
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService = retrofit.create(ApiService::class.java)

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
        val appKey = getMetaData("com.kakao.sdk.AppKey")
            ?: throw IllegalStateException("AppKey is missing in AndroidManifest.xml")
        KakaoSdk.init(this, appKey)

        // UI 요소 초기화
        loginButton = findViewById(R.id.LoginButton)
        kakaoLoginButton = findViewById(R.id.kakaoLoginButton)
        loginEditText = findViewById(R.id.login)
        passwordEditText = findViewById(R.id.password)
        wrongInputTextView = findViewById(R.id.wrongInputText)

        // 로그인 버튼 클릭 이벤트
        loginButton.setOnClickListener {
            performLogin()
        }

        // 카카오 로그인 버튼 클릭 이벤트
        kakaoLoginButton.setOnClickListener {
            performKakaoLogin(it)
        }
    }
    private fun performLogin() {
        val login = loginEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (login.isEmpty() || password.isEmpty()) {
            wrongInputTextView.visibility = View.VISIBLE
            wrongInputTextView.text = "아이디와 비밀번호를 입력하세요."
            return // 입력이 비어있으면 함수 종료
        }

        // 로그인 요청을 보냄
        val loginRequest = LoginRequest(identifier = login, password = password)
        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                try {
                    when (response.code()) {
                        200 -> {
                            val loginResponse = response.body()
                            if (loginResponse != null) {
                                // 로그인 성공 시 PaylogActivity로 이동
                                val intent = Intent(this@LoginActivity, PaylogActivity::class.java)
                                startActivity(intent)
                                finish() // 현재 액티비티 종료
                            } else {
                                Log.e("LoginResponse", "Response body is null")
                                wrongInputTextView.visibility = View.VISIBLE
                                wrongInputTextView.text = "서버 응답이 올바르지 않습니다."
                            }
                        }
                        401 -> {
                            response.errorBody()?.let { errorBody ->
                                try {
                                    val jsonObject = JSONObject(errorBody.string())
                                    val errorMessage = jsonObject.optString("message", "인증 실패")
                                    wrongInputTextView.visibility = View.VISIBLE
                                    wrongInputTextView.text = errorMessage
                                } catch (e: JSONException) {
                                    Log.e("LoginError", "JSON Parsing error: ${e.message}")
                                    wrongInputTextView.visibility = View.VISIBLE
                                    wrongInputTextView.text = "알 수 없는 오류가 발생했습니다."
                                }
                            } ?: run {
                                wrongInputTextView.visibility = View.VISIBLE
                                wrongInputTextView.text = "서버 오류가 발생했습니다."
                            }
                        }
                        else -> {
                            val errorBody = response.errorBody()?.string()
                            val errorMessage = if (!errorBody.isNullOrEmpty()) {
                                "${response.code()}: $errorBody"
                            } else {
                                "로그인에 실패했습니다."
                            }
                            Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("LoginError", "Exception occurred: ${e.message}")
                    wrongInputTextView.visibility = View.VISIBLE
                    wrongInputTextView.text = "오류가 발생했습니다."
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginError", "error: ${t.message}")
                wrongInputTextView.visibility = View.VISIBLE
                wrongInputTextView.text = "오류가 발생했습니다."
            }
        })
    }


    private fun performKakaoLogin(view: View) {
        UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
            if (error != null) {
                Log.e("KakaoLogin", "로그인 실패", error)
            } else if (token != null) {
                Log.i("KakaoLogin", "로그인 성공 ${token.accessToken}")
                // 여기서 토큰을 저장하거나, 백엔드로 전달하는 후속 작업을 수행하세요
            }
        }
    }
    private fun getMetaData(name: String): String? {
        return try {
            val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            appInfo.metaData.getString(name)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}