package com.warmpot.android.stackoverflow.data.tags.cache

import com.warmpot.android.stackoverflow.domain.model.TagEntity
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TagsResponseCache {

    companion object {
        private val RECENT_SEARCH_KEY = "_com.warmpot.android.stackoverflow.RECENT_SEARCH_"
    }

    private val storage = hashMapOf<String, List<TagEntity>>()

    private val mutex = Mutex()

    suspend fun clear() {
        mutex.withLock {
            storage.clear()
        }
    }

    suspend fun getData(key: String): List<TagEntity> {
        return mutex.withLock {
            storage[key] ?: emptyList()
        }
    }

    suspend fun update(key: String, data: List<TagEntity>): List<TagEntity> {
        return mutex.withLock {
            storage[key] = data
            data
        }
    }

    suspend fun addRecentSelection(tag: TagEntity) {
        mutex.withLock {
            val recentTags = storage[RECENT_SEARCH_KEY] ?: emptyList()
            storage[RECENT_SEARCH_KEY] = recentTags.plus(tag)
        }
    }
}
