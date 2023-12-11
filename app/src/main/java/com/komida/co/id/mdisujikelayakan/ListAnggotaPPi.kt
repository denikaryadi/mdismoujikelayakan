package com.komida.co.id.mdisujikelayakan

import ViewAnimation
import ViewAnimation.AnimListener
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.komida.co.id.mdisujikelayakan.adapter.AdapterInfoPertanyaanPPI
import com.komida.co.id.mdisujikelayakan.adapter.AdapterInfoPertanyaanTambahan
import com.komida.co.id.mdisujikelayakan.model.ModelListPertanyaanPPi
import com.komida.co.id.mdisujikelayakan.model.ModelListPertanyaanTambahan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ListAnggotaPPi : AppCompatActivity() {
    private lateinit var parent_view: View
    private lateinit var preferences: SharedPreferences
    private lateinit var kode_uk_keluarga_tv: TextView
    private lateinit var nik_ktp_keluarga_tv: TextView
    private lateinit var nama_anggota_keluarga_tv: TextView
    private lateinit var center_keluarga_tv: TextView
    private lateinit var kelompok_keluarga_tv: TextView
    private lateinit var tv_total_skor_ppi: TextView
    private lateinit var nested_scroll_view: NestedScrollView
    private lateinit var bt_toggle_ppi:ImageButton
    private lateinit var bt_toggle_ppi_add:ImageButton
    private lateinit var progressBarPPi:ProgressBar
    private lateinit var progressBarPPiAdd:ProgressBar
    private lateinit var lyt_expand_ppi:LinearLayout
    private lateinit var lyt_expand_ppi_add:LinearLayout

    private lateinit var bt_hide_info: Button
    private lateinit var bt_hide_info_ppi:Button


    private lateinit var database_mdismo: SQLiteDatabase

    val itemsListPpi: ArrayList<ModelListPertanyaanPPi> = ArrayList()
    private lateinit var AdapterInfoPertanyaanPPI: AdapterInfoPertanyaanPPI
    val itemsListPpiAdd: ArrayList<ModelListPertanyaanTambahan> = ArrayList()
    private lateinit var AdapterInfoPertanyaanTambahan: AdapterInfoPertanyaanTambahan
    private lateinit var recyclerViewFormPPI: RecyclerView
    private lateinit var recyclerViewFormPPIAdd: RecyclerView



    var DB_NAME = "mdismo.db"
    companion object {
        lateinit var nama_st: String
        lateinit var nik_st: String
        lateinit var cab_st: String
        lateinit var jab_st: String
        lateinit var dev_st: String
        lateinit var hp_st: String
        lateinit var kode_cab_st: String

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

    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_anggota_ppi)
        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        val toolbar: Toolbar = findViewById(R.id.toolbarlistppi)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Formulir PPI"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detilIntent = this.intent
        getPreferenceAnggota(detilIntent)
        getPreferenceStaff()
        setupView()
        prepareitem()
        // Initialize adapters before setting up RecyclerViews
        AdapterInfoPertanyaanPPI = AdapterInfoPertanyaanPPI(itemsListPpi, parent_view)
        AdapterInfoPertanyaanTambahan = AdapterInfoPertanyaanTambahan(itemsListPpiAdd, parent_view)

        // Setup RecyclerViews after initializing adapters

        setupRecyclerView(recyclerViewFormPPI, itemsListPpi, AdapterInfoPertanyaanPPI)
        setupRecyclerView(recyclerViewFormPPIAdd, itemsListPpiAdd, AdapterInfoPertanyaanTambahan)




        bt_toggle_ppi.setOnClickListener {
            toggleSectionInfoPpi(bt_toggle_ppi)


        }
        bt_hide_info.setOnClickListener {
            toggleSectionInfoPpi(bt_toggle_ppi)
        }

        bt_toggle_ppi_add.setOnClickListener {
            toggleSectionInfoPpiAdd(bt_toggle_ppi_add)
        }
        bt_hide_info_ppi.setOnClickListener {
            toggleSectionInfoPpiAdd(bt_toggle_ppi_add)
        }


    }
    private fun <T : RecyclerView.ViewHolder> setupRecyclerView(
        recyclerViewId: RecyclerView,
        itemList: List<Any>,  // Change 'Any' to the type of your list items
        adapter: RecyclerView.Adapter<T>
    ) {
        recyclerViewId.layoutManager = LinearLayoutManager(this)
        recyclerViewId.setHasFixedSize(true)
        recyclerViewId.adapter = adapter

    }

    private fun loadPertanyaanPPi(adapterInfoPertanyaanPPI: AdapterInfoPertanyaanPPI) {

        progressBarPPi.visibility = View.VISIBLE
        recyclerViewFormPPI.visibility=View.GONE

        CoroutineScope(Dispatchers.IO).launch {

            database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)

            try {
                val query = "SELECT pq.Comp_ID, "   +
                        "pq.QuestionID as id_soal, " +
                        "QuestionDesc as pertanyaan, " +
                        "'1' as tipe_pertanyaan, " +
                        "'1' as status, " +
                        "pq.EntriedBy as usr_crt, " +
                        "pq.EntriedDate as dt_crt " +
                        "FROM PPI_VersionQuestion pvq " +
                        "JOIN (SELECT MAX(VersionKey) VersionKey FROM PPI_Version) pvi " +
                        "ON pvi.VersionKey = pvq.VersionKey " +
                        "JOIN PPI_Question pq ON pq.QuestionID = pvq.QuestionID " +
                        "WHERE EXISTS (SELECT 1 FROM PPI_QuestionPoin WHERE QuestionID = pq.QuestionID) " +
                        "ORDER BY pq.QuestionID ASC"

                val res = database_mdismo.rawQuery(query, null)
                var no_urutan = 1 // Initialize the question numbering
                itemsListPpi.clear()
                while (res.moveToNext()) {
                    val Comp_ID = res.getString(res.getColumnIndex("Comp_ID"))
                    val id_soal = res.getString(res.getColumnIndex("id_soal"))
                    val pertanyaan = res.getString(res.getColumnIndex("pertanyaan"))
                    val tipe_pertanyaan = res.getString(res.getColumnIndex("tipe_pertanyaan"))
                    val status = res.getString(res.getColumnIndex("status"))
                    val usr_crt = res.getString(res.getColumnIndex("usr_crt"))
                    val dt_crt = res.getString(res.getColumnIndex("dt_crt"))

                    var queryjawaban = ""
                    queryjawaban = " SELECT pqp.Comp_ID as Comp_ID," +
                            " pqp.QuestionID as id_soal," +
                            " pqp.Answer as pg," +
                            " pqp.AnswerDesc as isi_jawaban," +
                            " pqp.Poin as poin," +
                            " pqp.Score as score," +
                            " '1' as status" +
                            " , CASE WHEN  b.id_jawaban = pqp.Answer THEN 'checked' ELSE 'unchecked' END checked" +
                            " FROM PPI_QuestionPoin pqp " +
                            " left join uk_ppi_anggota b on pqp.QuestionID=b.id_soal " +
                            " WHERE pqp.QuestionID = '$no_urutan' and b.kode_uk='$kode_uk_dtl'" +
                            " ORDER BY pqp.QuestionID ASC;"


                    val resa = database_mdismo.rawQuery(queryjawaban, null)
                    val pgs = mutableListOf<String>()
                    val isi_jawabans = mutableListOf<String>()
                    val poins = mutableListOf<String>()
                    val scores = mutableListOf<String>()
                    while (resa.moveToNext()) {
                        val id_soal = resa.getString(resa.getColumnIndex("id_soal"))
                        val pg = resa.getString(resa.getColumnIndex("pg"))
                        val isi_jawaban = resa.getString(resa.getColumnIndex("isi_jawaban"))
                        val poin = resa.getString(resa.getColumnIndex("poin"))
                        val score = resa.getString(resa.getColumnIndex("score"))
                        val checked = resa.getString(resa.getColumnIndex("checked"))

                        pgs.add(pg + "^" + isi_jawaban + "^" + poin + "^" + score + "^" + id_soal + "^" + checked)
                        isi_jawabans.add(isi_jawaban)
                        poins.add(poin)
                        scores.add(score)
                    }

                    // Close the answers cursor
                    resa.close()

                    val item = ModelListPertanyaanPPi(
                        pertanyaan,
                        no_urutan,
                        Comp_ID,
                        id_soal,
                        pertanyaan,
                        tipe_pertanyaan,
                        status,
                        usr_crt,
                        dt_crt,
                        pgs,
                        isi_jawabans,
                        poins,
                        scores,
                        flag_view = "view"
                    )

                    itemsListPpi.add(item)
                    no_urutan++
                }

                withContext(Dispatchers.Main) {
                    progressBarPPi.visibility = View.GONE
                    recyclerViewFormPPI.visibility = View.VISIBLE
                    adapterInfoPertanyaanPPI.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // Close the database after the operation is complete
                database_mdismo.close()
            }
        }
    }

    private fun loadPertanyaanPPiAdd(AdapterInfoPertanyaanTambahan: AdapterInfoPertanyaanTambahan) {
        println("loadingPPIADD")
        progressBarPPiAdd.visibility = View.VISIBLE
        recyclerViewFormPPIAdd.visibility=View.GONE
        CoroutineScope(Dispatchers.IO).launch {

            database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)

            try {
                val query = "select id_soal,urutan,tipe_pertanyaan,pertanyaan,status from ppi_soal where tipe_pertanyaan='2' order by urutan asc "
                val res = database_mdismo.rawQuery(query, null)
                var no_urutan = 1
                itemsListPpiAdd.clear()
                while (res.moveToNext()) {
                    val id_soal = res.getString(res.getColumnIndex("id_soal"))
                    val urutan = res.getString(res.getColumnIndex("urutan"))
                    val pertanyaan = res.getString(res.getColumnIndex("pertanyaan"))
                    val tipe_pertanyaan = res.getString(res.getColumnIndex("tipe_pertanyaan"))
                    val status = res.getString(res.getColumnIndex("status"))

                    val queryjawaban = "" +
                            " select " +
                            " a.id_jawaban,a.id_soal,a.urutan_jawaban,a.pg,a.isi_jawaban,a.poin,a.score,a.status, " +
                            " b.jml_pr,b.jml_pr_sklh,b.jml_lk,b.jml_lk_sklh,b.tot_jml,b.tot_jml_sklh,"+
                            " CASE WHEN  b.id_jawaban = a.pg THEN 'checked' ELSE 'unchecked' END checked " +
                            " from ppi_jawaban a left join uk_ppi_anggota b on a.id_soal=b.id_soal " +
                            " WHERE a.id_soal = '$id_soal' and kode_uk='$kode_uk_dtl' " +
                            " order by urutan_jawaban asc "
                    val resa = database_mdismo.rawQuery(queryjawaban, null)
                    val pgs = mutableListOf<String>()
                    val isi_jawabans = mutableListOf<String>()
                    val poins = mutableListOf<String>()
                    val scores = mutableListOf<String>()

                    while (resa.moveToNext()) {
                        val id_jawaban = resa.getString(resa.getColumnIndex("id_jawaban"))
                        val pg = resa.getString(resa.getColumnIndex("pg"))
                        val isi_jawaban = resa.getString(resa.getColumnIndex("isi_jawaban"))
                        val checked = resa.getString(resa.getColumnIndex("checked"))
                        val jml_pr = resa.getString(resa.getColumnIndex("jml_pr"))
                        val jml_pr_sklh = resa.getString(resa.getColumnIndex("jml_pr_sklh"))
                        val jml_lk = resa.getString(resa.getColumnIndex("jml_lk"))
                        val jml_lk_sklh = resa.getString(resa.getColumnIndex("jml_lk_sklh"))
                        val tot_jml = resa.getString(resa.getColumnIndex("tot_jml"))
                        val tot_jml_sklh = resa.getString(resa.getColumnIndex("tot_jml_sklh"))
                        pgs.add(pg+"^"+isi_jawaban+"^"+urutan+"^"+id_soal+"^"+urutan+"^"+checked+"^"+jml_pr+"^"+jml_pr_sklh+"^"+jml_lk+"^"+jml_lk_sklh+"^"+tot_jml+"^"+tot_jml_sklh)
                        isi_jawabans.add(isi_jawaban)
                    }
                    // Close the answers cursor
                    resa.close()


                    val item = ModelListPertanyaanTambahan(
                        pertanyaan,
                        id_soal,
                        urutan,
                        pertanyaan,
                        tipe_pertanyaan,
                        status,
                        pgs,
                        isi_jawabans,
                        flag_view = "view"
                    )

                    itemsListPpiAdd.add(item)
                    no_urutan++

                }

                withContext(Dispatchers.Main) {
                    progressBarPPiAdd.visibility = View.GONE
                    recyclerViewFormPPIAdd.visibility = View.VISIBLE
                    AdapterInfoPertanyaanTambahan.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // Close the database after the operation is complete
                database_mdismo.close()
            }
        }
    }

    fun toggleArrow(view: View): Boolean {
        return if (view.rotation == 0f) {
            view.animate().setDuration(100).rotation(180f)
            true
        } else {
            view.animate().setDuration(100).rotation(0f)
            false
        }
    }
    private fun toggleSectionInfoPpi(view: View) {
        val show: Boolean = toggleArrow(view)
        if (show) {
            ViewAnimation.expand(lyt_expand_ppi, object : AnimListener {
                override fun onFinish() {

                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_ppi)

                    loadPertanyaanPPi(AdapterInfoPertanyaanPPI)
                }
            })
        } else {
            ViewAnimation.collapse(lyt_expand_ppi)
            val emptyList: List<ModelListPertanyaanPPi> = listOf() // Create an empty list
            AdapterInfoPertanyaanPPI.updateList(emptyList)
        }
    }
    private fun toggleSectionInfoPpiAdd(view: View) {
        val show: Boolean = toggleArrow(view)
        if (show) {
            ViewAnimation.expand(lyt_expand_ppi_add, object : AnimListener {
                override fun onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_ppi_add)
                    loadPertanyaanPPiAdd(AdapterInfoPertanyaanTambahan)
                }
            })
        } else {
            ViewAnimation.collapse(lyt_expand_ppi_add)
            val emptyList: List<ModelListPertanyaanTambahan> = listOf() // Create an empty list
            AdapterInfoPertanyaanTambahan.updateList(emptyList)
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
    private fun getPreferenceStaff() {
        nama_st = preferences.getString("nama", "")!!
        nik_st = preferences.getString("nik", "")!!
        dev_st = preferences.getString("devid", "")!!
        jab_st = preferences.getString("jabatan", "")!!
        cab_st = preferences.getString("cabang", "")!!
        kode_cab_st = preferences.getString("kodecabang", "")!!
        hp_st = preferences.getString("hp", "")!!
    }
    private fun setupView() {

        recyclerViewFormPPI= findViewById(R.id.recyclerViewFormPPI)
        recyclerViewFormPPIAdd= findViewById(R.id.recyclerPpiAddView)

        progressBarPPiAdd= findViewById(R.id.loadingProgressBarPpiAdd)

        progressBarPPi= findViewById(R.id.loadingProgressBarPpi)
        progressBarPPiAdd= findViewById(R.id.loadingProgressBarPpiAdd)

        kode_uk_keluarga_tv = findViewById(R.id.kode_uk_keluarga_tv)
        nik_ktp_keluarga_tv = findViewById(R.id.nik_ktp_keluarga_tv)
        nama_anggota_keluarga_tv = findViewById(R.id.nama_anggota_keluarga_tv)
        center_keluarga_tv = findViewById(R.id.center_keluarga_tv)
        kelompok_keluarga_tv = findViewById(R.id.kelompok_keluarga_tv)
        nested_scroll_view = findViewById(R.id.nested_content)
        tv_total_skor_ppi = findViewById(R.id.tv_total_skor_ppi)
        bt_toggle_ppi = findViewById(R.id.bt_toggle_ppi)
        bt_toggle_ppi_add = findViewById(R.id.bt_toggle_ppi_add)
        lyt_expand_ppi = findViewById(R.id.lyt_expand_ppi)
        lyt_expand_ppi_add = findViewById(R.id.lyt_expand_ppi_add)

        bt_hide_info = findViewById(R.id.bt_hide_info)
        bt_hide_info_ppi = findViewById(R.id.bt_hide_info_ppi)
        parent_view = findViewById(R.id.parentLayout)

    }
    fun prepareitem(){
        kode_uk_keluarga_tv.text = kode_uk_dtl
        nik_ktp_keluarga_tv.text = nama_anggota_dtl
        nama_anggota_keluarga_tv.text = nik_ktp_dtl
        center_keluarga_tv.text = center_dtl
        kelompok_keluarga_tv.text = kelompok_dtl
        tv_total_skor_ppi.text ="0"
        var hitung_total_skor_ppi = (hitung_total_skor_ppi() ?: 0).toString()
        tv_total_skor_ppi?.text = getDecimalFormattedString(hitung_total_skor_ppi)

    }
    fun hitung_total_skor_ppi(): Int? {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        var total: Int? = null
        val res = database_mdismo.rawQuery(
            "select MAX(COALESCE(total_score,0))  as total  " +
                    "from uk_ppi_anggota where kode_uk='$kode_uk_dtl' ",
            null
        )
        while (res.moveToNext()) {
            total = res.getInt(0)
        }
        database_mdismo.close()
        return total
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
    fun clickAction(view: View) {
        val id = view.id
        if (id == R.id.btn_tambah_ppi) {
            val tamba_data_ppi = Intent(this, FormTambahPpiAnggota::class.java)

            tamba_data_ppi.putExtra("kode_uk_dtl", kode_uk_dtl)
            tamba_data_ppi.putExtra("nama_anggota_dtl", nama_anggota_dtl)
            tamba_data_ppi.putExtra("nik_ktp_dtl", nik_ktp_dtl)
            tamba_data_ppi.putExtra("tipe_dtl", tipe_dtl)
            tamba_data_ppi.putExtra("center_dtl", center_dtl)
            tamba_data_ppi.putExtra("kelompok_dtl", kelompok_dtl)
            tamba_data_ppi.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
            tamba_data_ppi.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
            tamba_data_ppi.putExtra("handphone_dtl", handphone_dtl)
            tamba_data_ppi.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
            tamba_data_ppi.putExtra("status_kawin_dtl", status_kawin_dtl)
            tamba_data_ppi.putExtra("nama_suami_dtl", nama_suami_dtl)
            tamba_data_ppi.putExtra("nama_ibu_kandung_dtl", nama_ibu_kandung_dtl)

            startActivity(tamba_data_ppi)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val lihat_data_ppi = Intent(this, UjiKelayakanActivity::class.java)
        lihat_data_ppi.putExtra("kode_uk_dtl", kode_uk_dtl)
        lihat_data_ppi.putExtra("nik_ktp_dtl", nik_ktp_dtl)
        lihat_data_ppi.putExtra("nama_anggota_dtl", nama_anggota_dtl)
        lihat_data_ppi.putExtra("tipe_dtl", tipe_dtl)
        lihat_data_ppi.putExtra("center_dtl", center_dtl)
        lihat_data_ppi.putExtra("kelompok_dtl", kelompok_dtl)
        lihat_data_ppi.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
        lihat_data_ppi.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
        lihat_data_ppi.putExtra("handphone_dtl", handphone_dtl)
        lihat_data_ppi.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
        lihat_data_ppi.putExtra("status_kawin_dtl", status_kawin_dtl)
        lihat_data_ppi.putExtra("nama_suami_dtl", nama_suami_dtl)
        lihat_data_ppi.putExtra("nama_ibu_kandung_dtl",nama_ibu_kandung_dtl)
        startActivity(lihat_data_ppi)
        //finish()
    }
}