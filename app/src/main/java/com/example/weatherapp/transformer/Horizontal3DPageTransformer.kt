package com.example.weatherapp.transformer


import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.R
import kotlin.math.abs

class Horizontal3DPageTransformer : ViewPager2.PageTransformer {
    private var isIdle = true

    override fun transformPage(page: View, position: Float) {
        page.apply {
            val pageWidth = width
            val translationX = -pageWidth * position

            when {
                position < -1 -> { // Page to the left of the screen
                    alpha = 1f // Fully visible
                }
                position <= 1 -> { // Page on the screen or entering/leaving the screen
                    alpha = 1f // Fully visible
                    translationZ = -abs(position) // Gradually reduce depth translation for a slight 3D effect
                    findViewById<View>(R.id.linearTemp)?.translationX = translationX
                    findViewById<View>(R.id.linearTemp)?.rotationY = 90f * position // Adjust the rotation based on the position
                }
                else -> { // Page to the right of the screen
                    alpha = 1f // Fully visible
                    // Reset translation and rotation for off-screen pages
                    findViewById<View>(R.id.linearTemp)?.translationX = 0f
                    findViewById<View>(R.id.linearTemp)?.rotationY = 0f
                }
            }
        }
    }

    fun setPageIdle(idle: Boolean) {
        isIdle = idle
    }

    fun isPageIdle(): Boolean {
        return isIdle
    }
}