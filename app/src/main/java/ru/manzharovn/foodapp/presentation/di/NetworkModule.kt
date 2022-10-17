package ru.manzharovn.foodapp.presentation.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.manzharovn.foodapp.data.network.FoodApiInfo
import javax.inject.Singleton


@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(FoodApiInfo.BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideOkHttpClient() : OkHttpClient {
        val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val url = original
                .url
                .newBuilder()
                .build()
            chain.proceed(
                original
                    .newBuilder()
                    .url(url)
                    .addHeader("X-RapidAPI-Key", FoodApiInfo.API_KEY)
                    .build()
            )
        }
        return okHttpClient.build()
    }
}