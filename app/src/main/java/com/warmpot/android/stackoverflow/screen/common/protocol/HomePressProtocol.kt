package com.warmpot.android.stackoverflow.screen.common.protocol

import android.view.MenuItem

interface HomePressProtocol : ActivityProtocol {
    fun homePressHandled(item: MenuItem, homeMenuId: Int = android.R.id.home): Boolean {
        if (item.itemId == homeMenuId) {
            self.finish()
            return true
        }
        return false
    }
}