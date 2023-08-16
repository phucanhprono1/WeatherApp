package com.example.weatherapp.transformer


import android.view.View
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.R
import kotlin.math.abs

class Horizontal3DPageTransformer : ViewPager2.PageTransformer {
    private val minScale = 0.85f
    private val minAlpha = 0.6f

    override fun transformPage(page: View, position: Float) {
        page.apply {
            val pageWidth = width
            var translationX = -pageWidth * position

            when {
                position < -1 -> {
                    alpha = minAlpha
                    scaleX = minScale
                    scaleY = minScale
                }
                position <= 1 -> {
                    alpha = 1.0f
                    scaleX = 1.0f
                    scaleY = 1.0f

                    // Apply translation for the 3D effect
                    translationX = translationX

                    // Gradually reduce alpha for depth effect
                    alpha = 1.0f - abs(position) * 0.2f
                }
                else -> {
                    alpha = minAlpha
                    scaleX = minScale
                    scaleY = minScale
                }
            }
        }
    }
}