import okhttp3.MultipartBody
import kotlinx.serialization.Serializable

@Serializable
data class WritePaylog(
    val title: String,
    val content: String,
    val category: String,
    val amount: Long,
    val isPublic: Int
)
data class Image(
    val paylog: WritePaylog,
    val images: List<MultipartBody.Part> //카메라 or 갤러리에서 가져온 사진
)
