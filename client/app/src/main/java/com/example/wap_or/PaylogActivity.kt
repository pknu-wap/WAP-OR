package com.example.wap_or
import Post
import PostAdapter
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wap_or.databinding.ActivityPaylogMainBinding
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView


class PaylogActivity : BaseActivity() {
    private val binding by lazy { ActivityPaylogMainBinding.inflate(layoutInflater) }
    private lateinit var adapter: PostAdapter
    private val viewModel by viewModels<PaylogViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupBottomNavigation()

        // 데이터 리스트 생성
//        val postList = arrayListOf(
//            Post(null, "와포어 회원1", "1시간 전", "두솥도시락 - 한우함박스테이크\n과소비^^♡", "식비", 8900, 1, 3),
//            Post("https://dimg.donga.com/wps/NEWS/IMAGE/2021/11/12/110217903.2.jpg", "와포어 회원2", "10시간 전", "시험기간에 몬스터 2캔 4600원 사치인가요\n진짜 너무 피곤한데 한 캔만 더 먹을지 고민", "식비", 2300, 1, 3),
//            Post(null, "와포어 회원3", "1일 전", "로스텍 마우스", "전자제품", 19800, 0, 2),
//            Post("https://d12zq4w4guyljn.cloudfront.net/750_750_20240220124444_photo1_cb08696e3d69.jpg", "와포어 회원4", "1일 전", "연어초밥 18,000원\n이런 날도 있는거지", "식비", 18000, 2, 14),
//            Post("https://thumbnews.nateimg.co.kr/view610///news.nateimg.co.kr/orgImg/ns/2022/04/23/NISI20220422_0000981000_web.jpg", "와포어 회원5", "3일 전", "결국 샀습니다", "전자제품", 854900, 1, 28),
//            Post(null, "와포어 회원6", "3일 전", "바쁘다 바빠\n현대사회 속\n단비같은\n감각적 ...더보기 구현 어캐하지", "식비", 1200, 1, 9),
//            Post("https://sellt.co.kr/file_data/sellt/2024/06/21/62caf0214a6493248fc01eab35d7d07c.jpg", "와포어 회원7", "3일 전", "꾸밈비. 세일할 때 사서 만오천원이에요\n진짜", "패션&잡화", 15900, 3, 1)
//        )

        // 어댑터 초기화
        adapter = PostAdapter(this, mutableListOf()) { post ->
            val intent = Intent(this, PostDetailActivity::class.java)
            // intent.putExtra("POST_ID", post.id) // 필요시 데이터 전달
            startActivity(intent)
        }

        // 스크롤 이벤트 리스너 추가
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (lastVisibleItemPosition == totalItemCount - 1) {
                    viewModel.loadPaylogs()
                }
            }
        })

        // ViewModel 데이터 관찰
        viewModel.paylogs.observe(this) { paylogs ->
            adapter.updateData(paylogs)
        }

        // 초기 데이터 로드
        viewModel.loadPaylogs()

        // RecyclerView 설정
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerView.getContext(),
            LinearLayoutManager.VERTICAL
        )
        ContextCompat.getDrawable(this, R.drawable.divider)
            ?.let { dividerItemDecoration.setDrawable(it) }
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        binding.writePostIcon.setOnClickListener {
            val intent = Intent(this, WriteActivity::class.java)
            startActivity(intent)
        }
    }
}