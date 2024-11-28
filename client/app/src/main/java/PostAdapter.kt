import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wap_or.R
import com.example.wap_or.databinding.ItemPostBinding
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration

// MyViewHolder 클래스
class MyViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

class PostAdapter(
    private val context: Context,
    private var postList: MutableList<Post>,
    private val onItemClick: (Post) -> Unit
) : RecyclerView.Adapter<MyViewHolder>() {

    override fun getItemCount(): Int = postList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = postList[position]
        var isLiked = false;
        // 데이터 바인딩
        holder.binding.postUsername.text = post.userNickname
        holder.binding.postCreatedDate.text = getRelativeTime(post.createdAt)
        holder.binding.postMainText.text = post.content
        holder.binding.postCategory.text = post.category
        holder.binding.postCash.text = "${post.amount} won"
        holder.binding.postLikeCount.text = post.likeCount.toString()
        holder.binding.postCommentCount.text = "0"
        //holder.binding.postCommentCount.text = post.comments.toString()

        // 좋아요 아이콘 처리
        val likeIconRes = if (isLiked) R.drawable.post_like_full else R.drawable.post_like
        holder.binding.postLikeIcon.setImageResource(likeIconRes)

        holder.binding.postLikeIcon.setOnClickListener {
            isLiked = !isLiked
            notifyItemChanged(holder.bindingAdapterPosition)
        }

        // 메뉴 버튼 토글 처리
        holder.binding.menuButton.setOnClickListener {
            holder.binding.menu.visibility =
                if (holder.binding.menu.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        // 메뉴 닫기 처리
        holder.binding.post.setOnTouchListener { _, _ ->
            if (holder.binding.menu.visibility == View.VISIBLE) {
                holder.binding.menu.visibility = View.GONE
                return@setOnTouchListener true
            }
            false
        }

        // 메뉴 항목 클릭 이벤트 처리
        holder.binding.menu.setOnClickListener {
            val customDialogView = LayoutInflater.from(context).inflate(R.layout.custom_popup_report, null)
            val customDialog = AlertDialog.Builder(context, R.style.CustomAlertDialog)
                .setView(customDialogView)
                .create()
            customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val confirmButton = customDialogView.findViewById<TextView>(R.id.confirm_button)
            confirmButton.setOnClickListener {
                customDialog.dismiss()
            }

            val cancelButton = customDialogView.findViewById<ImageButton>(R.id.cancel_button)
            cancelButton.setOnClickListener {
                customDialog.dismiss()
            }
            customDialog.show()
        }

        // Glide 이미지 로드
        val defaultDrawable = ResourcesCompat.getDrawable(
            context.resources,
            R.drawable.ex_img, // 기본 이미지 리소스 이름을 명시적으로 지정
            null
        )
        Glide.with(context)
            .load(post.imgUrl) // 이미지 URL
            .placeholder(defaultDrawable) // 로딩 중 기본 이미지
            .error(defaultDrawable) // 에러 발생 시 기본 이미지
            .into(holder.binding.postMainImg)

        // 아이템 클릭 이벤트 처리
        holder.binding.root.setOnClickListener {
            onItemClick(post)
        }
    }

    // 데이터 업데이트 메서드
    fun updateData(newPosts: List<Post>) {
        postList.clear()
        postList.addAll(newPosts)
        notifyDataSetChanged()
    }
}