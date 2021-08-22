package com.warmpot.android.stackoverflow.common

interface Mapper<INPUT, OUTPUT> {
    fun convert(src: INPUT): OUTPUT
}

interface AsyncMapper<INPUT, OUTPUT> {
    suspend fun convert(src: INPUT): OUTPUT
}
