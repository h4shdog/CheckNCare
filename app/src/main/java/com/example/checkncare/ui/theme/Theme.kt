package com.example.checkncare.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.example.checkncare.ui.language.LocalFontSize
import com.example.checkncare.ui.language.LocalTheme

private val LightColorScheme = lightColorScheme(
    primary              = CrimsonRed,
    onPrimary            = PureWhite,
    primaryContainer     = PaleRed,
    onPrimaryContainer   = DeepRed,

    secondary            = Color(0xFF795548),
    onSecondary          = PureWhite,
    secondaryContainer   = Color(0xFFD7CCC8),
    onSecondaryContainer = Color(0xFF3E2723),

    tertiary             = WarnAmber,
    onTertiary           = PureWhite,

    background           = OffWhite,
    onBackground         = TextPrimary,

    surface              = PureWhite,
    onSurface            = TextPrimary,
    surfaceVariant       = LightGray,
    onSurfaceVariant     = TextSecond,

    outline              = MidGray,
    outlineVariant       = DividerGray,

    error                = ErrorRed,
    onError              = PureWhite,
    errorContainer       = SoftRed,
    onErrorContainer     = DeepRed
)

private val DarkColorScheme = darkColorScheme(
    primary              = DarkRed,
    onPrimary            = PureWhite,
    primaryContainer     = DarkSoftRed,
    onPrimaryContainer   = Color(0xFFFFB3AE),

    secondary            = Color(0xFFA1887F),
    onSecondary          = DarkBg,
    secondaryContainer   = Color(0xFF3E2723),
    onSecondaryContainer = Color(0xFFD7CCC8),

    tertiary             = Color(0xFFFFAB40),
    onTertiary           = DarkBg,

    background           = DarkBg,
    onBackground         = DarkTextPrimary,

    surface              = DarkSurface,
    onSurface            = DarkTextPrimary,
    surfaceVariant       = DarkSurface2,
    onSurfaceVariant     = DarkTextSecond,

    outline              = DarkMidGray,
    outlineVariant       = DarkDivider,

    error                = Color(0xFFFF6B6B),
    onError              = DarkBg,
    errorContainer       = Color(0xFF4A0000),
    onErrorContainer     = Color(0xFFFFB3AE)
)

@Composable
fun CheckNCareTheme(content: @Composable () -> Unit) {
    val isDark    = LocalTheme.current.isDark
    // Base is 14sp (the original bodyMedium). Scale everything proportionally.
    val fontScale = (LocalFontSize.current.sizeSp / 14f).coerceIn(0.5f, 4f)

    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

    fun TextStyle.scaled() = copy(
        fontSize   = fontSize   * fontScale,
        lineHeight = lineHeight * fontScale
    )

    val scaledTypography = Typography.copy(
        displayLarge   = Typography.displayLarge.scaled(),
        displayMedium  = Typography.displayMedium.scaled(),
        headlineLarge  = Typography.headlineLarge.scaled(),
        headlineMedium = Typography.headlineMedium.scaled(),
        headlineSmall  = Typography.headlineSmall.scaled(),
        titleLarge     = Typography.titleLarge.scaled(),
        titleMedium    = Typography.titleMedium.scaled(),
        titleSmall     = Typography.titleSmall.scaled(),
        bodyLarge      = Typography.bodyLarge.scaled(),
        bodyMedium     = Typography.bodyMedium.scaled(),
        bodySmall      = Typography.bodySmall.scaled(),
        labelLarge     = Typography.labelLarge.scaled(),
        labelMedium    = Typography.labelMedium.scaled(),
        labelSmall     = Typography.labelSmall.scaled()
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = scaledTypography,
        content     = content
    )
}
