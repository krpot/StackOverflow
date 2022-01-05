package com.warmpot.android.stackoverflow.domain.questions

import com.warmpot.android.stackoverflow.data.qustions.schema.SearchParam

sealed class DataFetchMode {
    data class Search(val param: SearchParam) : DataFetchMode()
    data class Paging(val pagingType: PagingType) : DataFetchMode()
}
