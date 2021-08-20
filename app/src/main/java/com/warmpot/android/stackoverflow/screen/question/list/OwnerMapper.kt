package com.warmpot.android.stackoverflow.screen.question.list

import com.warmpot.android.stackoverflow.common.Mapper
import com.warmpot.android.stackoverflow.data.schema.OwnerSchema

class OwnerMapper : Mapper<OwnerSchema, Owner> {

    override fun convert(src: OwnerSchema): Owner {
        return src.run {
            Owner(
                userId = userId,
                acceptRate = acceptRate ?: 0,
                accountId = accountId,
                displayName = displayName ?: "",
                link = link ?: "",
                profileImage = profileImage ?: "",
                reputation = reputation ?: 0,
                userType = userType
            )
        }
    }
}
