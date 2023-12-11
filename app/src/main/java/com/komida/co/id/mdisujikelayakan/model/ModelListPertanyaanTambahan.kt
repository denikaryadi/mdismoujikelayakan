package com.komida.co.id.mdisujikelayakan.model

class ModelListPertanyaanTambahan(
    var label: String? = null,
    var id_soal: String? = null,
    var urutan: String? = null,
    var pertanyaan: String? = null,
    var tipe_pertanyaan: String? = null,
    var status: String? = null,
    val pg: List<String>,
    val isi_jawaban: List<String>,
    var flag_view: String? = null,
    var selectedAnswerIndex: Int = -1,
    // var answers: List<ModelListJawabanPPi>? = null, // Use a nullable list
    var section: Boolean = false,
    var isExpanded: Boolean = false,
    var isHeader: Boolean = false
) {
    constructor(
        label: String?,
        id_soal: String?,
        urutan: String,
        pertanyaan: String,
        tipePertanyaan: String,
        status: String,
        pg: List<String>,
        isi_jawaban: List<String>,
        flag_view:String,
        selectedAnswerIndex:Int,
        isHeader: Int,
        isExpanded: Boolean,
        section: Boolean,
        additionalParameter: Int
    ) : this(
        "$pertanyaan<br>",
        id_soal,
        urutan,
        pertanyaan,
        tipePertanyaan,
        status,
        pg,
        isi_jawaban,
        flag_view,
        selectedAnswerIndex,
        isHeader != 0,
        isExpanded,
        section
    )
}