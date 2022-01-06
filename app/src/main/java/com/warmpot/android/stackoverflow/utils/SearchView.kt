package com.warmpot.android.stackoverflow.utils

import android.R
import android.app.SearchManager
import android.content.ComponentName
import android.database.Cursor
import android.database.MatrixCursor
import android.graphics.drawable.ColorDrawable
import android.provider.BaseColumns
import android.widget.AutoCompleteTextView
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import com.warmpot.android.stackoverflow.screen.common.constants.ViewConst

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

inline fun <reified T> SearchView.setupSearchableInfo() {
    val searchManager: SearchManager = this.context.getSystemService()!!
    this.setSearchableInfo(
        searchManager.getSearchableInfo(
            ComponentName(
                this.context,
                T::class.java
            )
        )
    )
}

private const val SUGGESTION_NAME_FIELD_NAME = "name"

fun SearchView.simpleSuggestionAdapter(
    @LayoutRes layoutId: Int = R.layout.simple_list_item_1,
    from: Array<String> = arrayOf(SUGGESTION_NAME_FIELD_NAME),
    to: IntArray = intArrayOf(android.R.id.text1),
): CursorAdapter {
    return SimpleCursorAdapter(
        context,
        layoutId,
        null,
        from,
        to,
        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
    )
}

fun SearchView.onQueryTextChange(
    queryTextSubmitted: ((String) -> Unit)? = null,
    queryTextChanged: (String) -> Unit
) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(s: String): Boolean {
            queryTextSubmitted?.invoke(s) ?: return false
            return true
        }

        override fun onQueryTextChange(s: String): Boolean {
            queryTextChanged(s)
            return false
        }
    })
}

fun SearchView.onSuggestClick(
    suggestionSelected: ((Cursor, Int) -> Any)? = null,
    suggestionClicked: (Cursor, Int) -> String
) {
    val self = this@onSuggestClick

    fun cursorAt(position: Int): MatrixCursor = suggestionsAdapter.getItem(position) as MatrixCursor

    setOnSuggestionListener(object : SearchView.OnSuggestionListener {
        override fun onSuggestionSelect(position: Int): Boolean {
            suggestionSelected?.invoke(cursorAt(position), position)
            return false
        }

        override fun onSuggestionClick(position: Int): Boolean {
            val cursor = cursorAt(position)
            val selectedSuggestion = suggestionClicked(cursor, position)
            self.setQuery(selectedSuggestion, true) //setting suggestion
            self.onActionViewCollapsed()
            return true
        }
    })
}

fun SearchView.setupSuggestion(
    adapter: CursorAdapter,
    onTriggerSuggestion: (String) -> Unit,
    onSelectedItem: (Cursor) -> String,
) {
    suggestionsAdapter = adapter
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(s: String): Boolean = false

        override fun onQueryTextChange(s: String): Boolean {
            onTriggerSuggestion(s)
            return false
        }
    })

    setOnSuggestionListener(object : SearchView.OnSuggestionListener {
        override fun onSuggestionSelect(position: Int): Boolean = true

        override fun onSuggestionClick(position: Int): Boolean {
            val cursor = adapter.getItem(position) as MatrixCursor
            setQuery(onSelectedItem(cursor), true) //setting suggestion
            onActionViewCollapsed()
            return true
        }
    })
}

fun <T> CursorAdapter.submitList(
    items: List<T>,
    onRowMap: (Int, T) -> Array<Any>?
) {
    val cursor = MatrixCursor(
        arrayOf(
            BaseColumns._ID,
            SUGGESTION_NAME_FIELD_NAME
        )
    )

    items.forEachIndexed { index, tag ->
        val record = onRowMap(index, tag)
        if (record != null) {
            cursor.addRow(record)
        }
    }

    items.asSequence()
        .mapIndexed { index, item -> onRowMap(index, item) }
        .filterNotNull()
        .forEach { columnValues ->
            cursor.addRow(columnValues)
        }

    changeCursor(cursor)
}