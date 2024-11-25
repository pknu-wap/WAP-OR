package com.example.wap_or
import Comment
import CommentAdapter
import android.view.View
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wap_or.databinding.ActivityPostDetailBinding

class PostDetailActivity : BaseActivity() {
    private val binding by lazy { ActivityPostDetailBinding.inflate(layoutInflater) }
    private lateinit var adapter: CommentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupBottomNavigation()

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
}