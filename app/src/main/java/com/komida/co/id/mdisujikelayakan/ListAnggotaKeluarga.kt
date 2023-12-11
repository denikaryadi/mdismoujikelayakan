package com.komida.co.id.mdisujikelayakan

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.komida.co.id.mdisujikelayakan.adapter.AdapterAnggotaKeluarga
import com.komida.co.id.mdisujikelayakan.model.ModelListAgtKeluarga

class ListAnggotaKeluarga : AppCompatActivity() {
    private lateinit var parent_view: View
    private lateinit var preferences: SharedPreferences
    private lateinit var kode_uk_keluarga_tv: TextView
    private lateinit var nik_ktp_keluarga_tv: TextView
    private lateinit var nama_anggota_keluarga_tv: TextView
    private lateinit var center_keluarga_tv: TextView

    private lateinit var kelompok_keluarga_tv: TextView
    private lateinit var total_anggota_keluarga_tv: TextView



    companion object {
        lateinit var kode_uk_dtl: String
        lateinit var nama_anggota_dtl: String
        lateinit var nik_ktp_dtl: String
        lateinit var tipe_dtl: String
        lateinit var tempat_lahir_dtl: String
        lateinit var center_dtl: String
        lateinit var kelompok_dtl: String
        lateinit var tgl_bergabung_dtl: String
        lateinit var handphone_dtl: String
        lateinit var tgl_lahir_dtl: String
        lateinit var status_kawin_dtl: String
        lateinit var nama_suami_dtl: String
        lateinit var nama_ibu_kandung_dtl: String
        lateinit var totagtkeluarga: String
    }

    val itemsList: ArrayList<ModelListAgtKeluarga> = ArrayList()
    private lateinit var AdapterAnggotaKeluarga: AdapterAnggotaKeluarga
    private var recyclerView: RecyclerView? = null

