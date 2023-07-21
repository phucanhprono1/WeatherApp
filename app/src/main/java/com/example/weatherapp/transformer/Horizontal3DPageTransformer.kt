package com.example.weatherapp.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class Horizontal3DPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val rotation = 180 * position
        page.rotationY = rotation
    }
}