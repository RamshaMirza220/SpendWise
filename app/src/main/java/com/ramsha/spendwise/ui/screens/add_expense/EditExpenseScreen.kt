package com.ramsha.spendwise.ui.screens.add_expense

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramsha.spendwise.domain.model.Expense
import com.ramsha.spendwise.domain.model.ExpenseCategory
import com.ramsha.spendwise.ui.theme.PrimaryGreen
import com.ramsha.spendwise.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExpenseScreen(
    expense: Expense,
    viewModel: ExpenseViewModel,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(expense.title) }
    var amount by remember { mutableStateOf(expense.amount.toString()) }
    var note by remember { mutableStateOf(expense.note) }
    var selectedCategory by remember { mutableStateOf(expense.category) }
    var titleError by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Expense") },
            text = { Text("Are you sure you want to delete \"${expense.title}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteExpense(expense)
                        onBack()
                    }
                ) {
                    Text("Delete", color = Color(0xFFD32F2F))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Expense", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { showDeleteDialog = true }) {
                        Text("Delete", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it; titleError = false },
                label = { Text("Expense Title") },
                isError = titleError,
                supportingText = { if (titleError) Text("Title is required") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it; amountError = false },
                label = { Text("Amount (Rs)") },
                isError = amountError,
                supportingText = { if (amountError) Text("Enter a valid amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Text("Category", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.height(200.dp),
                userScrollEnabled = false,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ExpenseCategory.entries) { category ->
                    val isSelected = selectedCategory == category
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) category.color.copy(alpha = 0.2f) else Color.White)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) category.color else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedCategory = category }
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(category.icon, fontSize = 22.sp)
                        Text(
                            category.label.split(" ").first(),
                            fontSize = 10.sp,
                            color = if (isSelected) category.color else Color.Gray
                        )
                    }
                }
            }

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (optional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 2
            )

            Button(
                onClick = {
                    titleError = title.isBlank()
                    val parsedAmount = amount.toDoubleOrNull()
                    amountError = parsedAmount == null || parsedAmount <= 0
                    if (!titleError && !amountError) {
                        viewModel.deleteExpense(expense)
                        viewModel.addExpense(
                            expense.copy(
                                title = title.trim(),
                                amount = parsedAmount!!,
                                category = selectedCategory,
                                note = note.trim()
                            )
                        )
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
