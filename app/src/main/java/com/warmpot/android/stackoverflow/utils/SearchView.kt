package com.warmpot.android.stackoverflow.utils

import android.app.SearchManager
import android.content.ComponentName
import android.graphics.drawable.ColorDrawable
import android.widget.AutoCompleteTextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import com.warmpot.android.stackoverflow.screen.common.constants.ViewConst
import com.warmpot.android.stackoverflow.screen.question.search.SearchActivity

fun SearchView.autoCompleteTextViewOf(name: String = ViewConst.SEARCH_VIEW_AUTO_COMPLETE_VIEW_ID): AutoCompleteTextView {
    val autoCompleteTextViewID = resources.getIdentifier(
        name,
        ViewConst.ID_DEF_TYPE,
        this.context.packageName
    )

    return this.findViewById(autoCompleteTextViewID)
}

fun SearchView.setupAutoCompleteTextView(
    name: String = ViewConst.SEARCH_VIEW_AUTO_COMPLETE_VIEW_ID,
    @ColorInt dropdownBgColor: Int = this.themeOnPrimaryColor,
    maxWidth: Int = Integer.MAX_VALUE
) {
    val view = this.autoCompleteTextViewOf(name)
    view.setDropDownBackgroundDrawable(ColorDrawable(dropdownBgColor))
    this.maxWidth = maxWidth
}

inline fun <reified T> SearchView.setupSearch() {
    val searchManager: SearchManager = this.context.getSystemService()!!
    this.setSearchableInfo(
        searchManager.getSearchableInfo(
            ComponentName(
                this.context,
                T::class.java
            )
        )
    )

    setupAutoCompleteTextView()
}