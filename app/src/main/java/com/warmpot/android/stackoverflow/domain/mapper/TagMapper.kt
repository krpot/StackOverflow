package com.warmpot.android.stackoverflow.domain.mapper

import com.warmpot.android.stackoverflow.common.AsyncMapper
import com.warmpot.android.stackoverflow.data.tags.schema.TagSchema
import com.warmpot.android.stackoverflow.domain.model.TagEntity

class TagMapper : AsyncMapper<TagSchema, TagEntity> {

    override suspend fun convert(src: TagSchema): TagEntity {
        return TagEntity(
            name = src.name
        )
    }
}
