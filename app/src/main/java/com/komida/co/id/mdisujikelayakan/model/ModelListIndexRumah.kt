package com.komida.co.id.mdisujikelayakan.model

class ModelListIndexRumah(
    var label: String? = null,
    var kode_uk_rumah: String? = null,
    var kode_uk: String? = null,
    var index_dinding: String? = null,
    var kondisi_dinding: String? = null,
    var poin_dinding: String? = null,
    var index_atap: String? = null,
    var kondisi_atap: String? = null,
    var poin_atap: String? = null,
    var index_lantai: String? = null,
    var kondisi_lantai: String? = null,
    var poin_lantai: String? = null,
    var skor_index: String? = null,
    var status_milik: String? = null,
    var section: Boolean = false,
    var isExpanded: Boolean = false,
    var isHeader: Boolean = false
) {
    // Updated constructor
    constructor(
        label: String,
        kode_uk_rumah: String,
        kode_uk: String,
        index_dinding: String,
        kondisi_dinding: String,
        poin_dinding: String,
        index_atap: String,
        kondisi_atap: String,
        poin_atap: String,
        index_lantai: String,
        kondisi_lantai: String,
        poin_lantai: String,
        skor_index: String,
        status_milik: String,
        isHeader: Boolean, // Changed the type to Boolean
        isExpanded: Boolean,
        section: Boolean, // Added the 'section' parameter
        additionalParameter: Int
    ) : this(
        label,
        kode_uk_rumah,
        kode_uk,
        index_dinding,
        kondisi_dinding,
        poin_dinding,
        index_atap,
        kondisi_atap,
        poin_atap,
        index_lantai,
        kondisi_lantai,
        poin_lantai,
        skor_index,
        status_milik,
        section, // Use 'section' parameter here
        isExpanded,
        isHeader
    )
}
