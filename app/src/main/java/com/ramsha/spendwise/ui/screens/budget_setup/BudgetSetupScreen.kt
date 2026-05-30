package com.ramsha.spendwise.ui.screens.budget_setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramsha.spendwise.ui.theme.PrimaryGreen
import com.ramsha.spendwise.viewmodel.ExpenseViewModel

@Composable
fun BudgetSetupScreen(
    viewModel: ExpenseViewModel,
    onDone: () -> Unit
) {
    var budgetInput by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryGreen),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("💰", fontSize = 48.sp)
                Spacer(Modifier.height(12.dp))
                Text(
                    "Set Monthly Budget",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C2833)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "How much do you plan to spend this month?",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = budgetInput,
                    onValueChange = {
                        budgetInput = it
                        isError = false
                    },
                    label = { Text("Monthly Budget (Rs)") },
                    placeholder = { Text("e.g. 50000") },
                    prefix = { Text("Rs ") },
                    isError = isError,
                    supportingText = { if (isError) Text("Please enter a valid amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(Modifier.height(8.dp))

                // Quick select buttons
                Text("Quick select:", fontSize = 12.sp, color = Color.Gray)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("20000", "30000", "50000", "100000").forEach { amount ->
                        FilterChip(
                            selected = budgetInput == amount,
                            onClick = { budgetInput = amount },
                            label = { Text("${amount.toLong() / 1000}k", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryGreen,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        val amount = budgetInput.toDoubleOrNull()
                        if (amount != null && amount > 0) {
                            viewModel.setMonthlyBudget(amount)
                            onDone()
                        } else {
                            isError = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Text("Let's Go! 🚀", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
