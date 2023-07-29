package com.example.weatherapp.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class SynchronizedHorizontalPageTransformer : ViewPager2.PageTransformer {
    private var scrollOffset = 0

    fun setScrollOffset(offset: Int) {
        this.scrollOffset = offset
    }

    override fun transformPage(page: View, position: Float) {
        // Calculate the translation based on scrollOffset and the position of the current page
        val translationX = -scrollOffset + page.width * position

        // Apply the translation to the page
        page.translationX = translationX

        // Scale the page based on its position to create a 3D-like effect
        val scaleFactor = 1f - 0.2f * abs(position)
        page.scaleX = scaleFactor
        page.scaleY = scaleFactor

        // Set the elevation of the page based on its position to ensure proper layering
        page.translationZ = -abs(position)
    }
}