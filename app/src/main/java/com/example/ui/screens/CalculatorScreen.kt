package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PlanningDataRepository
import com.example.ui.PlanningViewModel
import com.example.ui.theme.CategoryTower
import com.example.ui.theme.CategoryVilla
import com.example.ui.theme.DangerRed
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningYellow
import com.example.util.FormatUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: PlanningViewModel,
    modifier: Modifier = Modifier
) {
    val selectedPlot by viewModel.calcSelectedPlot.collectAsState()
    val landAreaStr by viewModel.calcLandArea.collectAsState()
    val footprintStr by viewModel.calcFootprint.collectAsState()
    val floorsStr by viewModel.calcFloors.collectAsState()
    val basementsStr by viewModel.calcBasements.collectAsState()
    val compliance by viewModel.complianceResult.collectAsState()

    var dropdownExpanded by remember { mutableStateOf(false) }
    val allPlots = remember { PlanningDataRepository.getAllPlots() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("calculator_screen")
    ) {
        // Screen Header
        Text(
            text = "THẨM ĐỊNH & TÍNH TOÁN QUY HOẠCH",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Kiểm tra sự phù hợp của phương án thiết kế với chỉ số quy hoạch 1/500",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Target Plot Selector Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "1. CHỌN LÔ QUY HOẠCH ĐỐI CHIẾU",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (selectedPlot != null) {
                        Text(
                            text = "Bỏ chọn",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.clickable { viewModel.clearCalcPlot() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = if (selectedPlot != null) "${selectedPlot?.symbol} - ${selectedPlot?.name} (${selectedPlot?.zone})" else "Chọn từ danh sách 100+ lô quy hoạch...",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor(androidx.compose.material3.MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                            .testTag("plot_dropdown_selector"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        allPlots.take(50).forEach { plot ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(
                                            text = "${plot.symbol} - ${plot.name}",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "DT: ${FormatUtils.formatArea(plot.landArea)} • MĐ: ${FormatUtils.formatDensity(plot.buildingDensity)} • Tầng: ${plot.maxFloors} • FAR: ${FormatUtils.formatFar(plot.far)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                onClick = {
                                    viewModel.selectPlotForCalculator(plot)
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // If plot is selected, show approved limits summary badge
                if (selectedPlot != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("MĐXD Tối đa", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(FormatUtils.formatDensity(selectedPlot?.buildingDensity), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Tầng cao max", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${selectedPlot?.maxFloors} tầng", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("FAR Tối đa", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(FormatUtils.formatFar(selectedPlot?.far), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Input Form Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "2. THÔNG SỐ THIẾT KẾ ĐỀ XUẤT",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = landAreaStr,
                        onValueChange = { viewModel.updateCalcInputs(it, footprintStr, floorsStr, basementsStr) },
                        label = { Text("Diện tích đất (m²)") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_land_area"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = footprintStr,
                        onValueChange = { viewModel.updateCalcInputs(landAreaStr, it, floorsStr, basementsStr) },
                        label = { Text("DT xây dựng (m²)") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_footprint"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = floorsStr,
                        onValueChange = { viewModel.updateCalcInputs(landAreaStr, footprintStr, it, basementsStr) },
                        label = { Text("Số tầng nổi") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_floors"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = basementsStr,
                        onValueChange = { viewModel.updateCalcInputs(landAreaStr, footprintStr, floorsStr, it) },
                        label = { Text("Số tầng hầm") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_basements"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Presets quick buttons
                Text(
                    text = "Phương án mẫu nhanh:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .clickable {
                                viewModel.updateCalcInputs("2000", "720", "3", "0")
                            }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text("Biệt thự 3 tầng (36%)", style = MaterialTheme.typography.labelSmall)
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .clickable {
                                viewModel.updateCalcInputs("5000", "2500", "4", "1")
                            }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text("Shophouse 4T (50%)", style = MaterialTheme.typography.labelSmall)
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .clickable {
                                viewModel.updateCalcInputs("8000", "3600", "30", "1")
                            }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text("Cao tầng 30T", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Compliance Results Card
        val isAllPassed = compliance.isDensityOk && compliance.isFarOk && compliance.isFloorsOk
        val statusBgColor by animateColorAsState(
            if (isAllPassed) SuccessGreen.copy(alpha = 0.1f) else DangerRed.copy(alpha = 0.1f),
            label = "statusBg"
        )
        val statusBorderColor by animateColorAsState(
            if (isAllPassed) SuccessGreen.copy(alpha = 0.4f) else DangerRed.copy(alpha = 0.4f),
            label = "statusBorder"
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("compliance_result_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.5.dp, statusBorderColor)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Status Header
                Surface(
                    color = statusBgColor,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isAllPassed) Icons.Filled.CheckCircle else Icons.Filled.Warning,
                            contentDescription = null,
                            tint = if (isAllPassed) SuccessGreen else DangerRed,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isAllPassed) "HỢP CHUẨN QUY HOẠCH" else "CẢNH BÁO VI PHẠM CHỈ SỐ",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isAllPassed) SuccessGreen else DangerRed
                            )
                            Text(
                                text = compliance.summaryText,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Calculated Indicators Breakdown
                ResultRow(
                    label = "Mật độ xây dựng tính toán",
                    value = "${String.format(java.util.Locale.US, "%.2f", compliance.proposedDensity)}%",
                    limit = selectedPlot?.buildingDensity?.let { "Max: ${FormatUtils.formatDensity(it)}" } ?: "Khuyến nghị: <= 40%",
                    isPassed = compliance.isDensityOk
                )

                Spacer(modifier = Modifier.height(8.dp))

                ResultRow(
                    label = "Hệ số sử dụng đất (FAR)",
                    value = "${String.format(java.util.Locale.US, "%.2f", compliance.proposedFar)} lần",
                    limit = selectedPlot?.far?.let { "Max: ${FormatUtils.formatFar(it)}" } ?: "Khuyến nghị: <= 1.2 lần",
                    isPassed = compliance.isFarOk
                )

                Spacer(modifier = Modifier.height(8.dp))

                ResultRow(
                    label = "Tổng diện tích sàn xây dựng (GFA)",
                    value = FormatUtils.formatArea(compliance.proposedGfa),
                    limit = selectedPlot?.maxFloorArea?.let { "Max: ${FormatUtils.formatArea(it)}" } ?: "-",
                    isPassed = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                ResultRow(
                    label = "Tầng cao thiết kế",
                    value = "${floorsStr.toDoubleOrNull()?.toInt() ?: 0} tầng",
                    limit = selectedPlot?.let { "Max: ${it.maxFloors} tầng" } ?: "-",
                    isPassed = compliance.isFloorsOk
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ResultRow(
    label: String,
    value: String,
    limit: String,
    isPassed: Boolean
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = limit,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isPassed) MaterialTheme.colorScheme.onSurface else DangerRed
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = if (isPassed) Icons.Outlined.Check else Icons.Outlined.Close,
                    contentDescription = null,
                    tint = if (isPassed) SuccessGreen else DangerRed,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
