package com.example.wap_or
import Amount
import AmountResponse
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wap_or.model.LoginRequest
import com.example.wap_or.utils.Constants
import data.TokenSuccessResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

private lateinit var Balance: TextView
class DepositActivity : AppCompatActivity() {
    interface ApiService {
        @POST("/api/virtual-account/deposit")
        fun deposit(
            @Body amount: Amount
        ): Call<AmountResponse>
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
        setContentView(R.layout.activity_deposit)

        // 동전 ImageView 가져오기
        val won10 = findViewById<View>(R.id.won10)
        val won100 = findViewById<View>(R.id.won100)
        val won500 = findViewById<View>(R.id.won500)
        val cartSamkim = findViewById<View>(R.id.cartSamkim)

        // 카트 위치
        val cartY = cartSamkim.y

        // 동전 10원 애니메이션
        val won10Animator = ObjectAnimator.ofFloat(won10, "translationY", won10.y, cartY)
        won10Animator.duration = 1000

        // 동전 100원 애니메이션
        val won100Animator = ObjectAnimator.ofFloat(won100, "translationY", won100.y, cartY)
        won100Animator.duration = 1200

        // 동전 500원 애니메이션
        val won500Animator = ObjectAnimator.ofFloat(won500, "translationY", won500.y, cartY)
        won500Animator.duration = 1400

        // 애니메이션 그룹화
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(won10Animator, won100Animator, won500Animator)

        Balance = findViewById(R.id.balance)
        val writeButton = findViewById<ImageButton>(R.id.write)
        writeButton.setOnClickListener{
            val customDialogView = layoutInflater.inflate(R.layout.custom_popup_deposit_real, null)
            val customDialog = AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setView(customDialogView)
                .create()
            customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            customDialog.show()
            val priceText = customDialogView.findViewById<EditText>(R.id.price_text)
            val confirmButton = customDialogView.findViewById<TextView>(R.id.confirm_button)
            val cancelButton = customDialogView.findViewById<ImageButton>(R.id.cancel_button)
            confirmButton.setOnClickListener {
                val priceString = priceText.text.toString().trim() // 입력값 가져오기
                if (priceString.isNotEmpty()) {
                    try {
                        val priceValue = priceString.toLong() // 숫자로 변환
                        Balance.text = priceValue.toString() // 숫자값만 설정
                    } catch (e: NumberFormatException) {
                        // 숫자로 변환 불가능한 경우 처리 (예: 알림 표시)
                        Toast.makeText(this, "숫자만 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
                customDialog.dismiss() // 다이얼로그 닫기
            }
            cancelButton.setOnClickListener {
                customDialog.dismiss()
            }

        }
        // 애니메이션 시작 버튼 클릭
        val submitButton = findViewById<View>(R.id.submitButton)
        submitButton.setOnClickListener {
            val priceLong = Balance.text.toString().toLong()
            val amountData = Amount(amount = priceLong)
            sendDataToServer(amountData)
            animatorSet.start()
        }
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish() // 현재 액티비티 종료
        }
    }
    private fun sendDataToServer(amountData: Amount) {
            RetrofitInstance.apiService.deposit(amountData).enqueue(object : Callback<AmountResponse> {
                override fun onResponse(call: Call<AmountResponse>, response: Response<AmountResponse>) {
                    try {
                        Log.i("DepositResponse", "Status Code: ${response.code()}")

                        when (response.code()) {
                            200 -> {
                                // 성공 응답 처리
                                val responseBody = response.body()
                                if (responseBody != null) {
                                    Log.i("DepositSuccess", "입금 성공")
                                    // 응답 본문 처리 (예: accountId, Balance)
                                    val accountId = responseBody.accountid
                                    val balance = responseBody.Balance
                                    Log.i("DepositSuccess", "AccountId: $accountId, Balance: $balance")
                                    finish() // 현재 액티비티 종료
                                } else {
                                    Log.e("DepositError", "응답 본문이 비어 있음")
                                }
                            }
                            else -> {
                                val errorBody = response.errorBody()?.string()
                                Log.i("DepositResponse", "Other Response: $errorBody")
                                Toast.makeText(this@DepositActivity, "입금 실패: ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("DepositError", "Exception occurred: ${e.message}")
                    }
                }

                override fun onFailure(call: Call<AmountResponse>, t: Throwable) {
                    Log.e("DepositError", "Network error: ${t.message}")
                    Toast.makeText(this@DepositActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
                }
            })
        // Retrofit 호출

    }
}