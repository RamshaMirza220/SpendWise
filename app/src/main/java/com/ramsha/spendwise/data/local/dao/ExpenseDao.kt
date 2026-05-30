package com.ramsha.spendwise.data.local.dao

import androidx.room.*
import com.ramsha.spendwise.data.local.entities.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY timestamp DESC")
    fun getExpensesByCategory(category: String): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(amount) FROM expenses WHERE timestamp >= :startOfMonth")
    fun getTotalThisMonth(startOfMonth: Long): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses WHERE category = :category AND timestamp >= :startOfMonth")
    fun getCategoryTotalThisMonth(category: String, startOfMonth: Long): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Int): ExpenseEntity?
}
