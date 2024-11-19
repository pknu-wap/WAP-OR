package com.example.wap_or
import Comment
import CommentAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wap_or.databinding.ActivityPostDetailBinding

class PostDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostDetailBinding
    private lateinit var commentAdapter: CommentAdapter
    private val commentList = mutableListOf<Comment>() // 댓글 데이터를 담을 리스트

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 데이터 바인딩 초기화
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 더미 데이터 추가 (실제 데이터로 교체 가능)
        commentList.add(Comment("User1", "2024-11-19", "좋은 글 감사합니다!", 10))
        commentList.add(Comment("User2", "2024-11-19", "동의합니다.", 5))
        commentList.add(Comment("User3", "2024-11-18", "도움이 되었습니다.", 8))

        // 어댑터 초기화
        commentAdapter = CommentAdapter(commentList)

        // 리사이클러뷰 설정
        binding.commentRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@PostDetailActivity) // 수직 스크롤 레이아웃
            adapter = commentAdapter
        }

        // 댓글 등록 버튼 클릭 리스너
        binding.registerButton.setOnClickListener {
            val newCommentText = binding.commentInput.text.toString()
            if (newCommentText.isNotEmpty()) {
                // 새로운 댓글 추가
                val newComment = Comment(
                    memberName = "NewUser", // 실제 사용자의 이름으로 교체
                    createdTime = "2024-11-19", // 현재 시간으로 대체 가능
                    commentContent = newCommentText,
                    comments = 0 // 기본 댓글 수
                )
                commentList.add(newComment)
                commentAdapter.notifyItemInserted(commentList.size - 1)
                binding.commentRecyclerView.scrollToPosition(commentList.size - 1)

                // 입력창 초기화
                binding.commentInput.text.clear()
            }
        }
    }
}