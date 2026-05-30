package com.ramsha.spendwise.ui

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ramsha.spendwise.ui.screens.add_expense.AddExpenseScreen
import com.ramsha.spendwise.ui.screens.add_expense.EditExpenseScreen
import com.ramsha.spendwise.ui.screens.analytics.AnalyticsScreen
import com.ramsha.spendwise.ui.screens.auth.AuthScreen
import com.ramsha.spendwise.ui.screens.budget_setup.BudgetSetupScreen
import com.ramsha.spendwise.ui.screens.dashboard.DashboardScreen
import com.ramsha.spendwise.viewmodel.AuthViewModel
import com.ramsha.spendwise.viewmodel.ExpenseViewModel

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object BudgetSetup : Screen("budget_setup")
    object Dashboard : Screen("dashboard")
    object AddExpense : Screen("add_expense")
    object EditExpense : Screen("edit_expense/{expenseId}")
    object Analytics : Screen("analytics")
}

@Composable
fun SpendWiseNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val expenseViewModel: ExpenseViewModel = hiltViewModel()

    val startDestination = if (authViewModel.isLoggedIn) Screen.Dashboard.route else Screen.Auth.route

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Auth.route) {
            AuthScreen(
                authViewModel = authViewModel,
                onAuthSuccess = {
                    navController.navigate(Screen.BudgetSetup.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.BudgetSetup.route) {
            BudgetSetupScreen(
                viewModel = expenseViewModel,
                onDone = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.BudgetSetup.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = expenseViewModel,
                onAddExpense = { navController.navigate(Screen.AddExpense.route) },
                onViewAnalytics = { navController.navigate(Screen.Analytics.route) },
                onEditExpense = { expenseId ->
                    navController.navigate("edit_expense/$expenseId")
                },
                onLogout = {
                    authViewModel.signOut()
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AddExpense.route) {
            AddExpenseScreen(
                viewModel = expenseViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.EditExpense.route) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getString("expenseId")?.toIntOrNull()
            val allExpenses by expenseViewModel.allExpenses.collectAsState()
            val expense = allExpenses.find { it.id == expenseId }
            if (expense != null) {
                EditExpenseScreen(
                    expense = expense,
                    viewModel = expenseViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.Analytics.route) {
            AnalyticsScreen(
                viewModel = expenseViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
