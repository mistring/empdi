package com.vivint.coroutines_sample.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.mistring.empdi.view.data.EmployeeAPI
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class EmployeeModule {

    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideEmployeeAPI(retrofit: Retrofit): EmployeeAPI =
        retrofit.create(EmployeeAPI::class.java)

    companion object {
        private const val BASE_URL = "https://s3.amazonaws.com/sq-mobile-interview/"
    }

}
