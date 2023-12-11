package com.komida.co.id.mdisujikelayakan.model

class ModelListJawabanPPi(
    var label: String? = null,
    var Comp_ID: String? = null,
    var QuestionID: String? = null,
    var Answer: String? = null,
    var AnswerDesc: String? = null,
    var Poin: String? = null,
    var Score: String? = null,
    var section: Boolean = false,
    var isExpanded: Boolean = false,
    var isHeader: Boolean = false
) {
    constructor(
        label: String?,
        Comp_ID: String,
        QuestionID:String,
        Answer: String,
        AnswerDesc: String,
        Poin: String,
        Score: String,
        isHeader: Int,
        isExpanded: Boolean,
        section: Boolean,
        additionalParameter: Int
    ) : this(
        "$AnswerDesc<br>",
        Comp_ID,
        QuestionID,
        Answer,
        AnswerDesc,
        Poin,
        Score,
        isHeader != 0,
        isExpanded,
        section
    )
}
