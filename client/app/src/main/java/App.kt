package com.example.wap_or
import android.app.Application
import android.content.Context


class App : Application() {

    companion object {
        private lateinit var instance: App

        fun getContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("appPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }
}