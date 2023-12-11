package com.komida.co.id.mdisujikelayakan

import android.annotation.SuppressLint
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
import com.komida.co.id.mdisujikelayakan.adapter.AdapterAnggotaPendapatan
import com.komida.co.id.mdisujikelayakan.model.ModelListAgtPendapatan
import com.komida.co.id.mdisujikelayakan.model.ModelListIndexRumah
import java.util.*
import kotlin.collections.ArrayList

class ListAnggotaPendapatan : AppCompatActivity() {
    private lateinit var parent_view: View
    private lateinit var preferences: SharedPreferences
    private var tv_kode_uk_pen: TextView? =null
    private var tv_kode_uk_agt:TextView? =null
    private var tv_pendapatan_suami_tetap:TextView? =null
    private var tv_pendapatan_suami_tidak_tetap:TextView? =null
    private var tv_total_pendapatan_suami_per_bulan:TextView? =null
    private var tv_pendapatan_istri_tetap:TextView? =null
    private var tv_pendapatan_istri_tidak_tetap:TextView? =null
    private var tv_total_pendapatan_istri_per_bulan:TextView? =null
    private var tv_pendapatan_lainnya_tetap:TextView? =null
    private var tv_pendapatan_lainnya_tidak_tetap:TextView? =null
    private var tv_total_pendapatan_lainnya_per_bulan:TextView? =null
    private var tv_total_pendapatan_semua_per_bulan:TextView? =null
    private var tv_pengeluaran_rt:TextView? =null
    private var tv_pengeluaran_lainnya:TextView? =null
    private var tv_total_pengeluaran_per_bulan:TextView? =null
    private var tv_total_pendapatan_bersih:TextView? =null
    private var tv_total_pendapatan_list:TextView? =null
    private var tv_total_pengeluaran_list:TextView? =null
    private var tv_total_bersih_list:TextView? =null

    private  var kode_uk_pendapatan_tv: TextView? = null
    private  var nik_ktp_uk_pendapatan_tv: TextView? = null
    private  var nama_anggota_uk_pendapatan_tv: TextView? = null
    private  var center_keluarga_tv: TextView? = null
    private  var kelompok_keluarga_tv: TextView? = null
    private  var btn_tambah_pendapatan: Button?=null

