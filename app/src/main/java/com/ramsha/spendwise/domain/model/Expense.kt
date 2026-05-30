package com.ramsha.spendwise.domain.model

import androidx.compose.ui.graphics.Color

enum class ExpenseCategory(val label: String, val color: Color, val icon: String) {
    FOOD("Food & Dining", Color(0xFFE57373), "🍔"),
    TRANSPORT("Transport", Color(0xFF64B5F6), "🚗"),
    SHOPPING("Shopping", Color(0xFFBA68C8), "🛍️"),
    HEALTH("Health", Color(0xFF81C784), "💊"),
    ENTERTAINMENT("Entertainment", Color(0xFFFFD54F), "🎮"),
    BILLS("Bills & Utilities", Color(0xFF4DB6AC), "💡"),
    EDUCATION("Education", Color(0xFFFF8A65), "📚"),
    OTHER("Other", Color(0xFF90A4AE), "💰")
}

data class Expense(
    val id: Int = 0,
    val title: String,
    val amount: Double,
    val category: ExpenseCategory,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class BudgetLimit(
    val category: ExpenseCategory,
    val limitAmount: Double
)
