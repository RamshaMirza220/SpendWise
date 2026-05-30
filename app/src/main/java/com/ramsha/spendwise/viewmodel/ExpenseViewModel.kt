package com.ramsha.spendwise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramsha.spendwise.data.repository.ExpenseRepository
import com.ramsha.spendwise.domain.model.Expense
import com.ramsha.spendwise.domain.model.ExpenseCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val recentExpenses: List<Expense> = emptyList(),
    val totalThisMonth: Double = 0.0,
    val monthlyBudget: Double = 50000.0,
    val categoryBreakdown: Map<ExpenseCategory, Double> = emptyMap(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val _allExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val allExpenses: StateFlow<List<Expense>> = _allExpenses.asStateFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            combine(
                repository.getAllExpenses(),
                repository.getTotalThisMonth()
            ) { expenses, total ->
                val breakdown = ExpenseCategory.entries.associateWith { cat ->
                    expenses.filter { it.category == cat }.sumOf { it.amount }
                }.filter { it.value > 0.0 }

                _allExpenses.value = expenses

                DashboardUiState(
                    recentExpenses = expenses.take(10),
                    totalThisMonth = total,
                    categoryBreakdown = breakdown,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.addExpense(expense)
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    fun setMonthlyBudget(amount: Double) {
        _uiState.update { it.copy(monthlyBudget = amount) }
    }
}
