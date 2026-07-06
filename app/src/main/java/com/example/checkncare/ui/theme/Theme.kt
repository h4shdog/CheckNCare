package com.example.checkncare.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.example.checkncare.ui.language.LocalFontSize

private val LightColorScheme = lightColorScheme(
    primary            = CrimsonRed,
    onPrimary          = PureWhite,
    primaryContainer   = PaleRed,
    onPrimaryContainer = DeepRed,

    secondary          = Color(0xFF795548),   // warm brown accent
    onSecondary        = PureWhite,
    secondaryContainer = Color(0xFFD7CCC8),
    onSecondaryContainer = Color(0xFF3E2723),

    tertiary           = WarnAmber,
    onTertiary         = PureWhite,

    background         = OffWhite,
    onBackground       = TextPrimary,

    surface            = PureWhite,
    onSurface          = TextPrimary,
    surfaceVariant     = LightGray,
    onSurfaceVariant   = TextSecond,

    outline            = MidGray,
    outlineVariant     = DividerGray,

    error              = ErrorRed,
    onError            = PureWhite,
    errorContainer     = SoftRed,
    onErrorContainer   = DeepRed
)

private val DarkColorScheme = darkColorScheme(
    primary            = Color(0xFFEF9A9A),
    onPrimary          = Color(0xFF601410),
    primaryContainer   = Color(0xFF8B1A1A),
    onPrimaryContainer = Color(0xFFFFDAD6),

    background         = Color(0xFF1C1B1F),
    onBackground       = Color(0xFFE6E1E5),

    surface            = Color(0xFF1C1B1F),
    onSurface          = Color(0xFFE6E1E5),
    surfaceVariant     = Color(0xFF2C2C2E),
    onSurfaceVariant   = Color(0xFFCAC4D0),

    error              = Color(0xFFCF6679),
    onError            = Color(0xFF370B1E)
)

@Composable
fun CheckNCareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val fontScale   = LocalFontSize.current.current.scale

    // Scale every text style's fontSize and lineHeight by the chosen multiplier
    fun TextStyle.scaled() = copy(
        fontSize   = fontSize   * fontScale,
        lineHeight = lineHeight * fontScale
    )

    val scaledTypography = Typography.copy(
        displayLarge  = Typography.displayLarge.scaled(),
        displayMedium = Typography.displayMedium.scaled(),
        headlineLarge = Typography.headlineLarge.scaled(),
        headlineMedium= Typography.headlineMedium.scaled(),
        headlineSmall = Typography.headlineSmall.scaled(),
        titleLarge    = Typography.titleLarge.scaled(),
        titleMedium   = Typography.titleMedium.scaled(),
        titleSmall    = Typography.titleSmall.scaled(),
        bodyLarge     = Typography.bodyLarge.scaled(),
        bodyMedium    = Typography.bodyMedium.scaled(),
        bodySmall     = Typography.bodySmall.scaled(),
        labelLarge    = Typography.labelLarge.scaled(),
        labelMedium   = Typography.labelMedium.scaled(),
        labelSmall    = Typography.labelSmall.scaled()
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = scaledTypography,
        content     = content
    )
}
