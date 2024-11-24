package com.example.wap_or

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.wap_or.databinding.ActivityWriteMainBinding

class WriteActivity : AppCompatActivity() {

    // MainBinding 사용
    private lateinit var binding: ActivityWriteMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Main 바인딩 초기화
        binding = ActivityWriteMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 포함된 레이아웃 접근
        val paylogBinding = binding.writePaylog

        // Paylog 버튼 및 동작 설정
        paylogBinding.togglePublic.setOnClickListener {
            paylogBinding.togglePrivate.isChecked = !paylogBinding.togglePublic.isChecked
        }

        paylogBinding.togglePrivate.setOnClickListener {
            paylogBinding.togglePublic.isChecked = !paylogBinding.togglePrivate.isChecked
        }
    }
}