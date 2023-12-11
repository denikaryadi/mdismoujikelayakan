package com.komida.co.id.mdisujikelayakan.model
class ModelListAgtKeluarga(
    var label: String? = null,
    var nama_lengkap: String? = null,
    var tempat_lahir: String? = null,
    var tanggal_lahir: String? = null,
    var kode_uk: String? = null,
    var kode_uk_kel: String? = null,
    var status_kawin: String? = null,
    var hubungan_keluarga: String? = null,
    var pendidikan_terakhir: String? = null,
    var pekerjaan: String? = null,
    var keterangan:String?= null,
    var umur:String?= null,
    var section: Boolean = false,
    var isExpanded: Boolean = false,
    var isHeader: Boolean = false
) {
    constructor(
        label: String,
        nama_lengkap:String,
        tempat_lahir: String,
        tanggal_lahir: String,
        kode_uk: String,
        kode_uk_kel: String,
        status_kawin: String,
        hubungan_keluarga: String,
        pendidikan_terakhir: String,
        pekerjaan: String,
        keterangan: String,
        umur:String?,
        isHeader: Int,
        isExpanded: Boolean,
        section: Boolean,
        additionalParameter: Int
    ) : this(
        "$nama_lengkap<br>",
        nama_lengkap,
        tempat_lahir,
        tanggal_lahir,
        kode_uk,
        kode_uk_kel,
        status_kawin,
        hubungan_keluarga,
        pendidikan_terakhir,
        pekerjaan,
        keterangan,
        umur,
        isHeader != 0,
        isExpanded,
        section
    )
}
