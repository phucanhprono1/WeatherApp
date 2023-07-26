package com.example.weatherapp.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.R
import kotlin.math.abs

class LinearTemp3DPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        if (page.id == R.id.linearTemp) {
            // Apply 3D transformation only to the linearTemp LinearLayout
            val pageWidth = page.width
            var translationX = -pageWidth * position

            when {
                position < -1 -> { // Page to the left of the screen
                    page.alpha = 0f
                }
                position <= 0 -> { // Page on the screen or entering the screen
                    page.alpha = 1f
                    translationX *= 1.0f + abs(position)
                    page.translationZ = -1f // Apply a small depth translation to create a 3D effect
                    page.scaleX = 1f
                    page.scaleY = 1f
                }
                position <= 1 -> { // Page leaving the screen
                    page.alpha = 1f
                    page.translationZ = -1f
                    page.scaleX = 1f - 0.2f * abs(position) // Scale the page slightly to create a 3D effect
                    page.scaleY = 1f - 0.2f * abs(position)
                }
                else -> { // Page to the right of the screen
                    page.alpha = 0f
                }
            }

            page.translationX = translationX.coerceIn(-pageWidth.toFloat(), pageWidth.toFloat())
        }
    }
}
