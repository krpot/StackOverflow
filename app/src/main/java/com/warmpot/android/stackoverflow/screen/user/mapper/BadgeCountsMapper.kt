package com.warmpot.android.stackoverflow.screen.user.mapper

import com.warmpot.android.stackoverflow.common.Mapper
import com.warmpot.android.stackoverflow.data.schema.BadgeCountsSchema
import com.warmpot.android.stackoverflow.screen.user.model.BadgeCounts

class BadgeCountsMapper : Mapper<BadgeCountsSchema, BadgeCounts> {
    override fun convert(src: BadgeCountsSchema): BadgeCounts {
        return src.run {
            BadgeCounts(
                gold = gold,
                silver = silver,
                bronze = bronze
            )
        }
    }
}
