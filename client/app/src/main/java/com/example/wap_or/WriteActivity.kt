package com.example.wap_or
import WriteResponse
import WritePaylog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.wap_or.databinding.ActivityWriteMainBinding
import com.example.wap_or.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody.Companion.toRequestBody
import utils.LineBreakFilter
import java.io.File

interface WriteService {
    @Multipart
    @POST("paylog")
    @Headers("Content-Type: multipart/form-data")
    suspend fun uploadWrite(
        @Part("paylog") paylog: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<WriteResponse>
}
private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
private var selectedImageUri: Uri? = null
private val TAG = "WriteActivity"
class WriteActivity : AppCompatActivity() {

    // MainBinding 사용
    private lateinit var binding: ActivityWriteMainBinding

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

    private val service = retrofit.create(WriteService::class.java)

    private fun getAuthToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("appPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Camera launcher 설정
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                binding.writePaylog.addImg.setImageBitmap(imageBitmap)
                // 비트맵을 파일로 저장하고 Uri 생성
                val file = File(cacheDir, "temp_camera_image.jpg")
                file.outputStream().use { out ->
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                }
                selectedImageUri = Uri.fromFile(file)
            }
        }

        // Gallery launcher 설정
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                selectedImageUri = result.data?.data
                binding.writePaylog.addImg.setImageURI(selectedImageUri)
            }
        }

        // 포함된 레이아웃 접근
        val paylogBinding = binding.writePaylog
        //초기설정
        paylogBinding.togglePublic.isChecked = true
        paylogBinding.categoryFood.isChecked = true

        // Paylog 버튼 및 동작 설정
        paylogBinding.togglePublic.setOnClickListener {
            paylogBinding.togglePublic.isChecked = true
            paylogBinding.togglePrivate.isChecked = false

            // 배경 설정
            paylogBinding.togglePublic.setBackgroundResource(R.drawable.rounded_border3)
            paylogBinding.togglePrivate.setBackgroundResource(R.drawable.rounded_border4)

            //필수,선택
            paylogBinding.requiredLabel4.text = "필수"
            paylogBinding.requiredLabel4.setTextColor(Color.parseColor("#FF6464"))
        }

        paylogBinding.togglePrivate.setOnClickListener {
            paylogBinding.togglePrivate.isChecked = true
            paylogBinding.togglePublic.isChecked = false

            // 배경 설정
            paylogBinding.togglePrivate.setBackgroundResource(R.drawable.rounded_border3)
            paylogBinding.togglePublic.setBackgroundResource(R.drawable.rounded_border4)

            //필수,선택
            paylogBinding.requiredLabel4.text = "선택"
            paylogBinding.requiredLabel4.setTextColor(Color.parseColor("#99000000"))
        }

        val toggleButtons = listOf(
            paylogBinding.categoryFood,
            paylogBinding.categoryCafe,
            paylogBinding.categoryLife,
            paylogBinding.categoryElectronics,
            paylogBinding.categoryHobby,
            paylogBinding.categoryEtc
        )

        // 각 토글 버튼 클릭 리스너 설정
        toggleButtons.forEach { button ->
            button.setOnClickListener {
                // 모든 버튼 비활성화 및 기본 배경 설정
                toggleButtons.forEach { btn ->
                    btn.isChecked = false
                    btn.setBackgroundResource(R.drawable.rounded_border4)
                }

                // 클릭된 버튼 활성화 및 선택된 배경 설정
                button.isChecked = true
                button.setBackgroundResource(R.drawable.rounded_border3)
            }
        }


        paylogBinding.cammerIcon.setOnClickListener{
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(cameraIntent)
        }
        paylogBinding.galleryIcon.setOnClickListener{
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(galleryIntent)
        }
        paylogBinding.addImg.setOnClickListener{
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(galleryIntent)
        }

        val postContent = findViewById<EditText>(R.id.postContent)
        postContent.filters = arrayOf<InputFilter>(
            LineBreakFilter(9),  // 줄바꿈 9회 이하 제한
            LengthFilter(300)
        )
        // 선택된 카테고리를 반환하는 함수
        fun getSelectedCategory(): String {
            return when {
                paylogBinding.categoryFood.isChecked -> "음식"
                paylogBinding.categoryCafe.isChecked -> "카페"
                paylogBinding.categoryLife.isChecked -> "생필품"
                paylogBinding.categoryElectronics.isChecked -> "전자기기"
                paylogBinding.categoryHobby.isChecked -> "취미생활"
                paylogBinding.categoryEtc.isChecked -> "기타"
                else -> "음식" // 기본값
            }
        }

        // ImageView에서 파일을 가져오는 함수 (구현 필요)
        fun getImageFileFromImageView(imageView: ImageView): File? {
            selectedImageUri?.let { uri ->
                val inputStream = contentResolver.openInputStream(uri)
                val file = File(cacheDir, "temp_image.jpg")
                inputStream?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                return file
            }
            return null
        }

        val registerButton = findViewById<Button>(R.id.register_button)
        registerButton.setOnClickListener {
            // 1. WrtiePaylog 객체 생성
            val writePaylog = WritePaylog(
                title = "WAPOR",
                content = paylogBinding.postContent.text.toString(),
                category = getSelectedCategory(),
                amount = paylogBinding.amountInput.text.toString().toLongOrNull() ?: 0L,
                isPublic = if (paylogBinding.togglePublic.isChecked) 1 else 0
            )

            // 2. 이미지 준비
            val imageFile = getImageFileFromImageView(paylogBinding.addImg)
            val imageParts = mutableListOf<MultipartBody.Part>()
            if (imageFile != null) {
                val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData("images", imageFile.name, requestFile)
                imageParts.add(imagePart)
            }

            // 3. paylog를 JSON으로 변환
            val paylogJson = Json.encodeToString(writePaylog)
            val paylogRequestBody = paylogJson.toRequestBody("application/json".toMediaTypeOrNull())

            // 4. 서버에 데이터 전송
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = service.uploadWrite(paylogRequestBody, imageParts)
                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "업로드 성공")
                            finish() // 현재 액티비티 종료
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Log.e(TAG, "업로드 실패: ${response.message()}")
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e(TAG, "오류 발생: ${e.message}", e)
                    }
                }
            }
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish() // 현재 액티비티 종료
        }

    }

}