package com.komida.co.id.mdisujikelayakan.model

data class QuestionAnswerModel(
    val id: Int,
    val text: String,
    val answers: List<String>,
    var selectedAnswerIndex: Int = -1
)