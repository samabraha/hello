package com.androidclasses.flickerapp

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView, listener: OnRecyclerClickListener)
    : RecyclerView.SimpleOnItemTouchListener() {
    private val TAG = "RecyclerItemClickListen"

    interface OnRecyclerClickListener {
        fun onItemClick(view : View, position: Int)
        fun onItemLongClick(view : View, position: Int)
    }

    private val gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(event: MotionEvent): Boolean {
            Log.d(TAG, "onSingleTapUp: starts")
//            return super.onSingleTapUp(e)

            val childView = recyclerView.findChildViewUnder(event.x, event.y)

            Log.d(TAG, "onSingleTapUp calling listener.onItemClick")
            if (childView != null) {
                listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView))
            }
            return true
        }

        override fun onLongPress(event: MotionEvent) {
            Log.d(TAG, "onLongPress: starts")
//            super.onLongPress(e)

            val childView = recyclerView.findChildViewUnder(event.x, event.y)
            Log.d(TAG, "onLongPress: calling listener.onItemLongClick")
            listener.onItemLongClick(childView!!, recyclerView.getChildAdapterPosition(childView))
            return
        }
    })

    override fun onInterceptTouchEvent(recyclerView: RecyclerView, event : MotionEvent): Boolean {
        Log.d(TAG, "onInterceptTouchEvent starts $event")
        val result = gestureDetector.onTouchEvent(event)
        Log.d(TAG, "onInterceptTouchEvent: returning $result")
//        return super.onInterceptTouchEvent(rv, event)
        return result
    }
}
