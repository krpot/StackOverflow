package com.warmpot.android.stackoverflow.screen.common.listener

class FastClickHandler {
    private var lastItemClicked: Long = 0L

    fun performClick(action: () -> Unit) {
        synchronized(lastItemClicked) {
            if (System.currentTimeMillis() - lastItemClicked < 1000L) return
            action()
            lastItemClicked = System.currentTimeMillis()
        }
    }
}

