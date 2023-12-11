package com.komida.co.id.mdisujikelayakan

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.komida.co.id.mdisujikelayakan.adapter.AdapterAnggotaIndexRumah
import com.komida.co.id.mdisujikelayakan.model.ModelListIndexRumah

class ListAnggotaIndexRumah : AppCompatActivity() {
    private lateinit var parent_view: View
    private lateinit var preferences: SharedPreferences
    private var tv_index_dinding: TextView? = null
    private var tv_index_atap: TextView? = null
    private var tv_index_lantai: TextView? = null
    private var tv_kondisi_dinding: TextView? = null
    private var tv_kondisi_atap: TextView? = null
    private var tv_kondisi_lantai: TextView? = null
    private var tv_poin_dinding: TextView? = null
    private var tv_poin_atap: TextView? = null
    private var tv_poin_lantai: TextView? = null
    private var tv_total_skor_index: TextView? = null
    private var tv_status_milik: TextView? = null

    private  var kode_uk_index_tv: TextView? = null
    private  var nik_ktp_uk_index_tv: TextView? = null
    private  var nama_anggota_uk_index_tv: TextView? = null
    private  var center_keluarga_tv: TextView? = null
    private  var kelompok_keluarga_tv: TextView? = null
    private var btn_tambah_index_rumah:Button?=null

    companion object {
        lateinit var index_dinding_dtl:String
        lateinit var index_atap_dtl:String
        lateinit var index_lantai_dtl:String
        lateinit var kondisi_atap_dtl:String
        lateinit var kondisi_lantai_dtl:String
        lateinit var kondisi_dinding_dtl:String
        lateinit var poin_dinding_dtl:String
        lateinit var poin_atap_dtl:String
        lateinit var poin_lantai_dtl:String
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
        lateinit var total_skor_index_dtl:String
        lateinit var status_milik:String


    }

