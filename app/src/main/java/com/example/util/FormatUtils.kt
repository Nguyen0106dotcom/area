package com.example.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object FormatUtils {
    private val symbols = DecimalFormatSymbols(Locale.GERMANY) // Germany uses dot for thousand, comma for decimal
    private val decimalFormat = DecimalFormat("#,##0.00", symbols)
    private val intFormat = DecimalFormat("#,##0", symbols)
    private val compactFormat = DecimalFormat("#,##0.0", symbols)

    fun formatArea(area: Double?): String {
        if (area == null || area == 0.0) return "-"
        return "${decimalFormat.format(area)} m²"
    }

    fun formatAreaHa(areaM2: Double?): String {
        if (areaM2 == null || areaM2 == 0.0) return "-"
        val ha = areaM2 / 10000.0
        return "${compactFormat.format(ha)} ha (${decimalFormat.format(areaM2)} m²)"
    }

    fun formatDensity(density: Double?): String {
        if (density == null) return "-"
        return "${decimalFormat.format(density)}%"
    }

    fun formatFar(far: Double?): String {
        if (far == null) return "-"
        return "${decimalFormat.format(far)} lần"
    }

    fun formatInteger(value: Int?): String {
        if (value == null || value == 0) return "-"
        return intFormat.format(value)
    }

    fun formatDouble(value: Double?): String {
        if (value == null) return "-"
        return decimalFormat.format(value)
    }

    fun copyToClipboard(context: Context, label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context, "Đã sao chép: $label", Toast.LENGTH_SHORT).show()
    }
}
