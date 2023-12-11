package com.komida.co.id.mdisujikelayakan

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputLayout
import com.komida.co.id.mdisujikelayakan.utils.CustomDialogIntent
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListAnggotaRencanaBiaya : AppCompatActivity() {
    private lateinit var parent_view: View
    private lateinit var preferences: SharedPreferences
    private lateinit var kode_uk_keluarga_tv: TextView
    private lateinit var nik_ktp_keluarga_tv: TextView
    private lateinit var nama_anggota_keluarga_tv: TextView
    private lateinit var center_keluarga_tv: TextView

    private lateinit var kelompok_keluarga_tv: TextView
    //private lateinit var spinnerProductPembiayaan : Spinner
    private lateinit var spinnerPenggunaanPembiayaan : Spinner


    private lateinit var database_mdismo: SQLiteDatabase
    var DB_NAME = "mdismo.db"
    val productData = mutableListOf<String>()
    val rencanaData = mutableListOf<String>()
    val productDataList = mutableListOf<String>()
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
        lateinit var checkkodeuk_rencana: String
    }
    private lateinit var src_product_id: EditText
    private lateinit var src_purpose_code: EditText

    private lateinit var openDialogButton: Button
    private lateinit var openDialogButtonPenggunaan: Button
    private lateinit var etJumlahPembiayaan: EditText
    private lateinit var btn_simpanrencana: Button
    private lateinit var tilProductID: TextInputLayout
    private lateinit var tilPurposeCode: TextInputLayout

    private lateinit var tilJumlahPembiayaan: TextInputLayout
    private lateinit var tvKodeUKAgt: TextView
    private lateinit var tvKodeUkRencana: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_anggota_rencana_biaya)

        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        val toolbar: Toolbar = findViewById(R.id.toolbarlistanggotarencanabiaya)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Rencana Pembiayaan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        val detilIntent = this.intent
        getPreferenceAnggota(detilIntent)
        getPreferenceStaff()
        setupView()
        prepareitem()
        prepareSetData()

        openDialogButton.setOnClickListener {
            showDialog()
        }
        openDialogButtonPenggunaan.setOnClickListener {
            showDialogPenggunaan()
        }
        btn_simpanrencana.setOnClickListener {
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
    private fun simpanData() {
        val kode_uk_agt = kode_uk_dtl
        var kode_uk_rencana = tvKodeUkRencana.text.toString()
        val strProductId = src_product_id.text.toString()
        val strPurposeCode = src_purpose_code.text.toString()
        var strJumlahPenggunaan = etJumlahPembiayaan.text.toString()

        // Remove commas from the input for numeric values
        strJumlahPenggunaan = strJumlahPenggunaan.replace(",", "")

        // Clear previous error messages
        tilProductID.error = null
        tilPurposeCode.error = null
        tilJumlahPembiayaan.error = null

        if (strProductId.isEmpty()) {
            tilProductID.error = "Produk Pembiayaan Belum di Pilih"
            src_product_id.requestFocus()
            return
        }

        if (strPurposeCode.isEmpty()) {
            tilPurposeCode.error = "Rencana Pembiayaan Belum di Pilih"
            src_purpose_code.requestFocus()
            return
        }

        if (strJumlahPenggunaan.isEmpty()) {
            tilJumlahPembiayaan.error = "Jumlah Penggunaan Harus di Isi minimal 0"
            etJumlahPembiayaan.requestFocus()
            return
        }

        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val execRencana: String

        // If kode_uk_rencana is empty, generate a new kode_uk_rencana
        if (kode_uk_rencana.isNullOrBlank()) {
            kode_uk_rencana = generateRandom("ukren")
            execRencana = "INSERT INTO uk_rencana_biaya (" +
                    "kode_uk_rencana,kode_uk,productid,purposecode,jumlah_biaya," +
                    "usr_crt,dt_crt,usr_upd,dt_upd)" +
                    "values ('$kode_uk_rencana', '$kode_uk_agt', " +
                    "'$strProductId', '$strPurposeCode', '$strJumlahPenggunaan', " +
                    "'$nik_st', CURRENT_TIMESTAMP, '$nik_st', CURRENT_TIMESTAMP);"
        } else {
            execRencana = "UPDATE uk_rencana_biaya SET " +
                    "productid = '$strProductId', " +
                    "purposecode = '$strPurposeCode', " +
                    "jumlah_biaya = '$strJumlahPenggunaan', " +
                    "usr_upd = '$nik_st', " +
                    "dt_upd = CURRENT_TIMESTAMP " +
                    "WHERE kode_uk_rencana = '$kode_uk_rencana'  and kode_uk='$kode_uk_agt' "
        }

       println(execRencana)

        // Execute the SQL statement
       database_mdismo.execSQL(execRencana)

        // Close the database
        database_mdismo.close()

        // If the activity is not finishing, show a dialog
        if (!isFinishing) {
            val intent_list_idx = Intent(this, UjiKelayakanActivity::class.java)
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
    private fun showDialogPenggunaan() {
        val selectedProduct = src_product_id.text.toString().replace("'", "''")
        val productData = selectedProduct.split("|")
        src_purpose_code.setText("")
        var productid =""
        var productname=""
        if (productData.size == 2) {
            productid = productData[0].trim()
            productname = productData[1].trim()

        } else {
            println("Invalid input format")
        }

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_search_list)
        val dialogSearchEditText = dialog.findViewById<EditText>(R.id.dialogSearchEditText)
        val listView = dialog.findViewById<ListView>(R.id.listView)
        val btClose = dialog.findViewById<ImageButton>(R.id.bt_close)  // Change this line

        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val sqldata = "select comp_id, purposecode, purposename, sektorcode, productid, purposeenglish, sikp_sektor from loanpurpose where comp_id='KMD' and productid='$productid'"
        println(sqldata)
        val cursor = database_mdismo.rawQuery(sqldata, null)
        rencanaData.clear()
        while (cursor.moveToNext()) {
            val compid = cursor.getString(cursor.getColumnIndex("comp_id"))
            val purposecode = cursor.getString(cursor.getColumnIndex("purposecode"))
            val purposename = cursor.getString(cursor.getColumnIndex("purposename")) // Fixed column name
            val sektorcode = cursor.getString(cursor.getColumnIndex("sektorcode"))
            val productid = cursor.getString(cursor.getColumnIndex("productid"))
            val purposeenglish = cursor.getString(cursor.getColumnIndex("purposeenglish"))
            val sikp_sektor = cursor.getString(cursor.getColumnIndex("sikp_sektor"))
            rencanaData.add(purposecode + " | " + purposename)
        }

        // Close the cursor and the database when done
        cursor.close()
        database_mdismo.close()


        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, rencanaData)
        listView.adapter = adapter

        // Filter the list based on the search query
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
            src_purpose_code.setText(selectedItem)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_search_list)
        val dialogSearchEditText = dialog.findViewById<EditText>(R.id.dialogSearchEditText)
        val listView = dialog.findViewById<ListView>(R.id.listView)
        val btClose = dialog.findViewById<ImageButton>(R.id.bt_close)  // Change this line

        src_purpose_code.setText("")

        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val cursor = database_mdismo.rawQuery("SELECT comp_id,productid, productname,curr_id,startdate,enddate,periode,status FROM mdproduct", null)

        val productDataList = ArrayList<String>()  // Define the list inside the function

        while (cursor.moveToNext()) {
            val productid = cursor.getString(cursor.getColumnIndex("productid"))
            val productname = cursor.getString(cursor.getColumnIndex("productname"))
            productDataList.add("$productid | $productname")
        }

        // Close the cursor and the database when done
        cursor.close()
        database_mdismo.close()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, productDataList)
        listView.adapter = adapter

        // Filter the list based on the search query
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
            src_product_id.setText(selectedItem)
            dialog.dismiss()
        }

        dialog.show()
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
    @SuppressLint("Range")
    private fun setupView() {
        kode_uk_keluarga_tv = findViewById(R.id.kode_uk_keluarga_tv)
        nik_ktp_keluarga_tv = findViewById(R.id.nik_ktp_keluarga_tv)
        nama_anggota_keluarga_tv = findViewById(R.id.nama_anggota_keluarga_tv)
        center_keluarga_tv = findViewById(R.id.center_keluarga_tv)
        kelompok_keluarga_tv = findViewById(R.id.kelompok_keluarga_tv)
        src_product_id = findViewById(R.id.src_product_id)
        openDialogButton = findViewById(R.id.openDialogButton)
        src_purpose_code = findViewById(R.id.src_purpose_code)
        openDialogButtonPenggunaan = findViewById(R.id.openDialogButtonPenggunaan)
        etJumlahPembiayaan = findViewById(R.id.etJumlahPembiayaan)
        btn_simpanrencana= findViewById(R.id.btn_simpanrencana)
        tilProductID= findViewById(R.id.tilProductID)
        tilPurposeCode = findViewById(R.id.tilPurposeCode)
        tilJumlahPembiayaan = findViewById(R.id.tilJumlahPembiayaan)

        tvKodeUKAgt = findViewById(R.id.tvKodeUKAgt)
        tvKodeUkRencana = findViewById(R.id.tvKodeUkRencana)

        checkkodeuk_rencana = (checkkodeuk_rencana() ?: "").toString()

        etJumlahPembiayaan.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                etJumlahPembiayaan.removeTextChangedListener(this)

                try {
                    var originalString: String = etJumlahPembiayaan.text.toString()
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
                    etJumlahPembiayaan.setText(formattedString)
                    etJumlahPembiayaan.setSelection(etJumlahPembiayaan.text.length)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                etJumlahPembiayaan.addTextChangedListener(this)

            }
        })

    }
    fun checkkodeuk_rencana(): String? {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        var total: String? = null
        val res = database_mdismo.rawQuery(
            "select kode_uk_rencana  " +
                    "from uk_rencana_biaya where kode_uk='$kode_uk_dtl'  ",
            null
        )
        while (res.moveToNext()) {
            total = res.getString(0)
        }
        database_mdismo.close()
        return total
    }
    fun prepareitem(){
        kode_uk_keluarga_tv.text = kode_uk_dtl
        nik_ktp_keluarga_tv.text = nama_anggota_dtl
        nama_anggota_keluarga_tv.text = nik_ktp_dtl
        center_keluarga_tv.text = center_dtl
        kelompok_keluarga_tv.text = kelompok_dtl
        tvKodeUkRencana.text= checkkodeuk_rencana
    }
    private fun prepareSetData() {
        tvKodeUKAgt.text = kode_uk_dtl
        if (!checkkodeuk_rencana.isNullOrEmpty()) {
            getDataRencana(checkkodeuk_rencana)

        }
        // etKodeUkPendapatan.text = kode_uk_pendapatan

    }

    private fun getDataRencana(checkkodeukRencana: String) {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)

        val query = "SELECT kode_uk_rencana, kode_uk, productid, purposecode, jumlah_biaya, usr_crt, dt_crt, usr_upd, dt_upd " +
                "FROM uk_rencana_biaya where kode_uk='$kode_uk_dtl' and kode_uk_rencana='$checkkodeukRencana'"

        val res = database_mdismo.rawQuery(query, null)

        // Initialize variables to store the retrieved data
        var kode_uk_rencana: String? = null
        var kode_uk: String? = null
        var productid: String? = null
        var purposecode: String? = null
        var jumlah_biaya: String? = null

        // Check if there's any result
        if (res.moveToFirst()) {
            kode_uk_rencana = res.getString(res.getColumnIndex("kode_uk_rencana"))
            kode_uk = res.getString(res.getColumnIndex("kode_uk"))
            productid = res.getString(res.getColumnIndex("productid"))
            purposecode = res.getString(res.getColumnIndex("purposecode"))
            jumlah_biaya = res.getString(res.getColumnIndex("jumlah_biaya"))
        }

        // Set the retrieved data to your UI components
        src_product_id.text =  Editable.Factory.getInstance().newEditable(productid)
        src_purpose_code.text = Editable.Factory.getInstance().newEditable(purposecode)
        etJumlahPembiayaan.text =  Editable.Factory.getInstance().newEditable(jumlah_biaya)

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
}