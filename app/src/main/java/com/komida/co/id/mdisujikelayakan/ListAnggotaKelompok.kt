package com.komida.co.id.mdisujikelayakan

import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.komida.co.id.mdisujikelayakan.adapter.AdapterAnggotaKelompok
import com.komida.co.id.mdisujikelayakan.adapter.AdapterInfoAnggota
import com.komida.co.id.mdisujikelayakan.model.ModelListAgtUKAll
import com.komida.co.id.mdisujikelayakan.model.ModelListKelompok

class ListAnggotaKelompok : AppCompatActivity() {
    private lateinit var parent_view: View
    private lateinit var nama_staff_tv: TextView
    private lateinit var nik_staff_tv: TextView
    private lateinit var cab_staff_tv: TextView
    private lateinit var jab_staff_tv: TextView
    private lateinit var dev_staff_tv: TextView
    private lateinit var hp_staff_tv: TextView
    private lateinit var kode_cab_staff_tv: TextView
    private lateinit var tot_kelompok_tv: TextView
    private lateinit var btn_tambah_kelompok:FloatingActionButton

    private lateinit var preferences: SharedPreferences
    companion object {
        lateinit var nama_st: String
        lateinit var nik_st: String
        lateinit var cab_st: String
        lateinit var jab_st: String
        lateinit var dev_st: String
        lateinit var hp_st: String
        lateinit var kode_cab_st: String
        lateinit var RELEASE: String
        lateinit var DEVICE: String
        lateinit var MODEL: String
        lateinit var PRODUCT: String
        lateinit var BRAND: String
        lateinit var totkelompok: String
        lateinit var totagt: String
        lateinit var nomor_center: String


    }
    val itemsList: ArrayList<ModelListKelompok> = ArrayList()
    private lateinit var AdapterAnggotaKelompok: AdapterAnggotaKelompok
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
    var items: List<ModelListKelompok>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_anggota_kelompok)
        initToolbar()
        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        parent_view = findViewById<View>(android.R.id.content)
        getPrefenceStaff ()
        setupView()
        prepareSet()



    }
    private fun initToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbarlistanggota)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Daftar Kelompok"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
    }
    private fun getPrefenceStaff() {
        nama_st = preferences.getString("nama","")!!
        nik_st  = preferences.getString("nik", "")!!
        dev_st  = preferences.getString("devid", "")!!
        jab_st  = preferences.getString("jabatan", "")!!
        cab_st  = preferences.getString("cabang", "")!!
        kode_cab_st = preferences.getString("kodecabang", "")!!
        hp_st = preferences.getString("hp", "")!!
    }
    private fun setupView() {
        nama_staff_tv = findViewById(R.id.nama_staff)
        nik_staff_tv = findViewById(R.id.nik_staff)
        cab_staff_tv = findViewById(R.id.cab_staff)
        tot_kelompok_tv = findViewById(R.id.tot_kelompok_tv)


        RELEASE = Build.VERSION.RELEASE // Android version/release
        DEVICE = Build.DEVICE // Device name
        MODEL = Build.MODEL // Device model
        PRODUCT = Build.PRODUCT // Device product
        BRAND = Build.BRAND // Device brand
        var OSNAME = System.getProperty("os.name")



        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        loadMoreButton = findViewById(R.id.loadMoreButton)

        btn_tambah_kelompok = findViewById(R.id.btn_tambah_kelompok)
        btn_tambah_kelompok.setOnClickListener {
            val tambah_kelompok = Intent(this, FormTambahKelompok::class.java)
            startActivity(tambah_kelompok)
        }
        //isi kolom total center
        // tot_ctr_tv = findViewById<TextView>(R.id.total_center)
        // tot_agt_tv = findViewById<TextView>(R.id.total_anggota)
        //recyclerView = findViewById(R.id.recyclerView)
    }
    private fun prepareSet() {
        nama_staff_tv.text= nama_st
        nik_staff_tv.text = nik_st
        cab_staff_tv.text = cab_st

        totkelompok = (total_kelompok() ?: 0).toString()
        println("TOTALKELOMPOK"+totkelompok)
        tot_kelompok_tv.setText(totkelompok)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.setHasFixedSize(true)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        AdapterAnggotaKelompok = AdapterAnggotaKelompok(itemsList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = AdapterAnggotaKelompok
        loadInitialData()
        nestedSV = findViewById(R.id.nested_content)

    }
    private fun total_kelompok(): Int? {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        var total: Int? = null
        val query="select count(*) from (select no_trx_lwk from uk_lwk_anggota  where cabang='$cab_st' group by no_trx_lwk) as a";
        println(query)
        val res = database_mdismo.rawQuery(
            "select count(*) from (select no_trx_lwk from uk_lwk_anggota  where cabang='$kode_cab_st' group by no_trx_lwk) as a", null
        )
        while (res.moveToNext()) {
            total = res.getInt(0)
        }
        database_mdismo.close()
        return total
    }

    private fun loadInitialData() {
        load_kelompok(currentOffset, LIMIT)
    }
    private fun load_kelompok( offset: Int, limit: Int) {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)

        val query = "SELECT kode_uk_lwk,no_trx_lwk, tgl_input,cabang,no_center,nama_center,no_kelompok,nama_kelompok, " +
                "  tempat,waktu, kategori_lwk, ketua_center, wakil_ketua_center, ketua_kelompok,petugas,ttd_petugas,catatan, " +
                "  user_create,date_create, user_modified, date_modified, penerima_kedua,wakil_ketua_kelompok " +
                " FROM uk_lwk_anggota ORDER BY kode_uk_lwk DESC LIMIT $limit OFFSET $offset"

        val res = database_mdismo.rawQuery(query, null)

        while (res.moveToNext()) {
            val kode_uk_lwk = res.getString(0)
            val no_trx_lwk = res.getString(1)
            val tgl_input = res.getString(2)
            val cabang = res.getString(3)
            val no_center = res.getString(4)
            val nama_center = res.getString(5)
            val no_kelompok = res.getString(6)
            val nama_kelompok = res.getString(7)
            val tempat = res.getString(8)
            val waktu = res.getString(9)
            val kategori_lwk = res.getString(10)
            val ketua_center = res.getString(11)
            val partsKetuaCenter = ketua_center?.split("^")
            var nikKetuaCenter: String = "-"
            var nameKetuaCenter: String = "-"
            var kodeUkAgtKetuaCenter: String = "-"
            var concatKC: String = "-"

            if (partsKetuaCenter != null && partsKetuaCenter.size >= 3) {
                nikKetuaCenter = partsKetuaCenter[0].trim()
                nameKetuaCenter = partsKetuaCenter[1].trim()
                kodeUkAgtKetuaCenter = partsKetuaCenter[2].trim()
                concatKC = nikKetuaCenter+" | "+nameKetuaCenter
            }
            val wakil_ketua_center = res.getString(12)

            val partsWKetuaCenter = wakil_ketua_center?.split("^")
            var nikWKetuaCenter: String = "-"
            var nameWKetuaCenter: String = "-"
            var kodeUkAgtWKetuaCenter: String = "-"
            var concatWKC: String = "-"
            if (partsWKetuaCenter != null && partsWKetuaCenter.size >= 2) {
                nikWKetuaCenter = partsWKetuaCenter[0].trim()
                nameWKetuaCenter = partsWKetuaCenter[1].trim()
                kodeUkAgtWKetuaCenter = partsWKetuaCenter[2].trim()
                concatWKC = nikWKetuaCenter+" | "+nameWKetuaCenter

            }
            val ketua_kelompok = res.getString(13)

            val petugas = res.getString(14)
            val ttd_petugas = res.getString(15)
            val catatan = res.getString(16)
            val user_create = res.getString(17)
            val date_create = res.getString(18)
            val user_modified = res.getString(19)
            val date_modified = res.getString(20)
            val penerima_kedua = res.getString(21)
            val wakil_ketua_kelompok = res.getString(22)

            val item = ModelListKelompok(
                no_trx_lwk + "<br><small> "+kategori_lwk+"</small>",
                kode_uk_lwk,
                no_trx_lwk,
                tgl_input,
                cabang,
                no_center,
                nama_center,
                no_kelompok,
                nama_kelompok,
                tempat,
                waktu,
                concatKC,
                concatWKC,
                ketua_kelompok,
                wakil_ketua_kelompok,
                petugas,
                ttd_petugas,
                kategori_lwk,
                penerima_kedua,
            )
            itemsList.add(item)
        }

        AdapterAnggotaKelompok.notifyDataSetChanged()
        res.close()
        loadMoreButton?.visibility = View.GONE

        AdapterAnggotaKelompok.setOnItemClickListener(object : AdapterAnggotaKelompok.OnItemClickListener {
            override fun onItemClick(view: View?, obj: ModelListKelompok?, position: Int) {
                val context = view?.context

                val liatFormKelompok = Intent(context, FormTambahKelompok::class.java)
                liatFormKelompok.putExtra("kode_uk_lwk_dtl",obj?.kode_uk_trx_lwk)
                liatFormKelompok.putExtra("no_trx_lwk_dtl",obj?.no_trx_lwk)
                liatFormKelompok.putExtra("tgl_input_dtl",obj?.tgl_input)
                liatFormKelompok.putExtra("cabang_info_dtl",obj?.cabang)
                liatFormKelompok.putExtra("no_center_dtl",obj?.no_center)
                liatFormKelompok.putExtra("nama_center_dtl",obj?.nama_center)
                liatFormKelompok.putExtra("no_kelompok_dtl",obj?.no_kelompok)
                liatFormKelompok.putExtra("nama_kelompok_dtl",obj?.nama_kelompok)
                liatFormKelompok.putExtra("tempat_dtl",obj?.tempat)
                liatFormKelompok.putExtra("waktu_dtl",obj?.waktu)
                liatFormKelompok.putExtra("kategori_lwk_dtl",obj?.kategori_lwk)
                liatFormKelompok.putExtra("ketua_center_dtl",obj?.ketua_center )
                liatFormKelompok.putExtra("wakil_ketua_center_dtl",obj?.wakil_ketua_center)
                liatFormKelompok.putExtra("ketua_kelompok_dtl",obj?.ketua_kelompok)
                liatFormKelompok.putExtra("petugas_dtl",obj?.petugas)
                liatFormKelompok.putExtra("ttd_petugas_dtl",obj?.ttd_petugas)
                liatFormKelompok.putExtra("penerima_kedua_dtl",obj?.penerima_kedua)
                context?.startActivity(liatFormKelompok)


            }
        })

        AdapterAnggotaKelompok.setOnItemLongClickListener(object : AdapterAnggotaKelompok.OnItemLongClickListener {
            override fun onItemLongClick(view: View?, obj: ModelListKelompok?, position: Int) {
                val context = view?.context
               // panggilDialogAnggota(context, obj, view)
            }
        })

        try {
            database_mdismo.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

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