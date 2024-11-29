package com.example.wap_or
import Post
import Comment
import CommentAdapter
import android.content.Context
import android.view.View
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.wap_or.databinding.ActivityPostDetailBinding
import com.example.wap_or.utils.Constants
import getRelativeTime
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailService {
    @GET("paylog/detail/{id}")
    suspend fun getDetail(
        @Path("id") paylogId: Long
    ): Response<Post>
}
class PostDetailActivity : BaseActivity() {
    private val binding by lazy { ActivityPostDetailBinding.inflate(layoutInflater) }
    private lateinit var adapter: CommentAdapter
    // Retrofit 인스턴스 설정
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val token = getAuthToken(this)
                    val requestBuilder = chain.request().newBuilder()
                    if (!token.isNullOrEmpty()) {
                        requestBuilder.addHeader("Authorization", "Bearer $token")
                    }
                    chain.proceed(requestBuilder.build())
                }
                .build()
        )
        .build()

    private val service = retrofit.create(DetailService::class.java)

    private fun getAuthToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("appPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupBottomNavigation()

        // Intent로 전달된 paylog_id 가져오기
        val paylogId = intent.getLongExtra("PAYLOG_ID", -1L) // 기본값 -1L 설정
        if (paylogId != -1L) {
            Log.d("PostDetail", "Fetching post detail for ID: $paylogId")
            fetchPostDetail(paylogId)
        }
        else {
            // paylogId가 없을 경우 처리
            showToast("Invalid paylog ID")
            finish() // 잘못된 ID로 Activity 종료
        }

        // 데이터 리스트 생성
        val CommentList = arrayListOf(
            Comment("User1", "2024-11-19", "좋은 글 감사합니다!", 10),
            Comment("User2", "2024-11-19", "동의합니다.", 5),
            Comment("User3", "2024-11-18", "도움이 되었습니다.", 8),
        )
        // RecyclerView 설정
        adapter = CommentAdapter(CommentList) // 어댑터 초기화
        binding.commentRecyclerView.adapter = adapter
        binding.commentRecyclerView.layoutManager = LinearLayoutManager(this)

        // 구분선 추가
        val dividerItemDecoration = DividerItemDecoration(
            binding.commentRecyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        ContextCompat.getDrawable(this, R.drawable.divider)?.let {
            dividerItemDecoration.setDrawable(it)
        }
        binding.commentRecyclerView.addItemDecoration(dividerItemDecoration)
        updateUI(CommentList)
    }
    private fun updateUI(CommentList: List<Comment>) {
        if (CommentList.isEmpty()) {
            // 댓글이 없을 경우
            binding.emptyMessageTextView.visibility = View.VISIBLE
            binding.commentRecyclerView.visibility = View.GONE
        } else {
            // 댓글이 있을 경우
            binding.emptyMessageTextView.visibility = View.GONE
            binding.commentRecyclerView.visibility = View.VISIBLE
        }
    }
    // 서버에서 Post 객체를 받아오는 함수
    private fun fetchPostDetail(paylogId: Long) {
        lifecycleScope.launch {
            try {
                val response = service.getDetail(paylogId) // API 호출
                if (response.isSuccessful) {
                    val post = response.body() // Post 객체
                    Log.d("PostDetail", "Response received: ${response.body()}")
                    if (post != null) {
                        runOnUiThread {
                            updateUI(post)
                        }
                        Log.d("PostDetail", "Updating UI with post: $post")
                    } else {
                        showToast("Post data is empty")
                    }
                } else {
                    showToast("Failed to fetch post details: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("An error occurred: ${e.message}")
            }
        }
    }


    // UI 업데이트 함수
    private fun updateUI(post: Post) {
        val defaultDrawable = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ex_img, // 기본 이미지 리소스 이름을 명시적으로 지정
            null
        )
        binding.itemPostDetail.apply {
            postUsername.text = post.userNickname
            postCreatedDate.text = getRelativeTime(post.createdAt)
            postMainText.text = post.content
            postCategory.text = post.category
            postCash.text = "${post.amount} won"
            postLikeCount.text = post.likeCount.toString()

            // Glide를 사용해 이미지 로드
            Glide.with(this@PostDetailActivity)
                .load(post.imgUrl)
                .placeholder(defaultDrawable) // 기본 이미지
                .into(postMainImg)
        }
        Log.d("PostDetail", "Setting username: ${post.userNickname}")
    }

    // Toast 메시지 출력 함수
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}