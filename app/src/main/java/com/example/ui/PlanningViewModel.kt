package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FavoritesManager
import com.example.data.ItemLevel
import com.example.data.LandCategory
import com.example.data.PlanningDataRepository
import com.example.data.PlanningItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ComplianceResult(
    val plot: PlanningItem?,
    val proposedDensity: Double,
    val proposedFar: Double,
    val proposedGfa: Double,
    val isDensityOk: Boolean,
    val isFarOk: Boolean,
    val isFloorsOk: Boolean,
    val densityDiff: Double,
    val farDiff: Double,
    val summaryText: String
)

class PlanningViewModel(application: Application) : AndroidViewModel(application) {
    private val favoritesManager = FavoritesManager(application)
    val favoriteIds: StateFlow<Set<String>> = favoritesManager.favoriteIds

    // Current navigation tab
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    // Zone filter ("Tất cả", "Khu B", "Khu C")
    private val _selectedZone = MutableStateFlow("Tất cả")
    val selectedZone: StateFlow<String> = _selectedZone.asStateFlow()

    // Category filter
    private val _selectedCategory = MutableStateFlow<LandCategory?>(null)
    val selectedCategory: StateFlow<LandCategory?> = _selectedCategory.asStateFlow()

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Show only favorites toggle
    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites.asStateFlow()

    // Selected plot for detail bottom sheet
    private val _selectedPlotForDetail = MutableStateFlow<PlanningItem?>(null)
    val selectedPlotForDetail: StateFlow<PlanningItem?> = _selectedPlotForDetail.asStateFlow()

    // Calculator inputs
    private val _calcSelectedPlot = MutableStateFlow<PlanningItem?>(null)
    val calcSelectedPlot: StateFlow<PlanningItem?> = _calcSelectedPlot.asStateFlow()

    private val _calcLandArea = MutableStateFlow("2000")
    val calcLandArea: StateFlow<String> = _calcLandArea.asStateFlow()

    private val _calcFootprint = MutableStateFlow("700")
    val calcFootprint: StateFlow<String> = _calcFootprint.asStateFlow()

    private val _calcFloors = MutableStateFlow("3")
    val calcFloors: StateFlow<String> = _calcFloors.asStateFlow()

    private val _calcBasements = MutableStateFlow("0")
    val calcBasements: StateFlow<String> = _calcBasements.asStateFlow()

