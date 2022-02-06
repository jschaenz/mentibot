package com.menti.mentibot.model

import org.jetbrains.annotations.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "suggestions")
class SuggestionModel(
    @Id
    @NotNull
    val id: Long,

    @NotNull
    val content: String
)