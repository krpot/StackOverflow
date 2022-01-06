package com.warmpot.android.stackoverflow.domain.tags

import com.warmpot.android.stackoverflow.common.OneOf
import com.warmpot.android.stackoverflow.data.tags.cache.TagsResponseCache
import com.warmpot.android.stackoverflow.data.tags.datasource.TagDataSource
import com.warmpot.android.stackoverflow.domain.mapper.TagMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class GetTagsUseCase(
    private val dataSource: TagDataSource,
    private val cache: TagsResponseCache,
) {
    private val tagMapper by lazy { TagMapper() }

    suspend fun execute(query: String): TagFetchResult = withContext(Dispatchers.IO) {
        val localDeferred = async { cache.getData(query) }
        val remoteDeferred = async { dataSource.getTags(query) }

        val localResult = localDeferred.await()

        when (val remoteResult = remoteDeferred.await()) {
            is OneOf.Error -> {
                if (localResult.isEmpty()) {
                    TagFetchResult.Empty
                } else {
                    TagFetchResult.HasData(localResult)
                }
            }
            is OneOf.Success -> {
                val tagEntities =
                    localResult.plus(remoteResult.data.items.map { tagMapper.convert(it) })
                if (tagEntities.isEmpty()) {
                    TagFetchResult.Empty
                } else {
                    cache.update(query, tagEntities)
                    TagFetchResult.HasData(tagEntities)
                }
            }
        }
    }
}