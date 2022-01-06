package com.warmpot.android.stackoverflow.data.tags.schema


import com.google.gson.annotations.SerializedName

data class TagSchema(
    @SerializedName("count")
    val count: Int,

    @SerializedName("has_synonyms")
    val hasSynonyms: Boolean,

    @SerializedName("is_moderator_only")
    val isModeratorOnly: Boolean,

    @SerializedName("is_required")
    val isRequired: Boolean,

    @SerializedName("last_activity_date")
    val lastActivityDate: Int,

    @SerializedName("name")
    val name: String
)