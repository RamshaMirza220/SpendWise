package com.ramsha.spendwise.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ramsha.spendwise.data.local.dao.ExpenseDao
import com.ramsha.spendwise.data.local.entities.ExpenseEntity

@Database(entities = [ExpenseEntity::class], version = 1, exportSchema = false)
abstract class SpendWiseDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}
