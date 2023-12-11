package com.komida.co.id.mdisujikelayakan

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.komida.co.id.mdisujikelayakan.model.NIKParserResponse
import com.komida.co.id.mdisujikelayakan.utils.CustomDialogIntent
import com.komida.co.id.mdisujikelayakan.utils.NIKParser
import com.komida.co.id.mdisujikelayakan.utils.NIKParserImpl
import com.squareup.moshi.Moshi
import id.co.komida.mdismojava.CustomDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class FormTambahAnggotaActivity : AppCompatActivity() {

    private var kode_uk: String? = null
    private val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    private val calendar = Calendar.getInstance()


    private lateinit var etTempatTinggalOrtu: TextInputEditText
    private lateinit var etKodeUkAgt: TextView
    private lateinit var etTanggalLahir: TextInputEditText
    private lateinit var etTglPengambilanData: TextInputEditText
    private lateinit var etTglBergabung: TextInputEditText
    private lateinit var etNikKtp : TextInputEditText
    private lateinit var etNamaLengkap : TextInputEditText
    private lateinit var etInfoDesa : TextInputEditText
    private lateinit var etKecamatan : TextInputEditText
    private lateinit var etKabupaten : TextInputEditText
    private lateinit var etTahunKadaluarsa : TextInputEditText
    private lateinit var etNamaSuami : TextInputEditText
    private lateinit var etTempatLahir : TextInputEditText
    private lateinit var etRt : TextInputEditText
    private lateinit var etRw : TextInputEditText
    private lateinit var etCenter : TextInputEditText
    private lateinit var etKelompok : TextInputEditText
    private lateinit var etHandphone : TextInputEditText
    private lateinit var etNamaIbuKandung : AutoCompleteTextView
    private lateinit var spStatusPerkawinan : Spinner
    private lateinit var rGTmptTglOrtu: RadioGroup
    private lateinit var rbDesa: RadioButton
    private lateinit var rbKota: RadioButton
    private lateinit var checkboxKoperasi: CheckBox
    private lateinit var checkboxBank: CheckBox
    private lateinit var checkboxTidakAdaAkses: CheckBox
    private lateinit var checkboxRekeningTabungan: CheckBox
    private lateinit var checkboxAsuransi: CheckBox
    private lateinit var btnSimpanDataAnggota: Button
    private lateinit var rgTipeAnggota: RadioGroup
    private lateinit var rb_regular: RadioButton
    private lateinit var rb_non_regular: RadioButton
    private lateinit var rgPernahAgt: RadioGroup
    private lateinit var rbagt_ya: RadioButton
    private lateinit var rbagt_tidak: RadioButton
    private lateinit var rbagt_smartphone: RadioButton
    private lateinit var rbagt_tdksmartphone: RadioButton
    private lateinit var etKodeCabang : TextInputEditText
    private lateinit var etNamaCabang : TextInputEditText

    private lateinit var img_check_valid_ktp:ImageButton
    private lateinit var info_detail_ktp:ImageButton




    private lateinit var tilTmptTglOrtu:LinearLayout
    private lateinit var tilTipeAnggota:TextInputLayout
    private lateinit var tilNikKtp: TextInputLayout
    private lateinit var tilNamaLengkap: TextInputLayout
    private lateinit var tilTahunKadaluarsa: TextInputLayout
    private lateinit var tilInfoDesa: TextInputLayout
    private lateinit var tilKecamatan: TextInputLayout
    private lateinit var tilKabupaten: TextInputLayout


    private lateinit var tilNamaSuami: TextInputLayout
    private lateinit var tilTempatLahir: TextInputLayout
    private lateinit var tilRt: TextInputLayout
    private lateinit var tilRw: TextInputLayout
    private lateinit var tilCenter: TextInputLayout
    private lateinit var tilKelompok: TextInputLayout
    private lateinit var tilNamaIbuKandung: TextInputLayout
    private lateinit var tilHandphone: TextInputLayout
    private lateinit var tilKodeCabang: TextInputLayout
    private lateinit var tilNamaCabang: TextInputLayout


    private lateinit var database_mdismo: SQLiteDatabase
    private val DB_NAME = "mdismo.db"

    private lateinit var preferences: SharedPreferences
    private lateinit var kode_cab_staff_tv: TextInputEditText
    private lateinit var cab_staff_tv: TextInputEditText
    companion object {
        lateinit var nama_st: String
        lateinit var nik_st: String
        lateinit var cab_st: String
        lateinit var jab_st: String
        lateinit var dev_st: String
        lateinit var hp_st: String
        lateinit var kode_cab_st: String
        lateinit var cabang_dtl: String

        lateinit var cek_pnjmn_koperasi: String
        lateinit var cek_pnjmn_bank: String
        lateinit var cek_tidak_akses: String
        lateinit var cek_rekening_tabungan: String
        lateinit var cek_asuransi: String

        lateinit var cek_anggota_dtl:String
        lateinit var kode_uk_dtl:String
        lateinit var tgl_pengambilan_data_dtl:String
        lateinit var tgl_bergabung_dtl:String
        lateinit var tipe_dtl:String
        lateinit var nik_ktp_dtl:String
        lateinit var nama_anggota_dtl:String
        lateinit var tahun_kadaluarsa_dtl:String
        lateinit var nama_suami_dtl:String
        lateinit var tempat_lahir_dtl:String
        lateinit var tgl_lahir_dtl:String
        lateinit var rt_dtl:String
        lateinit var rw_dtl:String
        lateinit var center_dtl:String
        lateinit var kelompok_dtl:String
        lateinit var status_kawin_dtl:String
        lateinit var handphone_dtl:String
        lateinit var nama_ibu_kandung_dtl:String
        lateinit var desa_kota_dtl:String

        lateinit var cek_pnjmn_koperasi_dtl: String
        lateinit var cek_pnjmn_bank_dtl: String
        lateinit var cek_tidak_akses_dtl: String
        lateinit var cek_rekening_tabungan_dtl: String
        lateinit var cek_asuransi_dtl: String
    }
    private lateinit var nikParser:NIKParser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_tambah_anggota)

        val toolbar: Toolbar = findViewById(R.id.toolbartambahanggota)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Pendaftaran Anggota Baru"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)

        val detilIntent = this.intent
        nikParser= NIKParserImpl(this)

        getInformasiAnggota(detilIntent)
        getPrefenceStaff()
        setupView()
        prePareSet()

        btnSimpanDataAnggota.setOnClickListener {
            simpanData()
        }

    }

    fun generateRandom(prefix: String): String {
        val length = 14 // Specify the desired length of the random string
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val randomString = RandomStringGenerator().generateRandomString(length)
        val resultString = "$prefix-$currentDate-$randomString"

        return resultString
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
    fun getInformasiAnggota (intent: Intent){
        cabang_dtl = intent?.getStringExtra("cabang_dtl").toString()
        kode_uk_dtl = intent?.getStringExtra("kode_uk_dtl").toString()
        tgl_pengambilan_data_dtl = intent?.getStringExtra("tgl_pengambilan_data_dtl").toString()
        tgl_bergabung_dtl = intent?.getStringExtra("tgl_bergabung_dtl").toString()
        tipe_dtl = intent?.getStringExtra("tipe_dtl").toString()
        nik_ktp_dtl = intent?.getStringExtra("nik_ktp_dtl").toString()
        nama_anggota_dtl = intent?.getStringExtra("nama_anggota_dtl").toString()
        tahun_kadaluarsa_dtl = intent?.getStringExtra("tahun_kadaluarsa_dtl").toString()
        nama_suami_dtl = intent?.getStringExtra("nama_suami_dtl").toString()
        tempat_lahir_dtl = intent?.getStringExtra("tempat_lahir_dtl").toString()
        tgl_lahir_dtl = intent?.getStringExtra("tgl_lahir_dtl").toString()
        rt_dtl = intent?.getStringExtra("rt_dtl").toString()
        rw_dtl = intent?.getStringExtra("rw_dtl").toString()
        center_dtl = intent?.getStringExtra("center_dtl").toString()
        kelompok_dtl = intent?.getStringExtra("kelompok_dtl").toString()
        status_kawin_dtl = intent?.getStringExtra("status_kawin_dtl").toString()
        handphone_dtl = intent?.getStringExtra("handphone_dtl").toString()
        nama_ibu_kandung_dtl = intent?.getStringExtra("nama_ibu_kandung_dtl").toString()
        desa_kota_dtl = intent?.getStringExtra("desa_kota_dtl").toString()
        cek_anggota_dtl = intent?.getStringExtra("cek_anggota_dtl").toString()
        cek_pnjmn_koperasi_dtl = intent?.getStringExtra("cek_pnjmn_koperasi_dtl").toString()
        cek_pnjmn_bank_dtl = intent?.getStringExtra("cek_pnjmn_bank_dtl").toString()
        cek_tidak_akses_dtl = intent?.getStringExtra("cek_tidak_akses_dtl").toString()
        cek_rekening_tabungan_dtl = intent?.getStringExtra("cek_rekening_tabungan_dtl").toString()
        cek_asuransi_dtl = intent?.getStringExtra("cek_asuransi_dtl").toString()
    }
    fun prePareSet()
    {
        val kodecabs = Editable.Factory.getInstance().newEditable(kode_cab_st)
        val namacabs = Editable.Factory.getInstance().newEditable(cab_st)

        kode_cab_staff_tv.text = kodecabs
        cab_staff_tv.text = namacabs


        if(kode_uk_dtl == "null") {
            etKodeUkAgt.text = Editable.Factory.getInstance().newEditable(kode_uk_dtl)

        }
        else
        {
            supportActionBar?.title = "Perubahan Data Anggota"
            kode_cab_staff_tv.text =Editable.Factory.getInstance().newEditable(cabang_dtl)
            etKodeUkAgt.text = Editable.Factory.getInstance().newEditable(kode_uk_dtl)


            etTglBergabung.text = Editable.Factory.getInstance().newEditable(convertDateFormat(tgl_bergabung_dtl))
            etTglPengambilanData.text = Editable.Factory.getInstance().newEditable(convertDateFormat(tgl_pengambilan_data_dtl))

            val rgTipe: RadioGroup = findViewById(R.id.rgTipeAnggota)
            val rbtipeA: RadioButton = findViewById(R.id.rb_regular)
            val rbtipeB: RadioButton = findViewById(R.id.rb_non_regular)

            if (tipe_dtl == "Regular") {
                rbtipeA.isChecked = true
            } else {
                rbtipeB.isChecked = true
            }

            val rGTmptTglOrtu: RadioGroup = findViewById(R.id.rGTmptTglOrtu)
            val rbDesa: RadioButton = findViewById(R.id.rb_desa)
            val rbKota: RadioButton = findViewById(R.id.rb_kota)

            if (desa_kota_dtl == "Desa") {
                rbDesa.isChecked = true
            } else {
                rbKota.isChecked = true
            }


            val rgPernahAnggota: RadioGroup = findViewById(R.id.rgPernahAnggota)
            val rbYa: RadioButton = findViewById(R.id.rbagt_ya)
            val rbTidak: RadioButton = findViewById(R.id.rbagt_tidak)

            if ( cek_anggota_dtl== "Ya") {
                rbYa.isChecked = true
            } else {
                rbTidak.isChecked = true
            }


            checkboxKoperasi.isChecked =false
            checkboxBank.isChecked=false
            checkboxTidakAdaAkses.isChecked=false
            checkboxRekeningTabungan.isChecked=false
            checkboxAsuransi.isChecked=false


            if(cek_pnjmn_koperasi_dtl=="1")
            {
                checkboxKoperasi.isChecked =true
            }
            if(cek_pnjmn_bank_dtl=="1")
            {
                checkboxBank.isChecked =true
            }
            if(cek_tidak_akses_dtl=="1")
            {
                checkboxTidakAdaAkses.isChecked =true
            }
            if(cek_rekening_tabungan_dtl=="1")
            {
                checkboxRekeningTabungan.isChecked =true
            }
            if(cek_asuransi_dtl=="1")
            {
                checkboxAsuransi.isChecked =true
            }






            etNikKtp.text = Editable.Factory.getInstance().newEditable(nik_ktp_dtl)
            etNamaLengkap.text = Editable.Factory.getInstance().newEditable(nama_anggota_dtl)
            etTahunKadaluarsa.text =Editable.Factory.getInstance().newEditable(tahun_kadaluarsa_dtl)
            etNamaSuami.text = Editable.Factory.getInstance().newEditable(nama_suami_dtl)
            etTempatLahir.text = Editable.Factory.getInstance().newEditable(tempat_lahir_dtl)
            etTanggalLahir.text = Editable.Factory.getInstance().newEditable(convertDateFormat(tgl_lahir_dtl))
            etRt.text = Editable.Factory.getInstance().newEditable(rt_dtl)
            etRw.text = Editable.Factory.getInstance().newEditable(rw_dtl)
            etCenter.text = Editable.Factory.getInstance().newEditable(center_dtl)
            etKelompok.text = Editable.Factory.getInstance().newEditable(kelompok_dtl)
            etHandphone.text = Editable.Factory.getInstance().newEditable(handphone_dtl)
            etNamaIbuKandung.text = Editable.Factory.getInstance().newEditable(nama_ibu_kandung_dtl)
        }

    }
    private fun setupView() {

        etInfoDesa = findViewById(R.id.etInfoDesa)
        tilInfoDesa = findViewById(R.id.tilInfoDesa)
        etKecamatan = findViewById(R.id.etKecamatan)
        tilKecamatan = findViewById(R.id.tilKecamatan)
        etKabupaten = findViewById(R.id.etKabupaten)
        tilKabupaten = findViewById(R.id.tilKabupaten)

        tilTmptTglOrtu = findViewById(R.id.tilTmptTglOrtu)
        etTempatTinggalOrtu = findViewById(R.id.etTempatTinggalOrtu)

        etKodeUkAgt = findViewById(R.id.etKodeUkAgt)
        etTanggalLahir = findViewById(R.id.etTanggalLahir)
        etTglPengambilanData = findViewById(R.id.etTglPengambilanData)
        etTglBergabung = findViewById(R.id.etTglBergabung)
        val currentDate = dateFormatter.format(calendar.time)
        //etTanggalLahir.setText(currentDate)
        etTanggalLahir.setOnClickListener { showDatePickerDialog(etTanggalLahir) }
        etTglPengambilanData.setText(currentDate)
        etTglPengambilanData.setOnClickListener { showDatePickerDialog(etTglPengambilanData) }
        etTglBergabung.setText(currentDate)
        // etTglBergabung.setOnClickListener { showDatePickerDialog(etTglBergabung) }
        img_check_valid_ktp = findViewById(R.id.img_check_valid_ktp)
        info_detail_ktp = findViewById(R.id.info_detail_ktp)

        etNikKtp = findViewById(R.id.etNikKtp)
        etNikKtp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Do something before text changes
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Do something when text changes
            }

            override fun afterTextChanged(s: Editable?) {
                etNikKtp.removeTextChangedListener(this)

                try {
                    val originalString: String = etNikKtp.text.toString()

                    // Validation checks
                    if (originalString.isEmpty()) {
                        tilNikKtp.error = "NIK KTP harus Di Isi"
                        etNikKtp.requestFocus()
                    } else if (originalString.length != 16 || !originalString.matches(Regex("^[0-9]+$"))) {
                        tilNikKtp.error = "NIK KTP tidak Valid Harus 16 Digit"
                        etNikKtp.requestFocus()
                        img_check_valid_ktp.setImageResource(R.drawable.baseline_dangerous_24)
                        img_check_valid_ktp.setColorFilter(ContextCompat.getColor(this@FormTambahAnggotaActivity, R.color.app_shop_colorRed), PorterDuff.Mode.SRC_IN)

                    } else {
                        // Assuming you have a function to format the originalString
                        // Assuming nikParser is initialized somewhere in your code
                        val nikParserResponse = nikParser.parseNik(originalString)
                        val nik = nikParserResponse.nik
                        val isValid = nikParserResponse.isValid
                        val province = nikParserResponse.province
                        val regency = nikParserResponse.regency
                        val district = nikParserResponse.district
                        val birthDate = nikParserResponse.birthDate
                        val gender = nikParserResponse.gender
                        val uniqueCode = nikParserResponse.uniqueCode



                        if (isValid) {
                            img_check_valid_ktp.setImageResource(R.drawable.ic_check_circle)
                            img_check_valid_ktp.setColorFilter(ContextCompat.getColor(this@FormTambahAnggotaActivity, R.color.green), PorterDuff.Mode.SRC_IN)
                            if(birthDate?.isNotEmpty() == true)
                            {
                                val formattedDate: String? = convertDateFormat(birthDate)
                                etTanggalLahir.text = Editable.Factory.getInstance().newEditable(formattedDate)

                                etKecamatan.text = Editable.Factory.getInstance().newEditable(district?.name)
                                etKabupaten.text = Editable.Factory.getInstance().newEditable(regency?.name)
                            }

                            println("NIK: $nik")
                            println("IsValid: $isValid")
                            println("Province: ${province?.name} (ID: ${province?.id})")
                            println("Regency: ${regency?.name} (ID: ${regency?.id})")
                            println("District: ${district?.name} (ID: ${district?.id}, Zip Code: ${district?.zipCode})")
                            println("Birth Date: $birthDate")
                            println("Gender: $gender")
                            println("Unique Code: $uniqueCode")
                            val formattedString = formatOriginalString(originalString)

                            // Clear any previous error
                            tilNikKtp.error = null

                            // Set the formatted text to the EditText
                            // etNikKtp.setText(formattedString)

                        } else {
                            tilNikKtp.error = "NIK KTP tidak Valid"
                            etNikKtp.requestFocus()
                            img_check_valid_ktp.setImageResource(R.drawable.baseline_dangerous_24)
                            img_check_valid_ktp.setColorFilter(ContextCompat.getColor(this@FormTambahAnggotaActivity, R.color.app_shop_colorRed), PorterDuff.Mode.SRC_IN)

                        }


                    }
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                etNikKtp.addTextChangedListener(this)

            }
        })

        info_detail_ktp.setOnClickListener  {

            val originalString: String = etNikKtp.text.toString()
            if (originalString.length == 16) {
                // Your logic for a long click (show tooltip)
                // This will be executed when the button is long-clicked
                // You can show a tooltip or any other information here
                showDialogKtp(originalString) // Replace with your tooltip implementation
                true // Return true to indicate that the long click event is consumed
            }
        }
        etNamaLengkap = findViewById(R.id.etNamaLengkap)
        etTahunKadaluarsa = findViewById(R.id.etTahunKadaluarsa)
        etNamaSuami = findViewById(R.id.etNamaSuami)
        etTempatLahir = findViewById(R.id.etTempatLahir)
        etRt = findViewById(R.id.etRt)
        etRw = findViewById(R.id.etRw)
        etCenter = findViewById(R.id.etCenter)
        etKelompok = findViewById(R.id.etKelompok)
        etHandphone = findViewById(R.id.etHandphone)
        etNamaIbuKandung = findViewById(R.id.etNamaIbuKandung)
        spStatusPerkawinan = findViewById(R.id.spinnerStatusPerkawinan)
        rGTmptTglOrtu = findViewById(R.id.rGTmptTglOrtu)
        rbDesa = findViewById(R.id.rb_desa)
        rbKota = findViewById(R.id.rb_kota)
        checkboxKoperasi = findViewById(R.id.checkbox_koperasi)
        checkboxBank = findViewById(R.id.checkbox_bank)
        checkboxTidakAdaAkses = findViewById(R.id.checkbox_tidak_ada_akses)
        checkboxRekeningTabungan = findViewById(R.id.checkbox_rekening_tabungan)
        checkboxAsuransi = findViewById(R.id.checkbox_asuransi)

        rgPernahAgt = findViewById(R.id.rgPernahAnggota)
        rbagt_ya = findViewById(R.id.rbagt_ya)
        rbagt_tidak = findViewById(R.id.rbagt_tidak)
        btnSimpanDataAnggota= findViewById(R.id.btn_simpandataanggota)

        tilNikKtp = findViewById(R.id.tilNikKtp)
        tilNamaLengkap = findViewById(R.id.tilNamaLengkap)
        tilTahunKadaluarsa = findViewById(R.id.tilTahunKadaluarsa)
        tilNamaSuami = findViewById(R.id.tilNamaSuami)
        tilTempatLahir = findViewById(R.id.tilTempatLahir)
        tilRt = findViewById(R.id.tilRt)
        tilRw = findViewById(R.id.tilRw)
        tilCenter = findViewById(R.id.tilCenter)
        tilKelompok = findViewById(R.id.tilKelompok)
        tilNamaIbuKandung = findViewById(R.id.tilNamaIbuKandung)
        tilHandphone = findViewById(R.id.tilHandphone)

        rgTipeAnggota = findViewById(R.id.rgTipeAnggota)
        rb_regular = findViewById(R.id.rb_regular)
        rb_non_regular = findViewById(R.id.rb_non_regular)
        kode_cab_staff_tv = findViewById(R.id.etKodeCabang)
        cab_staff_tv = findViewById(R.id.etNamaCabang)
    }
    private fun showDialogKtp(originalString:String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val nikParserResponse = nikParser.parseNik(originalString)
        val nik = nikParserResponse.nik
        val isValid = nikParserResponse.isValid
        val province = nikParserResponse.province
        val regency = nikParserResponse.regency
        val district = nikParserResponse.district
        val birthDate = nikParserResponse.birthDate
        val gender = nikParserResponse.gender
        val uniqueCode = nikParserResponse.uniqueCode
        var msg:String=""

        if(isValid)
        {
            msg += "Nik : $nik\n"
            msg += "Provinsi: ${province?.name}\n"
            msg += "Kota/Kabupaten: ${regency?.name} \n"
            msg += "Kecamatan: ${district?.name} , Kode Pos: ${district?.zipCode}\n"
            msg += "Tanggal Lahir: $birthDate\n"
            msg += "Jenis Kelamin: $gender\n"
            msg += "Kode Unik: $uniqueCode\n"
        }
        else
        {
            msg += "NIK KTP tidak Valid"


        }

        // Set the title and message for the dialog
        alertDialogBuilder.setTitle("Informasi Tentang Data KTP")
        alertDialogBuilder.setMessage(msg)

        // Add a positive button and its action
        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            // You can perform any action here when the "OK" button is clicked
            dialog.dismiss()
        }

        // Add a negative button and its action (optional)
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            // You can perform any action here when the "Cancel" button is clicked
            dialog.dismiss()
        }

        // Create and show the dialog
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    private fun formatOriginalString(originalString: String): String {
        val formattedStringBuilder = StringBuilder()

        // Assuming the NIK is a 16-digit number
        for (i in 0 until originalString.length) {
            formattedStringBuilder.append(originalString[i])

            // Add a space after every 4 digits
            if ((i + 1) % 4 == 0 && i < originalString.length - 1) {
                formattedStringBuilder.append(" ")
            }
        }

        return formattedStringBuilder.toString()
    }
    private fun simpanData() {
        val strTanggalLahir = convertDateFormat(etTanggalLahir.text.toString())
        val strTglPengambilanData = convertDateFormat(etTglPengambilanData.text.toString())
        val strTglBergabung = convertDateFormat(etTglBergabung.text.toString())
        val strNikKtp = etNikKtp.text.toString()
        val strNamaLengkap = etNamaLengkap.text.toString().replace("'", "''")

        val strInfoDesa = etInfoDesa.text.toString().replace("'", "''")
        val strKecamatan = etKecamatan.text.toString().replace("'", "''")
        val strKabupaten = etKabupaten.text.toString().replace("'", "''")

        val strTahunKadaluarsa = etTahunKadaluarsa.text.toString()
        val strNamaSuami = etNamaSuami.text.toString().replace("'", "''")
        val strTempatLahir = etTempatLahir.text.toString().replace("'", "''")
        val strRt = etRt.text.toString()
        val strRw = etRw.text.toString()
        val strCenter = etCenter.text.toString()
        val strKelompok = etKelompok.text.toString()
        val strHandphone = etHandphone.text.toString()
        val strNamaIbuKandung = etNamaIbuKandung.text.toString().replace("'", "''")
        val strStatusPerkawinan = spStatusPerkawinan.selectedItem.toString()
        val strTempatTinggalOrtu = etTempatTinggalOrtu.text.toString()
        val strTempatTglOrtu = when (rGTmptTglOrtu.checkedRadioButtonId) {
            R.id.rb_desa -> rbDesa.text.toString()
            R.id.rb_kota -> rbKota.text.toString()
            else -> ""
        }

        var strTipeAnggota = ""

        for (rb in rgTipeAnggota.children) {
            if (rb is RadioButton && rb.isChecked) {
                // If a RadioButton is checked, assign its text to strTipeAnggota
                strTipeAnggota = rb.text.toString()
                break // Exit the loop since we found the checked RadioButton
            }
        }

        var strPernahAgt = ""

        for (rb in rgPernahAgt.children) {
            if (rb is RadioButton && rb.isChecked) {
                // If a RadioButton is checked, assign its text to strTipeAnggota
                strPernahAgt = rb.text.toString()
                break // Exit the loop since we found the checked RadioButton
            }
        }
        /*
        val strTipeAnggota = when (rgTipeAnggota.checkedRadioButtonId) {
            R.id.rb_regular -> rb_regular.text.toString()
            R.id.rb_non_regular -> rb_non_regular.text.toString()
            else -> ""
        }
         */

        val pinjamanList = ArrayList<String>()
        cek_pnjmn_koperasi="0"
        cek_pnjmn_bank="0"
        cek_tidak_akses="0"
        cek_rekening_tabungan="0"
        cek_asuransi="0"
        if (checkboxKoperasi.isChecked) {
            cek_pnjmn_koperasi="1"
            pinjamanList.add(checkboxKoperasi.text.toString())
        }
        if (checkboxBank.isChecked) {
            cek_pnjmn_bank="1"
            pinjamanList.add(checkboxBank.text.toString())
        }
        if (checkboxTidakAdaAkses.isChecked) {
            cek_tidak_akses="1"
            pinjamanList.add(checkboxTidakAdaAkses.text.toString())
        }
        if (checkboxRekeningTabungan.isChecked) {
            cek_rekening_tabungan="1"
            pinjamanList.add(checkboxRekeningTabungan.text.toString())
        }
        if (checkboxAsuransi.isChecked) {
            cek_asuransi="1"
            pinjamanList.add(checkboxAsuransi.text.toString())
        }
        val strPinjamanList = TextUtils.join(", ", pinjamanList)



        tilNikKtp.error =null
        tilNamaLengkap.error =null
        tilTahunKadaluarsa.error =null
        tilNamaSuami.error =null
        tilTempatLahir.error =null
        tilRt.error =null
        tilRw.error =null
        tilCenter.error =null
        tilKelompok.error =null
        tilNamaIbuKandung.error =null
        tilHandphone.error =null

        /* if NIK is 3576447103910003.*/

        val nikParseResult = nikParser.parseNik(strNikKtp)
       

        if (strNikKtp.isEmpty()) {
            tilNikKtp.error = "NIK KTP harus Di Isi"
            etNikKtp.requestFocus()
            return
        }

        /*
        if (strNikKtp.length != 16 || !strNikKtp.matches(Regex("^[0-9]+\$"))) {
            tilNikKtp.error = "NIK KTP tidak Valid Harus 16 Digit"
            etNikKtp.requestFocus()
            return
        }
         */
        var checkValidKtp = isNIKValid((strNikKtp))
        println("CHECK VALID KTP"+checkValidKtp)

        if (checkValidKtp.isNotEmpty()){
            tilNikKtp.error = checkValidKtp
            etNikKtp.requestFocus()
            return
        }


        if (strNamaLengkap.isEmpty()) {
            tilNamaLengkap.error = "Nama Lengkap harus Di Isi"
            etNamaLengkap.requestFocus()
            return
        }
        /*
        if (strTahunKadaluarsa.isEmpty()) {
            tilTahunKadaluarsa.error = "Tahun Kadaluarsa harus Di Isi"
            etTahunKadaluarsa.requestFocus()
            return
        }

         */
        println("ANUGEBLEG"+strNamaSuami)
        if (strNamaSuami.isEmpty()) {
            tilNamaSuami.error = "Nama Suami harus Di Isi"
            etNamaSuami.requestFocus()
            return
        }
        if (strTempatLahir.isEmpty()) {
            tilTempatLahir.error = "Tempat Lahir harus Di Isi"
            etTempatLahir.requestFocus()
            return
        }
        if (strRt.isEmpty()) {
            tilRt.error = "RT harus Di Isi"
            etRt.requestFocus()
            return
        }
        if (strRw.isEmpty()) {
            tilRw.error = "RW harus Di Isi"
            etRw.requestFocus()
            return
        }
        if (strCenter.isEmpty()) {
            tilCenter.error = "Center harus Di Isi"
            etCenter.requestFocus()
            return
        }
        if (strKelompok.isEmpty()) {
            tilKelompok.error = "Kelompok harus Di Isi"
            etKelompok.requestFocus()
            return
        }
        if (strNamaIbuKandung.isEmpty()) {
            tilNamaIbuKandung.error = "Nama Ibu Kandung harus Di Isi"
            etNamaIbuKandung.requestFocus()
            return
        }
        if (strHandphone.isEmpty()) {
            tilHandphone.error = "No Handphone harus Di Isi"
            etHandphone.requestFocus()
            return
        }



        // If all checks pass, continue with the desired logic
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val execUkAnggota: String
        println(kode_uk_dtl)
        if(kode_uk_dtl != "null")
        {
            val kode_uk = kode_uk_dtl;
            execUkAnggota = "UPDATE uk_anggota SET " +
                    "cabang = '$kode_cab_st', tipe = '$strTipeAnggota', nama = '$strNamaLengkap', nik = '$strNikKtp', tahun_kadaluarsa = '$strTahunKadaluarsa', " +
                    "tempat_lahir = '$strTempatLahir', tgl_lahir = '$strTanggalLahir', nama_suami = '$strNamaSuami', status_kawin = '$strStatusPerkawinan', " +
                    "rt = '$strRt', rw = '$strRw', no_center = '$strCenter', kelompok = '$strKelompok', " +
                    "tgl_pengambilan_data = '$strTglPengambilanData', tgl_bergabung = '$strTglBergabung', nama_ibu_kandung = '$strNamaIbuKandung', " +
                    "tempat_tgl_ortu = '$strTempatTglOrtu', handphone = '$strHandphone', " +
                    "cek_pnjmn_koperasi = '$cek_pnjmn_koperasi', cek_pnjmn_bank = '$cek_pnjmn_bank', " +
                    "cek_tidak_ada_akses = '$cek_tidak_akses', cek_rekening_tabungan = '$cek_rekening_tabungan', " +
                    "cek_asuransi = '$cek_asuransi', cek_anggota_komida = '$strPernahAgt', " +
                    "usr_upd = '$nik_st', dt_upd = CURRENT_TIMESTAMP WHERE kode_uk = '$kode_uk' ;"
        }
        else {
            val kode_uk = generateRandom("ukagt");
            execUkAnggota = "INSERT INTO uk_anggota (" +
                    "kode_uk,cabang, tipe, nama, nik, tahun_kadaluarsa, tempat_lahir, tgl_lahir, " +
                    "desa,kecamatan,kabupaten,nama_suami, status_kawin, rt, rw, desa, kecamatan, kabupaten, " +
                    "no_id, no_center, kelompok, tgl_pengambilan_data, tgl_bergabung, " +
                    "nama_ibu_kandung, tempat_tgl_ortu, wilayah, cek_client, handphone, " +
                    "cek_pnjmn_koperasi, cek_pnjmn_bank, cek_tidak_ada_akses, " +
                    "cek_rekening_tabungan, cek_asuransi, cek_anggota_komida, " +
                    "status_uji, usr_crt, dt_crt, usr_upd, dt_upd) VALUES (" +
                    "'$kode_uk','$kode_cab_st', '$strTipeAnggota', '$strNamaLengkap', '$strNikKtp', '$strTahunKadaluarsa', '$strTempatLahir','$strTanggalLahir', " +
                    "'$strInfoDesa','$strKecamatan','$strKabupaten','$strNamaSuami', '$strStatusPerkawinan', '$strRt', '$strRw', '','', '',  " +
                    "'', '$strCenter', '$strKelompok','$strTglPengambilanData', '$strTglBergabung', " +
                    "'$strNamaIbuKandung', '$strTempatTinggalOrtu', '$strTempatTglOrtu','', '$strHandphone', " +
                    "'$cek_pnjmn_koperasi', '$cek_pnjmn_bank', '$cek_tidak_akses'," +
                    "'$cek_rekening_tabungan', '$cek_asuransi','$strPernahAgt' ," +
                    "'1','$nik_st',CURRENT_TIMESTAMP,'$nik_st',CURRENT_TIMESTAMP);"
        }
        //println(execUkAnggota)
        database_mdismo.execSQL(execUkAnggota)
        val intent_main_activity: Intent
        if(kode_uk_dtl.isNotEmpty()) {
            intent_main_activity = Intent(this, ListAnggotaUk::class.java)
        }
        else{
            intent_main_activity = Intent(this, MainActivity::class.java)

        }
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
    fun isNIKValid(nik: String): String {
        // Check length
        var msg: String = ""
        if (nik.length != 16) {
            msg = "Nik Ktp tidak valid Harus 16 Digit"
            return msg
        }
        val nikParserResponse = nikParser.parseNik(nik)
        val isValid = nikParserResponse.isValid
        if (!isValid) {
            msg = "Nik Ktp tidak valid,Mohon di isi dengan Benar"
            return msg
        }
        val gender = nikParserResponse.gender
        if (gender==null) {
            msg = "Nik Ktp tidak valid,Mohon di isi dengan Benar"
            return msg
        }
        return msg
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


    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    private fun showDatePickerDialog(editTextDate: TextInputEditText) {
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
}
