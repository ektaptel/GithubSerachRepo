package  com.example.githubsearchrepo

import android.app.Application
import  com.example.githubsearchrepo.api.APIService
import  com.example.githubsearchrepo.api.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class GithubApplication : Application() {


    companion object {


          lateinit var service: APIService


        operator fun invoke(): APIService {
            service =  Retrofit.Builder()
                .baseUrl(Constants.HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService::class.java)

            return service;
        }


     }

    override fun onCreate() {
        super.onCreate()

         Timber.plant(Timber.DebugTree())

        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
           Timber.d(it)
        }).setLevel(HttpLoggingInterceptor.Level.BASIC)

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.HOST_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

//        service = retrofit.create(APIService::class.java)


    }


}