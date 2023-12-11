package com.komida.co.id.mdisujikelayakan

import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import com.komida.co.id.mdisujikelayakan.utils.CustomDialogIntent
import java.text.SimpleDateFormat
import java.util.*
import android.view.ViewGroup
@Suppress("DEPRECATION")
class FormTambahIndexRumah : AppCompatActivity() {
    private lateinit var spinnerIndexDinding: Spinner
    private lateinit var spinnerIndexAtap: Spinner
    private lateinit var spinnerIndexLantai: Spinner
    private lateinit var rGstatuKepemilikan: RadioGroup
    private lateinit var rbSendiri: RadioButton
    private lateinit var rbSewa: RadioButton
    private lateinit var rbLainnya: RadioButton
    private lateinit var etKodeUkAgt: TextView
    private lateinit var etKodeUkIdxRumah: TextView
    private lateinit var btn_simpandataindexrumah: Button
    private lateinit var database_mdismo: SQLiteDatabase
    private lateinit var preferences: SharedPreferences
    private val DB_NAME = "mdismo.db"

    companion object {
        lateinit var nama_st: String
        lateinit var nik_st: String
        lateinit var cab_st: String
        lateinit var jab_st: String
        lateinit var dev_st: String
        lateinit var hp_st: String
        lateinit var kode_cab_st: String
        var kode_uk_dtl: String? = null
        var nama_anggota_dtl: String? = null
        var kelompok_dtl: String? = null
        var nik_ktp_dtl: String? = null
        var tipe_dtl: String? = null
        var center_dtl: String? = null
        var tgl_bergabung_dtl : String? = null
        var handphone_dtl:  String? = null
        var tempat_lahir_dtl:  String? = null
        var tgl_lahir_dtl:  String? = null
        var status_kawin_dtl : String? = null
        var nama_suami_dtl:  String? = null
        var nama_ibu_kandung_dtl : String? = null
        var kode_uk_rumah: String? = null
        var index_dinding: String? = null
        var kondisi_dinding: String? = null
        var poin_dinding: String? = null
        var index_atap: String? = null
        var kondisi_atap: String? = null
        var poin_atap: String? = null
        var index_lantai: String? = null
        var kondisi_lantai: String? = null
        var poin_lantai: String? = null
        var skor_index: String? = null
        var status_milik: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_tambah_index_rumah)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val toolbar: Toolbar = findViewById(R.id.toolbartambahanggotaindexrumah)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val detilIntent = this.intent

        getPreferenceIndex(detilIntent)
        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        getPreferenceStaff()
        setupView()
        prepareSetData()

