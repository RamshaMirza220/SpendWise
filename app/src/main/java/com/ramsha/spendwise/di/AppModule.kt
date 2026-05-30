package com.ramsha.spendwise.di

import android.content.Context
import androidx.room.Room
import com.ramsha.spendwise.data.local.SpendWiseDatabase
import com.ramsha.spendwise.data.local.dao.ExpenseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SpendWiseDatabase =
        Room.databaseBuilder(
            context,
            SpendWiseDatabase::class.java,
            "spendwise_db"
        ).build()

    @Provides
    @Singleton
    fun provideExpenseDao(db: SpendWiseDatabase): ExpenseDao = db.expenseDao()
}