    companion object {

        lateinit var pendapatan_suami_tetap_dtl:String
        lateinit var pendapatan_suami_tidak_tetap_dtl:String
        lateinit var total_pendapatan_suami_per_bulan_dtl:String
        lateinit var pendapatan_istri_tetap_dtl:String
        lateinit var pendapatan_istri_tidak_tetap_dtl:String
        lateinit var total_pendapatan_istri_per_bulan_dtl:String
        lateinit var pendapatan_lainnya_tetap_dtl:String
        lateinit var pendapatan_lainnya_tidak_tetap_dtl:String
        lateinit var total_pendapatan_lainnya_per_bulan_dtl:String
        lateinit var total_pendapatan_semua_per_bulan_dtl:String
        lateinit var pengeluaran_rt_dtl:String
        lateinit var pengeluaran_lainnya_dtl:String
        lateinit var total_pengeluaran_per_bulan_dtl:String
        lateinit var total_pendapatan_bersih_dtl:String
        lateinit var nama_anggota_dtl: String
        lateinit var kode_uk_dtl: String
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


    }
    val itemsList: ArrayList<ModelListAgtPendapatan> = ArrayList()
    private lateinit var AdapterAnggotaPendapatan: AdapterAnggotaPendapatan
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
    var items: List<ModelListAgtPendapatan>? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_anggota_pendapatan)
        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        val toolbar: Toolbar = findViewById(R.id.toolbarlistanggotapendapatan)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val detilIntent = this.intent
        getPreferenceAnggota(detilIntent)
        setupView()
        prepareItem()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        AdapterAnggotaPendapatan = AdapterAnggotaPendapatan(itemsList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = AdapterAnggotaPendapatan

        loadInitialData()
        val nestedSV: NestedScrollView = findViewById(R.id.nested_content)


    }

    private fun loadInitialData() {
        itemsList.clear()
        load_pendapatan_agt(currentOffset, LIMIT)
    }

    private fun load_pendapatan_agt(currentOffset: Int, limit: Int) {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)

        val query = "SELECT kode_uk_pendapatan,kode_uk,suami_tetap,suami_tidak_tetap,suami_per_bulan,istri_tetap,istri_tidak_tetap,istri_per_bulan,pendapatan_lainnya_tetap,pendapatan_lainnya_tdk_tetap,pendapatan_lainnya_per_bulan,total_pendapatan_per_bulan,total_pendapatan_bersih_per_bulan,total_pengeluaran_rt,total_pengeluaran_lain,total_pengeluaran_per_bulan,usr_crt,dt_crt,usr_upd,dt_upd " +
                "FROM uk_pendapatan where kode_uk='${kode_uk_dtl}' ORDER BY dt_upd"

        val res = database_mdismo.rawQuery(query, null)

        while (res.moveToNext()) {

            val kode_uk_pendapatan = res.getString(res.getColumnIndex("kode_uk_pendapatan"))
            val kode_uk = res.getString(res.getColumnIndex("kode_uk"))
            val suami_tetap = res.getString(res.getColumnIndex("suami_tetap"))
            val suami_tidak_tetap = res.getString(res.getColumnIndex("suami_tidak_tetap"))
            val suami_per_bulan = res.getString(res.getColumnIndex("suami_per_bulan"))
            val istri_tetap = res.getString(res.getColumnIndex("istri_tetap"))
            val istri_tidak_tetap = res.getString(res.getColumnIndex("istri_tidak_tetap"))
            val istri_per_bulan = res.getString(res.getColumnIndex("istri_per_bulan"))
            val pendapatan_lainnya_tetap = res.getString(res.getColumnIndex("pendapatan_lainnya_tetap"))
            val pendapatan_lainnya_tdk_tetap = res.getString(res.getColumnIndex("pendapatan_lainnya_tdk_tetap"))
            val pendapatan_lainnya_per_bulan = res.getString(res.getColumnIndex("pendapatan_lainnya_per_bulan"))
            val total_pendapatan_per_bulan = res.getString(res.getColumnIndex("total_pendapatan_per_bulan"))
            val total_pendapatan_bersih_per_bulan = res.getString(res.getColumnIndex("total_pendapatan_bersih_per_bulan"))
            val total_pengeluaran_rt = res.getString(res.getColumnIndex("total_pengeluaran_rt"))
            val total_pengeluaran_lain = res.getString(res.getColumnIndex("total_pengeluaran_lain"))
            val total_pengeluaran_per_bulan = res.getString(res.getColumnIndex("total_pengeluaran_per_bulan"))

            val item = ModelListAgtPendapatan(
                "Hasil Index Rumah" + "",
                kode_uk_pendapatan,
                kode_uk,
                getDecimalFormattedString(suami_tetap),
                getDecimalFormattedString(suami_tidak_tetap),
                getDecimalFormattedString(suami_per_bulan),
                getDecimalFormattedString(istri_tetap),
                getDecimalFormattedString(istri_tidak_tetap),
                getDecimalFormattedString(istri_per_bulan),
                getDecimalFormattedString(pendapatan_lainnya_tetap),
                getDecimalFormattedString(pendapatan_lainnya_tdk_tetap),
                getDecimalFormattedString(pendapatan_lainnya_per_bulan),
                getDecimalFormattedString(total_pendapatan_per_bulan),
                getDecimalFormattedString(total_pendapatan_bersih_per_bulan),
                getDecimalFormattedString(total_pengeluaran_rt),
                getDecimalFormattedString(total_pengeluaran_lain),
                getDecimalFormattedString(total_pengeluaran_per_bulan)
            )
            itemsList.add(item)
        }

        AdapterAnggotaPendapatan.notifyDataSetChanged()
        res.close()
        loadMoreButton?.visibility = View.VISIBLE

        AdapterAnggotaPendapatan.setOnItemClickListener(object : AdapterAnggotaPendapatan.OnItemClickListener {
            override fun onItemClick(view: View?, obj: ModelListAgtPendapatan?, position: Int) {
                val context = view?.context

                val lihatDataPendapatan = Intent(context, FormTambahAnggotaPendapatan::class.java)

                lihatDataPendapatan.putExtra("kode_uk_pendapatan", obj?.kode_uk_pendapatan)
                lihatDataPendapatan.putExtra("suami_tetap", obj?.suami_tetap)
                lihatDataPendapatan.putExtra("suami_tidak_tetap", obj?.suami_tidak_tetap)
                lihatDataPendapatan.putExtra("suami_per_bulan", obj?.suami_per_bulan)
                lihatDataPendapatan.putExtra("istri_tetap", obj?.istri_tetap)
                lihatDataPendapatan.putExtra("istri_tidak_tetap", obj?.istri_tidak_tetap)
                lihatDataPendapatan.putExtra("istri_per_bulan", obj?.istri_per_bulan)
                lihatDataPendapatan.putExtra("pendapatan_lainnya_tetap", obj?.pendapatan_lainnya_tetap)
                lihatDataPendapatan.putExtra("pendapatan_lainnya_tdk_tetap", obj?.pendapatan_lainnya_tdk_tetap)
                lihatDataPendapatan.putExtra("pendapatan_lainnya_per_bulan", obj?.pendapatan_lainnya_per_bulan)
                lihatDataPendapatan.putExtra("total_pendapatan_per_bulan", obj?.total_pendapatan_per_bulan)
                lihatDataPendapatan.putExtra("total_pendapatan_bersih_per_bulan", obj?.total_pendapatan_bersih_per_bulan)
                lihatDataPendapatan.putExtra("total_pengeluaran_rt", obj?.total_pengeluaran_rt)
                lihatDataPendapatan.putExtra("total_pengeluaran_lain", obj?.total_pengeluaran_lain)
                lihatDataPendapatan.putExtra("total_pengeluaran_per_bulan", obj?.total_pengeluaran_per_bulan)
                lihatDataPendapatan.putExtra("kode_uk_dtl", kode_uk_dtl)
                lihatDataPendapatan.putExtra("nama_anggota_dtl",nama_anggota_dtl)
                lihatDataPendapatan.putExtra("nik_ktp_dtl", nik_ktp_dtl)
                lihatDataPendapatan.putExtra("tipe_dtl", tipe_dtl)
                lihatDataPendapatan.putExtra("center_dtl", center_dtl)
                lihatDataPendapatan.putExtra("kelompok_dtl", kelompok_dtl)
                lihatDataPendapatan.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
                lihatDataPendapatan.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
                lihatDataPendapatan.putExtra("handphone_dtl", handphone_dtl)
                lihatDataPendapatan.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
                lihatDataPendapatan.putExtra("status_kawin_dtl", status_kawin_dtl)
                lihatDataPendapatan.putExtra("nama_suami_dtl", nama_suami_dtl)
                lihatDataPendapatan.putExtra("nama_ibu_kandung_dtl",nama_ibu_kandung_dtl)
                context?.startActivity(lihatDataPendapatan)

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

    private fun prepareItem() {



        tv_kode_uk_agt?.text = kode_uk_dtl
        tv_pendapatan_suami_tetap?.text = pendapatan_suami_tetap_dtl
        tv_pendapatan_suami_tidak_tetap?.text = pendapatan_suami_tidak_tetap_dtl
        tv_total_pendapatan_suami_per_bulan?.text = total_pendapatan_suami_per_bulan_dtl
        tv_pendapatan_istri_tetap?.text = pendapatan_istri_tetap_dtl
        tv_pendapatan_istri_tidak_tetap?.text =pendapatan_istri_tidak_tetap_dtl
        tv_total_pendapatan_istri_per_bulan?.text =total_pendapatan_istri_per_bulan_dtl
        tv_pendapatan_lainnya_tetap?.text = pendapatan_lainnya_tetap_dtl
        tv_pendapatan_lainnya_tidak_tetap?.text = pendapatan_lainnya_tidak_tetap_dtl
        tv_total_pendapatan_lainnya_per_bulan?.text =total_pendapatan_lainnya_per_bulan_dtl
        tv_total_pendapatan_semua_per_bulan?.text =total_pendapatan_semua_per_bulan_dtl
        tv_pengeluaran_rt?.text = pengeluaran_rt_dtl
        tv_pengeluaran_lainnya?.text = pengeluaran_lainnya_dtl
        tv_total_pengeluaran_per_bulan?.text = total_pengeluaran_per_bulan_dtl
        tv_total_pendapatan_bersih?.text = total_pendapatan_bersih_dtl
        //kode_uk_pendapatan_tv?.text =kode_uk_pendapatan_dtl


        nik_ktp_uk_pendapatan_tv?.text = nik_ktp_dtl
        nama_anggota_uk_pendapatan_tv?.text = nama_anggota_dtl
        center_keluarga_tv?.text = center_dtl
        kelompok_keluarga_tv?.text = kelompok_dtl


        var hitung_total_pendapatan = (hitung_total_pendapatan() ?: 0).toString()
        var hitung_total_pengeluaran = (hitung_total_pengeluaran() ?: 0).toString()
        var hitung_total_pendapatan_bersih = (hitung_total_pendapatan_bersih() ?: 0).toString()

        tv_total_pendapatan_list?.text = getDecimalFormattedString(hitung_total_pendapatan)
        tv_total_pengeluaran_list?.text = getDecimalFormattedString(hitung_total_pengeluaran)
        tv_total_bersih_list?.text = getDecimalFormattedString(hitung_total_pendapatan_bersih)


    }
    fun getDecimalFormattedString(value: String): String {
        val lst = StringTokenizer(value, ".")
        var str1 = value
        var str2 = ""
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken()
            str2 = lst.nextToken()
        }
        var str3 = ""
        var i = 0
        var j = str1.length - 1
        if (str1.isNotEmpty() && str1[j] == '.') {
            j--
            str3 = "."
        }
        for (k in j downTo 0) {
            if (i == 3) {
                str3 = ",$str3"
                i = 0
            }
            str3 = str1[k] + str3
            i++
        }
        if (str2.isNotEmpty()) {
            str3 = "$str3.$str2"
        }
        return str3
    }
     fun hitung_total_pendapatan_bersih(): Int? {
         database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
         var total: Int? = 0
         val res = database_mdismo.rawQuery(
             "select COALESCE(total_pendapatan_bersih_per_bulan,0)   " +
                     "from uk_pendapatan where kode_uk='${kode_uk_dtl}'  ",
             null
         )
         while (res.moveToNext()) {
             total = res.getInt(0)
         }
         database_mdismo.close()
         return total
    }

     fun hitung_total_pendapatan(): Int? {
         database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
         var total: Int? = null
         val res = database_mdismo.rawQuery(
             "select COALESCE(total_pendapatan_per_bulan,0)   " +
                     "from uk_pendapatan where kode_uk='${kode_uk_dtl}'  ",
             null
         )
         while (res.moveToNext()) {
             total = res.getInt(0)
         }
         database_mdismo.close()
         return total
    }
     fun hitung_total_pengeluaran(): Int? {
         database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
         var total: Int? = null
         val res = database_mdismo.rawQuery(
             "select  COALESCE(total_pengeluaran_per_bulan, 0)   " +
                     "from uk_pendapatan where kode_uk='${kode_uk_dtl}'  ",
             null
         )
         while (res.moveToNext()) {
             total = res.getInt(0)
         }
         database_mdismo.close()
         return total
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

        tv_kode_uk_pen = findViewById(R.id.tv_kode_uk_pen)
        tv_kode_uk_agt = findViewById(R.id.tv_kode_uk_agt)
        tv_pendapatan_suami_tetap = findViewById(R.id.tv_pendapatan_suami_tetap)
        tv_pendapatan_suami_tidak_tetap = findViewById(R.id.tv_pendapatan_suami_tidak_tetap)
        tv_total_pendapatan_suami_per_bulan = findViewById(R.id.tv_total_pendapatan_suami_per_bulan)
        tv_pendapatan_istri_tetap = findViewById(R.id.tv_pendapatan_istri_tetap)
        tv_pendapatan_istri_tidak_tetap = findViewById(R.id.tv_pendapatan_istri_tidak_tetap)
        tv_total_pendapatan_istri_per_bulan = findViewById(R.id.tv_total_pendapatan_istri_per_bulan)
        tv_pendapatan_lainnya_tetap = findViewById(R.id.tv_pendapatan_lainnya_tetap)
        tv_pendapatan_lainnya_tidak_tetap = findViewById(R.id.tv_pendapatan_lainnya_tidak_tetap)
        tv_total_pendapatan_lainnya_per_bulan = findViewById(R.id.tv_total_pendapatan_lainnya_per_bulan)
        tv_total_pendapatan_semua_per_bulan = findViewById(R.id.tv_total_pendapatan_per_bulan)
        tv_pengeluaran_rt = findViewById(R.id.tv_pengeluaran_rt)
        tv_pengeluaran_lainnya = findViewById(R.id.tv_pengeluaran_lainnya)
        tv_total_pengeluaran_per_bulan = findViewById(R.id.tv_total_pengeluaran_per_bulan)
        tv_total_pendapatan_bersih = findViewById(R.id.tv_total_pendapatan_bersih)
        kode_uk_pendapatan_tv = findViewById(R.id.kode_uk_pendapatan_tv)


        nik_ktp_uk_pendapatan_tv = findViewById(R.id.nik_ktp_uk_pendapatan_tv)
        nama_anggota_uk_pendapatan_tv = findViewById(R.id.nama_anggota_uk_pendapatan_tv)
        center_keluarga_tv = findViewById(R.id.center_keluarga_tv)
        kelompok_keluarga_tv = findViewById(R.id.kelompok_keluarga_tv)


        tv_total_pendapatan_list = findViewById(R.id.tv_total_pendapatan_list)
        tv_total_pengeluaran_list = findViewById(R.id.tv_total_pengeluaran_list)
        tv_total_bersih_list = findViewById(R.id.tv_total_bersih_list)




        supportActionBar?.title = "Pendapatan Anggota"
    }
    fun clickAction(view: View) {
        val id = view.id
        if (id == R.id.btn_tambah_pendapatan) {
            println("ANU"+R.id.btn_tambah_pendapatan)
            println(hitung_total_pendapatan_bersih())

            if(hitung_total_pendapatan_bersih()!! >0)
            {
                gagal_app("Pengisian Pendapatan Rumah sudah di Isi  : \n " +
                        "Nama : $nama_anggota_dtl\n" +
                        "Silahkan ubah datanya dengan cara klik informasi dibawah"
                )
            }
            else {

                val tambah_data_pendapatan = Intent(this, FormTambahAnggotaPendapatan::class.java)
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