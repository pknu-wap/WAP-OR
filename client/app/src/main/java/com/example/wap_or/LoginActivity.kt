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
import com.example.wap_or.model.TokenRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import android.content.pm.PackageManager
import com.google.gson.Gson
import data.TokenErrorResponse
import data.TokenSuccessResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.http.Header
import com.example.wap_or.utils.Constants

interface ApiService {
    @POST("api/users/login/email") // 실제 엔드포인트에 맞게 수정
    //ex http://localhost:8080/api/users/login/email
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
interface KakaoApiService {
    @POST("/auth/kakao")
    fun sendToken(
        @Header("Authorization") accessToken: String
    ): Call<TokenSuccessResponse>
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

    // KakaoApiService 인스턴스 생성
    val kakaoApiService: KakaoApiService by lazy {
        retrofit.create(KakaoApiService::class.java)
    }
}
class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var kakaoLoginButton: FrameLayout
    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var wrongInputTextView: TextView
    private lateinit var signUpButton: Button

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
        signUpButton = findViewById(R.id.SignUPButton)

        // 로그인 버튼 클릭 이벤트
        loginButton.setOnClickListener {
            performLogin()
        }
        signUpButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
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
        RetrofitInstance.apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
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
    private fun sendAccessTokenToServer(accessToken: String) {
        // Bearer 형식으로 헤더에 토큰 추가
        val call = RetrofitInstance.kakaoApiService.sendToken("Bearer $accessToken")

        call.enqueue(object : Callback<TokenSuccessResponse> {
            override fun onResponse(call: Call<TokenSuccessResponse>, response: Response<TokenSuccessResponse>) {
                if (response.isSuccessful) {
                    // 성공 응답 처리
                    val responseBody = response.body()
                    Log.i("TokenSend", "토큰 전송 성공: ${responseBody?.nickname}")

                    val intent = Intent(this@LoginActivity, PaylogActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티 종료
                } else {
                    // 실패 응답 처리 - 상태 코드와 메시지 로그에 출력
                    val errorBodyString = response.errorBody()?.string()
                    val statusCode = response.code() // 상태 코드 가져오기

                    if (errorBodyString != null) {
                        val gson = Gson()
                        val errorResponse = gson.fromJson(errorBodyString, TokenErrorResponse::class.java)
                        Log.e("TokenSend", "토큰 전송 실패 - 상태 코드: $statusCode, 에러: ${errorResponse.error}")
                    } else {
                        Log.e("TokenSend", "토큰 전송 실패 - 상태 코드: $statusCode, 알 수 없는 오류 발생")
                    }
                }
            }

            override fun onFailure(call: Call<TokenSuccessResponse>, t: Throwable) {
                Log.e("TokenSend", "토큰 전송 실패 - 네트워크 오류", t)
            }
        })
    }


    private fun performKakaoLogin(view: View) {
        UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
            if (error != null) {
                Log.e("KakaoLogin", "로그인 실패", error)
            } else if (token != null) {
                Log.i("KakaoLogin", "로그인 성공 ${token.accessToken}")
                sendAccessTokenToServer(token.accessToken)
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