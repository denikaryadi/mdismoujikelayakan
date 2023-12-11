package com.komida.co.id.mdisujikelayakan

import AnggotaList
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.komida.co.id.mdisujikelayakan.model.AnggotaChip
import com.komida.co.id.mdisujikelayakan.utils.CustomDialogIntent
import com.pchmn.materialchips.ChipView
import com.pchmn.materialchips.ChipsInput
import com.pchmn.materialchips.model.ChipInterface
import java.text.SimpleDateFormat
import java.util.*


class CustomAdapterTambahKelompok(
    context: Context,
    resource: Int,
    objects: List<String>?,
) : ArrayAdapter<String>(context, resource, objects ?: emptyList()) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_people_contacts, parent, false)
        val nameTextView: TextView = view.findViewById(R.id.name)
        val nikKtpTextView: TextView = view.findViewById(R.id.nik_ktp)

        val item = getItem(position)
        val parts = item?.split(" | ")

        if (parts != null && parts.size >= 2) {
            nameTextView.text = Html.fromHtml(parts[1] ?: "")
            nikKtpTextView.text = Html.fromHtml(parts[0] ?: "")
        }
        return view
    }

}


class FormTambahKelompok : AppCompatActivity() {
    private lateinit var parent_view: View
    private lateinit var preferences: SharedPreferences
    private lateinit var database_mdismo: SQLiteDatabase
    var DB_NAME = "mdismo.db"

    private val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    private val calendar = Calendar.getInstance()

    private lateinit var nama_staff_tv: TextView
    private lateinit var nik_staff_tv: TextView
    private lateinit var cab_staff_tv: TextView


    private lateinit var tvKodeUkLwkDtl: TextView
    private lateinit var tvNoTrxUkLwkDtl: TextView


    private lateinit var tilKodeCabang: TextInputLayout
    private lateinit var etKodeUkAgt: TextView
    private lateinit var etKodeCabang: TextInputEditText
    private lateinit var tilNamaCabang:TextInputLayout
    private lateinit var etNamaCabang:TextInputEditText
    private lateinit var etTglInput:TextInputEditText
    private lateinit var tilCenter:TextInputLayout
    private lateinit var etCenter: AppCompatEditText
    private lateinit var tilNamaCenter:TextInputLayout
    private lateinit var etNamaCenter:TextInputEditText
    private lateinit var tilKelompok:TextInputLayout
    private lateinit var etKelompok:TextInputEditText
    private lateinit var tilNamaKelompok:TextInputLayout
    private lateinit var etNamaKelompok:TextInputEditText
    private lateinit var tilTempatCenter:TextInputLayout
    private lateinit var etTempatCenter:TextInputEditText
    private lateinit var tilWaktuMinggon:TextInputLayout
    private lateinit var etWaktuLwk:TextInputEditText
    private lateinit var rgKategoriKelompok: RadioGroup
    private lateinit var rb_kelompokbarucenterbaru: RadioButton
    private lateinit var rb_kelompokbarucenterlama:RadioButton
    private lateinit var tilKetuaCenter:TextInputLayout
    private lateinit var src_ketua_center:TextInputEditText
    private lateinit var tilPilihAnggota:TextInputLayout

    private lateinit var openDialogKetuaCenter:Button
    private lateinit var tilWakilKetuaCenter:TextInputLayout
    private lateinit var src_wakil_ketua_center:TextInputEditText
    private lateinit var openDialogButtonWakilKetuaCenter:Button
    private lateinit var btn_simpandatakelompok:Button
    private lateinit var showAnggota:ImageButton


    private lateinit var kode_cab_staff_tv: TextInputEditText
    companion object {

        lateinit var nama_st: String
        lateinit var nik_st: String
        lateinit var cab_st: String
        lateinit var jab_st: String
        lateinit var dev_st: String
        lateinit var hp_st: String
        lateinit var kode_cab_st: String

        lateinit var kode_uk_lwk_dtl:String
        lateinit var no_trx_lwk_dtl:String
        lateinit var tgl_input_dtl:String
        lateinit var cabang_dtl:String
        lateinit var cabang_info_dtl:String
        lateinit var no_center_dtl:String
        lateinit var nama_center_dtl:String
        lateinit var no_kelompok_dtl:String
        lateinit var nama_kelompok_dtl:String
        lateinit var tempat_dtl:String
        lateinit var waktu_dtl:String
        lateinit var kategori_lwk_dtl:String
        lateinit var ketua_center_dtl:String
        lateinit var wakil_ketua_center_dtl:String
        lateinit var ketua_kelompok_dtl:String
        lateinit var petugas_dtl:String
        lateinit var ttd_petugas_dtl:String
        lateinit var catatan_dtl:String
        lateinit var user_create_dtl:String
        lateinit var date_create_dtl:String
        lateinit var user_modified_dtl:String
        lateinit var date_modified_dtl:String
        lateinit var penerima_kedua_dtl:String
    }



