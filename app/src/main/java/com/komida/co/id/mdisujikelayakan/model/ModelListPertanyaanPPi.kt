package com.komida.co.id.mdisujikelayakan.model

class ModelListPertanyaanPPi(
    var label: String? = null,
    var no_urutan: Int? = 0,
    var Comp_ID: String? = null,
    var QuestionID: String? = null,
    var QuestionDesc: String? = null,
    var tipePertanyaan: String? = null,
    var status: String? = null,
    var usr_crt: String? = null,
    var date_crt: String? = null,

    val pg: List<String>,
    val isi_jawaban: List<String>,
    val poin: List<String>,
    val score: List<String>,
    var selectedAnswerIndex: Int = -1,
    var flag_view: String? = null,
    // var answers: List<ModelListJawabanPPi>? = null, // Use a nullable list
    var section: Boolean = false,
    var isExpanded: Boolean = false,
    var isHeader: Boolean = false
) {

    constructor(
        label: String?,
        no_urutan: Int?,
        Comp_ID: String,
        QuestionID: String,
        QuestionDesc: String,
        tipePertanyaan: String,
        status: String,
        usr_crt: String,
        date_crt: String,
       // answers: List<ModelListJawabanPPi>?,
        pg: List<String>,
        isi_jawaban: List<String>,
        poin: List<String>,
        score: List<String>,
        selectedAnswerIndex:Int,
        flag_view:String,
        isHeader: Int,
        isExpanded: Boolean,
        section: Boolean,
        additionalParameter: Int
    ) : this(
        "$QuestionDesc<br>",
        no_urutan,
        Comp_ID,
        QuestionID,
        QuestionDesc,
        tipePertanyaan,
        status,
        usr_crt,
        date_crt,
        pg,
        isi_jawaban,
        poin,
        score,
        selectedAnswerIndex,
        flag_view,
        isHeader != 0,
        isExpanded,
        section
    )
}