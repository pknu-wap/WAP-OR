package com.example.wap_or

import Post
import PostAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wap_or.databinding.ActivityPaylogBinding

class PaylogActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPaylogBinding.inflate(layoutInflater) }
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 데이터 리스트 생성
        val postList = arrayListOf(
            Post("와포어 회원1", "1시간 전", "두솥도시락 - 한우함박스테이크\n과소비^^♡", "식비", 8900, 1, 3),
            Post("와포어 회원2", "2시간 전", "버거킹 - 와퍼세트", "식비", 10500, 2, 5),
            Post("와포어 회원3", "3시간 전", "스타벅스 - 아메리카노", "카페", 4500, 0, 1)
        )

        // 어댑터 초기화
        adapter = PostAdapter(postList)

        // RecyclerView 설정
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}