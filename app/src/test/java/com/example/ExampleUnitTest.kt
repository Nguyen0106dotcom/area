package com.example

import com.example.data.ItemLevel
import com.example.data.PlanningDataRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testPlanningDatasetLoaded() {
        val items = PlanningDataRepository.items
        assertTrue("Dataset should contain over 150 planning items", items.size >= 150)
    }

    @Test
    fun testMasterplanSummaries() {
        val summaryAll = PlanningDataRepository.getSummary("Toàn khu")
        assertEquals(438112.77, summaryAll.totalLandArea, 0.1)
        assertEquals(5000, summaryAll.totalUnits)
        assertEquals(10152, summaryAll.tourists)

        val summaryB = PlanningDataRepository.getSummary("Khu B")
        assertEquals(281948.39, summaryB.totalLandArea, 0.1)
        assertEquals(1975, summaryB.totalUnits)
        assertEquals(4402, summaryB.tourists)

        val summaryC = PlanningDataRepository.getSummary("Khu C")
        assertEquals(156164.38, summaryC.totalLandArea, 0.1)
        assertEquals(3025, summaryC.totalUnits)
        assertEquals(5750, summaryC.tourists)
    }

    @Test
    fun testSpecificPlotData() {
        val bt17_1 = PlanningDataRepository.items.firstOrNull { it.symbol == "BT17.1" }
        assertNotNull(bt17_1)
        assertEquals(2215.83, bt17_1!!.landArea!!, 0.01)
        assertEquals(40.62, bt17_1.buildingDensity!!, 0.01)
        assertEquals(1.40, bt17_1.far!!, 0.01)
        assertEquals(9, bt17_1.unitsCount)

        val ct10 = PlanningDataRepository.items.firstOrNull { it.symbol == "CT10" }
        assertNotNull(ct10)
        assertEquals(6077.00, ct10!!.landArea!!, 0.01)
        assertEquals(63.75, ct10.buildingDensity!!, 0.01)
        assertEquals(13.65, ct10.far!!, 0.01)
        assertEquals(752, ct10.unitsCount)
    }
}