        btn_simpandataindexrumah.setOnClickListener {
            simpanData()
        }
    }

    private fun prepareSetData() {
        supportActionBar?.title = "Tambah Data Index Rumah"
        etKodeUkAgt.text = kode_uk_dtl
        etKodeUkIdxRumah.text = kode_uk_rumah
        if (!kode_uk_rumah.isNullOrEmpty()) {
            supportActionBar?.title = "Ubah Data Index Rumah"
            val setIndexDinding = "$poin_dinding^$kondisi_dinding"
            val setIndexAtap = "$poin_atap^$kondisi_atap"
            val setIndexLantai = "$poin_lantai^$kondisi_lantai"
            setSpinnerValue(spinnerIndexDinding, R.array.index_rumah_dinding, setIndexDinding)
            setSpinnerValue(spinnerIndexAtap, R.array.index_rumah_atap, setIndexAtap)
            setSpinnerValue(spinnerIndexLantai, R.array.index_rumah_lantai, setIndexLantai)
            val radioGroup: RadioGroup = findViewById(R.id.rgStatusKepemilikan)
            val radioButtonA: RadioButton = findViewById(R.id.rb_sendiri)
            val radioButtonB: RadioButton = findViewById(R.id.rb_sewa)
            val radioButtonC: RadioButton = findViewById(R.id.rb_ortu_lain)


            if (status_milik == "Sendiri") {
                radioButtonA.isChecked = true
            } else if (status_milik == "Sewa") {
                radioButtonB.isChecked = true
            } else {
                radioButtonC.isChecked = true
            }
        }
    }

    private fun setSpinnerValue(spinner: Spinner, arrayResId: Int, selectedValue: String) {
        val stringArray = resources.getStringArray(arrayResId)
        val position = stringArray.indexOf(selectedValue)
        if (position != -1) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stringArray)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(position)
        } else {
            // Handle the case where the value is not found in the array
            // You can show a default selection or an error message.
        }
    }

    private fun setupView() {
        spinnerIndexAtap = findViewById(R.id.spinnerIndexAtap)
        spinnerIndexDinding = findViewById(R.id.spinnerIndexDinding)
        spinnerIndexLantai = findViewById(R.id.spinnerIndexLantai)
        rGstatuKepemilikan = findViewById(R.id.rgStatusKepemilikan)
        rbSendiri = findViewById(R.id.rb_sendiri)
        rbSewa = findViewById(R.id.rb_sewa)
        rbLainnya = findViewById(R.id.rb_ortu_lain)
        btn_simpandataindexrumah = findViewById(R.id.btn_simpandataindexrumah)
        etKodeUkAgt = findViewById(R.id.etKodeUkAgt)
        etKodeUkIdxRumah= findViewById(R.id.etKodeUkIdxRumah)
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

    private fun getPreferenceIndex(detilIntent: Intent?) {
        kode_uk_dtl = detilIntent?.getStringExtra("kode_uk_dtl")
        kode_uk_rumah = detilIntent?.getStringExtra("kode_uk_rumah")
        index_dinding = detilIntent?.getStringExtra("index_dinding")
        kondisi_dinding = detilIntent?.getStringExtra("kondisi_dinding")
        poin_dinding = detilIntent?.getStringExtra("poin_dinding")
        index_atap = detilIntent?.getStringExtra("index_atap")
        kondisi_atap = detilIntent?.getStringExtra("kondisi_atap")
        poin_atap = detilIntent?.getStringExtra("poin_atap")
        index_lantai = detilIntent?.getStringExtra("index_lantai")
        kondisi_lantai = detilIntent?.getStringExtra("kondisi_lantai")
        poin_lantai = detilIntent?.getStringExtra("poin_lantai")
        skor_index = detilIntent?.getStringExtra("skor_index")
        status_milik = detilIntent?.getStringExtra("status_milik")
        kode_uk_dtl = detilIntent?.getStringExtra("kode_uk_dtl")
        nama_anggota_dtl = detilIntent?.getStringExtra("nama_anggota_dtl")
        nik_ktp_dtl = detilIntent?.getStringExtra("nik_ktp_dtl")
        tipe_dtl = detilIntent?.getStringExtra("tipe_dtl")
        center_dtl = detilIntent?.getStringExtra("center_dtl")
        kelompok_dtl = detilIntent?.getStringExtra("kelompok_dtl")
        tgl_bergabung_dtl = detilIntent?.getStringExtra("tgl_bergabung_dtl") ?: ""
        handphone_dtl = detilIntent?.getStringExtra("handphone_dtl") ?: ""
        tempat_lahir_dtl = detilIntent?.getStringExtra("tempat_lahir_dtl") ?: ""
        tgl_lahir_dtl = detilIntent?.getStringExtra("tgl_lahir_dtl") ?: ""
        status_kawin_dtl = detilIntent?.getStringExtra("status_kawin_dtl") ?: ""
        nama_suami_dtl = detilIntent?.getStringExtra("nama_suami_dtl") ?: ""
        nama_ibu_kandung_dtl = detilIntent?.getStringExtra("nama_ibu_kandung_dtl") ?: ""

    }

    private fun simpanData() {
        val kode_uk_agt = kode_uk_dtl
        val strIndexDinding = spinnerIndexDinding.selectedItem.toString()
        val strIndexAtap = spinnerIndexAtap.selectedItem.toString()
        val strIndexLantai = spinnerIndexLantai.selectedItem.toString()
        var strKepemilikan = ""
        for (rb in rGstatuKepemilikan.children) {
            if (rb is RadioButton && rb.isChecked) {
                strKepemilikan = rb.text.toString()
                break
            }
        }
        var index_dinding = "dinding"
        var poinDinding = "0"
        var kondisiDinding = ""
        val indexDinding = strIndexDinding.split("\\^".toRegex())
        if (indexDinding.size == 2) {
            poinDinding = indexDinding[0].trim()
            kondisiDinding = indexDinding[1].trim()
        } else {
            println("Invalid input format")
        }
        var index_atap = "atap"
        var poinAtap = "0"
        var kondisiAtap = ""
        val indexAtap = strIndexAtap.split("\\^".toRegex())
        if (indexAtap.size == 2) {
            poinAtap = indexAtap[0].trim()
            kondisiAtap = indexAtap[1].trim()
        } else {
            println("Invalid input format")
        }
        var index_lantai = "lantai"
        var poinLantai = "0"
        var kondisiLantai = ""
        val indexLantai = strIndexLantai.split("\\^".toRegex())
        if (indexLantai.size == 2) {
            poinLantai = indexLantai[0].trim()
            kondisiLantai = indexLantai[1].trim()
        } else {
            println("Invalid input format")
        }
        val j_poinDinding = poinDinding.toInt()
        val j_poinAtap = poinAtap.toInt()
        val j_poinLantai = poinLantai.toInt()
        val skor = j_poinDinding + j_poinAtap + j_poinLantai

        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val execIndexRumah: String
        val kode_uk_idx: String
        if (kode_uk_rumah.isNullOrEmpty()) {
             kode_uk_idx = generateRandom("ukidx")
             execIndexRumah = "INSERT INTO uk_dtl_index_rumah (" +
                    "kode_uk_rumah, kode_uk, index_dinding, kondisi_dinding, poin_dinding, index_atap, kondisi_atap, poin_atap, index_lantai, kondisi_lantai, poin_lantai, skor_index, status_milik, usr_crt, dt_crt, usr_upd, dt_upd)" +
                    "values ('$kode_uk_idx', '$kode_uk_agt', '$index_dinding', '$kondisiDinding', $j_poinDinding, '$index_atap', '$kondisiAtap', $j_poinAtap, '$index_lantai', '$kondisiLantai', $poinLantai, $skor, '$strKepemilikan'," +
                    "'${nik_st}', CURRENT_TIMESTAMP, '${nik_st}', CURRENT_TIMESTAMP);"

        }
        else
        {
            kode_uk_idx = kode_uk_rumah ?: ""
            execIndexRumah = "UPDATE uk_dtl_index_rumah " +
                    "SET " +
                    "index_dinding = '$index_dinding', " +
                    "kondisi_dinding = '$kondisiDinding', " +
                    "poin_dinding = $j_poinDinding, " +
                    "index_atap = '$index_atap', " +
                    "kondisi_atap = '$kondisiAtap', " +
                    "poin_atap = $j_poinAtap, " +
                    "index_lantai = '$index_lantai', " +
                    "kondisi_lantai = '$kondisiLantai', " +
                    "poin_lantai = $poinLantai, " +
                    "skor_index = $skor, " +
                    "status_milik = '$strKepemilikan', " +
                    "usr_upd = '${nik_st}', " +
                    "dt_upd = CURRENT_TIMESTAMP " +
                    "WHERE kode_uk_rumah = '$kode_uk_idx' AND kode_uk = '$kode_uk_agt';"

        }
        //println(execIndexRumah)
        database_mdismo.execSQL(execIndexRumah)
        database_mdismo.close()
        if (!isFinishing) {
            val intent_list_idx = Intent(this, ListAnggotaIndexRumah::class.java)
            intent_list_idx.putExtra("kode_uk_dtl", kode_uk_dtl)
            intent_list_idx.putExtra("nik_ktp_dtl", nik_ktp_dtl)
            intent_list_idx.putExtra("nama_anggota_dtl", nama_anggota_dtl)
            intent_list_idx.putExtra("tipe_dtl", tipe_dtl)
            intent_list_idx.putExtra("center_dtl", center_dtl)
            intent_list_idx.putExtra("kelompok_dtl", kelompok_dtl)
            intent_list_idx.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
            intent_list_idx.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
            intent_list_idx.putExtra("handphone_dtl", handphone_dtl)
            intent_list_idx.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
            intent_list_idx.putExtra("status_kawin_dtl", status_kawin_dtl)
            intent_list_idx.putExtra("nama_suami_dtl", nama_suami_dtl)
            intent_list_idx.putExtra("nama_ibu_kandung_dtl",nama_ibu_kandung_dtl)

            val dialog = CustomDialogIntent(
                this,
                "Konfirmasi",
                "Data Berhasil Di Simpan.",
                intent_list_idx
            )

            if (!isFinishing) {
                dialog.show()

            }
        }
    }

    fun generateRandom(prefix: String): String {
        val length = 14
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val randomString = RandomStringGenerator().generateRandomString(length)
        return "$prefix-$currentDate-$randomString"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val  intent_main_activity = Intent(this, ListAnggotaIndexRumah::class.java)
        intent_main_activity.putExtra("kode_uk_dtl", kode_uk_dtl)
        intent_main_activity.putExtra("nik_ktp_dtl", nik_ktp_dtl)
        intent_main_activity.putExtra("nama_anggota_dtl", nama_anggota_dtl)
        intent_main_activity.putExtra("tipe_dtl", tipe_dtl)
        intent_main_activity.putExtra("center_dtl", center_dtl)
        intent_main_activity.putExtra("kelompok_dtl", kelompok_dtl)
        intent_main_activity.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
        intent_main_activity.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
        intent_main_activity.putExtra("handphone_dtl", handphone_dtl)
        intent_main_activity.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
        intent_main_activity.putExtra("status_kawin_dtl", status_kawin_dtl)
        intent_main_activity.putExtra("nama_suami_dtl", nama_suami_dtl)
        intent_main_activity.putExtra("nama_ibu_kandung_dtl",nama_ibu_kandung_dtl)
        startActivity(intent_main_activity)
        finish()

    }
}
