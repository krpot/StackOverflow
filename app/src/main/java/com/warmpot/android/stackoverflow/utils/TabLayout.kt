package com.warmpot.android.stackoverflow.utils

import com.google.android.material.tabs.TabLayout

fun TabLayout.doTabSelected(
    onTabReselected: ((TabLayout.Tab) -> Unit)? = null,
    onTabUnselected: ((TabLayout.Tab) -> Unit)? = null,
    onTabSelected: ((TabLayout.Tab, Int) -> Unit)? = null,
) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            val position = tab?.position ?: return
            onTabSelected?.invoke(tab, position)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            onTabUnselected?.invoke(tab!!)
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
            onTabReselected?.invoke(tab!!)
        }
    })
}