package com.example.wap_or
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DepositActivity : AppCompatActivity() {

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

        // 애니메이션 시작 버튼 클릭
        val submitButton = findViewById<View>(R.id.submitButton)
        submitButton.setOnClickListener {
            animatorSet.start()
        }
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish() // 현재 액티비티 종료
        }
    }
}