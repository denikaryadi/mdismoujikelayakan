package com.komida.co.id.mdisujikelayakan

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputLayout
import com.komida.co.id.mdisujikelayakan.utils.CustomDialogIntent
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class FormTambahAnggotaKeluarga : AppCompatActivity() {

    // private var kode_uk_agt: String? = null
    private val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    private val calendar = Calendar.getInstance()


    private lateinit var tvnomoragtkeluarga: TextView
    private lateinit var etKodeUkAgt: TextView

    private lateinit var etNamaLengkapKeluarga: EditText
    private lateinit var etTempatLahirKeluarga: EditText
    private lateinit var etTglLahirKeluarga: EditText
    private lateinit var spinnerHubunganKeluarga : Spinner
    private lateinit var spinnerStatusPerkawinan : Spinner
    private lateinit var spinnerPekerjaan : Spinner

    private lateinit var spinnerPendidikan : Spinner
    private lateinit var etKeteranganKeluarga : EditText
    private lateinit var btn_simpandataanggotakeluarga : Button


    private lateinit var tilNamaLengkapKeluarga: TextInputLayout
    private lateinit var tilTempatLahirKeluarga: TextInputLayout
    private lateinit var tilTglLahirKeluarga: TextInputLayout
    private lateinit var tilKeteranganKeluarga: TextInputLayout
    companion object {
        lateinit var nama_st: String
        lateinit var nik_st: String
        lateinit var cab_st: String
        lateinit var jab_st: String
        lateinit var dev_st: String
        lateinit var hp_st: String
        lateinit var kode_cab_st: String
        var cek_pnjmn_koperasi: Int? = null
        var cek_pnjmn_bank: Int? = null
        var cek_tidak_akses: Int? = null
        var cek_rekening_tabungan: Int? = null
        var cek_asuransi: Int? = null
        lateinit var kode_uk_dtl: String
        lateinit var nama_anggota_dtl: String
        lateinit var kelompok_dtl:String
        lateinit var nik_ktp_dtl: String
        lateinit var tipe_dtl: String
        lateinit var tempat_lahir_dtl: String
        lateinit var center_dtl: String
        lateinit var tgl_bergabung_dtl: String
        lateinit var handphone_dtl: String
        lateinit var tgl_lahir_dtl: String
        lateinit var status_kawin_dtl: String
        lateinit var nama_suami_dtl: String
        lateinit var nama_ibu_kandung_dtl: String
        lateinit var kode_uk_kel:String
        lateinit var nama_lengkap_kel:String
        lateinit var tempat_lahir_kel:String
        lateinit var tanggal_lahir_kel:String
        lateinit var status_kawin_kel:String
        lateinit var hubungan_keluarga_kel:String
        lateinit var pendidikan_terakhir_kel:String
        lateinit var pekerjaan_kel:String
        lateinit var keterangan_kel:String
        lateinit var umur_kel:String
    }



    private lateinit var database_mdismo: SQLiteDatabase
    private val DB_NAME = "mdismo.db"

    private lateinit var preferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_tambah_anggota_keluarga)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val toolbar: Toolbar = findViewById(R.id.toolbartambahanggotakeluarga)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detilIntent = this.intent

        getPreferenceAnggota(detilIntent)
        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        getPreferenceStaff()
        setupView()
        prepareSetData()

        btn_simpandataanggotakeluarga.setOnClickListener {
            simpanData()
        }

    }

    private fun prepareSetData() {
        supportActionBar?.title = "Tambah Data Keluarga"
        etKodeUkAgt.text = kode_uk_dtl
        tvnomoragtkeluarga.text= tipe_dtl+" - "+nama_anggota_dtl
        if(kode_uk_kel.isNotEmpty())
        {
            supportActionBar?.title = "Ubah Data Keluarga"
            etKodeUkAgt.text = kode_uk_dtl
            etNamaLengkapKeluarga.text = Editable.Factory.getInstance().newEditable(nama_lengkap_kel)
            etTempatLahirKeluarga.text = Editable.Factory.getInstance().newEditable(tempat_lahir_kel)
            etTglLahirKeluarga.text = Editable.Factory.getInstance().newEditable(convertDateFormat(tanggal_lahir_kel))
            etKeteranganKeluarga.text =  Editable.Factory.getInstance().newEditable(keterangan_kel)
            setSpinnerValue(spinnerHubunganKeluarga, R.array.hubungan_keluarga, hubungan_keluarga_kel)
            setSpinnerValue(spinnerStatusPerkawinan, R.array.status_perkawinan, status_kawin_kel)
            setSpinnerValue(spinnerPendidikan, R.array.pendidikan_list, pendidikan_terakhir_kel)
            setSpinnerValue(spinnerPekerjaan, R.array.pekerjaan_list, pekerjaan_kel)

        }

    }
    fun setSpinnerValue(spinner: Spinner, arrayResId: Int, selectedValue: String) {
        val stringArray = resources.getStringArray(arrayResId)
        val position = stringArray.indexOf(selectedValue)

        if (position != -1) {
            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringArray)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(position)
        } else {
            // Handle the case where the value is not found in the array
            // You can show a default selection or an error message.
        }
    }
    fun getPreferenceAnggota(intent: Intent?) {
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
            kode_uk_kel = intent?.getStringExtra("kode_uk_kel")?:""
            nama_lengkap_kel = intent?.getStringExtra("nama_lengkap_kel")?:""
            tempat_lahir_kel = intent?.getStringExtra("tempat_lahir_kel")?:""
            tanggal_lahir_kel = intent?.getStringExtra("tanggal_lahir_kel")?:""
            status_kawin_kel = intent?.getStringExtra("status_kawin_kel")?:""
            hubungan_keluarga_kel = intent?.getStringExtra("hubungan_keluarga_kel")?:""
            pendidikan_terakhir_kel = intent?.getStringExtra("pendidikan_terakhir_kel")?:""
            pekerjaan_kel = intent?.getStringExtra("pekerjaan_kel")?:""
            keterangan_kel = intent?.getStringExtra("keterangan_kel")?:""
            umur_kel = intent?.getStringExtra("umur_kel")?:""


    }

    private fun setupView() {
        tvnomoragtkeluarga = findViewById(R.id.tvnomoragtkeluarga)
        etNamaLengkapKeluarga = findViewById(R.id.etNamaLengkapKeluarga)
        etTempatLahirKeluarga = findViewById(R.id.etTempatLahirKeluarga)
        val currentDate = dateFormatter.format(calendar.time)
        etTglLahirKeluarga = findViewById(R.id.etTglLahirKeluarga)
        spinnerStatusPerkawinan = findViewById(R.id.spinnerStatusPerkawinan)
        spinnerHubunganKeluarga = findViewById(R.id.spinnerHubunganKeluarga)
        spinnerPendidikan = findViewById(R.id.spinnerPendidikan)

        spinnerPekerjaan = findViewById(R.id.spinnerPekerjaan)

        etTglLahirKeluarga.setOnClickListener { showDatePickerDialog(etTglLahirKeluarga) }
        etTglLahirKeluarga.setText(currentDate)

        etKeteranganKeluarga = findViewById(R.id.etKeteranganKeluarga)

        tilNamaLengkapKeluarga = findViewById(R.id.tilNamaLengkapKeluarga)
        tilTempatLahirKeluarga = findViewById(R.id.tilTempatLahirKeluarga)
        tilTglLahirKeluarga = findViewById(R.id.tilTglLahirKeluarga)
        tilKeteranganKeluarga = findViewById(R.id.tilKeteranganKeluarga)

        etKodeUkAgt = findViewById(R.id.etKodeUkAgt)
        btn_simpandataanggotakeluarga = findViewById(R.id.btn_simpandataanggotakeluarga)

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        etKeteranganKeluarga.requestFocus()
        imm.showSoftInput(etKeteranganKeluarga, InputMethodManager.SHOW_IMPLICIT)
    }
    private fun showDatePickerDialog(editTextDate: EditText) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // Set the selected date in the EditText
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val formattedDate = dateFormatter.format(selectedDate.time)
                editTextDate.setText(formattedDate)
            },
            year, month, day
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }
    private fun getPreferenceStaff() {
        nama_st = preferences.getString("nama","")!!
        nik_st = preferences.getString("nik", "")!!
        dev_st = preferences.getString("devid", "")!!
        jab_st = preferences.getString("jabatan", "")!!
        cab_st = preferences.getString("cabang", "")!!
        kode_cab_st = preferences.getString("kodecabang", "")!!
        hp_st = preferences.getString("hp", "")!!
    }
    fun convertDateFormat(inputDate: String): String {
        val parts = inputDate.split("-")
        if (parts.size == 3) {
            val day = parts[2]
            val month = parts[1]
            val year = parts[0]
            return "$day-$month-$year"
        }
        return "Invalid Date"
    }
    fun calculateAgeFromDateString(dateString: String, dateFormat: String): Int {
        val dateFormatter = SimpleDateFormat(dateFormat)
        val birthDate: Date = dateFormatter.parse(dateString)
        val today: Calendar = Calendar.getInstance()
        val birthCalendar: Calendar = Calendar.getInstance()
        birthCalendar.time = birthDate

        var age: Int = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

        // Check if the birth date has occurred this year or not
        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }

    private fun simpanData() {
        val strTanggalLahir = convertDateFormat(etTglLahirKeluarga.text.toString())
        val strNamaLengkap = etNamaLengkapKeluarga.text.toString().replace("'", "''")
        val strTempatLahir = etTempatLahirKeluarga.text.toString().replace("'", "''")
        val strKeterangan = etKeteranganKeluarga.text.toString().replace("'", "''")
        val strStatusPerkawinan = spinnerStatusPerkawinan.selectedItem.toString()
        val strHubunganKeluarga = spinnerHubunganKeluarga.selectedItem.toString()
        val strPendidikan = spinnerPendidikan.selectedItem.toString()
        val strPekerjaan = spinnerPekerjaan.selectedItem.toString()
        tilNamaLengkapKeluarga.error =null
        tilTempatLahirKeluarga.error =null
        tilTglLahirKeluarga.error =null

        if (strNamaLengkap.isEmpty()) {
            tilNamaLengkapKeluarga.error = "Nama Lengkap harus Di Isi"
            etNamaLengkapKeluarga.requestFocus()
            return
        }
        if (strTempatLahir.isEmpty()) {
            tilTempatLahirKeluarga.error = "Tempat Lahir harus Di Isi"
            etTempatLahirKeluarga.requestFocus()
            return
        }
        if (strKeterangan.isEmpty()) {
            tilKeteranganKeluarga.error = "Keterangan harus Di Isi"
            etKeteranganKeluarga.requestFocus()
            return
        }
        if (strTanggalLahir.isEmpty()) {
            tilTglLahirKeluarga.error = "Tanggal Lahir harus Di Isi"
            etTglLahirKeluarga.requestFocus()
            return
        }
// If all checks pass, continue with the desired logic
        // database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val kode_uk_agt = kode_uk_dtl
        if(kode_uk_kel.isEmpty()) {
            val kode_uk_kel = generateRandom("ukagtkel")
            val dateFormat = "yyyy-MM-dd"
            val strUmur = calculateAgeFromDateString(strTanggalLahir, dateFormat)

            // If all checks pass, continue with the desired logic
            database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
            val execUkAnggotaKeluarga = "INSERT INTO uk_anggota_keluarga (" +
                    "kode_uk_kel,kode_uk, nama, tmpt_lahir, tgl_lahir, umur, hubungan, status_perkawinan, " +
                    "pekerjaan, pendidikan, keterangan, usr_crt, dt_crt, usr_upd, dt_upd) VALUES ( " +
                    "'$kode_uk_kel','$kode_uk_agt', '$strNamaLengkap', '$strTempatLahir', '$strTanggalLahir', '$strUmur', '$strHubunganKeluarga','$strStatusPerkawinan', " +
                    "'$strPekerjaan', '$strPendidikan', '$strKeterangan'" +
                    ",'$nik_st}',CURRENT_TIMESTAMP,'$nik_st}',CURRENT_TIMESTAMP);"
            database_mdismo.execSQL(execUkAnggotaKeluarga)
            database_mdismo.close()
        }
        else
        {
            val dateFormat = "yyyy-MM-dd"
            val strUmur = calculateAgeFromDateString(strTanggalLahir, dateFormat)

            // If all checks pass, continue with the desired logic
            database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
            val execUkAnggotaKeluarga = "UPDATE uk_anggota_keluarga " +
                    "SET " +
                    "nama = '$strNamaLengkap', " +
                    "tmpt_lahir = '$strTempatLahir', " +
                    "tgl_lahir = '$strTanggalLahir', " +
                    "umur = '$strUmur', " +
                    "hubungan = '$strHubunganKeluarga', " +
                    "status_perkawinan = '$strStatusPerkawinan', " +
                    "pekerjaan = '$strPekerjaan', " +
                    "pendidikan = '$strPendidikan', " +
                    "keterangan = '$strKeterangan', " +
                    "usr_upd = '$nik_st', " +
                    "dt_upd = CURRENT_TIMESTAMP " +
                    "WHERE kode_uk_kel = '$kode_uk_kel' AND kode_uk = '$kode_uk_agt';";
            database_mdismo.execSQL(execUkAnggotaKeluarga)
            database_mdismo.close()
        }
        val  intent_main_activity = Intent(this, ListAnggotaKeluarga::class.java)
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

        try {

            val dialog = CustomDialogIntent(
                this,
                "Konfirmasi",
                "Data Berhasil Di Simpan.",
                intent_main_activity
            )
            dialog.show()

        } catch (e: Exception) {
            // Tangani pengecualian di sini jika terjadi masalah saat menampilkan dialog
            e.printStackTrace() // Ini hanya untuk mencetak pesan kesalahan ke log
        }

    }
    fun generateRandom(prefix: String): String {
        val length = 10 // Specify the desired length of the random string
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val randomString = RandomStringGenerator().generateRandomString(length)
        val resultString = "$prefix-$currentDate-$randomString"

        return resultString
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val  intent_main_activity = Intent(this, ListAnggotaKeluarga::class.java)
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