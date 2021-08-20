package com.warmpot.android.stackoverflow.common

interface Mapper<INPUT, OUTPUT> {
    fun convert(src: INPUT): OUTPUT
}
