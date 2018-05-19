package com.ekalips.cahscrowd.di.modules

import com.ekalips.cahscrowd.BuildConfig
import com.ekalips.cahscrowd.network.Api
import com.ekalips.cahscrowd.network.interceptor.BearerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {


    @Provides
    @Singleton
    internal fun provideMoshi(): Moshi {
        return Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
    }

    @Provides
    @Singleton
    internal fun providerConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    internal fun provideLoggingInterceptor(): HttpLoggingInterceptor? {
        return if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            loggingInterceptor
        } else {
            null
        }
    }

    @Provides
    @Singleton
    internal fun provideBearerInterceptor(): BearerInterceptor {
        return BearerInterceptor()
    }


    @Provides
    @Singleton
    internal fun provideClient(loggingInterceptor: HttpLoggingInterceptor?, bearerInterceptor: BearerInterceptor): OkHttpClient {
        val okhttpBuilder = OkHttpClient.Builder()
        loggingInterceptor?.let { okhttpBuilder.addInterceptor(it) }
        okhttpBuilder.addInterceptor(bearerInterceptor)
        return okhttpBuilder.build()
    }


    @Provides
    @Singleton
    internal fun provideRetrofit(moshiConverterFactory: MoshiConverterFactory, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .client(client)
                .addConverterFactory(moshiConverterFactory)
                .baseUrl(BuildConfig.SERVER_URL)
                .build()
    }

    @Provides
    @Singleton
    internal fun provideApiService(retrofit: Retrofit): Api {
        return retrofit.create<Api>(Api::class.java)
    }

}