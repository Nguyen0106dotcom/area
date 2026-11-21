package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.LandCategory
import com.example.ui.PlanningViewModel
import com.example.ui.components.PlanningCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorerScreen(
    viewModel: PlanningViewModel,
    modifier: Modifier = Modifier
) {
    val plots by viewModel.filteredPlots.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedZone by viewModel.selectedZone.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val showOnlyFavorites by viewModel.showOnlyFavorites.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()

    val zones = listOf("Tất cả", "Khu B", "Khu C")
    val categories = listOf(
        null to "Tất cả loại đất",
        LandCategory.BIET_THU to "Biệt thự (BT)",
        LandCategory.CAO_TANG to "Cao tầng (CT)",
        LandCategory.SHOPHOUSE to "Shophouse (SH)",
        LandCategory.DICH_VU to "Dịch vụ (DV/TT)",
        LandCategory.CAY_XANH to "Cây xanh (CX)",
        LandCategory.HA_TANG to "Hạ tầng (HTKT)",
        LandCategory.BAI_XE to "Bãi xe (BDX)",
        LandCategory.GIAO_THONG to "Giao thông"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("explorer_screen")
    ) {
        // Search bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_text_field"),
                    placeholder = { Text("Tìm theo mã lô (BT17.1, CT10, SH1...) hoặc tên") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Tìm kiếm",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Xóa tìm kiếm"
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Zone filters row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Favorites Toggle Chip
                    FilterChip(
                        selected = showOnlyFavorites,
                        onClick = { viewModel.toggleFavoritesOnly() },
                        label = { Text("Đã lưu (${favoriteIds.size})") },
                        leadingIcon = {
                            Icon(
                                imageVector = if (showOnlyFavorites) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (showOnlyFavorites) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    )

                    zones.forEach { zone ->
                        val isZoneSelected = if (zone == "Tất cả") {
                            selectedZone == "Tất cả" || selectedZone == "Toàn khu"
                        } else {
                            selectedZone == zone
                        }
                        FilterChip(
                            selected = isZoneSelected,
                            onClick = { viewModel.setZone(zone) },
                            label = { Text(zone) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Categories horizontal chips row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { (cat, label) ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setCategory(cat) },
                            label = { Text(label) },
                            colors = if (cat != null) {
                                FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(cat.colorHex).copy(alpha = 0.2f),
                                    selectedLabelColor = Color(cat.colorHex)
                                )
                            } else {
                                FilterChipDefaults.filterChipColors()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Count result
                Text(
                    text = "Hiển thị ${plots.size} chỉ số quy hoạch",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Plot List or Empty State
        if (plots.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Không tìm thấy chỉ số quy hoạch nào phù hợp",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Hãy thử thay đổi từ khóa tìm kiếm hoặc bỏ chọn các bộ lọc phân khu/loại đất.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = plots,
                    key = { it.id }
                ) { item ->
                    val isFav = favoriteIds.contains(item.id)
                    PlanningCard(
                        item = item,
                        isFavorite = isFav,
                        onFavoriteToggle = { viewModel.toggleFavorite(item.id) },
                        onClick = { viewModel.openPlotDetail(item) },
                        onCalculateClick = { viewModel.selectPlotForCalculator(item) }
                    )
                }
            }
        }
    }
}
