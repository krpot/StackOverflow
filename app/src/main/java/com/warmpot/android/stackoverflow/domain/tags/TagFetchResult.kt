package com.warmpot.android.stackoverflow.domain.tags

import com.warmpot.android.stackoverflow.data.tags.schema.TagSchema
import com.warmpot.android.stackoverflow.domain.model.TagEntity

sealed class TagFetchResult {
    data class Failure(val e: Throwable) : TagFetchResult()
    object Empty : TagFetchResult()
    data class HasData(val data: List<TagEntity>) : TagFetchResult()
}