    // Filtered plots list
    val filteredPlots: StateFlow<List<PlanningItem>> = combine(
        _selectedZone,
        _selectedCategory,
        _searchQuery,
        _showOnlyFavorites,
        favoriteIds
    ) { zone, category, query, onlyFavs, favs ->
        PlanningDataRepository.items.filter { item ->
            // Skip top zone and overall summary headers from plot list
            val isPlotOrSubgroup = item.level == ItemLevel.PLOT_DETAIL || item.level == ItemLevel.SUBGROUP_HEADER
            val matchesZone = when (zone) {
                "Tất cả" -> true
                else -> item.zone == zone
            }
            val matchesCategory = category == null || item.category == category
            val matchesFav = !onlyFavs || favs.contains(item.id)
            val cleanQuery = query.trim().lowercase()
            val matchesQuery = cleanQuery.isEmpty() ||
                    item.symbol.lowercase().contains(cleanQuery) ||
                    item.name.lowercase().contains(cleanQuery) ||
                    item.stt.lowercase().contains(cleanQuery) ||
                    item.notes.lowercase().contains(cleanQuery)

            isPlotOrSubgroup && matchesZone && matchesCategory && matchesFav && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Live compliance result
    val complianceResult: StateFlow<ComplianceResult> = combine(
        _calcSelectedPlot,
        _calcLandArea,
        _calcFootprint,
        _calcFloors
    ) { plot, landStr, footStr, floorStr ->
        val land = landStr.toDoubleOrNull() ?: 0.0
        val footprint = footStr.toDoubleOrNull() ?: 0.0
        val floors = floorStr.toDoubleOrNull() ?: 0.0

        val density = if (land > 0) (footprint / land) * 100.0 else 0.0
        val gfa = footprint * floors
        val far = if (land > 0) gfa / land else 0.0

        val maxAllowedDensity = plot?.buildingDensity ?: 40.0
        val maxAllowedFar = plot?.far ?: 1.2
        val maxAllowedFloors = plot?.maxFloors?.split("-")?.lastOrNull()?.toDoubleOrNull() ?: 4.0

        val isDensityOk = density <= (maxAllowedDensity + 0.05) // allow micro floating tolerance
        val isFarOk = far <= (maxAllowedFar + 0.05)
        val isFloorsOk = floors <= maxAllowedFloors

        val densityDiff = density - maxAllowedDensity
        val farDiff = far - maxAllowedFar

        val summary = buildString {
            if (isDensityOk && isFarOk && isFloorsOk) {
                append("✓ ĐẠT CHUẨN: Phương án thiết kế tuân thủ toàn bộ chỉ số quy hoạch được phê duyệt.")
            } else {
                append("⚠ CẢNH BÁO VI PHẠM CHỈ SỐ:")
                if (!isDensityOk) append("\n• Mật độ XD vượt quy chuẩn ${String.format(java.util.Locale.US, "+%.2f%%", densityDiff)}")
                if (!isFarOk) append("\n• Hệ số SDĐ (FAR) vượt quy chuẩn ${String.format(java.util.Locale.US, "+%.2f lần", farDiff)}")
                if (!isFloorsOk) append("\n• Tầng cao (${floors.toInt()} tầng) vượt tối đa cho phép (${maxAllowedFloors.toInt()} tầng)")
            }
        }

        ComplianceResult(
            plot = plot,
            proposedDensity = density,
            proposedFar = far,
            proposedGfa = gfa,
            isDensityOk = isDensityOk,
            isFarOk = isFarOk,
            isFloorsOk = isFloorsOk,
            densityDiff = densityDiff,
            farDiff = farDiff,
            summaryText = summary
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ComplianceResult(null, 35.0, 1.05, 2100.0, true, true, true, 0.0, 0.0, "Đang tính toán...")
    )

    fun setTab(tab: Int) {
        _currentTab.value = tab
    }

    fun setZone(zone: String) {
        _selectedZone.value = zone
    }

    fun setCategory(category: LandCategory?) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavoritesOnly() {
        _showOnlyFavorites.value = !_showOnlyFavorites.value
    }

    fun toggleFavorite(id: String) {
        favoritesManager.toggleFavorite(id)
    }

    fun openPlotDetail(item: PlanningItem) {
        _selectedPlotForDetail.value = item
    }

    fun closePlotDetail() {
        _selectedPlotForDetail.value = null
    }

    fun selectPlotForCalculator(item: PlanningItem) {
        _calcSelectedPlot.value = item
        item.landArea?.let { _calcLandArea.value = String.format(java.util.Locale.US, "%.2f", it) }
        item.maxBuildArea?.let { _calcFootprint.value = String.format(java.util.Locale.US, "%.2f", it) }
        val parsedFloor = item.maxFloors.split("-").lastOrNull()?.trim() ?: "3"
        _calcFloors.value = if (parsedFloor.isNotEmpty() && parsedFloor != "-") parsedFloor else "3"
        item.basements?.let { _calcBasements.value = it.toString() } ?: run { _calcBasements.value = "0" }
        _currentTab.value = 2 // Navigate to calculator
    }

    fun updateCalcInputs(land: String, footprint: String, floors: String, basements: String) {
        _calcLandArea.value = land
        _calcFootprint.value = footprint
        _calcFloors.value = floors
        _calcBasements.value = basements
    }

    fun clearCalcPlot() {
        _calcSelectedPlot.value = null
    }
}
