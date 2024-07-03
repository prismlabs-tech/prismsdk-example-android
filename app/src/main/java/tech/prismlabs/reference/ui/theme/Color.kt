package tech.prismlabs.reference.ui.theme

import androidx.compose.ui.graphics.Color

internal class PrismColors {
    companion object {
        val prismBase50: Color = Color(0xFF121111).copy(alpha = 0.5f)
        val prismBase40: Color = Color(0xFF121111).copy(alpha = 0.4f)
        val prismBase30: Color = Color(0xFF121111).copy(alpha = 0.3f)
        val prismBase20: Color = Color(0xFFCCCCCC)
        val prismBase15: Color = Color(0xFFD9D9D9)
        val prismBase10: Color = Color(0xFFE4E4E4)
        val prismBase5: Color = Color(0xFFF1F1F1)
        val prismBase2: Color = Color(0xFFF9F9F9)

        val primaryColor: Color = Color(0xFFFBDA6B)
        val secondaryColor: Color = Color(0xFFE4499B)

        val prismBlack: Color = Color(0xFF121111)
        val prismWhite: Color = Color(0xFFFFFFFF)
        val prismWhite80: Color = Color(0xFFFFFFFF).copy(alpha = 0.8f)

        val prismError: Color = Color(0xFFEF4148)
        val prismSuccess: Color = Color(0xFF58B947)

        val prismBlue: Color = Color(0xFF39BDED)
        val prismGreen: Color = Color(0xFF58B947)
        val prismMint: Color = Color(0xFF47B990)
        val prismOrange: Color = Color(0xFFF79150)
        val prismPurple: Color = Color(0xFF51469C)
        val prismRed: Color = Color(0xFFEF4148)
        val prismYellow: Color = Color(0xFFFBDA6B)
    }
}