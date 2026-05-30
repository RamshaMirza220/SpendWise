package com.ramsha.spendwise.ui.screens.analytics

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.ramsha.spendwise.domain.model.ExpenseCategory
import com.ramsha.spendwise.ui.theme.PrimaryGreen
import com.ramsha.spendwise.viewmodel.ExpenseViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: ExpenseViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val fmt = NumberFormat.getInstance(Locale("en", "PK"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analytics", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
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
            // Summary Cards
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard(
                    modifier = Modifier.weight(1f),
                    label = "Total Spent",
                    value = "Rs ${fmt.format(uiState.totalThisMonth)}",
                    color = Color(0xFFE53935)
                )
                SummaryCard(
                    modifier = Modifier.weight(1f),
                    label = "Remaining",
                    value = "Rs ${fmt.format((uiState.monthlyBudget - uiState.totalThisMonth).coerceAtLeast(0.0))}",
                    color = PrimaryGreen
                )
            }

            // Pie Chart
            if (uiState.categoryBreakdown.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Spending by Category", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Spacer(Modifier.height(8.dp))
                        AndroidView(
                            factory = { context ->
                                PieChart(context).apply {
                                    description.isEnabled = false
                                    isDrawHoleEnabled = true
                                    holeRadius = 45f
                                    transparentCircleRadius = 50f
                                    setHoleColor(AndroidColor.WHITE)
                                    setUsePercentValues(true)
                                    legend.isEnabled = false
                                    setEntryLabelTextSize(11f)
                                    setEntryLabelColor(AndroidColor.DKGRAY)
                                }
                            },
                            update = { chart ->
                                val entries = uiState.categoryBreakdown.map { (cat, amount) ->
                                    PieEntry(amount.toFloat(), cat.icon)
                                }
                                val colors = uiState.categoryBreakdown.keys.map { it.color.toArgb() }
                                val dataSet = PieDataSet(entries, "").apply {
                                    this.colors = colors
                                    valueTextSize = 11f
                                    valueTextColor = AndroidColor.WHITE
                                    sliceSpace = 2f
                                }
                                chart.data = PieData(dataSet).apply {
                                    setValueFormatter(PercentFormatter(chart))
                                }
                                chart.invalidate()
                            },
                            modifier = Modifier.fillMaxWidth().height(260.dp)
                        )
                    }
                }

                // Category list breakdown
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Breakdown", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        uiState.categoryBreakdown.entries.sortedByDescending { it.value }.forEach { (cat, amount) ->
                            val pct = if (uiState.totalThisMonth > 0) (amount / uiState.totalThisMonth * 100).toInt() else 0
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(cat.icon, fontSize = 20.sp)
                                    Spacer(Modifier.width(8.dp))
                                    Text(cat.label, fontSize = 14.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Rs ${fmt.format(amount)}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                    Spacer(Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = cat.color.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            "$pct%",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            color = cat.color,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text("Add some expenses to see analytics!", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun SummaryCard(modifier: Modifier = Modifier, label: String, value: String, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}
