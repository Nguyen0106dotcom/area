package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.PlanningDataRepository
import com.example.ui.PlanningViewModel
import com.example.ui.theme.CategoryGreenery
import com.example.ui.theme.CategoryService
import com.example.ui.theme.CategoryShophouse
import com.example.ui.theme.CategoryTower
import com.example.ui.theme.CategoryTraffic
import com.example.ui.theme.CategoryVilla
import com.example.util.FormatUtils

@Composable
fun OverviewScreen(
    viewModel: PlanningViewModel,
    modifier: Modifier = Modifier
) {
    val selectedZone by viewModel.selectedZone.collectAsState()
    val summary = PlanningDataRepository.getSummary(selectedZone)

    val zones = listOf("Toàn khu", "Khu B", "Khu C")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
            .testTag("overview_screen")
    ) {
        // Hero Masterplan Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.banner_urban_planning_1793937081696),
                contentDescription = "Bản đồ quy hoạch tổng thể",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark gradient overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0x990B192C),
                                Color(0xE60B192C)
                            )
                        )
                    )
            )

            // Title & Status inside Banner
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "QUY HOẠCH CHI TIẾT 1/500",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Đồ Án Khu B & Khu C",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Tổng diện tích 43,8 ha • 5.000 căn • Sức chứa 10.152 du khách",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFE2E8F0)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Zone Segmented Selector
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "PHÂN KHU THỐNG KÊ",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                zones.forEach { zone ->
                    val isSelected = selectedZone == zone
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                            )
                            .clickable { viewModel.setZone(zone) }
                            .padding(vertical = 10.dp)
                            .testTag("zone_tab_$zone"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when(zone) {
                                "Toàn khu" -> "Toàn khu (43.8 ha)"
                                "Khu B" -> "Khu B (28.2 ha)"
                                "Khu C" -> "Khu C (15.6 ha)"
                                else -> zone
                            },
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 6 Main Key Indicators Grid
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricKpiCard(
                    title = "Diện tích khu đất",
                    value = FormatUtils.formatArea(summary.totalLandArea),
                    subtitle = "${String.format(java.util.Locale.US, "%.1f", summary.totalLandArea / 10000.0)} hecta",
                    icon = Icons.Filled.SquareFoot,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                MetricKpiCard(
                    title = "Mật độ xây dựng",
                    value = FormatUtils.formatDensity(summary.buildingDensity),
                    subtitle = "Tiêu chuẩn nghỉ dưỡng",
                    icon = Icons.Filled.Landscape,
                    color = CategoryGreenery,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricKpiCard(
                    title = "Hệ số SDĐ (FAR)",
                    value = FormatUtils.formatFar(summary.far),
                    subtitle = "Tối đa cho phép",
                    icon = Icons.Filled.Timeline,
                    color = CategoryService,
                    modifier = Modifier.weight(1f)
                )

                MetricKpiCard(
                    title = "Diện tích sàn (GFA)",
                    value = FormatUtils.formatArea(summary.maxFloorArea),
                    subtitle = "Tổng diện tích sàn",
                    icon = Icons.Filled.Business,
                    color = CategoryTower,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricKpiCard(
                    title = "Tổng sản phẩm",
                    value = "${FormatUtils.formatInteger(summary.totalUnits)} căn",
                    subtitle = if (selectedZone == "Toàn khu") "BT: 766 • CT: 4.022 • SH: 212" else "Biệt thự, căn hộ & shophouse",
                    icon = Icons.Filled.Apartment,
                    color = CategoryShophouse,
                    modifier = Modifier.weight(1f)
                )

                MetricKpiCard(
                    title = "Khách du lịch",
                    value = "${FormatUtils.formatInteger(summary.tourists)} khách",
                    subtitle = "Sức chứa quy hoạch",
                    icon = Icons.Filled.Groups,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Land Use Breakdown (Cơ cấu sử dụng đất)
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "CƠ CẤU SỬ DỤNG ĐẤT TOÀN ĐỒ ÁN",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Tỷ lệ phân bổ diện tích và mục đích sử dụng đất",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                LandUseBarItem(
                    title = "Đất dịch vụ du lịch (BT, CT, SH)",
                    area = "221.315 m²",
                    percentage = 50.52f,
                    color = CategoryVilla
                )

                Spacer(modifier = Modifier.height(10.dp))

                LandUseBarItem(
                    title = "Đất cây xanh cảnh quan (CX, CXHC, CXCD)",
                    area = "84.103 m²",
                    percentage = 19.19f,
                    color = CategoryGreenery
                )

                Spacer(modifier = Modifier.height(10.dp))

                LandUseBarItem(
                    title = "Đất giao thông nội khu",
                    area = "88.915 m²",
                    percentage = 20.29f,
                    color = CategoryTraffic
                )

                Spacer(modifier = Modifier.height(10.dp))

                LandUseBarItem(
                    title = "Đất công trình dịch vụ công cộng",
                    area = "28.400 m²",
                    percentage = 6.48f,
                    color = CategoryService
                )

                Spacer(modifier = Modifier.height(10.dp))

                LandUseBarItem(
                    title = "Đất hạ tầng kỹ thuật & bãi đỗ xe",
                    area = "15.378 m²",
                    percentage = 3.52f,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Navigation Actions
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { viewModel.setTab(1) },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tra cứu lô đất")
            }

            OutlinedButton(
                onClick = { viewModel.setTab(2) },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Calculate, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Bộ tính thẩm định")
            }
        }
    }
}

@Composable
fun MetricKpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun LandUseBarItem(
    title: String,
    area: String,
    percentage: Float,
    color: Color
) {
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
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "$area (${String.format(java.util.Locale.US, "%.1f", percentage)}%)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.15f)
        )
    }
}