    private lateinit var mChipsInput: ChipsInput
    private var items: MutableList<AnggotaChip> = ArrayList()
    private var items_added: MutableList<ChipInterface> = ArrayList()
    private var items_people: MutableList<AnggotaList> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_tambah_kelompok)
        initToolbar()
        iniComponent()

    }

    private fun prePareSet() {
        val kodecabs = Editable.Factory.getInstance().newEditable(kode_cab_st)
        val namacabs = Editable.Factory.getInstance().newEditable(cab_st)
        etKodeCabang.text = kodecabs
        etNamaCabang.text = namacabs


        if(kode_uk_lwk_dtl == "null") {
            tvKodeUkLwkDtl.text = kode_uk_lwk_dtl
            tvNoTrxUkLwkDtl.text = no_trx_lwk_dtl

        }
        else {
            supportActionBar?.title = "Perubahan Data Kelompok"
            etKodeCabang.text =Editable.Factory.getInstance().newEditable(cabang_info_dtl)
            etTglInput.text = Editable.Factory.getInstance().newEditable(convertDateFormat(tgl_input_dtl))
            etCenter.text  = Editable.Factory.getInstance().newEditable(no_center_dtl)
            etNamaCenter.text  = Editable.Factory.getInstance().newEditable(nama_center_dtl)
            etKelompok.text  = Editable.Factory.getInstance().newEditable(no_kelompok_dtl)
            etNamaKelompok.text  = Editable.Factory.getInstance().newEditable(nama_kelompok_dtl)
            etTempatCenter.text  = Editable.Factory.getInstance().newEditable(tempat_dtl)
            etWaktuLwk.text  = Editable.Factory.getInstance().newEditable(waktu_dtl)


            if (kategori_lwk_dtl == "Kelompok Baru Center Baru") {
                rb_kelompokbarucenterbaru.isChecked = true
            } else {
                rb_kelompokbarucenterlama.isChecked = true
            }
            src_ketua_center.text  = Editable.Factory.getInstance().newEditable(ketua_center_dtl)
            src_wakil_ketua_center.text  = Editable.Factory.getInstance().newEditable(wakil_ketua_center_dtl)

            database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
            val cursor = database_mdismo.rawQuery("select kode_uk,nik,nama  from uk_lwk_anggota_dtl where kode_uk_lwk='$kode_uk_lwk_dtl'", null)

            val anggotaDataList = ArrayList<String>()  // Define the list inside the function

            while (cursor.moveToNext()) {
                val drawableResId = R.drawable.woman
                val defaultDrawable = ContextCompat.getDrawable(this, drawableResId)
                val drawable = defaultDrawable ?: getFallbackDrawable() // Replace with your fallback logic
                val nik = cursor.getString(cursor.getColumnIndex("nik"))
                val nama = cursor.getString(cursor.getColumnIndex("nama"))
                mChipsInput.addChip(drawable, nama,nik)

            }

            // Close the cursor and the database when done
            cursor.close()
            database_mdismo.close()
        }

    }

    private fun setupView() {
        tvKodeUkLwkDtl  = findViewById(R.id.tvKodeUkLwkDtl)
        tilKodeCabang = findViewById(R.id.tilKodeCabang)
        etKodeUkAgt = findViewById(R.id.etKodeUkAgt)
        etKodeCabang = findViewById(R.id.etKodeCabang)
        tilNamaCabang = findViewById(R.id.tilNamaCabang)
        etNamaCabang = findViewById(R.id.etNamaCabang)
        etTglInput = findViewById(R.id.etTglInput)
        tilCenter = findViewById(R.id.tilCenter)
        etCenter = findViewById(R.id.etCenter)
        tilNamaCenter = findViewById(R.id.tilNamaCenter)
        etNamaCenter = findViewById(R.id.etNamaCenter)
        tilKelompok = findViewById(R.id.tilKelompok)
        etKelompok = findViewById(R.id.etKelompok)
        tilNamaKelompok = findViewById(R.id.tilNamaKelompok)
        etNamaKelompok = findViewById(R.id.etNamaKelompok)
        tilTempatCenter = findViewById(R.id.tilTempatCenter)
        etTempatCenter = findViewById(R.id.etTempatCenter)
        tilWaktuMinggon = findViewById(R.id.tilWaktuMinggon)
        etWaktuLwk = findViewById(R.id.etWaktuLwk)
        rgKategoriKelompok = findViewById(R.id.rgKategoriKelompok)
        rb_kelompokbarucenterbaru = findViewById(R.id.rb_kelompokbarucenterbaru)
        rb_kelompokbarucenterlama = findViewById(R.id.rb_kelompokbarucenterlama)
        tilKetuaCenter = findViewById(R.id.tilKetuaCenter)
        src_ketua_center = findViewById(R.id.src_ketua_center)
        openDialogKetuaCenter = findViewById(R.id.openDialogKetuaCenter)
        tilWakilKetuaCenter = findViewById(R.id.tilWakilKetuaCenter)
        src_wakil_ketua_center = findViewById(R.id.src_wakil_ketua_center)
        openDialogButtonWakilKetuaCenter = findViewById(R.id.openDialogButtonWakilKetuaCenter)

        tilPilihAnggota= findViewById(R.id.tilPilihAnggota)
        btn_simpandatakelompok = findViewById(R.id.btn_simpandatakelompok)

        showAnggota = findViewById(R.id.contacts)
        mChipsInput = findViewById(R.id.chips_input)

        val currentDate = dateFormatter.format(calendar.time)



        etTglInput.setOnClickListener { showDatePickerDialog(etTglInput) }
        etTglInput.setText(currentDate)
        openDialogButtonWakilKetuaCenter.setOnClickListener {
            showDialogWakilKetuaCenter()
        }
        openDialogKetuaCenter.setOnClickListener {
            showDialogKetuaCenter()
        }

        showAnggota.setOnClickListener {
            showDialogAnggotaLain()
        }

        btn_simpandatakelompok.setOnClickListener {
            simpanData()
        }


    }

    private fun simpanData() {


        val strTglInput = convertDateFormat(etTglInput.text.toString())
        val strCenter = etCenter.text.toString().replace("'", "''")
        val strNamaCenter = etNamaCenter.text.toString().replace("'", "''")
        val strKelompok = etKelompok.text.toString().replace("'", "''")
        val strNamaKelompok = etNamaKelompok.text.toString().replace("'", "''")
        val strTempat = etTempatCenter.text.toString()
        val strWaktu = etWaktuLwk.text.toString().replace("'", "''")

        val strJenisKategori = rgKategoriKelompok.checkedRadioButtonId
            .let { findViewById<RadioButton>(it)?.text.toString() }

        val strKetuaCenter = src_ketua_center.text.toString()
        val strWakilKetuaCenter = src_wakil_ketua_center.text.toString()

        val selectedChips: List<ChipInterface> = mChipsInput.selectedChipList

        val chipLabels: List<String> = selectedChips.map { it.getLabel() } // Assuming getLabel() is a method in ChipInterface

        val chipInfos: List<String> = selectedChips.map { it.getInfo() } // Assuming getLabel() is a method in ChipInterface

        tilCenter.error = null
        tilNamaCenter.error=null
        tilKelompok.error = null
        tilNamaKelompok.error =null
        tilTempatCenter.error = null
        tilWaktuMinggon.error = null
        tilWakilKetuaCenter.error =null
        tilKetuaCenter.error =null
        tilPilihAnggota.error=null
        if (strCenter.isEmpty()) {
            tilCenter.error = "Kode Center Belum Di Isi"
            etCenter.requestFocus()
            return
        }
        if (strNamaCenter.isEmpty()) {
            tilNamaCenter.error = "Nama Center Belum Di Isi"
            etNamaCenter.requestFocus()
            return
        }
        if (strKelompok.isEmpty()) {
            tilKelompok.error = "Kode Kelompok Belum Di Isi"
            etKelompok.requestFocus()
            return
        }
        if (strNamaKelompok.isEmpty()) {
            tilNamaKelompok.error = "Nama Kelompok Belum Di Isi"
            etNamaKelompok.requestFocus()
            return
        }

        if (strTempat.isEmpty()) {
            tilTempatCenter.error = "Tempat Center Belum Di Isi"
            etTempatCenter.requestFocus()
            return
        }
        if (strWaktu.isEmpty()) {
            tilWaktuMinggon.error = "Waktu Lwk Belum Di Isi"
            etWaktuLwk.requestFocus()
            return
        }

        if (strKetuaCenter.isEmpty()) {
            tilKetuaCenter.error = "Ketua Center Belum Di Pilih"
            etWaktuLwk.requestFocus()
            return
        }

        if (strWakilKetuaCenter.isEmpty()) {
            tilWakilKetuaCenter.error = "Ketua Kelompok Belum Di Pilih"
            etWaktuLwk.requestFocus()
            return
        }
        val numberOfSelectedChips: Int = selectedChips.size

        if(numberOfSelectedChips==0)
        {
            tilPilihAnggota.error ="Anggota Belum di Pilih"
            mChipsInput.requestFocus()
            return
        }

        val partsKetuaCenter = strKetuaCenter?.split(" | ")
        val concate_kc: String?
        if (partsKetuaCenter != null && partsKetuaCenter.size >= 2) {
            val nikKetuaCenter = partsKetuaCenter[0].trim()
            val nameKetuaCenter = partsKetuaCenter[1].trim()
            val kodeUkAnggotaKc = getDataInfoAnggota(nikKetuaCenter, nameKetuaCenter)
            concate_kc = "$nikKetuaCenter^${nameKetuaCenter.replace("'","''")}^$kodeUkAnggotaKc"
        } else {
            concate_kc = null
        }

        val partsWakilKetuaCenter = strWakilKetuaCenter?.split(" | ")
        val concate_wkc: String?
        if (partsWakilKetuaCenter != null && partsWakilKetuaCenter.size >= 2) {
            val nikWakilKetuaCenter = partsWakilKetuaCenter[0].trim()
            val nameWakilKetuaCenter = partsWakilKetuaCenter[1].trim()
            val kodeUkAnggotaWKc = getDataInfoAnggota(nikWakilKetuaCenter, nameWakilKetuaCenter)
            concate_wkc = "$nikWakilKetuaCenter^${nameWakilKetuaCenter.replace("'","''")}^$kodeUkAnggotaWKc"
        } else {
            concate_wkc = null
        }

        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val execUkKelompok: String
        if(kode_uk_lwk_dtl == "null") {
            val kode_uk_lwk = generateRandom("uklwk")
            val no_trx_lwk = generateTrxLwk("F03/LWK", kode_cab_st)
            execUkKelompok = "INSERT INTO uk_lwk_anggota (" +
                    "kode_uk_lwk, no_trx_lwk, tgl_input, cabang, no_center, nama_center, no_kelompok, nama_kelompok, " +
                    "tempat, waktu, kategori_lwk, ketua_center, wakil_ketua_center, ketua_kelompok, petugas, ttd_petugas, catatan, " +
                    "user_create, date_create, user_modified, date_modified, penerima_kedua)" +
                    "values ('$kode_uk_lwk', '$no_trx_lwk', " +
                    "'$strTglInput', '$kode_cab_st', '$strCenter', '$strNamaCenter', '$strKelompok', '$strNamaKelompok', " +
                    "'$strTempat', '$strWaktu', '$strJenisKategori', '$concate_kc', '$concate_wkc', '', '$nik_st', '', '', " +
                    "'$nik_st', CURRENT_TIMESTAMP, '$nik_st', CURRENT_TIMESTAMP,'');"
            database_mdismo.execSQL(execUkKelompok)
           // println(execUkKelompok)
            var nik_anggota: String
            var nama_anggota: String
            var kode_uk_anggota: String
            var execagtdtl: String
            val database_mdismo1 = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)

            for (chip in selectedChips) {
                val kode_uk_lwk_dtl = generateRandom("uklwkdtl")
                nik_anggota = chip.getInfo()
                nama_anggota = chip.getLabel()
                kode_uk_anggota = getDataInfoAnggota(nik_anggota, nama_anggota)
                execagtdtl = "insert into uk_lwk_anggota_dtl (kode_uk_lwk_dtl,kode_uk_lwk,kode_uk,no_trx,nik,nama,urutan_penerima_cair,sta_jabatan)" +
                        " values ('$kode_uk_lwk_dtl','$kode_uk_lwk','$kode_uk_anggota','$no_trx_lwk','$nik_anggota','${nama_anggota.replace("'", "''")}','','AGT')"

               database_mdismo1.execSQL(execagtdtl)
            }
            database_mdismo1.close()
            database_mdismo.close()
        }
        else {
            val kode_uk_lwk = kode_uk_lwk_dtl
            val no_trx_lwk = no_trx_lwk_dtl
            execUkKelompok = "UPDATE uk_lwk_anggota SET " +
                    "tgl_input = '$strTglInput', " +
                    "cabang = '$kode_cab_st', " +
                    "no_center = '$strCenter', " +
                    "nama_center = '$strNamaCenter', " +
                    "no_kelompok = '$strKelompok', " +
                    "nama_kelompok = '$strNamaKelompok', " +
                    "tempat = '$strTempat', " +
                    "waktu = '$strWaktu', " +
                    "kategori_lwk = '$strJenisKategori', " +
                    "ketua_center = '$concate_kc', " +
                    "wakil_ketua_center = '$concate_wkc', " +
                    "petugas = '', " +
                    "ttd_petugas = '', " +
                    "catatan = '', " +
                    "user_modified = '$nik_st', " +
                    "date_modified = CURRENT_TIMESTAMP " +
                    "WHERE kode_uk_lwk = '$kode_uk_lwk_dtl' and no_trx_lwk = '$no_trx_lwk_dtl';";
               // println(execUkKelompok)
                database_mdismo.execSQL(execUkKelompok)
                var nik_anggota: String
                var nama_anggota: String
                var kode_uk_anggota: String
                var execagtdtl: String
                val database_mdismo1 = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
                database_mdismo1.execSQL("delete from uk_lwk_anggota_dtl where kode_uk_lwk='$kode_uk_lwk_dtl' ")
                for (chip in selectedChips) {
                    val kode_uk_lwk_dtl = generateRandom("uklwkdtl")
                    nik_anggota = chip.getInfo()
                    nama_anggota = chip.getLabel()
                    kode_uk_anggota = getDataInfoAnggota(nik_anggota, nama_anggota)
                    execagtdtl = "insert into uk_lwk_anggota_dtl (kode_uk_lwk_dtl,kode_uk_lwk,kode_uk,no_trx,nik,nama,urutan_penerima_cair,sta_jabatan)" +
                            " values ('$kode_uk_lwk_dtl','$kode_uk_lwk','$kode_uk_anggota','$no_trx_lwk','$nik_anggota','${nama_anggota.replace("'", "''")}','','AGT')"

                    //println(execagtdtl)
                    database_mdismo1.execSQL(execagtdtl)
                }
                database_mdismo1.close()
                database_mdismo.close()
        }


        val intent_main_activity = Intent(this, ListAnggotaKelompok::class.java)

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
    fun getDataInfoAnggota(nik: String, nama: String): String {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        var kode_uk: String? = ""
        var query = "select kode_uk from uk_anggota where nik='${nik}' and nama ='${nama.replace("'", "''")}' "
        val res = database_mdismo.rawQuery(
            query,
            null
        )
        while (res.moveToNext()) {
            kode_uk = res.getString(0)
        }
        database_mdismo.close()

        // Use the let extension function to handle the nullable case
        return kode_uk?.let { it } ?: ""
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
    fun generateRandom(prefix: String): String {
        val length = 14
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val randomString = RandomStringGenerator().generateRandomString(length)
        return "$prefix-$currentDate-$randomString"
    }
    fun generateTrxLwk(prefix: String, kodeCabSt: String): String {
        val length = 5
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val randomString = RandomStringGenerator().generateRandomString(length)
        return "$prefix/$kodeCabSt/$currentDate$randomString"
    }
    private fun iniComponent() {
        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        val detilIntent = this.intent

        getInformasiKelompok(detilIntent)
        getPrefenceStaff()
        setupView()
        prePareSet()
        getAnggotaChipList()

        // chips listener
        mChipsInput.addChipsListener(object : ChipsInput.ChipsListener {
            override fun onChipAdded(chip: ChipInterface, newSize: Int) {
                items_added.add(chip)
            }

            override fun onChipRemoved(chip: ChipInterface, newSize: Int) {
                items_added.remove(chip)
            }

            override fun onTextChanged(text: CharSequence) {
                //Log.e(TAG, "text changed: " + text.toString())
            }
        })



    }
    fun getInformasiKelompok (intent: Intent){
        kode_uk_lwk_dtl = intent?.getStringExtra("kode_uk_lwk_dtl").toString()

        no_trx_lwk_dtl = intent?.getStringExtra("no_trx_lwk_dtl").toString()
        tgl_input_dtl = intent?.getStringExtra("tgl_input_dtl").toString()
        cabang_info_dtl = intent?.getStringExtra("cabang_info_dtl").toString()
        no_center_dtl = intent?.getStringExtra("no_center_dtl").toString()
        nama_center_dtl = intent?.getStringExtra("nama_center_dtl").toString()
        no_kelompok_dtl = intent?.getStringExtra("no_kelompok_dtl").toString()
        nama_kelompok_dtl = intent?.getStringExtra("nama_kelompok_dtl").toString()
        tempat_dtl = intent?.getStringExtra("tempat_dtl").toString()
        waktu_dtl = intent?.getStringExtra("waktu_dtl").toString()
        kategori_lwk_dtl = intent?.getStringExtra("kategori_lwk_dtl").toString()
        ketua_center_dtl = intent?.getStringExtra("ketua_center_dtl").toString()
        wakil_ketua_center_dtl = intent?.getStringExtra("wakil_ketua_center_dtl").toString()
        ketua_kelompok_dtl = intent?.getStringExtra("ketua_kelompok_dtl").toString()
        petugas_dtl = intent?.getStringExtra("petugas_dtl").toString()
        ttd_petugas_dtl = intent?.getStringExtra("ttd_petugas_dtl").toString()
        catatan_dtl = intent?.getStringExtra("catatan_dtl").toString()
        user_create_dtl = intent?.getStringExtra("user_create_dtl").toString()
        date_create_dtl = intent?.getStringExtra("date_create_dtl").toString()
        user_modified_dtl = intent?.getStringExtra("user_modified_dtl").toString()
        date_modified_dtl = intent?.getStringExtra("date_modified_dtl").toString()
        penerima_kedua_dtl = intent?.getStringExtra("penerima_kedua_dtl").toString()
    }
    private fun getAnggotaChipList() {



        try {
            database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
            var id = 0
            val query = "SELECT kode_uk, cabang, tipe, nama, nik, tahun_kadaluarsa, tempat_lahir, tgl_lahir, " +
                    "nama_suami, status_kawin, rt, rw, desa, kecamatan, kabupaten, no_id, no_center, " +
                    "kelompok, tgl_pengambilan_data, tgl_bergabung, nama_ibu_kandung, tempat_tgl_ortu, " +
                    "wilayah, cek_client, handphone, cek_pnjmn_koperasi, cek_pnjmn_bank, cek_tidak_ada_akses, " +
                    "cek_rekening_tabungan, cek_asuransi, cek_anggota_komida, no_hp, status_uji, usr_crt, " +
                    "dt_crt, usr_upd, dt_upd FROM uk_anggota where status_uji='1' ORDER BY kode_uk asc"

            database_mdismo.rawQuery(query, null).use { res ->
                val drawableResId = R.drawable.women
                val defaultDrawable = ContextCompat.getDrawable(this, drawableResId)
                val drawable = defaultDrawable ?: getFallbackDrawable() // Replace with your fallback logic
                while (res.moveToNext()) {
                    val nama = res.getString(3)
                    val nik = res.getString(4)

                    val item = AnggotaChip(
                        id.toString(),
                        drawable,
                        nama,
                        nik
                    )
                    items.add(item)
                    id++
                }

            }
        } finally {
            database_mdismo?.close()
        }

        mChipsInput.setFilterableList(items)
    }
    private fun getFallbackDrawable(): Drawable {
        // Replace with your logic to obtain a fallback drawable
        // For example, you can use ContextCompat.getDrawable with another resource ID
        val fallbackResId = R.drawable.women
        return ContextCompat.getDrawable(this, fallbackResId)!!
    }
    private fun showDialogAnggotaLain() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_search_list)
        val dialogSearchEditText = dialog.findViewById<EditText>(R.id.dialogSearchEditText)
        val listView = dialog.findViewById<ListView>(R.id.listView)
        val btClose = dialog.findViewById<ImageButton>(R.id.bt_close)  // Change this line


        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val cursor = database_mdismo.rawQuery("select kode_uk,nik,nama  from uk_anggota where status_uji='1'", null)

        val anggotaDataList = ArrayList<String>()  // Define the list inside the function

        while (cursor.moveToNext()) {
            val nik = cursor.getString(cursor.getColumnIndex("nik"))
            val nama = cursor.getString(cursor.getColumnIndex("nama"))
            anggotaDataList.add("$nik | $nama")
        }

        // Close the cursor and the database when done
        cursor.close()
        database_mdismo.close()
        val adapter = CustomAdapterTambahKelompok(this, R.layout.item_people_contacts, anggotaDataList)
        listView.adapter = adapter
        dialogSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btClose.setOnClickListener {
            dialog.dismiss()
        }

        // Handle item click
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position).toString()

            val parts = selectedItem.split(" | ")
            val drawableResId = R.drawable.woman
            val defaultDrawable = ContextCompat.getDrawable(this, drawableResId)
            val drawable = defaultDrawable ?: getFallbackDrawable()
            if (parts.size == 2) {
                val nik = parts[0].trim()
                val name = parts[1].trim()
                mChipsInput.addChip(drawable, name,nik)
                // Now you can use 'nik' and 'name' as needed
                println("NIK: $nik")
                println("Name: $name")
            } else {
                // Handle the case where the input string doesn't match the expected format
                println("Invalid input string format")
            }
            // src_wakil_ketua_center.setText(selectedItem)
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun showAnggotaDialog(srcTextView: EditText) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_search_list)
        val dialogSearchEditText = dialog.findViewById<EditText>(R.id.dialogSearchEditText)
        val listView = dialog.findViewById<ListView>(R.id.listView)
        val btClose = dialog.findViewById<ImageButton>(R.id.bt_close)

        srcTextView.setText("")

        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val cursor = database_mdismo.rawQuery("select kode_uk,nik,nama  from uk_anggota where status_uji='1'", null)

        val anggotaDataList = ArrayList<String>()

        while (cursor.moveToNext()) {
            val nik = cursor.getString(cursor.getColumnIndex("nik"))
            val nama = cursor.getString(cursor.getColumnIndex("nama"))
            anggotaDataList.add("$nik | $nama")
        }

        cursor.close()
        database_mdismo.close()

        val adapter = CustomAdapterTambahKelompok(this, R.layout.item_people_contacts, anggotaDataList)
        listView.adapter = adapter

        dialogSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btClose.setOnClickListener {
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position).toString()
            srcTextView.setText(selectedItem)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDialogKetuaCenter() {
        showAnggotaDialog(src_ketua_center)
    }

    private fun showDialogWakilKetuaCenter() {
        showAnggotaDialog(src_wakil_ketua_center)
    }

    fun showTimePickerDialog(view: View) {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                findViewById<TextInputEditText>(R.id.etWaktuLwk).setText(selectedTime)
            },
            currentHour,
            currentMinute,
            true
        )

        timePickerDialog.show()
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
    private fun getPrefenceStaff() {
        nama_st = preferences.getString("nama","")!!
        nik_st  = preferences.getString("nik", "")!!
        dev_st  = preferences.getString("devid", "")!!
        jab_st  = preferences.getString("jabatan", "")!!
        cab_st  = preferences.getString("cabang", "")!!
        kode_cab_st = preferences.getString("kodecabang", "")!!
        hp_st = preferences.getString("hp", "")!!
    }

    private fun initToolbar() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbartambahkelompok)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Tambah Form Kelompok"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {

        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}

