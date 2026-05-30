package com.ramsha.spendwise.data.repository

import com.ramsha.spendwise.data.local.dao.ExpenseDao
import com.ramsha.spendwise.data.local.entities.toEntity
import com.ramsha.spendwise.domain.model.Expense
import com.ramsha.spendwise.domain.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
    private val dao: ExpenseDao
) {
    fun getAllExpenses(): Flow<List<Expense>> =
        dao.getAllExpenses().map { list -> list.map { it.toDomain() } }

    fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>> =
        dao.getExpensesByCategory(category.name).map { list -> list.map { it.toDomain() } }

    fun getTotalThisMonth(): Flow<Double> =
        dao.getTotalThisMonth(startOfCurrentMonth()).map { it ?: 0.0 }

    fun getCategoryTotalThisMonth(category: ExpenseCategory): Flow<Double> =
        dao.getCategoryTotalThisMonth(category.name, startOfCurrentMonth()).map { it ?: 0.0 }

    suspend fun addExpense(expense: Expense) = dao.insertExpense(expense.toEntity())

    suspend fun deleteExpense(expense: Expense) = dao.deleteExpense(expense.toEntity())

    private fun startOfCurrentMonth(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
