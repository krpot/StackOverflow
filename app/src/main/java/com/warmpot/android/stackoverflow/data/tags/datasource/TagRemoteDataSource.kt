package com.warmpot.android.stackoverflow.data.tags.datasource

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.common.tryCatchOf
import com.warmpot.android.stackoverflow.data.tags.schema.TagsResponse
import com.warmpot.android.stackoverflow.network.StackoverflowApi

class TagRemoteDataSource(
    private val api: StackoverflowApi
) : TagDataSource {
    override suspend fun getTags(query: String): OneOf<TagsResponse> {
        return tryCatchOf {
            api.getTags(query)
        }
    }
}