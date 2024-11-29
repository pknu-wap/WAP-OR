package com.example.wap_or
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.wap_or.databinding.ActivityWriteMainBinding
import com.example.wap_or.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import utils.LineBreakFilter


private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

class WriteActivity : AppCompatActivity() {

    // MainBinding 사용
    private lateinit var binding: ActivityWriteMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Camera launcher 설정
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                binding.writePaylog.addImg.setImageBitmap(bitmap)
            }
        }

        // Gallery launcher 설정
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data?.data
                binding.writePaylog.addImg.setImageURI(uri)
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

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish() // 현재 액티비티 종료
        }
    }
}