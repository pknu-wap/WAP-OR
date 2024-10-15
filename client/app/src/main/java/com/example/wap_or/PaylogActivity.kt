package com.example.wap_or

import Post
import PostAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wap_or.databinding.ActivityPaylogMainBinding

class PaylogActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPaylogMainBinding.inflate(layoutInflater) }
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 데이터 리스트 생성
        val postList = arrayListOf(
            Post("와포어 회원1", "1시간 전", "두솥도시락 - 한우함박스테이크\n과소비^^♡", "식비", 8900, 1, 3),
            Post("와포어 회원2", "10시간 전", "시험기간에 몬스터 2캔 4600원 사치인가요\n진짜 너무 피곤한데 한 캔만 더 먹을지 고민", "식비", 2300, 1, 3),
            Post("와포어 회원3", "1일 전", "로스텍 마우스", "전자제품", 19800, 0, 2),
            Post("와포어 회원4", "1일 전", "연어초밥 18,000원\n이런 날도 있는거지", "식비", 18000, 2, 14),
            Post("와포어 회원5", "3일 전", "결국 샀습니다", "전자제품", 854900, 1, 28),
            Post("와포어 회원6", "3일 전", "바쁘다 바빠\n현대사회 속\n단비같은\n감각적 ...더보기 구현 어캐하지", "식비", 1200, 1, 9),
            Post("와포어 회원7", "3일 전", "꾸밈비. 세일할 때 사서 만오천원이에요\n진짜", "패션&잡화", 15900, 3, 1)
        )

        // 어댑터 초기화
        adapter = PostAdapter(postList)

        // RecyclerView 설정
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}