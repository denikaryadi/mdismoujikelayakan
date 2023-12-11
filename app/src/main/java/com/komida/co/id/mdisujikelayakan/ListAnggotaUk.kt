package com.komida.co.id.mdisujikelayakan


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.komida.co.id.mdisujikelayakan.adapter.AdapterInfoAnggota
import com.komida.co.id.mdisujikelayakan.model.ModelListAgtUKAll
import com.komida.co.id.mdisujikelayakan.utils.CustomDialogIntent

class ListAnggotaUk : AppCompatActivity() {
    private lateinit var parent_view: View
    private lateinit var nama_staff_tv: TextView
    private lateinit var nik_staff_tv: TextView
    private lateinit var cab_staff_tv: TextView
    private lateinit var jab_staff_tv: TextView
    private lateinit var dev_staff_tv: TextView
    private lateinit var hp_staff_tv: TextView
    private lateinit var kode_cab_staff_tv: TextView
    private lateinit var tot_ctr_tv: TextView
    private lateinit var tot_agt_tv: TextView

    private lateinit var preferences: SharedPreferences
    companion object {
        lateinit var nama_st: String
        lateinit var nik_st: String
        lateinit var cab_st: String
        lateinit var RELEASE: String
        lateinit var DEVICE: String
        lateinit var MODEL: String
        lateinit var PRODUCT: String
        lateinit var BRAND: String
        lateinit var totc: String
        lateinit var totagt: String
        lateinit var nomor_center: String


    }
    val itemsList: ArrayList<ModelListAgtUKAll> = ArrayList()
    private lateinit var adapterInfoAnggota: AdapterInfoAnggota
    private var recyclerView: RecyclerView? = null

