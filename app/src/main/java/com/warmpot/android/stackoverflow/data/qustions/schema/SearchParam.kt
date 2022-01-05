package com.warmpot.android.stackoverflow.data.qustions.schema

data class SearchParam(
    val page: Int = 1,
    val pageSize: Int? = null,
    val inTitle: String,
    val fromDate: String? = null,
    val toDate: String? = null,
    val order: SearchOrder = SearchOrder.DESC,
    val min: String? = null,
    val max: String? = null,
    val sort: SearchSort = SearchSort.ACTIVITY,
) {

    fun nextPage(): SearchParam = copy(page = page.inc())

    fun toQueryMap(): Map<String, String> {
        val result = hashMapOf<String, String>()
        page.also { result["page"] = it.toString() }
        pageSize?.also { result["pagesize"] = it.toString() }
        fromDate?.also { result["fromdate"] = it }
        toDate?.also { result["todate"] = it }
        result["intitle"] = inTitle
        min?.also { result["min"] = it }
        max?.also { result["max"] = it }

        result["order"] = order.name.lowercase()
        result["sort"] = sort.name.lowercase()
        return result
    }
}

enum class SearchOrder {
    DESC,
    ASC
}

enum class SearchSort {
    ACTIVITY,
    VOTES,
    CREATION,
    RELEVANCE
}