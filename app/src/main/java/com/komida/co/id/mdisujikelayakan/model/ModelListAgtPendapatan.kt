package com.komida.co.id.mdisujikelayakan.model

class ModelListAgtPendapatan(
    var label:String? =null,
    var kode_uk_pendapatan:String? =null,
    var kode_uk:String? =null,
    var suami_tetap:String? =null,
    var suami_tidak_tetap:String? =null,
    var suami_per_bulan:String? =null,
    var istri_tetap:String? =null,
    var istri_tidak_tetap:String? =null,
    var istri_per_bulan:String? =null,
    var pendapatan_lainnya_tetap:String? =null,
    var pendapatan_lainnya_tdk_tetap:String? =null,
    var pendapatan_lainnya_per_bulan:String? =null,
    var total_pendapatan_per_bulan:String? =null,
    var total_pendapatan_bersih_per_bulan:String? =null,
    var total_pengeluaran_rt:String? =null,
    var total_pengeluaran_lain:String? =null,
    var total_pengeluaran_per_bulan:String? =null,
    var section: Boolean = false,
    var isExpanded: Boolean = false,
    var isHeader: Boolean = false
) {
    // Updated constructor
    constructor(

        label:String,
        kode_uk_pendapatan:String,
        kode_uk:String,
        suami_tetap:String,
        suami_tidak_tetap:String,
        suami_per_bulan:String,
        istri_tetap:String,
        istri_tidak_tetap:String,
        istri_per_bulan:String,
        pendapatan_lainnya_tetap:String,
        pendapatan_lainnya_tdk_tetap:String,
        pendapatan_lainnya_per_bulan:String,
        total_pendapatan_per_bulan:String,
        total_pendapatan_bersih_per_bulan:String,
        total_pengeluaran_rt:String,
        total_pengeluaran_lain:String,
        total_pengeluaran_per_bulan:String,
        isHeader: Boolean, // Changed the type to Boolean
        isExpanded: Boolean,
        section: Boolean, // Added the 'section' parameter
        additionalParameter: Int
    ) : this(
        label,
        kode_uk_pendapatan,
        kode_uk,
        suami_tetap,
        suami_tidak_tetap,
        suami_per_bulan,
        istri_tetap,
        istri_tidak_tetap,
        istri_per_bulan,
        pendapatan_lainnya_tetap,
        pendapatan_lainnya_tdk_tetap,
        pendapatan_lainnya_per_bulan,
        total_pendapatan_per_bulan,
        total_pendapatan_bersih_per_bulan,
        total_pengeluaran_rt,
        total_pengeluaran_lain,
        total_pengeluaran_per_bulan,
        section, // Use 'section' parameter here
        isExpanded,
        isHeader
    )
}
