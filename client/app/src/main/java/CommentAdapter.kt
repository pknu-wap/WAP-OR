import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wap_or.databinding.CommentPostBinding

// MyViewHolder 클래스
class CommentViewHolder(val binding: CommentPostBinding) : RecyclerView.ViewHolder(binding.root)

// 어댑터 클래스
class CommentAdapter(private val datas: MutableList<Comment>,
    ) : RecyclerView.Adapter<CommentViewHolder>() {

    override fun getItemCount(): Int = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = CommentPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = datas[position]

        // 데이터 바인딩
        holder.binding.commentPostUsername.text = comment.memberName
        holder.binding.postCreatedDate.text = comment.createdTime
        holder.binding.postMainText.text = comment.commentContent
        holder.binding.postCommentCount.text = comment.comments.toString()
    }
}