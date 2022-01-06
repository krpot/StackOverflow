package com.warmpot.android.stackoverflow.data.tags.datasource

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.data.tags.schema.TagsResponse

interface TagDataSource {
    suspend fun getTags(query: String): OneOf<TagsResponse>
}