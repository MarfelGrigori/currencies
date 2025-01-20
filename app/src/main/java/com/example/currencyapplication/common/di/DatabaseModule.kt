package com.example.currencyapplication.common.di

import android.content.Context
import androidx.room.Room
import com.example.currencyapplication.common.usecases.delete_currency.DeleteCurrencyUseCaseImpl
import com.example.currencyapplication.common.usecases.add_favorite.AddFavoriteCurrencyUseCase
import com.example.currencyapplication.common.usecases.add_favorite.AddFavoriteCurrencyUseCaseImpl
import com.example.currencyapplication.common.repository.CurrenciesFavoriteRepository
import com.example.currencyapplication.common.repository.CurrenciesFavoriteRepositoryImpl
import com.example.currencyapplication.common.db.CurrenciesFavouriteDao
import com.example.currencyapplication.common.usecases.delete_currency.DeleteCurrencyUseCase
import com.example.currencyapplication.common.db.FavoriteCurrenciesDataBase
import com.example.currencyapplication.common.usecases.load_favorites.LoadFavoriteCurrenciesUseCase
import com.example.currencyapplication.common.usecases.load_favorites.LoadFavoriteCurrenciesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): FavoriteCurrenciesDataBase =
        Room.databaseBuilder(
            context,
            FavoriteCurrenciesDataBase::class.java,
            "database"
        ).build()

    @Provides
    fun provideDao(
        database: FavoriteCurrenciesDataBase
    ): CurrenciesFavouriteDao = database.currenciesFavoritesDao()

    @Provides
    @Singleton
    fun provideFavoriteRatesRepository(
        currenciesFavouriteDao: CurrenciesFavouriteDao
    ): CurrenciesFavoriteRepository =
        CurrenciesFavoriteRepositoryImpl(currenciesFavouriteDao)

    @Provides
    @Singleton
    fun provideFavoritesCurrenciesUseCase(
        repository: CurrenciesFavoriteRepository
    ): LoadFavoriteCurrenciesUseCase =
        LoadFavoriteCurrenciesUseCaseImpl(repository)

    @Provides
    @Singleton
    fun provideAddFavoriteCurrencyUseCase(
        repository: CurrenciesFavoriteRepository
    ): AddFavoriteCurrencyUseCase =
        AddFavoriteCurrencyUseCaseImpl(repository)

    @Provides
    @Singleton
    fun provideDeleteFavoriteCurrencyUseCase(
        repository: CurrenciesFavoriteRepository
    ): DeleteCurrencyUseCase =
        DeleteCurrencyUseCaseImpl(repository)
}