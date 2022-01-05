package com.warmpot.android.stackoverflow.data.qustions.cache

import com.warmpot.android.stackoverflow.data.qustions.schema.QuestionsResponse
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class QuestionsResponseCache {

    private val storage = hashMapOf<Int, QuestionsResponse>()
    var currentPage: Int = 1
        private set

    private val mutex = Mutex()

    suspend fun clear() {
        mutex.withLock {
            storage.clear()
        }
    }

    suspend fun getData(page: Int): QuestionsResponse? {
        return mutex.withLock {
            storage[page]
        }
    }

    suspend fun update(page: Int, data: QuestionsResponse): QuestionsResponse {
        mutex.withLock {
            currentPage = page
            storage[page] = data
        }
        return data
    }
}
