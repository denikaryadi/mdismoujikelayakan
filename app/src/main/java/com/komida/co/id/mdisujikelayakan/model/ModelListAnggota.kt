package com.komida.co.id.mdisujikelayakan.model
class ModelListAnggota(
    var label: String? = null,
    var nama: String? = null,
    var cabang: String? = null,
    var tipe: String? = null,
    var kode_uk: String? = null,
    var nik: String? = null,
    var tahun_kadaluarsa: String? = null,
    var tempat_lahir: String? = null,
    var tgl_lahir: String? = null,
    var nama_suami: String? = null,
    var status_kawin: String? = null,
    var rt: String? = null,
    var rw: String? = null,
    var desa: String? = null,
    var kecamatan: String? = null,
    var kabupaten: String? = null,
    var no_id: String? = null,
    var no_center: String? = null,
    var kelompok: String? = null,
    var tgl_pengambilan_data: String? = null,
    var tgl_bergabung: String? = null,
    var nama_ibu_kandung: String? = null,
    var tempat_tgl_ortu: String? = null,
    var wilayah: String? = null,
    var cek_client: String? = null,
    var handphone: String? = null,
    var cek_pnjmn_koperasi: String? = "1",
    var cek_pnjmn_bank: String? = "1",
    var cek_tidak_ada_akses: String? = "1",
    var cek_rekening_tabungan: String? = "1",
    var cek_asuransi: String? = "1",
    var cek_anggota_komida: String? = null,
    var section: Boolean = false,
    var isExpanded: Boolean = false,
    var isHeader: Boolean = false
) {
    constructor(
        label: String,
        nama: String,
        cabang: String,
        tipe: String,
        kode_uk: String,
        nik: String,
        tahun_kadaluarsa: String,
        tempat_lahir: String,
        tgl_lahir: String,
        nama_suami: String,
        status_kawin: String,
        rt: String,
        rw: String,
        desa: String,
        kecamatan: String,
        kabupaten: String,
        no_id: String,
        no_center: String,
        kelompok: String,
        tgl_pengambilan_data: String,
        tgl_bergabung: String,
        nama_ibu_kandung: String,
        tempat_tgl_ortu: String,
        wilayah: String,
        cek_client: String,
        handphone: String,
        cek_pnjmn_koperasi: String,
        cek_pnjmn_bank: String,
        cek_tidak_ada_akses: String,
        cek_rekening_tabungan: String,
        cek_asuransi: String,
        cek_anggota_komida: String,
        isHeader: Int,
        isExpanded: Boolean,
        section: Boolean,
        additionalParameter: Int
    ) : this(
        "$nama<br><small>$tipe - $nik - $nama - $no_center - $handphone</small>",
        nama,
        cabang,
        tipe,
        kode_uk,
        nik,
        tahun_kadaluarsa,
        tempat_lahir,
        tgl_lahir,
        nama_suami,
        status_kawin,
        rt,
        rw,
        desa,
        kecamatan,
        kabupaten,
        no_id,
        no_center,
        kelompok,
        tgl_pengambilan_data,
        tgl_bergabung,
        nama_ibu_kandung,
        tempat_tgl_ortu,
        wilayah,
        cek_client,
        handphone,
        cek_pnjmn_koperasi,
        cek_pnjmn_bank,
        cek_tidak_ada_akses,
        cek_rekening_tabungan,
        cek_asuransi,
        cek_anggota_komida,
        isHeader != 0,
        isExpanded,
        section
    )
}
