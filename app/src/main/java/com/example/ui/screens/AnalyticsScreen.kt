package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PlanningDataRepository
import com.example.ui.theme.CategoryShophouse
import com.example.ui.theme.CategoryTower
import com.example.ui.theme.CategoryVilla
import com.example.util.FormatUtils

@Composable
fun AnalyticsScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val summaryB = PlanningDataRepository.getSummary("Khu B")
    val summaryC = PlanningDataRepository.getSummary("Khu C")
    val summaryAll = PlanningDataRepository.getSummary("Toàn khu")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("analytics_screen")
    ) {
        Text(
            text = "CƠ CẤU SẢN PHẨM & CĂN CỨ PHÁP LÝ",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Báo cáo phân tích loại hình bất động sản, phân bổ diện tích và văn bản pháp lý",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Product Breakdown (5,000 units total)
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
                        text = "1. CƠ CẤU 5.000 CĂN TOÀN ĐỒ ÁN",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "5.000 CĂN",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                ProductTypeRow(
                    title = "Căn hộ chung cư cao tầng (CT)",
                    count = 4022,
                    total = 5000,
                    breakdown = "Khu B (CT1): 1.268 căn • Khu C (CT10, CT11): 2.754 căn",
                    color = CategoryTower,
                    icon = Icons.Filled.Apartment
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProductTypeRow(
                    title = "Biệt thự nghỉ dưỡng (BT)",
                    count = 766,
                    total = 5000,
                    breakdown = "Khu B (BT17, BT18, BT19): 668 căn • Khu C (BT1-16): 98 căn",
                    color = CategoryVilla,
                    icon = Icons.Filled.Home
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProductTypeRow(
                    title = "Shophouse thương mại (SH)",
                    count = 212,
                    total = 5000,
                    breakdown = "Khu B (SH6-13): 39 căn • Khu C (SH1-5): 173 căn",
                    color = CategoryShophouse,
                    icon = Icons.Filled.Storefront
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Unit Size Distribution Table (Bảng diện tích căn hộ mẫu)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "2. PHÂN BỔ DIỆN TÍCH CĂN HỘ (m²)",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SizeStatChip("55 m²", "318 căn", Modifier.weight(1f))
                    SizeStatChip("65 m²", "274 căn", Modifier.weight(1f))
                    SizeStatChip("75 m²", "46 căn", Modifier.weight(1f))
                    SizeStatChip("100 m²", "30 căn", Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "• Biệt thự Khu B: Diện tích đất phân lô từ 150 - 370 m²/lô.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "• Biệt thự Khu C: Định mức tiêu chuẩn 200 m², 300 m², 400 m², 450 m², 500 m²/căn.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Side-by-Side Zone Comparison Table
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "3. SO SÁNH CHỈ SỐ: KHU B vs KHU C",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Chỉ tiêu", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.3f))
                    Text("Khu B (28.2 ha)", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("Khu C (15.6 ha)", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(6.dp))

                ComparisonRow("Diện tích đất", FormatUtils.formatArea(summaryB.totalLandArea), FormatUtils.formatArea(summaryC.totalLandArea))
                ComparisonRow("Mật độ xây dựng", FormatUtils.formatDensity(summaryB.buildingDensity), FormatUtils.formatDensity(summaryC.buildingDensity))
                ComparisonRow("Hệ số SDĐ (FAR)", FormatUtils.formatFar(summaryB.far), FormatUtils.formatFar(summaryC.far))
                ComparisonRow("Tổng diện tích sàn", FormatUtils.formatArea(summaryB.maxFloorArea), FormatUtils.formatArea(summaryC.maxFloorArea))
                ComparisonRow("Tổng số căn", "${summaryB.totalUnits} căn", "${summaryC.totalUnits} căn")
                ComparisonRow("Khách du lịch", "${summaryB.tourists} khách", "${summaryC.tourists} khách")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legal & Administrative Notes
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Gavel,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "4. CĂN CỨ PHÁP LÝ & PHÊ DUYỆT",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "• Quyết định số 1313/QĐ-UBND ngày 07/5/2019 của UBND Tỉnh: Phê duyệt đồ án quy hoạch chi tiết xây dựng tỷ lệ 1/500.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• Văn bản số 191/SXD ngày 27/5/2021 của Sở Xây dựng: Thỏa thuận điều chỉnh và xác định chỉ tiêu quy hoạch kiến trúc các lô đất biệt thự, shophouse và hạ tầng kỹ thuật.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• Toàn bộ chỉ số mật độ xây dựng, hệ số sử dụng đất, tầng cao tối đa và tầng hầm đã được chuẩn hóa và kiểm tra theo dữ liệu đồ án.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Copy Full Planning Summary
        OutlinedButton(
            onClick = {
                val fullReport = """
BÁO CÁO TỔNG QUAN CHỈ SỐ QUY HOẠCH ĐỒ ÁN (KHU B & KHU C)
------------------------------------------------------
1. QUY MÔ TỔNG THỂ:
- Tổng diện tích đất: ${FormatUtils.formatArea(summaryAll.totalLandArea)} (43,8 ha)
- Tổng diện tích xây dựng: ${FormatUtils.formatArea(summaryAll.maxBuildArea)}
- Mật độ xây dựng toàn khu: ${FormatUtils.formatDensity(summaryAll.buildingDensity)}
- Hệ số sử dụng đất (FAR): ${FormatUtils.formatFar(summaryAll.far)}
- Tổng diện tích sàn (GFA): ${FormatUtils.formatArea(summaryAll.maxFloorArea)}
- Tổng số sản phẩm: ${summaryAll.totalUnits} căn
- Quy mô khách du lịch: ${summaryAll.tourists} người

2. PHÂN BỔ SẢN PHẨM (5.000 căn):
- Căn hộ cao tầng: 4.022 căn (Khu B: 1.268, Khu C: 2.754)
- Biệt thự nghỉ dưỡng: 766 căn (Khu B: 668, Khu C: 98)
- Shophouse thương mại: 212 căn (Khu B: 39, Khu C: 173)

3. CĂN CỨ PHÁP LÝ:
- Quyết định số 1313/QĐ-UBND ngày 07/5/2019
- Văn bản số 191/SXD ngày 27/5/2021
                """.trimIndent()
                FormatUtils.copyToClipboard(context, "Báo cáo quy hoạch", fullReport)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Outlined.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sao chép báo cáo tổng hợp")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ProductTypeRow(
    title: String,
    count: Int,
    total: Int,
    breakdown: String,
    color: Color,
    icon: ImageVector
) {
    val pct = (count.toFloat() / total.toFloat()) * 100f
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text(text = breakdown, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                }
            }

            Text(
                text = "${FormatUtils.formatInteger(count)} căn (${String.format(java.util.Locale.US, "%.1f", pct)}%)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { pct / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.15f)
        )
    }
}

@Composable
fun SizeStatChip(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun ComparisonRow(label: String, valB: String, valC: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1.3f))
        Text(valB, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        Text(valC, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
    }
}
