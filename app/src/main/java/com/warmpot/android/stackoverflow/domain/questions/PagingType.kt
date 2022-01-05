package com.warmpot.android.stackoverflow.domain.questions

sealed class PagingType {
    object FirstPage : PagingType()
    object NextPage : PagingType()
    object Refresh : PagingType()
    object Retry : PagingType()
}