    private var nestedSV: NestedScrollView? = null
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
    var items: List<ModelListAgtKeluarga>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_anggota_keluarga)
        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        val toolbar: Toolbar = findViewById(R.id.toolbarlistanggotakeluarga)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val detilIntent = this.intent
        getPreferenceAnggota(detilIntent)
        setupView()
        prepareitem()


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        AdapterAnggotaKeluarga = AdapterAnggotaKeluarga(itemsList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = AdapterAnggotaKeluarga

        loadInitialData()
        val nestedSV: NestedScrollView = findViewById(R.id.nested_content)

    }
    fun prepareitem(){
        kode_uk_keluarga_tv.text = kode_uk_dtl
        nik_ktp_keluarga_tv.text = nik_ktp_dtl
        nama_anggota_keluarga_tv.text = nama_anggota_dtl
        center_keluarga_tv.text = center_dtl
        kelompok_keluarga_tv.text = kelompok_dtl
        totagtkeluarga = (totagtkeluarga() ?: 0).toString()
        total_anggota_keluarga_tv.setText(totagtkeluarga)
        supportActionBar?.title = tipe_dtl
    }
    fun totagtkeluarga(): Int? {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        var total: Int? = null
        val res = database_mdismo.rawQuery(
            "select count(*)  " +
                    "from uk_anggota_keluarga where kode_uk='$kode_uk_dtl'  ",
            null
        )
        while (res.moveToNext()) {
            total = res.getInt(0)
        }
        database_mdismo.close()
        return total
    }
    private fun loadInitialData() {
        load_agt_keluarga(currentOffset, LIMIT)
    }
    private fun load_agt_keluarga( offset: Int, limit: Int) {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)

        val query = "SELECT kode_uk_kel,kode_uk,nama,tmpt_lahir,tgl_lahir,umur,hubungan,status_perkawinan,pekerjaan,pendidikan,keterangan,usr_crt,dt_crt,usr_upd,dt_upd " +
                "FROM uk_anggota_Keluarga where kode_uk='$kode_uk_dtl' ORDER BY dt_upd DESC LIMIT $limit OFFSET $offset"
        println(query)
        val res = database_mdismo.rawQuery(query, null)

        while (res.moveToNext()) {
            val kode_uk_kel = res.getString(res.getColumnIndex("kode_uk_kel"))
            val kode_uk = res.getString(res.getColumnIndex("kode_uk"))
            val nama = res.getString(res.getColumnIndex("nama"))
            val tempatLahir = res.getString(res.getColumnIndex("tmpt_lahir"))
            val tanggalLahir = res.getString(res.getColumnIndex("tgl_lahir"))
            val umur = res.getString(res.getColumnIndex("umur"))
            val hubungan = res.getString(res.getColumnIndex("hubungan"))
            val statusPerkawinan = res.getString(res.getColumnIndex("status_perkawinan"))
            val pekerjaan = res.getString(res.getColumnIndex("pekerjaan"))
            val pendidikan = res.getString(res.getColumnIndex("pendidikan"))
            val keterangan = res.getString(res.getColumnIndex("keterangan"))
            val usrCrt = res.getString(res.getColumnIndex("usr_crt"))
            val dtCrt = res.getString(res.getColumnIndex("dt_crt"))
            val usrUpd = res.getString(res.getColumnIndex("usr_upd"))
            val dtUpd = res.getString(res.getColumnIndex("dt_upd"))

            val item = ModelListAgtKeluarga(
                nama + "<br><small> Umur : "+umur+" Tahun",
                nama,
                tempatLahir,
                tanggalLahir,
                kode_uk,
                kode_uk_kel,
                statusPerkawinan,
                hubungan,
                pendidikan,
                pekerjaan,
                keterangan,
                umur
            )
            itemsList.add(item)
        }

        AdapterAnggotaKeluarga.notifyDataSetChanged()
        res.close()
        loadMoreButton?.visibility = View.VISIBLE

        AdapterAnggotaKeluarga.setOnItemClickListener(object : AdapterAnggotaKeluarga.OnItemClickListener {
            override fun onItemClick(view: View?, obj: ModelListAgtKeluarga?, position: Int) {
                val context = view?.context

                val lihatDataAnggotaKeluarga = Intent(context, FormTambahAnggotaKeluarga::class.java)
                lihatDataAnggotaKeluarga.putExtra("kode_uk_dtl", obj?.kode_uk)
                lihatDataAnggotaKeluarga.putExtra("kode_uk_kel", obj?.kode_uk_kel)
                lihatDataAnggotaKeluarga.putExtra("nama_lengkap_kel", obj?.nama_lengkap)
                lihatDataAnggotaKeluarga.putExtra("tempat_lahir_kel", obj?.tempat_lahir)
                lihatDataAnggotaKeluarga.putExtra("tanggal_lahir_kel", obj?.tanggal_lahir)
                lihatDataAnggotaKeluarga.putExtra("status_kawin_kel", obj?.status_kawin)
                lihatDataAnggotaKeluarga.putExtra("hubungan_keluarga_kel", obj?.hubungan_keluarga)
                lihatDataAnggotaKeluarga.putExtra("pendidikan_terakhir_kel", obj?.pendidikan_terakhir)
                lihatDataAnggotaKeluarga.putExtra("pekerjaan_kel", obj?.pekerjaan)
                lihatDataAnggotaKeluarga.putExtra("keterangan_kel", obj?.keterangan)
                lihatDataAnggotaKeluarga.putExtra("umur_kel", obj?.umur)
                lihatDataAnggotaKeluarga.putExtra("nama_anggota_dtl", nama_anggota_dtl)
                lihatDataAnggotaKeluarga.putExtra("nik_ktp_dtl", nik_ktp_dtl)
                lihatDataAnggotaKeluarga.putExtra("nama_anggota_dtl", nama_anggota_dtl)
                lihatDataAnggotaKeluarga.putExtra("tipe_dtl", tipe_dtl)
                lihatDataAnggotaKeluarga.putExtra("center_dtl", center_dtl)
                lihatDataAnggotaKeluarga.putExtra("kelompok_dtl", kelompok_dtl)
                lihatDataAnggotaKeluarga.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
                lihatDataAnggotaKeluarga.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
                lihatDataAnggotaKeluarga.putExtra("handphone_dtl", handphone_dtl)
                lihatDataAnggotaKeluarga.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
                lihatDataAnggotaKeluarga.putExtra("status_kawin_dtl", status_kawin_dtl)
                lihatDataAnggotaKeluarga.putExtra("nama_suami_dtl", nama_suami_dtl)
                lihatDataAnggotaKeluarga.putExtra("nama_ibu_kandung_dtl",nama_ibu_kandung_dtl)

                context?.startActivity(lihatDataAnggotaKeluarga)

                // Finish the current activity
                (context as Activity).finish()

            }
        })
        AdapterAnggotaKeluarga.setOnItemLongClickListener(object : AdapterAnggotaKeluarga.OnItemLongClickListener {
            override fun onItemLongClick(view: View?, obj: ModelListAgtKeluarga?, position: Int) {
                val context = view?.context
                panggilDialogAnggota(context, obj, view)
                // Now, you can call your function with a nullable view
                // panggilDialogAnggotaKeluarga(context: Context?, obj: ModelListAgtKeluarga?, view: View?) {
                //
            }
        })


        try {
            database_mdismo.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun panggilDialogAnggota(context: Context?, obj: ModelListAgtKeluarga?, view: View?) {
        if (context != null) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Pilih Aksi yang akan dilakukan") // Gantilah dengan judul yang sesuai
            builder.setCancelable(false)

            val tujuansy = arrayOf("Hapus Anggota Keluarga") // Gantilah dengan daftar pilihan Anda
            val selection = arrayOfNulls<String>(1) // Gunakan array untuk menyimpan pilihan yang dipilih

            builder.setSingleChoiceItems(tujuansy, -1) { dialogInterface, which ->
                selection[0] = tujuansy[which]
            }

            builder.setPositiveButton("OK") { dialogInterface, i ->
                val selectedItem = selection[0]
                var kode_uk_kel_hapus  = obj?.kode_uk_kel
                var kode_uk_hapus  = obj?.kode_uk
                deleteAnggotaKeluarga(kode_uk_hapus,kode_uk_kel_hapus)
                //clearData()
                //loadInitialData()
                val lihatDataPendapatan = Intent(this, ListAnggotaKeluarga::class.java)
                lihatDataPendapatan.putExtra("kode_uk_dtl", kode_uk_dtl)
                lihatDataPendapatan.putExtra("nik_ktp_dtl", nik_ktp_dtl)
                lihatDataPendapatan.putExtra("nama_anggota_dtl", nama_anggota_dtl)
                lihatDataPendapatan.putExtra("tipe_dtl", tipe_dtl)
                lihatDataPendapatan.putExtra("center_dtl", center_dtl)
                lihatDataPendapatan.putExtra("kelompok_dtl", kelompok_dtl)
                lihatDataPendapatan.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
                lihatDataPendapatan.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
                lihatDataPendapatan.putExtra("handphone_dtl", handphone_dtl)
                lihatDataPendapatan.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
                lihatDataPendapatan.putExtra("status_kawin_dtl", status_kawin_dtl)
                lihatDataPendapatan.putExtra("nama_suami_dtl", nama_suami_dtl)
                lihatDataPendapatan.putExtra("nama_ibu_kandung_dtl", nama_ibu_kandung_dtl)

                startActivity(lihatDataPendapatan)

                val message = "Anda Berhasil Menghapus Data"
                val snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    message,
                    Snackbar.LENGTH_SHORT
                )
                snackbar.addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        if (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_CONSECUTIVE) {
                            // Handle the Snackbar dismissal (timeout or consecutive)
                            finish() // Finish the current activity to go back
                        }
                    }
                })
                snackbar.show()

            }

            builder.setNegativeButton("Batal") { dialogInterface, i ->
                // Handle aksi ketika pengguna menekan tombol Batal
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
    private fun deleteAnggotaKeluarga(kodeUkHapus: String?,kodeUkKelHapus: String?) {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val execUkAnggota = "DELETE FROM uk_anggota_keluarga where kode_uk_kel='$kodeUkKelHapus' and kode_uk='$kodeUkHapus'"
        database_mdismo.execSQL(execUkAnggota)
        database_mdismo.close()

    }
    private fun showInfoSnackbar(view: View, message: String,status:String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.red_400)) // Ganti dengan warna yang sesuai

        snackbar.show()
    }
    fun clearData() {
        itemsList.clear()
        AdapterAnggotaKeluarga.notifyDataSetChanged()
    }
    private fun getPreferenceAnggota(intent: Intent?) {
        kode_uk_dtl = intent?.getStringExtra("kode_uk_dtl") ?: ""
        nama_anggota_dtl = intent?.getStringExtra("nama_anggota_dtl") ?: ""
        nik_ktp_dtl = intent?.getStringExtra("nik_ktp_dtl") ?: ""
        tipe_dtl = intent?.getStringExtra("tipe_dtl") ?: ""
        center_dtl = intent?.getStringExtra("center_dtl") ?: ""
        kelompok_dtl = intent?.getStringExtra("kelompok_dtl") ?: ""
        tempat_lahir_dtl = intent?.getStringExtra("tempat_lahir_dtl") ?: ""
        tgl_bergabung_dtl = intent?.getStringExtra("tgl_bergabung_dtl") ?: ""
        handphone_dtl = intent?.getStringExtra("handphone_dtl") ?: ""
        tgl_lahir_dtl = intent?.getStringExtra("tgl_lahir_dtl") ?: ""
        status_kawin_dtl = intent?.getStringExtra("status_kawin_dtl") ?: ""
        nama_suami_dtl = intent?.getStringExtra("nama_suami_dtl") ?: ""
        nama_ibu_kandung_dtl = intent?.getStringExtra("nama_ibu_kandung_dtl") ?: ""
    }
    private fun setupView() {
        kode_uk_keluarga_tv = findViewById(R.id.kode_uk_keluarga_tv)
        nik_ktp_keluarga_tv = findViewById(R.id.nik_ktp_keluarga_tv)
        nama_anggota_keluarga_tv = findViewById(R.id.nama_anggota_keluarga_tv)
        center_keluarga_tv = findViewById(R.id.center_keluarga_tv)
        kelompok_keluarga_tv = findViewById(R.id.kelompok_keluarga_tv)
        total_anggota_keluarga_tv = findViewById(R.id.total_anggota_keluarga_tv)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {

        val lihat_data_pendapatan = Intent(this, UjiKelayakanActivity::class.java)
        lihat_data_pendapatan.putExtra("kode_uk_dtl", kode_uk_dtl)
        lihat_data_pendapatan.putExtra("nik_ktp_dtl", nik_ktp_dtl)
        lihat_data_pendapatan.putExtra("nama_anggota_dtl", nama_anggota_dtl)
        lihat_data_pendapatan.putExtra("tipe_dtl", tipe_dtl)
        lihat_data_pendapatan.putExtra("center_dtl", center_dtl)
        lihat_data_pendapatan.putExtra("kelompok_dtl", kelompok_dtl)
        lihat_data_pendapatan.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
        lihat_data_pendapatan.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
        lihat_data_pendapatan.putExtra("handphone_dtl", handphone_dtl)
        lihat_data_pendapatan.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
        lihat_data_pendapatan.putExtra("status_kawin_dtl", status_kawin_dtl)
        lihat_data_pendapatan.putExtra("nama_suami_dtl", nama_suami_dtl)
        lihat_data_pendapatan.putExtra("nama_ibu_kandung_dtl",nama_ibu_kandung_dtl)
        startActivity(lihat_data_pendapatan)
        finish()
    }

    fun clickAction(view: View) {
        val id = view.id
        if (id == R.id.btn_tambah_agt_keluarga) {
            val tambah_data_keluarga = Intent(this, FormTambahAnggotaKeluarga::class.java)

            tambah_data_keluarga.putExtra("kode_uk_dtl", kode_uk_dtl)
            tambah_data_keluarga.putExtra("nama_anggota_dtl", nama_anggota_dtl)
            tambah_data_keluarga.putExtra("nik_ktp_dtl", nik_ktp_dtl)
            tambah_data_keluarga.putExtra("tipe_dtl", tipe_dtl)
            tambah_data_keluarga.putExtra("center_dtl", center_dtl)
            tambah_data_keluarga.putExtra("kelompok_dtl", kelompok_dtl)
            tambah_data_keluarga.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
            tambah_data_keluarga.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
            tambah_data_keluarga.putExtra("handphone_dtl", handphone_dtl)
            tambah_data_keluarga.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
            tambah_data_keluarga.putExtra("status_kawin_dtl", status_kawin_dtl)
            tambah_data_keluarga.putExtra("nama_suami_dtl", nama_suami_dtl)
            tambah_data_keluarga.putExtra("nama_ibu_kandung_dtl", nama_ibu_kandung_dtl)

            startActivity(tambah_data_keluarga)
        }
    }
}