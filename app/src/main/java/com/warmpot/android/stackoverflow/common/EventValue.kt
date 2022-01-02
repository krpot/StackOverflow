package com.warmpot.android.stackoverflow.common

import java.util.concurrent.ConcurrentHashMap

class EventValue<T>(
    private val initialValue: T
) {
    private val consumed = ConcurrentHashMap<String, Boolean>()

    fun value(consumerId: String = ""): T? {
        if (consumed[consumerId] == true) {
            return null
        }

        consumed[consumerId] = true
        return initialValue
    }

    fun rawValue(consumerId: String = ""): T {
        consumed[consumerId] = true
        return initialValue
    }
}