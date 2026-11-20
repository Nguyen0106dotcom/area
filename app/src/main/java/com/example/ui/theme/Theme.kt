package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = PrimaryTeal,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCCE8E8),
    onPrimaryContainer = Color(0xFF002020),
    secondary = PrimaryNavy,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD6E4FF),
    onSecondaryContainer = Color(0xFF001A41),
    tertiary = CategoryShophouse,
    onTertiary = Color.White,
    background = SlateBackground,
    onBackground = SlateDark,
    surface = CardSurface,
    onSurface = SlateDark,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = SlateMedium,
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0)
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryTealLight,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF004D4D),
    onPrimaryContainer = Color(0xFF80CBC4),
    secondary = Color(0xFF90CAF9),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF1E3A8A),
    onSecondaryContainer = Color(0xFFDBEAFE),
    tertiary = Color(0xFFFBBF24),
    onTertiary = Color.Black,
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569),
    outlineVariant = Color(0xFF334155)
)

@Composable
fun PlanningTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