    private var nestedSV: NestedScrollView? = null
    //var items = ArrayList<ListAgtUKAll>() // Create a mutable list
    var doubleBackToExitPressedOnces = false
    private lateinit var database_mdismo: SQLiteDatabase
    var DB_NAME = "mdismo.db"
    var currentOffset = 0
    var currentOffsetCari:Int = 0
    var LIMIT = 20
    var LIMITCARI:Int = 20
    var total_cari:Int = 0
    var loadMoreButton: Button? = null
    var mSearchView: SearchView? = null
    var loadingProgressBar: ProgressBar? = null
    var items: List<ModelListAgtUKAll>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_anggota_uk)
        nomor_center = "Semua Center"
        val toolbar: Toolbar = findViewById(R.id.toolbarlistanggota)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Daftar Anggota Baru"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        parent_view = findViewById<View>(android.R.id.content)
        getPrefenceStaff ()
        setupView()
        totc = (total_center() ?: 0).toString()
        totagt = (total_agt() ?: 0).toString()
        tot_ctr_tv.setText(totc)
        tot_agt_tv.setText(totagt)


        //parent_view = findViewById(android.R.id.content);

        ///nestedSV = findViewById(R.id.nested_content)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.setHasFixedSize(true)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        adapterInfoAnggota = AdapterInfoAnggota(itemsList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapterInfoAnggota

        if (nomor_center == "Semua Center") {
            loadInitialData()
            nestedSV = findViewById(R.id.nested_content)
        }



    }

    private fun loadInitialData() {
        load_agt(currentOffset, LIMIT)
    }
    private fun load_agt( offset: Int, limit: Int) {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)

        val query = "SELECT kode_uk, cabang, tipe, nama, nik, tahun_kadaluarsa, tempat_lahir, tgl_lahir, " +
                "nama_suami, status_kawin, rt, rw, desa, kecamatan, kabupaten, no_id, no_center, " +
                "kelompok, tgl_pengambilan_data, tgl_bergabung, nama_ibu_kandung, tempat_tgl_ortu, " +
                "wilayah, cek_client, handphone, cek_pnjmn_koperasi, cek_pnjmn_bank, cek_tidak_ada_akses, " +
                "cek_rekening_tabungan, cek_asuransi, cek_anggota_komida, no_hp, status_uji, usr_crt, " +
                "dt_crt, usr_upd, dt_upd FROM uk_anggota ORDER BY kode_uk DESC LIMIT $limit OFFSET $offset"

        val res = database_mdismo.rawQuery(query, null)

        while (res.moveToNext()) {
            val kode_uk = res.getString(0)
            val cabang = res.getString(1)
            val tipe = res.getString(2)
            val nama = res.getString(3)
            val nik = res.getString(4)
            val tahun_kadaluarsa = res.getString(5)
            val tempat_lahir = res.getString(6)
            val tgl_lahir = res.getString(7)
            val nama_suami = res.getString(8)
            val status_kawin = res.getString(9)
            val rt = res.getString(10)
            val rw = res.getString(11)
            val desa = res.getString(12)
            val kecamatan = res.getString(13)
            val kabupaten = res.getString(14)
            val no_id = res.getString(15)
            val no_center = res.getString(16)
            val kelompok = res.getString(17)
            val tgl_pengambilan_data = res.getString(18)
            val tgl_bergabung = res.getString(19)
            val nama_ibu_kandung = res.getString(20)
            val tempat_tgl_ortu = res.getString(21)
            val wilayah = res.getString(22)
            val cek_client = res.getString(23)
            val handphone = res.getString(24)
            val cek_pnjmn_koperasi = res.getString(25)
            val cek_pnjmn_bank = res.getString(26)
            val cek_tidak_ada_akses = res.getString(27)
            val cek_rekening_tabungan = res.getString(28)
            val cek_asuransi = res.getString(29)
            val cek_anggota_komida = res.getString(30)

            val item = ModelListAgtUKAll(
                nama + "<br><small> "+tipe+" - " + no_center + " - " + kelompok + " - "+handphone+"</small>",
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
                cek_anggota_komida
            )
            itemsList.add(item)
        }

        adapterInfoAnggota.notifyDataSetChanged()
        res.close()
        loadMoreButton?.visibility = View.GONE

        adapterInfoAnggota.setOnItemClickListener(object : AdapterInfoAnggota.OnItemClickListener {
            override fun onItemClick(view: View?, obj: ModelListAgtUKAll?, position: Int) {
                val context = view?.context
                val daftarDetailAnggota = Intent(context, UjiKelayakanActivity::class.java)
                daftarDetailAnggota.putExtra("kode_uk_dtl", obj?.kode_uk)
                daftarDetailAnggota.putExtra("nama_anggota_dtl", obj?.nama)
                daftarDetailAnggota.putExtra("nik_ktp_dtl", obj?.nik)
                daftarDetailAnggota.putExtra("tipe_dtl", obj?.tipe)
                daftarDetailAnggota.putExtra("center_dtl", obj?.no_center)
                daftarDetailAnggota.putExtra("kelompok_dtl", obj?.kelompok)
                daftarDetailAnggota.putExtra("tempat_lahir_dtl", obj?.tempat_lahir)
                daftarDetailAnggota.putExtra("tgl_bergabung_dtl", obj?.tgl_bergabung)
                daftarDetailAnggota.putExtra("handphone_dtl", obj?.handphone)
                daftarDetailAnggota.putExtra("tgl_lahir_dtl", obj?.tgl_lahir)
                daftarDetailAnggota.putExtra("status_kawin_dtl", obj?.status_kawin)
                daftarDetailAnggota.putExtra("nama_suami_dtl", obj?.nama_suami)
                daftarDetailAnggota.putExtra("nama_ibu_kandung_dtl", obj?.nama_ibu_kandung)
                context?.startActivity(daftarDetailAnggota)
            }
        })

        adapterInfoAnggota.setOnItemLongClickListener(object : AdapterInfoAnggota.OnItemLongClickListener {
            override fun onItemLongClick(view: View, obj: ModelListAgtUKAll?, position: Int) {
                val context = view.context
                panggilDialogAnggota(context, obj, view)
            }
        })

        try {
            database_mdismo.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    fun panggilDialogAnggota(context: Context?, obj: ModelListAgtUKAll?, view: View) {
        if (context != null) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Pilih Aksi yang akan dilakukan") // Gantilah dengan judul yang sesuai
            builder.setCancelable(false)

            val tujuansy = arrayOf("Ubah Anggota", "Hapus Anggota") // Gantilah dengan daftar pilihan Anda
            val selection = arrayOfNulls<String>(1) // Gunakan array untuk menyimpan pilihan yang dipilih

            builder.setSingleChoiceItems(tujuansy, -1) { dialogInterface, which ->
                selection[0] = tujuansy[which]
            }

            builder.setPositiveButton("OK") { dialogInterface, i ->
                val selectedItem = selection[0]
                if (selectedItem == "Ubah Anggota") {

                    val daftarDetailAnggota = Intent(context, FormTambahAnggotaActivity::class.java)

                    daftarDetailAnggota.putExtra("kode_uk_dtl", obj?.kode_uk)
                    daftarDetailAnggota.putExtra("cabang_dtl", obj?.cabang)
                    daftarDetailAnggota.putExtra("tgl_pengambilan_data_dtl", obj?.tgl_pengambilan_data)
                    daftarDetailAnggota.putExtra("tgl_bergabung_dtl", obj?.tgl_bergabung)
                    daftarDetailAnggota.putExtra("tipe_dtl", obj?.tipe)
                    daftarDetailAnggota.putExtra("nik_ktp_dtl", obj?.nik)
                    daftarDetailAnggota.putExtra("nama_anggota_dtl", obj?.nama)
                    daftarDetailAnggota.putExtra("tahun_kadaluarsa_dtl", obj?.tahun_kadaluarsa)
                    daftarDetailAnggota.putExtra("nama_suami_dtl", obj?.nama_suami)
                    daftarDetailAnggota.putExtra("tempat_lahir_dtl", obj?.tempat_lahir)
                    daftarDetailAnggota.putExtra("tgl_lahir_dtl", obj?.tgl_lahir)
                    daftarDetailAnggota.putExtra("rt_dtl", obj?.rt)
                    daftarDetailAnggota.putExtra("rw_dtl", obj?.rw)
                    daftarDetailAnggota.putExtra("center_dtl", obj?.no_center)
                    daftarDetailAnggota.putExtra("kelompok_dtl", obj?.kelompok)
                    daftarDetailAnggota.putExtra("status_kawin_dtl", obj?.status_kawin)
                    daftarDetailAnggota.putExtra("handphone_dtl", obj?.handphone)
                    daftarDetailAnggota.putExtra("nama_ibu_kandung_dtl", obj?.nama_ibu_kandung)
                    daftarDetailAnggota.putExtra("desa_kota_dtl", obj?.desa)
                    daftarDetailAnggota.putExtra("cek_pnjmn_koperasi_dtl", obj?.cek_pnjmn_koperasi)
                    daftarDetailAnggota.putExtra("cek_pnjmn_bank_dtl", obj?.cek_pnjmn_bank)
                    daftarDetailAnggota.putExtra("cek_tidak_akses_dtl", obj?.cek_tidak_ada_akses)
                    daftarDetailAnggota.putExtra("cek_rekening_tabungan_dtl", obj?.cek_rekening_tabungan)
                    daftarDetailAnggota.putExtra("cek_asuransi_dtl", obj?.cek_asuransi)
                    daftarDetailAnggota.putExtra("cek_anggota_dtl", obj?.cek_anggota_komida)
                    context?.startActivity(daftarDetailAnggota)


                } else if (selectedItem == "Hapus Anggota") {

                    var kode_uk_hapus  = obj?.kode_uk
                    var checkDataAnggota = checkDataAnggota(kode_uk_hapus)
                    if (checkDataAnggota.isNotEmpty()) {
                        val message = checkDataAnggota
                        showInfoSnackbar(view, message,"tidakhapus") // Panggil fungsi untuk menampilkan Snackbar info

                    } else {

                        deleteAnggota(kode_uk_hapus)
                        clearData()
                        loadInitialData()
                        val message = "Anda Berhasil Menghapus Data"
                        showInfoSnackbar(view, message,"hapus") // Panggil fungsi untuk menampilkan Snackbar info

                    }
                }
            }

            builder.setNegativeButton("Batal") { dialogInterface, i ->
                // Handle aksi ketika pengguna menekan tombol Batal
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
    fun clearData() {
        itemsList.clear()
        adapterInfoAnggota.notifyDataSetChanged()
    }
    private fun deleteAnggota(kodeUkHapus: String?) {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val execUkAnggota = "DELETE FROM uk_anggota where kode_uk='$kodeUkHapus'"
        database_mdismo.execSQL(execUkAnggota)
        database_mdismo.close()

    }

    private fun checkDataAnggota(kodeUkHapus: String?): String {
        var msg = ""

        if (kodeUkHapus != null && kodeUkHapus.isNotEmpty()) {
            val checkDataKeluarga: Int?  = this.checkDataKeluarga(kodeUkHapus)
            val checkDataIndexRumah: Int?  = this.checkDataIndexRumah(kodeUkHapus)
            val checkDataPendapatan: Int? = this.checkDataPendapatan(kodeUkHapus)
            val checkDataFoto: Int?  = this.checkDataFoto(kodeUkHapus)
            val checkDataFotoKtp: Int?  = this.checkDataFotoKTP(kodeUkHapus)

            if (checkDataKeluarga?.compareTo(0) == 1 ) {
                msg += "Keluarga ,"
            }

            if (checkDataIndexRumah?.compareTo(0) == 1) {
                msg += "Index ,"
            }

            if (checkDataPendapatan?.compareTo(0) == 1) {
                msg += "Pendapatan ,"
            }
            if (checkDataFoto?.compareTo(0) == 1) {
                msg += "Foto Anggota ,"
            }
            if (checkDataFotoKtp?.compareTo(0) == 1) {
                msg += "Foto KTP ,"
            }
            if (msg.isNotEmpty()) {
                msg = "Data Tidak Dapat di Hapus, Data $msg sudah Terisi" // You can modify this as needed
            }
        }

        return msg
    }

    fun checkDataKeluarga(kodeUkHapus:String): Int? {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        var total: Int? = 0
        val res = database_mdismo.rawQuery(
            "select count(*)  " +
                    "from uk_anggota_keluarga where kode_uk='${kodeUkHapus}'  ",
            null
        )
        while (res.moveToNext()) {
            total = res.getInt(0)
        }
        database_mdismo.close()
        return total
    }
    fun checkDataIndexRumah(kodeUkHapus:String): Int? {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        var total: Int? = 0
        val res = database_mdismo.rawQuery(
            "select count(*)  " +
                    "from uk_dtl_index_rumah where kode_uk='${kodeUkHapus}'  ",
            null
        )
        while (res.moveToNext()) {
            total = res.getInt(0)
        }
        database_mdismo.close()
        return total
    }
    fun checkDataPendapatan(kodeUkHapus:String): Int? {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        var total: Int? = null
        val res = database_mdismo.rawQuery(
            "select count(*)  " +
                    "from uk_pendapatan where kode_uk='${kodeUkHapus}'  ",
            null
        )
        while (res.moveToNext()) {
            total = res.getInt(0)
        }
        database_mdismo.close()
        return total
    }
    fun checkDataFoto(kodeUkHapus:String): Int? {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        var total: Int? = null
        val res = database_mdismo.rawQuery(
            "select count(*)  " +
                    "from uk_fotoktpttd_anggota where kode_uk='${kodeUkHapus}' and jenis='foto' ",
            null
        )
        while (res.moveToNext()) {
            total = res.getInt(0)
        }
        database_mdismo.close()
        return total
    }
    fun checkDataFotoKTP(kodeUkHapus:String): Int? {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        var total: Int? = null
        val res = database_mdismo.rawQuery(
            "select count(*)  " +
                    "from uk_fotoktpttd_anggota where kode_uk='${kodeUkHapus}' and jenis='ktp' ",
            null
        )
        while (res.moveToNext()) {
            total = res.getInt(0)
        }
        database_mdismo.close()
        return total
    }

    private fun showInfoSnackbar(view: View, message: String,status:String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        if(status=="edit")
        {
            snackbarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.green_400)) // Ganti dengan warna yang sesuai

        } // Ganti dengan warna yang sesuai\
       else if(status=="tidakhapus")
        {
            snackbarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.yellow_900)) // Ganti dengan warna yang sesuai
        }
        else
        {
            snackbarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.red_400)) // Ganti dengan warna yang sesuai
        }
        snackbar.show()
    }

    private fun total_center(): Int? {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        var total: Int? = null
        val res = database_mdismo.rawQuery(
            "select count(*) from (select no_center from uk_anggota group by no_center) as a", null
        )
        while (res.moveToNext()) {
            total = res.getInt(0)
        }
        database_mdismo.close()
        return total
    }
    private fun total_agt(): Int? {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        var total: Int? = null
        val res = database_mdismo.rawQuery(
            "select count(*)  " +
                    "from uk_anggota  ",
            null
        )
        while (res.moveToNext()) {
            total = res.getInt(0)
        }
        database_mdismo.close()
        return total
    }
    private fun getPrefenceStaff() {
        nama_st =preferences.getString("nama","")!!
        nik_st = preferences.getString("nik", "")!!
        cab_st = preferences.getString("cabang", "")!!

    }

    private fun setupView() {
        nama_staff_tv = findViewById(R.id.nama_staff)
        nik_staff_tv = findViewById(R.id.nik_staff)
        cab_staff_tv = findViewById(R.id.cab_staff)
        RELEASE = Build.VERSION.RELEASE // Android version/release
        DEVICE = Build.DEVICE // Device name
        MODEL = Build.MODEL // Device model
        PRODUCT = Build.PRODUCT // Device product
        BRAND = Build.BRAND // Device brand
        var OSNAME = System.getProperty("os.name")

        nama_staff_tv.text= nama_st
        nik_staff_tv.text = nik_st
        cab_staff_tv.text = cab_st

        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        loadMoreButton = findViewById(R.id.loadMoreButton)
        //isi kolom total center
        tot_ctr_tv = findViewById<TextView>(R.id.total_center)
        tot_agt_tv = findViewById<TextView>(R.id.total_anggota)
        //recyclerView = findViewById(R.id.recyclerView)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    fun clickAction(view: View) {
        val id = view.id
        if (id == R.id.logob) {
            val mainactivityintent = Intent(this, MainActivity::class.java)
            startActivity(mainactivityintent)
        }
    }

    override fun onBackPressed() {
        val mainactivityintent = Intent(this, MainActivity::class.java)
        startActivity(mainactivityintent)
        finish()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        println("TEST")
        //println(item.itemId)
        when (item.itemId) {

            android.R.id.home -> {
                // Handle the Up button click (e.g., navigate back to the previous activity)
                onBackPressed()
                println("dadada")
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}