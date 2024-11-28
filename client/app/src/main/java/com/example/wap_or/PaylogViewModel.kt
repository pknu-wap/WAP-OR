package com.example.wap_or
import Post
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wap_or.utils.Constants
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PaylogService {
    @GET("paylog/search")
    suspend fun getPaylogs(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<List<Post>>
}
class PaylogViewModel(application: Application) : AndroidViewModel(application) {

    private val _paylogs = MutableLiveData<List<Post>>()
    val paylogs: LiveData<List<Post>> get() = _paylogs

    private var currentPage = 0
    private val pageSize = 6
    private var isLoading = false
    private var hasMoreData = true

    // Retrofit 인스턴스 설정
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val token = getAuthToken()
                    val requestBuilder = chain.request().newBuilder()
                    if (!token.isNullOrEmpty()) {
                        requestBuilder.addHeader("Authorization", "Bearer $token")
                    }
                    chain.proceed(requestBuilder.build())
                }
                .build()
        )
        .build()

    private val service = retrofit.create(PaylogService::class.java)

    private fun getAuthToken(): String? {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("appPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }

    fun loadPaylogs() {
        if (isLoading || !hasMoreData) return

        isLoading = true
        viewModelScope.launch {
            try {
                val response = service.getPaylogs(currentPage, pageSize)
                if (response.isSuccessful) {
                    val newPaylogs = response.body() ?: emptyList()
                    if (newPaylogs.isNotEmpty()) {
                        // 기존 데이터에 새 데이터를 추가
                        _paylogs.value = (_paylogs.value ?: emptyList()) + newPaylogs
                        currentPage++
                    } else {
                        hasMoreData = false
                    }
                } else {
                    Log.e("PaylogViewModel", "Failed to load paylogs: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("PaylogViewModel", "Error loading paylogs", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun resetPagination() {
        currentPage = 0
        hasMoreData = true
        _paylogs.value = emptyList()
    }
}
