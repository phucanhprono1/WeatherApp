//package com.example.weatherapp.ux
//
//import android.content.Context
//import android.view.GestureDetector
//import android.view.MotionEvent
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//
//class HorizontalScrollInterceptor(context: Context) : RecyclerView.OnItemTouchListener {
//
//    private val gestureDetector: GestureDetector = GestureDetector(context, GestureListener())
//
//    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
//        return gestureDetector.onTouchEvent(e)
//    }
//
//    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
//        // Do nothing here, as we are only interested in intercepting touch events.
//    }
//
//    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
//        // Do nothing here.
//    }
//
//    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
//
//        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
//            // Check if the user scrolls horizontally.
//            if (Math.abs(distanceX) > Math.abs(distanceY)) {
//                val layoutManager = rv.layoutManager as LinearLayoutManager
//                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
//                val itemCount = rv.adapter?.itemCount ?: 0
//
//                // If the last item is visible, allow swiping to the next fragment.
//                if (lastVisibleItemPosition == itemCount - 1) {
//                    rv.requestDisallowInterceptTouchEvent(false)
//                } else {
//                    rv.requestDisallowInterceptTouchEvent(true)
//                }
//            }
//            return super.onScroll(e1, e2, distanceX, distanceY)
//        }
//    }
//}