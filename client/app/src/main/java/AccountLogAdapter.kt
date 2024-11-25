import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wap_or.databinding.ItemAccountLogBinding

// MyViewHolder 클래스
class AccountLogViewHolder(val binding: ItemAccountLogBinding) : RecyclerView.ViewHolder(binding.root)

// 어댑터 클래스
class AccountLogAdapter(private val datas: MutableList<AccountLog>,
) : RecyclerView.Adapter<AccountLogViewHolder>() {

    override fun getItemCount(): Int = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountLogViewHolder {
        val binding = ItemAccountLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountLogViewHolder, position: Int) {
        val accountLog = datas[position]

        // 데이터 바인딩
        holder.binding.accountDate.text = accountLog.date
        holder.binding.accountCategory.text = accountLog.category
        holder.binding.accountDepositCash.text = accountLog.depositCash
        holder.binding.accountRemainder.text = accountLog.remainder
    }
}