package com.ramsha.spendwise.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramsha.spendwise.domain.model.Expense
import com.ramsha.spendwise.ui.theme.PrimaryGreen
import com.ramsha.spendwise.viewmodel.ExpenseViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: ExpenseViewModel,
    onAddExpense: () -> Unit,
    onViewAnalytics: () -> Unit,
    onEditExpense: (Int) -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val fmt = NumberFormat.getInstance(Locale("en", "PK"))
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(onClick = { onLogout() }) {
                    Text("Logout", color = Color(0xFFD32F2F))
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("SpendWise", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        Text("Track. Budget. Save.", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onViewAnalytics) {
                        Icon(Icons.Default.BarChart, contentDescription = "Analytics", tint = Color.White)
                    }
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddExpense,
                containerColor = PrimaryGreen,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                BudgetSummaryCard(
                    total = uiState.totalThisMonth,
                    budget = uiState.monthlyBudget,
                    fmt = fmt
                )
            }

            item {
                if (uiState.categoryBreakdown.isNotEmpty()) {
                    Text("Category Breakdown", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
                    uiState.categoryBreakdown.entries.sortedByDescending { it.value }.take(4).forEach { (cat, amount) ->
                        val progress = if (uiState.totalThisMonth > 0) (amount / uiState.totalThisMonth).toFloat() else 0f
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(cat.icon, fontSize = 20.sp, modifier = Modifier.width(32.dp))
                            Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(cat.label, fontSize = 13.sp)
                                    Text("Rs ${fmt.format(amount)}", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                }
                                Spacer(Modifier.height(2.dp))
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                    color = cat.color,
                                    trackColor = cat.color.copy(alpha = 0.15f)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text("Recent Transactions", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
            }

            if (uiState.recentExpenses.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No expenses yet.\nTap + to add one!", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            } else {
                items(uiState.recentExpenses) { expense ->
                    ExpenseCard(
                        expense = expense,
                        fmt = fmt,
                        onEdit = { onEditExpense(expense.id) },
                        onDelete = { viewModel.deleteExpense(expense) }
                    )
                }
            }
        }
    }
}

@Composable
fun BudgetSummaryCard(total: Double, budget: Double, fmt: NumberFormat) {
    val progress = (total / budget).coerceIn(0.0, 1.0).toFloat()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryGreen)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Monthly Budget", color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
            Spacer(Modifier.height(4.dp))
            Text("Rs ${fmt.format(total)}", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text("of Rs ${fmt.format(budget)} budget", color = Color.White.copy(alpha = 0.75f), fontSize = 13.sp)
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f)
            )
            Spacer(Modifier.height(6.dp))
            Text("Rs ${fmt.format(budget - total)} remaining", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
        }
    }
}

@Composable
fun ExpenseCard(
    expense: Expense,
    fmt: NumberFormat,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Expense") },
            text = { Text("Delete \"${expense.title}\"?") },
            confirmButton = {
                TextButton(onClick = { onDelete(); showDeleteDialog = false }) {
                    Text("Delete", color = Color(0xFFD32F2F))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(expense.category.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(expense.category.icon, fontSize = 20.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(expense.title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text("${expense.category.label} • ${sdf.format(Date(expense.timestamp))}", color = Color.Gray, fontSize = 12.sp)
            }
            Text("- Rs ${fmt.format(expense.amount)}", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(Modifier.width(8.dp))
            // Edit button
            IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = PrimaryGreen, modifier = Modifier.size(18.dp))
            }
            // Delete button
            IconButton(onClick = { showDeleteDialog = true }, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFD32F2F), modifier = Modifier.size(18.dp))
            }
        }
    }
}
