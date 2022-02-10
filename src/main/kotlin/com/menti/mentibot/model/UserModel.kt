package com.menti.mentibot.model

import com.menti.mentibot.enums.CustomPermissionEnum
import com.mongodb.BasicDBObject
import org.jetbrains.annotations.NotNull
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
class UserModel(
    @NotNull
    val name: String,

    @NotNull
    val permission: CustomPermissionEnum
)