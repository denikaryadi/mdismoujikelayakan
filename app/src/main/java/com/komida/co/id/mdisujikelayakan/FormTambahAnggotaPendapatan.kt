package com.komida.co.id.mdisujikelayakan

import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputLayout
import com.komida.co.id.mdisujikelayakan.utils.CustomDialogIntent
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class FormTambahAnggotaPendapatan : AppCompatActivity() {

    private lateinit var tv_kode_uk_agt: TextView
    private lateinit var bt_toggle_detil: Button
    private lateinit var etKodeUkAgt:TextView
    private lateinit var etKodeUkPendapatan:TextView

    private lateinit var tilPendapatanSuamiTetap: TextInputLayout
    private lateinit var etPendapatanSuamiTetap: EditText
    private lateinit var tilPendapatanSuamiTidakTetap:TextInputLayout
    private lateinit var etPendapatanSuamiTidakTetap:EditText
    private lateinit var tilTotalPendapatanSuamiPerBulan:TextInputLayout
    private lateinit var etTotalPendapatanSuamiPerBulan:EditText
    private lateinit var tilPendapatanIstriTetap:TextInputLayout
    private lateinit var etPendapatanIstriTetap:EditText
    private lateinit var tilPendapatanIstriTidakTetap:TextInputLayout
    private lateinit var etPendapatanIstriTidakTetap:EditText
    private lateinit var tilTotalPendapatanIstriPerBulan:TextInputLayout
    private lateinit var etTotalPendapatanIstriPerBulan:EditText
    private lateinit var tilPendapatanLainnyaTetap:TextInputLayout
    private lateinit var etPendapatanLainnyaTetap:EditText
    private lateinit var tilPendapatanLainnyaTidakTetap:TextInputLayout
    private lateinit var etPendapatanLainnyaTidakTetap:EditText
    private lateinit var tilTotalPendapatanLainnyaPerBulan:TextInputLayout
    private lateinit var etTotalPendapataLainnyaPerBulan:EditText
    private lateinit var tilPengeluaranRT:TextInputLayout
    private lateinit var etPengeluaranRT:EditText
    private lateinit var tilPengeluaranLainnya:TextInputLayout
    private lateinit var etPengeluaranLainnya:EditText
    private lateinit var tilTotalPengeluaranPerBulan:TextInputLayout
    private lateinit var etTotalPengeluaranPerBulan:EditText
    private lateinit var tilTotalPendapatanBersih:TextInputLayout
    private lateinit var etTotalPendapatanBersih:EditText
    private lateinit var tilTotalPendapatanSemuaPerBulan:TextInputLayout
    private lateinit var etTotalPendapatanSemuaPerBulan:EditText



    private lateinit var btn_simpananggotapendapatan:Button
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
        var kode_uk_pendapatan:String? =null
        var kode_uk:String? =null
        var suami_tetap:String? =null
        var suami_tidak_tetap:String? =null
        var suami_per_bulan:String? =null
        var istri_tetap:String? =null
        var istri_tidak_tetap:String? =null
        var istri_per_bulan:String? =null
        var pendapatan_lainnya_tetap:String? =null
        var pendapatan_lainnya_tdk_tetap:String? =null
        var pendapatan_lainnya_per_bulan:String? =null
        var total_pendapatan:String? =null
        var total_pendapatan_per_bulan:String? =null
        var total_pendapatan_bersih_per_bulan:String? =null
        var total_pengeluaran_rt:String? =null
        var total_pengeluaran_lain:String? =null
        var total_pengeluaran_per_bulan:String? =null
        var pendapatanSuamiTetap:Int? = 0
        var pendapatanSuamiTidakTetap:Int? = 0
        var pendapatanIstriTetap:Int? = 0
        var pendapatanIstriTidakTetap:Int? = 0
        var pendapatanLainnyaTetap:Int? = 0
        var pendapatanLainnyaTidakTetap:Int? = 0
        var pengeluaranRT:Int? = 0
        var pengeluaranLainnya:Int? = 0
        var totalsuamiperbulan:Int? = 0
        var totalistriperbulan:Int? = 0
        var totallainnyaperbulan:Int? = 0
        var totalsemuaperbulan:Int? = 0
        var totalkeluarperbulan:Int? = 0
        var totalsemuapendapatanbersih:Int? = 0



    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_tambah_anggota_pendapatan)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        val toolbar: Toolbar = findViewById(R.id.toolbartambahanggotapendapatan)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val detilIntent = this.intent

        getPreferencePendapatan(detilIntent)

        getPreferenceStaff()
        setupView()
        prepareSetData()

        btn_simpananggotapendapatan.setOnClickListener {
            simpanData()
        }
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

    private fun getPreferencePendapatan(detilIntent: Intent?) {
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


        kode_uk_pendapatan = detilIntent?.getStringExtra("kode_uk_pendapatan");
        suami_tetap = detilIntent?.getStringExtra("suami_tetap");
        suami_tidak_tetap = detilIntent?.getStringExtra("suami_tidak_tetap");
        suami_per_bulan = detilIntent?.getStringExtra("suami_per_bulan");
        istri_tetap = detilIntent?.getStringExtra("istri_tetap");
        istri_tidak_tetap = detilIntent?.getStringExtra("istri_tidak_tetap");
        istri_per_bulan = detilIntent?.getStringExtra("istri_per_bulan");
        pendapatan_lainnya_tetap = detilIntent?.getStringExtra("pendapatan_lainnya_tetap");
        pendapatan_lainnya_tdk_tetap = detilIntent?.getStringExtra("pendapatan_lainnya_tdk_tetap");
        pendapatan_lainnya_per_bulan = detilIntent?.getStringExtra("pendapatan_lainnya_per_bulan");
        total_pendapatan_per_bulan = detilIntent?.getStringExtra("total_pendapatan_per_bulan");
        total_pendapatan_bersih_per_bulan = detilIntent?.getStringExtra("total_pendapatan_bersih_per_bulan");
        total_pengeluaran_rt = detilIntent?.getStringExtra("total_pengeluaran_rt");
        total_pengeluaran_lain = detilIntent?.getStringExtra("total_pengeluaran_lain");
        total_pengeluaran_per_bulan = detilIntent?.getStringExtra("total_pengeluaran_per_bulan");
    }
    private fun setupView() {
        tilPendapatanSuamiTetap = findViewById(R.id.tilPendapatanSuamiTetap)
        etPendapatanSuamiTetap = findViewById(R.id.etPendapatanSuamiTetap)
        tilPendapatanSuamiTidakTetap = findViewById(R.id.tilPendapatanSuamiTidakTetap)
        etPendapatanSuamiTidakTetap = findViewById(R.id.etPendapatanSuamiTidakTetap)
        tilTotalPendapatanSuamiPerBulan = findViewById(R.id.tilTotalPendapatanSuamiPerBulan)
        etTotalPendapatanSuamiPerBulan = findViewById(R.id.etTotalPendapatanSuamiPerBulan)
        tilPendapatanIstriTetap = findViewById(R.id.tilPendapatanIstriTetap)
        etPendapatanIstriTetap = findViewById(R.id.etPendapatanIstriTetap)
        tilPendapatanIstriTidakTetap = findViewById(R.id.tilPendapatanIstriTidakTetap)
        etPendapatanIstriTidakTetap = findViewById(R.id.etPendapatanIstriTidakTetap)
        tilTotalPendapatanIstriPerBulan = findViewById(R.id.tilTotalPendapatanIstriPerBulan)
        etTotalPendapatanIstriPerBulan = findViewById(R.id.etTotalPendapatanIstriPerBulan)
        tilPendapatanLainnyaTetap = findViewById(R.id.tilPendapatanLainnyaTetap)
        etPendapatanLainnyaTetap = findViewById(R.id.etPendapatanLainnyaTetap)
        tilPendapatanLainnyaTidakTetap = findViewById(R.id.tilPendapatanLainnyaTidakTetap)
        etPendapatanLainnyaTidakTetap = findViewById(R.id.etPendapatanLainnyaTidakTetap)
        tilTotalPendapatanLainnyaPerBulan = findViewById(R.id.tilTotalPendapatanLainnyaPerBulan)
        etTotalPendapataLainnyaPerBulan = findViewById(R.id.etTotalPendapataLainnyaPerBulan)
        tilPengeluaranRT = findViewById(R.id.tilPengeluaranRT)
        etPengeluaranRT = findViewById(R.id.etPengeluaranRT)
        tilPengeluaranLainnya = findViewById(R.id.tilPengeluaranLainnya)
        etPengeluaranLainnya = findViewById(R.id.etPengeluaranLainnya)
        tilTotalPengeluaranPerBulan = findViewById(R.id.tilTotalPengeluaranPerBulan)
        etTotalPengeluaranPerBulan = findViewById(R.id.etTotalPengeluaranPerBulan)
        tilTotalPendapatanBersih = findViewById(R.id.tilTotalPendapatanBersih)
        etTotalPendapatanBersih = findViewById(R.id.etTotalPendapatanBersih)
        btn_simpananggotapendapatan = findViewById(R.id.btn_simpananggotapendapatan)
        etKodeUkAgt = findViewById(R.id.etKodeUkAgt)
        etKodeUkPendapatan= findViewById(R.id.etKodeUkPendapatan)
        etTotalPendapatanSemuaPerBulan = findViewById(R.id.etTotalPendapatanSemuaPerBulan)

        tilTotalPendapatanSemuaPerBulan= findViewById(R.id.tilTotalPendapatanSemuaPerBulan)
        tilTotalPengeluaranPerBulan= findViewById(R.id.tilTotalPengeluaranPerBulan)

        etPendapatanSuamiTetap.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                etPendapatanSuamiTetap.removeTextChangedListener(this)

                try {
                    var originalString: String = etPendapatanSuamiTetap.text.toString()
                    val longval: Long
                    if(originalString.isNullOrEmpty())
                    {
                        originalString ="0"
                    }
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",", "")
                    }

                    longval = originalString.toLong()

                    val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString: String = formatter.format(longval)

                    // Set the formatted text to the EditText
                    etPendapatanSuamiTetap.setText(formattedString)
                    etPendapatanSuamiTetap.setSelection(etPendapatanSuamiTetap.text.length)
                    setPemasukanSuami("suamitetap", originalString)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                etPendapatanSuamiTetap.addTextChangedListener(this)

            }
        })
        etPendapatanSuamiTidakTetap.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                etPendapatanSuamiTidakTetap.removeTextChangedListener(this)

                try {
                    var originalString: String = etPendapatanSuamiTidakTetap.text.toString()
                    val longval: Long
                    if(originalString.isNullOrEmpty())
                    {
                        originalString ="0"
                    }
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",", "")
                    }

                    longval = originalString.toLong()

                    val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString: String = formatter.format(longval)

                    // Set the formatted text to the EditText
                    etPendapatanSuamiTidakTetap.setText(formattedString)
                    etPendapatanSuamiTidakTetap.setSelection(etPendapatanSuamiTidakTetap.text.length)
                    setPemasukanSuami("suamitidaktetap", originalString)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                etPendapatanSuamiTidakTetap.addTextChangedListener(this)

            }
        })

        etPendapatanIstriTetap.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                etPendapatanIstriTetap.removeTextChangedListener(this)

                try {
                    var originalString: String = etPendapatanIstriTetap.text.toString()
                    val longval: Long
                    if(originalString.isNullOrEmpty())
                    {
                        originalString ="0"
                    }
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",", "")
                    }

                    longval = originalString.toLong()

                    val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString: String = formatter.format(longval)

                    // Set the formatted text to the EditText
                    etPendapatanIstriTetap.setText(formattedString)
                    etPendapatanIstriTetap.setSelection(etPendapatanIstriTetap.text.length)
                    setPemasukanIstri("istritetap", originalString)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                etPendapatanIstriTetap.addTextChangedListener(this)

            }
        })

        etPendapatanIstriTidakTetap.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                etPendapatanIstriTidakTetap.removeTextChangedListener(this)

                try {
                    var originalString: String = etPendapatanIstriTidakTetap.text.toString()
                    val longval: Long
                    if(originalString.isNullOrEmpty())
                    {
                        originalString ="0"
                    }
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",", "")
                    }

                    longval = originalString.toLong()

                    val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString: String = formatter.format(longval)

                    // Set the formatted text to the EditText
                    etPendapatanIstriTidakTetap.setText(formattedString)
                    etPendapatanIstriTidakTetap.setSelection(etPendapatanIstriTidakTetap.text.length)
                    setPemasukanIstri("istritidaktetap", originalString)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                etPendapatanIstriTidakTetap.addTextChangedListener(this)

            }
        })
        etPendapatanLainnyaTetap.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                etPendapatanLainnyaTetap.removeTextChangedListener(this)

                try {
                    var originalString: String = etPendapatanLainnyaTetap.text.toString()
                    val longval: Long
                    if(originalString.isNullOrEmpty())
                    {
                        originalString ="0"
                    }
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",", "")
                    }

                    longval = originalString.toLong()

                    val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString: String = formatter.format(longval)

                    // Set the formatted text to the EditText
                    etPendapatanLainnyaTetap.setText(formattedString)
                    etPendapatanLainnyaTetap.setSelection(etPendapatanLainnyaTetap.text.length)
                    setPemasukanLainnya("lainnyatetap", originalString)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                etPendapatanLainnyaTetap.addTextChangedListener(this)

            }
        })

        etPendapatanLainnyaTidakTetap.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                etPendapatanLainnyaTidakTetap.removeTextChangedListener(this)

                try {
                    var originalString: String = etPendapatanLainnyaTidakTetap.text.toString()
                    val longval: Long
                    if(originalString.isNullOrEmpty())
                    {
                        originalString ="0"
                    }
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",", "")
                    }

                    longval = originalString.toLong()

                    val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString: String = formatter.format(longval)

                    // Set the formatted text to the EditText
                    etPendapatanLainnyaTidakTetap.setText(formattedString)
                    etPendapatanLainnyaTidakTetap.setSelection(etPendapatanLainnyaTidakTetap.text.length)
                    setPemasukanLainnya("lainnyatidaktetap", originalString)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                etPendapatanLainnyaTidakTetap.addTextChangedListener(this)

            }
        })


        etPengeluaranRT.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                etPengeluaranRT.removeTextChangedListener(this)

                try {
                    var originalString: String = etPengeluaranRT.text.toString()
                    val longval: Long
                    if(originalString.isNullOrEmpty())
                    {
                        originalString ="0"
                    }
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",", "")
                    }

                    longval = originalString.toLong()

                    val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString: String = formatter.format(longval)

                    // Set the formatted text to the EditText
                    etPengeluaranRT.setText(formattedString)
                    etPengeluaranRT.setSelection(etPengeluaranRT.text.length)
                    setPengeluaran("keluarrt", originalString)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                etPengeluaranRT.addTextChangedListener(this)

            }
        })

        etPengeluaranLainnya.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                etPengeluaranLainnya.removeTextChangedListener(this)

                try {
                    var originalString: String = etPengeluaranLainnya.text.toString()
                    val longval: Long
                    if(originalString.isNullOrEmpty())
                    {
                        originalString ="0"
                    }
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",", "")
                    }

                    longval = originalString.toLong()

                    val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString: String = formatter.format(longval)

                    // Set the formatted text to the EditText
                    etPengeluaranLainnya.setText(formattedString)
                    etPengeluaranLainnya.setSelection(etPengeluaranLainnya.text.length)
                    setPengeluaran("keluarlainnya", originalString)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                etPengeluaranLainnya.addTextChangedListener(this)

            }
        })


    }
    private fun simpanData() {
        val kode_uk_agt = kode_uk_dtl
        var strPendapatanSuamiTetap = etPendapatanSuamiTetap.text.toString()
        var strPendapatanSuamiTidakTetap = etPendapatanSuamiTidakTetap.text.toString()
        var strPendapatanSuamiPerBulan = etTotalPendapatanSuamiPerBulan.text.toString()
        var strPendapatanIstriTetap = etPendapatanIstriTetap.text.toString()
        var strPendapatanIstriTidakTetap = etPendapatanIstriTidakTetap.text.toString()
        var strPendapatanIstriPerBulan = etTotalPendapatanIstriPerBulan.text.toString()
        var strPendapatanLainnyaTetap = etPendapatanLainnyaTetap.text.toString()
        var strPendapatanLainnyaTidakTetap = etPendapatanLainnyaTidakTetap.text.toString()
        var strPendapatanLainnyaPerBulan = etTotalPendapataLainnyaPerBulan.text.toString()
        var strPendapatanSemuaPerBulan = etTotalPendapatanSemuaPerBulan.text.toString()
        var strPengeluaranRT = etPengeluaranRT.text.toString()
        var strPengeluaranLainnya = etPengeluaranLainnya.text.toString()
        var strPengeluaranPerbulan = etTotalPengeluaranPerBulan.text.toString()
        var strPendapatanBersih = etTotalPendapatanBersih.text.toString()

        strPendapatanSuamiTetap = strPendapatanSuamiTetap.replace(",","")
        strPendapatanSuamiTidakTetap = strPendapatanSuamiTidakTetap.replace(",","")
        strPendapatanSuamiPerBulan = strPendapatanSuamiPerBulan.replace(",","")
        strPendapatanIstriTetap = strPendapatanIstriTetap.replace(",","")
        strPendapatanIstriTidakTetap = strPendapatanIstriTidakTetap.replace(",","")
        strPendapatanIstriPerBulan = strPendapatanIstriPerBulan.replace(",","")
        strPendapatanLainnyaTetap = strPendapatanLainnyaTetap.replace(",","")
        strPendapatanLainnyaTidakTetap = strPendapatanLainnyaTidakTetap.replace(",","")
        strPendapatanLainnyaPerBulan = strPendapatanLainnyaPerBulan.replace(",","")
        strPendapatanSemuaPerBulan = strPendapatanSemuaPerBulan.replace(",","")
        strPengeluaranRT = strPengeluaranRT.replace(",","")
        strPengeluaranLainnya = strPengeluaranLainnya.replace(",","")
        strPengeluaranPerbulan = strPengeluaranPerbulan.replace(",","")
        strPendapatanBersih = strPendapatanBersih.replace(",","")


        if(strPendapatanSuamiTetap.isEmpty())
        {
            tilPendapatanSuamiTetap.error="Pendapatan Suami Tetap Harus di Isi minimal 0"
            etPendapatanSuamiTetap.requestFocus()
            return
        }
        if(strPendapatanSuamiTidakTetap.isEmpty())
        {
            tilPendapatanSuamiTidakTetap.error="Pendapatan Tidak Tetap Harus di Isi minimal 0"
            etPendapatanSuamiTidakTetap.requestFocus()
            return
        }
        if(strPendapatanSuamiPerBulan.isEmpty())
        {
            tilTotalPendapatanSuamiPerBulan.error="Pendapatan Suami Per Bulan Harus di Isi minimal 0"
            etTotalPendapatanSuamiPerBulan.requestFocus()
            return
        }
        if(strPendapatanIstriTetap.isEmpty())
        {
            tilPendapatanIstriTetap.error="Pendapatan Istri Tetap Harus di Isi minimal 0"
            etPendapatanIstriTetap.requestFocus()
            return
        }
        if(strPendapatanIstriTidakTetap.isEmpty())
        {
            tilPendapatanIstriTidakTetap.error="Pendapatan Tidak Tetap Harus di Isi minimal 0"
            etPendapatanIstriTidakTetap.requestFocus()
            return
        }
        if(strPendapatanIstriPerBulan.isEmpty())
        {
            tilTotalPendapatanIstriPerBulan.error="Pendapatan Istri Per Bulan Harus di Isi minimal 0"
            etTotalPendapatanIstriPerBulan.requestFocus()
            return
        }
        if(strPendapatanLainnyaTetap.isEmpty())
        {
            tilPendapatanLainnyaTetap.error="Pendapatan Lainnya Tetap Harus di Isi minimal 0"
            etPendapatanLainnyaTetap.requestFocus()
            return
        }
        if(strPendapatanLainnyaTidakTetap.isEmpty())
        {
            tilPendapatanLainnyaTidakTetap.error="Pendapatan Lainnya Tidak Tetap Harus di Isi minimal 0"
            etPendapatanLainnyaTidakTetap.requestFocus()
            return
        }
        if(strPendapatanLainnyaPerBulan.isEmpty())
        {
            tilTotalPendapatanLainnyaPerBulan.error="Pendapatan Lainnya Per  Bulan Harus di Isi minimal 0"
            etTotalPendapataLainnyaPerBulan.requestFocus()
            return
        }
        if(strPendapatanSemuaPerBulan.isEmpty())
        {
            tilTotalPendapatanSemuaPerBulan.error="Pendapatan Semua Per Bulan Harus di Isi minimal 0"
            etTotalPendapatanSemuaPerBulan.requestFocus()
            return
        }
        if(strPengeluaranRT.isEmpty())
        {
            tilPengeluaranRT.error="Pengeluaran RT Bulan Harus di Isi minimal 0"
            etPengeluaranRT.requestFocus()
            return
        }
        if(strPengeluaranLainnya.isEmpty())
        {
            tilPengeluaranLainnya.error="Pengeluaran Lainnya Harus di Isi minimal 0"
            etPengeluaranLainnya.requestFocus()
            return
        }
        if(strPengeluaranPerbulan.isEmpty())
        {
            tilTotalPengeluaranPerBulan.error="Pengeluaran Per Bulan Harus di Isi minimal 0"
            etTotalPengeluaranPerBulan.requestFocus()
            return
        }
        if(strPendapatanBersih.isEmpty())
        {
            tilTotalPendapatanBersih.error="Pendapatan Bersih Harus di Isi minimal 0"
            etTotalPendapatanBersih.requestFocus()
            return
        }
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val execPendapatan: String // Declare execPendapatan as a String variable
        if(kode_uk_pendapatan.isNullOrEmpty()) {
            kode_uk_pendapatan = generateRandom("ukdpt")

            execPendapatan = "INSERT INTO uk_pendapatan (" +
                    "kode_uk_pendapatan,kode_uk,suami_tetap,suami_tidak_tetap,suami_per_bulan," +
                    "istri_tetap,istri_tidak_tetap,istri_per_bulan," +
                    "pendapatan_lainnya_tetap,pendapatan_lainnya_tdk_tetap,pendapatan_lainnya_per_bulan," +
                    "total_pendapatan_per_bulan,total_pendapatan_bersih_per_bulan," +
                    "total_pengeluaran_rt,total_pengeluaran_lain,total_pengeluaran_per_bulan,usr_crt,dt_crt,usr_upd,dt_upd)" +
                    "values ('$kode_uk_pendapatan', '$kode_uk_agt', " +
                    "'$strPendapatanSuamiTetap', '$strPendapatanSuamiTidakTetap', '$strPendapatanSuamiPerBulan', " +
                    "'$strPendapatanIstriTetap', '$strPendapatanIstriTidakTetap', '$strPendapatanIstriPerBulan', " +
                    "'$strPendapatanLainnyaTetap', '$strPendapatanLainnyaTidakTetap', '$strPendapatanLainnyaPerBulan'," +
                    "'$strPendapatanSemuaPerBulan','$strPendapatanBersih'," +
                    "'$strPengeluaranRT','$strPengeluaranLainnya','$strPengeluaranPerbulan'," +
                    "'${nik_st}', CURRENT_TIMESTAMP, '${nik_st}', CURRENT_TIMESTAMP);"
        }
        else
        {
            execPendapatan= "UPDATE uk_pendapatan " +
                    "SET " +
                    "suami_tetap = '$strPendapatanSuamiTetap', " +
                    "suami_tidak_tetap = '$strPendapatanSuamiTidakTetap', " +
                    "suami_per_bulan = '$strPendapatanSuamiPerBulan', " +
                    "istri_tetap = '$strPendapatanIstriTetap', " +
                    "istri_tidak_tetap = '$strPendapatanIstriTidakTetap', " +
                    "istri_per_bulan = '$strPendapatanIstriPerBulan', " +
                    "pendapatan_lainnya_tetap = '$strPendapatanLainnyaTetap', " +
                    "pendapatan_lainnya_tdk_tetap = '$strPendapatanLainnyaTidakTetap', " +
                    "pendapatan_lainnya_per_bulan = '$strPendapatanLainnyaPerBulan', " +
                    "total_pendapatan_per_bulan = '$strPendapatanSemuaPerBulan', " +
                    "total_pendapatan_bersih_per_bulan = '$strPendapatanBersih', " +
                    "total_pengeluaran_rt = '$strPengeluaranRT', " +
                    "total_pengeluaran_lain = '$strPengeluaranLainnya', " +
                    "total_pengeluaran_per_bulan = '$strPengeluaranPerbulan', " +
                    "usr_upd = '${nik_st}', dt_upd = CURRENT_TIMESTAMP " +
                    "WHERE kode_uk_pendapatan = '$kode_uk_pendapatan' and kode_uk='$kode_uk_agt'"

        }

        database_mdismo.execSQL(execPendapatan)
        database_mdismo.close()
        if (!isFinishing) {
            val intent_list_idx = Intent(this, ListAnggotaPendapatan::class.java)
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
            intent_list_idx.putExtra("kode_uk_pendapatan", kode_uk_pendapatan)

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
    fun setPemasukanSuami(jenis: String, nilaistr: String) {
        when (jenis) {
            "suamitetap" -> pendapatanSuamiTetap = nilaistr.toInt()
            "suamitidaktetap" -> pendapatanSuamiTidakTetap = nilaistr.toInt()
        }
        hitungPemasukanSuami()
        hitungPendapatanTotal()
        hitungPendapatanBersihTotal()
    }
    fun generateRandom(prefix: String): String {
        val length = 14
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val randomString = RandomStringGenerator().generateRandomString(length)
        return "$prefix-$currentDate-$randomString"
    }
    fun setPemasukanIstri(jenis: String, nilaistr: String) {
        when (jenis) {
            "istritetap" -> pendapatanIstriTetap = nilaistr.toInt()
            "istritidaktetap" -> pendapatanIstriTidakTetap = nilaistr.toInt()
        }
        hitungPemasukanIstri()
        hitungPendapatanTotal()
        hitungPendapatanBersihTotal()
    }
    fun setPemasukanLainnya(jenis: String, nilaistr: String) {
        when (jenis) {
            "lainnyatetap" -> pendapatanLainnyaTetap = nilaistr.toInt()
            "lainnyatidaktetap" -> pendapatanLainnyaTidakTetap = nilaistr.toInt()
        }
        hitungPemasukanLainnya()
        hitungPendapatanTotal()
        hitungPendapatanBersihTotal()
    }
    fun setPengeluaran(jenis: String, nilaistr: String) {
        when (jenis) {
            "keluarrt" -> pengeluaranRT = nilaistr.toInt()
            "keluarlainnya" -> pengeluaranLainnya = nilaistr.toInt()
        }
        hitungPengeluaranLainnya()
        hitungPendapatanBersihTotal()
    }
    fun hitungPendapatanTotal() {
        val totalSuamiPerBulan = totalsuamiperbulan?.toInt() ?: 0 // Use the safe call operator to handle null
        val totalIstriPerBulan = totalistriperbulan?.toInt() ?: 0 // Use the safe call operator to handle null
        val totalLainnyaPerbulan = totallainnyaperbulan?.toInt() ?: 0 // Use the safe call operator to handle null


        totalsemuaperbulan = totalSuamiPerBulan + totalIstriPerBulan+totalLainnyaPerbulan
        etTotalPendapatanSemuaPerBulan.text = Editable.Factory.getInstance().newEditable(getDecimalFormattedString(totalsemuaperbulan.toString()))
    }

    fun hitungPendapatanBersihTotal() {
        val totalSemuaPerBulan = totalsemuaperbulan?.toInt() ?: 0 // Use the safe call operator to handle null
        val totalSemuaKeluarPerBulan = totalkeluarperbulan?.toInt() ?: 0 // Use the safe call operator to handle null

        totalsemuapendapatanbersih = totalSemuaPerBulan - totalSemuaKeluarPerBulan
        etTotalPendapatanBersih.text = Editable.Factory.getInstance().newEditable(getDecimalFormattedString(totalsemuapendapatanbersih.toString()))
    }


    fun hitungPemasukanSuami() {
        val totalSuamiTetap = pendapatanSuamiTetap?.toInt() ?: 0 // Use the safe call operator to handle null
        val totalSuamiTidakTetap = pendapatanSuamiTidakTetap?.toInt() ?: 0 // Use the safe call operator to handle null

        totalsuamiperbulan = totalSuamiTetap + totalSuamiTidakTetap
        etTotalPendapatanSuamiPerBulan.text = Editable.Factory.getInstance().newEditable(getDecimalFormattedString(totalsuamiperbulan.toString()))
    }
    fun hitungPemasukanIstri() {
        val totalIstriTetap = pendapatanIstriTetap?.toInt() ?: 0 // Use the safe call operator to handle null
        val totalIstriTidakTetap = pendapatanIstriTidakTetap?.toInt() ?: 0 // Use the safe call operator to handle null

        totalistriperbulan = totalIstriTetap + totalIstriTidakTetap
        etTotalPendapatanIstriPerBulan.text = Editable.Factory.getInstance().newEditable(getDecimalFormattedString(totalistriperbulan.toString()))
    }
    fun hitungPemasukanLainnya() {
        val totalLainnyaTetap = pendapatanLainnyaTetap?.toInt() ?: 0 // Use the safe call operator to handle null
        val totalLainnyaTidakTetap = pendapatanLainnyaTidakTetap?.toInt() ?: 0 // Use the safe call operator to handle null

        totallainnyaperbulan = totalLainnyaTetap + totalLainnyaTidakTetap
        etTotalPendapataLainnyaPerBulan.text = Editable.Factory.getInstance().newEditable(getDecimalFormattedString(totallainnyaperbulan.toString()))
    }
    fun hitungPengeluaranLainnya() {
        val totalKeluarRT = pengeluaranRT?.toInt() ?: 0 // Use the safe call operator to handle null
        val totalKeluarLainnya = pengeluaranLainnya?.toInt() ?: 0 // Use the safe call operator to handle null

        totalkeluarperbulan = totalKeluarRT + totalKeluarLainnya
        etTotalPengeluaranPerBulan.text = Editable.Factory.getInstance().newEditable(getDecimalFormattedString(totalkeluarperbulan.toString()))
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



    private fun prepareSetData() {
        supportActionBar?.title = "Tambah Pendapatan"
        etKodeUkAgt.text = kode_uk_dtl
        if (!kode_uk_pendapatan.isNullOrEmpty()) {
            supportActionBar?.title = "Ubah Pendapatan"
            etPendapatanSuamiTetap.text = Editable.Factory.getInstance().newEditable(suami_tetap)
            etPendapatanSuamiTidakTetap.text = Editable.Factory.getInstance().newEditable(suami_tidak_tetap)

            etTotalPendapatanSuamiPerBulan.text = Editable.Factory.getInstance().newEditable(suami_per_bulan)
            etPendapatanIstriTetap.text = Editable.Factory.getInstance().newEditable(istri_tetap)
            etPendapatanIstriTidakTetap.text = Editable.Factory.getInstance().newEditable(istri_tidak_tetap)
            etTotalPendapatanIstriPerBulan.text = Editable.Factory.getInstance().newEditable(istri_per_bulan)
            etPendapatanLainnyaTetap.text = Editable.Factory.getInstance().newEditable(pendapatan_lainnya_tetap)
            etPendapatanLainnyaTidakTetap.text = Editable.Factory.getInstance().newEditable(pendapatan_lainnya_tdk_tetap)
            etTotalPendapataLainnyaPerBulan.text = Editable.Factory.getInstance().newEditable(pendapatan_lainnya_per_bulan)
            etPengeluaranRT.text = Editable.Factory.getInstance().newEditable(total_pengeluaran_rt)
            etPengeluaranLainnya.text = Editable.Factory.getInstance().newEditable(total_pengeluaran_lain)
            etTotalPengeluaranPerBulan.text = Editable.Factory.getInstance().newEditable(total_pengeluaran_per_bulan)
            etTotalPendapatanBersih.text = Editable.Factory.getInstance().newEditable(total_pendapatan_bersih_per_bulan)


        }
       // etKodeUkPendapatan.text = kode_uk_pendapatan

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val intent_list_idx = Intent(this, ListAnggotaPendapatan::class.java)
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

        intent_list_idx.putExtra("kode_uk_pendapatan", kode_uk_pendapatan)
        startActivity(intent_list_idx)
        finish()

    }

}