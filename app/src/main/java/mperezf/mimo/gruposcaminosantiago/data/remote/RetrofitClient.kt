package mperezf.mimo.gruposcaminosantiago.data.remote

import com.google.gson.GsonBuilder
import mperezf.mimo.gruposcaminosantiago.BuildConfig.DEBUG
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(private val baseUrl: String) {

    private fun getOkHttpClient(): OkHttpClient {
        val client = Builder()
        if (DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
        }
        return client.build()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .client(getOkHttpClient())
            .build()
    }

    fun getApiService(): ApiService {
        return getRetrofit().create(ApiService::class.java)
    }
}