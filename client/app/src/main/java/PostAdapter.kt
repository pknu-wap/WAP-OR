import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wap_or.databinding.ItemPostBinding

// MyViewHolder 클래스 수정
class MyViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

// 어댑터 클래스 수정
class PostAdapter(private val datas: MutableList<Post>) : RecyclerView.Adapter<MyViewHolder>() {

    override fun getItemCount(): Int = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = datas[position]

        // 데이터 바인딩
        holder.binding.postUsername.text = post.memberName
        holder.binding.postCreatedDate.text = post.createdTime
        holder.binding.postMainText.text = post.postContent
        holder.binding.postCategory.text = post.category
        holder.binding.postCash.text = "${post.amount} won"
        holder.binding.postLikeCount.text = post.likes.toString()
        holder.binding.postCommentCount.text = post.comments.toString()

        // Glide를 사용해 이미지 로드
//        Glide.with(holder.itemView.context)
//            .load(post.imageUrl)
//            .placeholder(R.drawable.user_profile_tmp) // 기본 이미지 설정
//            .into(holder.binding.userProfile)
    }
}