    val itemsList: ArrayList<ModelListIndexRumah> = ArrayList()
    private lateinit var AdapterAnggotaIndexRumah: AdapterAnggotaIndexRumah
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
    var items: List<ModelListIndexRumah>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_anggota_index_rumah)

        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        val toolbar: Toolbar = findViewById(R.id.toolbarlistanggotaindexrumah)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detilIntent = this.intent
        getPreferenceAnggota(detilIntent)
        setupView()
        prepareItem()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        AdapterAnggotaIndexRumah = AdapterAnggotaIndexRumah(itemsList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = AdapterAnggotaIndexRumah

        loadInitialData()
        val nestedSV: NestedScrollView = findViewById(R.id.nested_content)


    }

    private fun loadInitialData()
    {
        itemsList.clear()
        load_agt_idx_rumah(currentOffset, LIMIT)
    }

    private fun load_agt_idx_rumah(currentOffset: Int, limit: Int) {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)

        val query = "SELECT kode_uk_rumah,kode_uk,index_dinding,kondisi_dinding,poin_dinding,index_atap,kondisi_atap,poin_atap,index_lantai,kondisi_lantai,poin_lantai,skor_index,status_milik,usr_crt,dt_crt,usr_upd,dt_upd " +
                "FROM uk_dtl_index_rumah where kode_uk='${kode_uk_dtl}' ORDER BY dt_upd"
        println(query)
        val res = database_mdismo.rawQuery(query, null)

        while (res.moveToNext()) {
            val kode_uk_rumah = res.getString(res.getColumnIndex("kode_uk_rumah"))
            val kode_uk = res.getString(res.getColumnIndex("kode_uk"))
            val index_dinding = res.getString(res.getColumnIndex("index_dinding"))
            val kondisi_dinding = res.getString(res.getColumnIndex("kondisi_dinding"))
            val poin_dinding = res.getString(res.getColumnIndex("poin_dinding"))
            val index_atap = res.getString(res.getColumnIndex("index_atap"))
            val kondisi_atap = res.getString(res.getColumnIndex("kondisi_atap"))
            val poin_atap = res.getString(res.getColumnIndex("poin_atap"))
            val index_lantai = res.getString(res.getColumnIndex("index_lantai"))
            val kondisi_lantai = res.getString(res.getColumnIndex("kondisi_lantai"))
            val poin_lantai = res.getString(res.getColumnIndex("poin_lantai"))
            val skor_index = res.getString(res.getColumnIndex("skor_index"))
            val status_milik = res.getString(res.getColumnIndex("status_milik"))
            val usr_crt = res.getString(res.getColumnIndex("usr_crt"))
            val dt_crt = res.getString(res.getColumnIndex("dt_crt"))
            val usr_upd = res.getString(res.getColumnIndex("usr_upd"))
            val dt_upd = res.getString(res.getColumnIndex("dt_upd"))

            val item = ModelListIndexRumah(
                "Hasil Index Rumah" + "<br><small> dengan Skor : "+skor_index+" Tahun",
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
                status_milik
            )
            itemsList.add(item)
        }

        AdapterAnggotaIndexRumah.notifyDataSetChanged()
        res.close()
        loadMoreButton?.visibility = View.VISIBLE

        AdapterAnggotaIndexRumah.setOnItemClickListener(object : AdapterAnggotaIndexRumah.OnItemClickListener {
            override fun onItemClick(view: View?, obj: ModelListIndexRumah?, position: Int) {
                val context = view?.context

                val lihatDataIndexRumah = Intent(context, FormTambahIndexRumah::class.java)

                lihatDataIndexRumah.putExtra("kode_uk_dtl", obj?.kode_uk)
                lihatDataIndexRumah.putExtra("kode_uk_rumah", obj?.kode_uk_rumah)
                lihatDataIndexRumah.putExtra("index_dinding", obj?.index_dinding)
                lihatDataIndexRumah.putExtra("kondisi_dinding", obj?.kondisi_dinding)
                lihatDataIndexRumah.putExtra("poin_dinding", obj?.poin_dinding)
                lihatDataIndexRumah.putExtra("index_atap", obj?.index_atap)
                lihatDataIndexRumah.putExtra("kondisi_atap", obj?.kondisi_atap)
                lihatDataIndexRumah.putExtra("poin_atap", obj?.poin_atap)
                lihatDataIndexRumah.putExtra("index_lantai", obj?.index_lantai)
                lihatDataIndexRumah.putExtra("kondisi_lantai", obj?.kondisi_lantai)
                lihatDataIndexRumah.putExtra("poin_lantai", obj?.poin_lantai)
                lihatDataIndexRumah.putExtra("skor_index", obj?.skor_index)
                lihatDataIndexRumah.putExtra("status_milik", obj?.status_milik)
                lihatDataIndexRumah.putExtra("kode_uk_dtl", kode_uk_dtl)
                lihatDataIndexRumah.putExtra("nama_anggota_dtl",nama_anggota_dtl)
                lihatDataIndexRumah.putExtra("nik_ktp_dtl", nik_ktp_dtl)
                lihatDataIndexRumah.putExtra("tipe_dtl", tipe_dtl)
                lihatDataIndexRumah.putExtra("center_dtl", center_dtl)
                lihatDataIndexRumah.putExtra("kelompok_dtl", kelompok_dtl)
                lihatDataIndexRumah.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
                lihatDataIndexRumah.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
                lihatDataIndexRumah.putExtra("handphone_dtl", handphone_dtl)
                lihatDataIndexRumah.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
                lihatDataIndexRumah.putExtra("status_kawin_dtl", status_kawin_dtl)
                lihatDataIndexRumah.putExtra("nama_suami_dtl", nama_suami_dtl)
                lihatDataIndexRumah.putExtra("nama_ibu_kandung_dtl",nama_ibu_kandung_dtl)

                context?.startActivity(lihatDataIndexRumah)

                // Finish the current activity
                (context as Activity).finish()
            }
        })

        try {
            database_mdismo.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

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

     fun prepareItem() {
        kode_uk_index_tv?.text = kode_uk_dtl
        nik_ktp_uk_index_tv?.text = nik_ktp_dtl
        nama_anggota_uk_index_tv?.text = nama_anggota_dtl
        center_keluarga_tv?.text = center_dtl
        kelompok_keluarga_tv?.text = kelompok_dtl
        tv_index_dinding?.text = index_dinding_dtl
        tv_index_atap?.text = index_atap_dtl
        tv_index_lantai?.text = index_lantai_dtl
        tv_kondisi_dinding?.text = kondisi_dinding_dtl
        tv_kondisi_atap?.text = kondisi_atap_dtl
        tv_kondisi_lantai?.text = kondisi_lantai_dtl
        tv_poin_dinding?.text = poin_dinding_dtl
        tv_poin_atap?.text = poin_atap_dtl
        tv_poin_lantai?.text = poin_lantai_dtl
        tv_total_skor_index?.text = total_skor_index_dtl
        tv_status_milik?.text = status_milik
        supportActionBar?.title = tipe_dtl

    }


    private fun setupView() {
        tv_index_dinding = findViewById(R.id.tv_index_dinding)
        tv_index_atap = findViewById(R.id.tv_index_atap)
        tv_index_lantai = findViewById(R.id.tv_index_lantai)
        tv_kondisi_dinding = findViewById(R.id.tv_kondisi_dinding)
        tv_kondisi_atap = findViewById(R.id.tv_kondisi_atap)
        tv_kondisi_lantai = findViewById(R.id.tv_kondisi_lantai)
        tv_poin_dinding = findViewById(R.id.tv_poin_dinding)
        tv_poin_atap = findViewById(R.id.tv_poin_atap)
        tv_poin_lantai = findViewById(R.id.tv_poin_lantai)
        tv_total_skor_index = findViewById(R.id.tv_total_skor_index)
        kode_uk_index_tv = findViewById(R.id.kode_uk_index_tv)
        nik_ktp_uk_index_tv = findViewById(R.id.nik_ktp_uk_index_tv)
        nama_anggota_uk_index_tv = findViewById(R.id.nama_anggota_uk_index_tv)
        center_keluarga_tv = findViewById(R.id.center_keluarga_tv)
        kelompok_keluarga_tv = findViewById(R.id.kelompok_keluarga_tv)
        total_skor_index_dtl = (totalskorindex() ?: 0).toString()
        tv_status_milik = findViewById(R.id.tv_status_milik)
        tv_total_skor_index?.text = total_skor_index_dtl

        supportActionBar?.title = "Index Rumah"
    }


    fun totalskorindex(): Int? {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        var total: Int? = null
        val res = database_mdismo.rawQuery(
            "select COALESCE(skor_index, 0)  " +
                    "from uk_dtl_index_rumah where kode_uk='${kode_uk_dtl}'  ",
            null
        )
        while (res.moveToNext()) {
            total = res.getInt(0)
        }
        database_mdismo.close()
        return total
    }
    fun totalindex(): Int? {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        var total: Int? = null
        val res = database_mdismo.rawQuery(
            "select count(*)    " +
                    "from uk_dtl_index_rumah where kode_uk='${kode_uk_dtl}'  ",
            null
        )
        while (res.moveToNext()) {
            total = res.getInt(0)
        }
        database_mdismo.close()
        return total
    }
    fun clickAction(view: View) {
        val id = view.id
        if (id == R.id.btn_tambah_index_rumah) {

            if(totalindex()!! >0)
            {
                gagal_app("Pengisian Index Rumah sudah di Isi  : \n " +
                        "Nama : $nama_anggota_dtl\n" +
                        "Silahkan ubah datanya dengan cara klik informasi dibawah"
                )
            }
            else {
                val tambah_data_pendapatan = Intent(this, FormTambahIndexRumah::class.java)

                tambah_data_pendapatan.putExtra("kode_uk_dtl", kode_uk_dtl)
                tambah_data_pendapatan.putExtra("nama_anggota_dtl", nama_anggota_dtl)
                tambah_data_pendapatan.putExtra("nik_ktp_dtl", nik_ktp_dtl)
                tambah_data_pendapatan.putExtra("tipe_dtl", tipe_dtl)
                tambah_data_pendapatan.putExtra("center_dtl", center_dtl)
                tambah_data_pendapatan.putExtra("kelompok_dtl", kelompok_dtl)
                tambah_data_pendapatan.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
                tambah_data_pendapatan.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
                tambah_data_pendapatan.putExtra("handphone_dtl", handphone_dtl)
                tambah_data_pendapatan.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
                tambah_data_pendapatan.putExtra("status_kawin_dtl", status_kawin_dtl)
                tambah_data_pendapatan.putExtra("nama_suami_dtl", nama_suami_dtl)
                tambah_data_pendapatan.putExtra("nama_ibu_kandung_dtl", nama_ibu_kandung_dtl)

                startActivity(tambah_data_pendapatan)
            }

        }
    }
    private fun gagal_app(ketnih: String) {
        val teksket: TextView
        val btnUpdate: Button
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val builder = AlertDialog.Builder(this)
        val view1 = LayoutInflater.from(this).inflate(R.layout.gagal_app, viewGroup, false)
        builder.setCancelable(false)
        builder.setView(view1)
        val alertDialog = builder.create()

        btnUpdate = view1.findViewById(R.id.btn_dialog)
        teksket = view1.findViewById(R.id.ket)
        teksket.text = ketnih

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnUpdate.setOnClickListener { view ->
            // Handle button click
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        //super.onBackPressed()

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
        //finish()
    }
}