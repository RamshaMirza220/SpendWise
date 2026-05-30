package com.ramsha.spendwise.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ramsha.spendwise.domain.model.Expense
import com.ramsha.spendwise.domain.model.ExpenseCategory

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val note: String,
    val timestamp: Long
) {
    fun toDomain(): Expense = Expense(
        id = id,
        title = title,
        amount = amount,
        category = ExpenseCategory.valueOf(category),
        note = note,
        timestamp = timestamp
    )
}

fun Expense.toEntity(): ExpenseEntity = ExpenseEntity(
    id = id,
    title = title,
    amount = amount,
    category = category.name,
    note = note,
    timestamp = timestamp
)
