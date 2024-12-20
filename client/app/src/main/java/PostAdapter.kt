import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wap_or.databinding.ItemPostBinding

// MyViewHolder 클래스
class MyViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

// 어댑터 클래스
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

        // 기본 이미지 리소스 가져오기
        val context = holder.binding.postMainImg.context
        val defaultDrawable = ResourcesCompat.getDrawable(context.resources,
            context.resources.getIdentifier("ex_img", "drawable", context.packageName),
            null)

        // Glide를 사용해 이미지 로드
        if (!post.imageUrl.isNullOrEmpty()) {
            // 이미지 URL이 있으면 Glide로 이미지를 로드
            Glide.with(context)
                .load(post.imageUrl)
                .placeholder(defaultDrawable) // 로딩 중일 때 표시할 기본 이미지
                .error(defaultDrawable)       // 에러 발생 시 표시할 기본 이미지
                .into(holder.binding.postMainImg)
        } else {
            // 이미지 URL이 없으면 기본 XML 드로어블 사용
            holder.binding.postMainImg.setImageDrawable(defaultDrawable)
        }
    }
}