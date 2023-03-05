package com.example.recipeapp.roomdb

import android.content.Context
import androidx.room.Room
import com.example.recipeapp.roomdb.dao.FavouriteDAO
import com.example.recipeapp.roomdb.dao.ShoppingListDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideFavouriteDao(appDatabase: AppDatabase): FavouriteDAO {
        return appDatabase.favouriteDAO()
    }

    @Provides
    fun provideShoppingListDao(appDatabase: AppDatabase): ShoppingListDAO {
        return appDatabase.shoppingListDAO()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "FavouriteTable"
        ).fallbackToDestructiveMigration().build()
    }
}