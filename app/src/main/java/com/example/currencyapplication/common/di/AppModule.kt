package com.example.currencyapplication.common.di

import com.example.currencyapplication.common.sorting_preferences.SortingPreferences
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.currencyapplication.common.network.CurrencyService
import com.example.currencyapplication.common.utils.dataStore
import com.example.currencyapplication.common.repository.CurrencyRepositoryImpl
import com.example.currencyapplication.common.repository.CurrencyRepository
import com.example.currencyapplication.common.usecases.load_all_curencies.LoadCurrencyUseCase
import com.example.currencyapplication.common.usecases.load_all_curencies.LoadCurrencyUseCaseImpl
import com.example.currencyapplication.common.utils.Constants.API_HEADER
import com.example.currencyapplication.common.utils.Constants.API_KEY
import com.example.currencyapplication.common.utils.Constants.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader(API_HEADER, API_KEY)
                    .build()
                chain.proceed(request)
            }
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): CurrencyService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .build()
            .create(CurrencyService::class.java)

    @Provides
    @Singleton
    fun provideRepository(api: CurrencyService): CurrencyRepository =
        CurrencyRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideUseCase(repository: CurrencyRepository): LoadCurrencyUseCase =
        LoadCurrencyUseCaseImpl(repository)

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Provides
    @Singleton
    fun provideSortingPreferences(@ApplicationContext context: Context): SortingPreferences =
        SortingPreferences(context)
}

