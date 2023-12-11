package com.komida.co.id.mdisujikelayakan

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.komida.co.id.mdisujikelayakan.adapter.AdapterAnggotaKeluarga
import com.komida.co.id.mdisujikelayakan.adapter.AdapterInfoProductSaving
import com.komida.co.id.mdisujikelayakan.model.ModelListAgtKeluarga
import com.komida.co.id.mdisujikelayakan.model.ModelListProductSaving
import com.komida.co.id.mdisujikelayakan.utils.CustomDialogIntent
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ListAnggotaProdukBiaya : AppCompatActivity() {
    private lateinit var parent_view: View
    private lateinit var preferences: SharedPreferences
    private lateinit var kode_uk_keluarga_tv: TextView
    private lateinit var nik_ktp_keluarga_tv: TextView
    private lateinit var nama_anggota_keluarga_tv: TextView
    private lateinit var center_keluarga_tv: TextView

    private lateinit var kelompok_keluarga_tv: TextView
    //private lateinit var spinnerProductPembiayaan : Spinner
    private lateinit var spinnerPenggunaanPembiayaan : Spinner
    private lateinit var tv_total_simpanan: TextView


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
    val itemsList: ArrayList<ModelListProductSaving> = ArrayList()
    private lateinit var AdapterInfoProductSaving: AdapterInfoProductSaving
    private var recyclerView: RecyclerView? = null

    private var nestedSV: NestedScrollView? = null
    var doubleBackToExitPressedOnces = false
    var currentOffset = 0
    var currentOffsetCari:Int = 0
    var LIMIT = 20
    var LIMITCARI:Int = 20
    var total_cari:Int = 0
    var loadMoreButton: Button? = null
    var mSearchView: SearchView? = null
    var loadingProgressBar: ProgressBar? = null
    var items: List<ModelListProductSaving>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_anggota_produk_biaya)

        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        val toolbar: Toolbar = findViewById(R.id.toolbarlistprodukbiaya)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Produk Pembiayaan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val detilIntent = this.intent
        getPreferenceAnggota(detilIntent)
        getPreferenceStaff()
        setupView()
        prepareitem()


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        AdapterInfoProductSaving = AdapterInfoProductSaving(itemsList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = AdapterInfoProductSaving

        loadInitialData()


    }

    private fun loadInitialData() {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)

        /*
        val query = "SELECT a.productid, a.productname, '0' as deposit, ''as flag, '' as kode_uk, '' as kode_ukprodsav " +
                "FROM productsavingbranch a " +
                "LEFT JOIN uk_productsavingbranch b ON a.productid = b.productid " +
                "WHERE a.branchcode = '080' " +
                "ORDER BY a.productid ASC"

         */
        val query = "SELECT a.productid, a.productname, " +
                "CASE " +
                "  WHEN b.deposit IS NULL THEN '0' " +
                "  WHEN b.deposit > 0 THEN b.deposit " +
                "  ELSE '0' " +
                "END AS deposit, " +
                "CASE " +
                "  WHEN b.flag IS NULL THEN '' " +
                "  ELSE b.flag " +
                "END AS flag, " +
                "CASE " +
                "  WHEN b.kode_uk IS NULL THEN '$kode_uk_dtl' " +
                "  ELSE b.kode_uk " +
                "END AS kode_uk, " +
                "CASE " +
                "  WHEN b.kode_ukprodsav IS NULL THEN '' " +
                "  ELSE b.kode_ukprodsav " +
                "END AS kode_ukprodsav " +
                "FROM productsavingbranch a " +
                "LEFT JOIN uk_productsavingbranch b ON a.productid = b.productid and b.kode_uk='$kode_uk_dtl' " +
                "WHERE a.branchcode = '080' " +
                "ORDER BY a.productid ASC"
        println(query)

        try {
            val res = database_mdismo.rawQuery(query, null)

            while (res.moveToNext()) {
                val productid = res.getString(res.getColumnIndex("productid"))
                val productname = res.getString(res.getColumnIndex("productname"))
                val deposit = getDecimalFormattedString(res.getString(res.getColumnIndex("deposit")))
                val flag = res.getString(res.getColumnIndex("flag"))
                val kode_uk = res.getString(res.getColumnIndex("kode_uk"))
                val kode_ukprodsav = res.getString(res.getColumnIndex("kode_ukprodsav"))

                val item = ModelListProductSaving(
                    productid,
                    productname,
                    deposit,
                    flag,
                    kode_uk,
                    kode_ukprodsav
                )
                itemsList.add(item)
            }

            res.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            database_mdismo.close()
        }

        AdapterInfoProductSaving.notifyDataSetChanged()
        loadMoreButton?.visibility = View.VISIBLE

        AdapterInfoProductSaving.setOnItemClickListener(object : AdapterInfoProductSaving.OnItemClickListener {
            override fun onItemClick(view: View?, obj: ModelListProductSaving?, position: Int) {
                val context = view?.context
                panggilDialogSaving(context, obj, view)
            }
        })

        AdapterInfoProductSaving.setOnItemLongClickListener(object : AdapterInfoProductSaving.OnItemLongClickListener {
            override fun onItemLongClick(view: View?, obj: ModelListProductSaving?, position: Int) {
                val context = view?.context
                // Handle long item click
            }
        })
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
    private fun panggilDialogSaving(context: Context?, obj: ModelListProductSaving?, view: View?) {
        if (context != null) {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_input_product_saving)

            val bt_close = dialog.findViewById<ImageButton>(R.id.bt_close)
            val btn_simpansaving = dialog.findViewById<Button>(R.id.btn_simpansaving)

            val tvKodeUKAgt = dialog.findViewById<TextView>(R.id.tvKodeUKAgt)
            val tvKodeUkProdSav = dialog.findViewById<TextView>(R.id.tvKodeUkProdSav)
            val tilProductIDSaving = dialog.findViewById<TextInputLayout>(R.id.tilProductIDSaving)
            val src_productsaving_id = dialog.findViewById<TextInputEditText>(R.id.src_productsaving_id)
            val tilProductNameSaving = dialog.findViewById<TextInputLayout>(R.id.tilProductNameSaving)
            val src_productsaving_name = dialog.findViewById<TextInputEditText>(R.id.src_productsaving_name)
            val tilDepositSaving = dialog.findViewById<TextInputLayout>(R.id.tilDepositSaving)
            val etDepositSaving = dialog.findViewById<TextInputEditText>(R.id.etDepositSaving)

            // Set default values if obj properties are null
            tvKodeUKAgt.text = obj?.kode_uk ?: ""
            tvKodeUkProdSav.text = obj?.kode_ukprodsav ?: ""
            src_productsaving_id.text = (obj?.product_id ?: "").toEditable()
            src_productsaving_name.text = (obj?.product_name ?: "").toEditable()

            // Set deposit value with formatting
            val depositText = obj?.deposit?.replace(",", "") ?: "0"
            val formattedDeposit = formatCurrency(depositText)
            etDepositSaving.text = formattedDeposit.toEditable()
            etDepositSaving.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    etDepositSaving.removeTextChangedListener(this)

                    try {
                        var originalString = s.toString()
                        val longval: Long
                        if (originalString.isNullOrEmpty()) {
                            originalString = "0"
                        }
                        if (originalString.contains(",")) {
                            originalString = originalString.replace(",", "")
                        }

                        longval = originalString.toLong()

                        val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
                        formatter.applyPattern("#,###,###,###")
                        val formattedString: String = formatter.format(longval)

                        // Set the formatted text to the EditText
                        etDepositSaving.setText(formattedString)

                        // Move the cursor to the end of the text
                        etDepositSaving.setSelection(formattedString.length)
                    } catch (nfe: NumberFormatException) {
                        nfe.printStackTrace()
                    }

                    etDepositSaving.addTextChangedListener(this)
                }
            })



            bt_close.setOnClickListener {
                dialog.dismiss()
            }
            btn_simpansaving.setOnClickListener {
                val kode_uk_agt = tvKodeUKAgt.text.toString()
                var kode_ukprodsav = tvKodeUkProdSav.text.toString()
                val productsavingid = src_productsaving_id.text.toString()
                val productsavingname = src_productsaving_name.text.toString()
                val depositsaving = etDepositSaving.text.toString().replace(",", "")

                database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)

                val execSaving: String

// If kode_uk_rencana is empty, generate a new kode_uk_rencana
                if (kode_ukprodsav.isNullOrBlank()) {
                    kode_ukprodsav = generateRandom("ukpsav")
                    execSaving = "INSERT INTO uk_productsavingbranch (" +
                            "kode_ukprodsav,kode_uk,productid,productname,deposit,flag," +
                            "usr_crt,dt_crt,usr_upd,dt_upd)" +
                            "values ('$kode_ukprodsav', '$kode_uk_agt', " +
                            "'$productsavingid', '$productsavingname', '$depositsaving','1'," +
                            "'$nik_st', CURRENT_TIMESTAMP, '$nik_st', CURRENT_TIMESTAMP);"
                } else {
                    execSaving = "UPDATE uk_productsavingbranch SET " +
                            "productid = '$productsavingid', " +
                            "productname = '$productsavingname', " +
                            "deposit = '$depositsaving', " +
                            "usr_upd = '$nik_st', " +
                            "dt_upd = CURRENT_TIMESTAMP " +
                            "WHERE kode_ukprodsav = '$kode_ukprodsav' " +
                            "AND kode_uk = '$kode_uk_agt' and productid='$productsavingid';"

                }

                println(execSaving)

                database_mdismo.execSQL(execSaving)
                database_mdismo.close()
                if (!isFinishing) {
                    val intent_list_idx = Intent(this, ListAnggotaProdukBiaya::class.java)
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


                    startActivity(intent_list_idx)
                    dialog.dismiss()

                }

            }

            dialog.show()
        }
    }
    fun generateRandom(prefix: String): String {
        val length = 14
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val randomString = RandomStringGenerator().generateRandomString(length)
        return "$prefix-$currentDate-$randomString"
    }
    // Helper function to format currency with commas
    private fun formatCurrency(amount: String): String {
        try {
            val longval: Long = amount.toLong()
            val formatter = DecimalFormat("#,###,###,###")
            return formatter.format(longval)
        } catch (nfe: NumberFormatException) {
            nfe.printStackTrace()
        }
        return "0"
    }

    // Extension function to convert String to Editable
    fun String.toEditable(): Editable {
        return Editable.Factory.getInstance().newEditable(this)
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
        kode_uk_keluarga_tv = findViewById(R.id.kode_uk_keluarga_tv)
        nik_ktp_keluarga_tv = findViewById(R.id.nik_ktp_keluarga_tv)
        nama_anggota_keluarga_tv = findViewById(R.id.nama_anggota_keluarga_tv)
        center_keluarga_tv = findViewById(R.id.center_keluarga_tv)
        kelompok_keluarga_tv = findViewById(R.id.kelompok_keluarga_tv)
        tv_total_simpanan = findViewById(R.id.tv_total_simpanan)
        var hitung_total_simpanan = (hitung_total_simpanan() ?: 0).toString()

        tv_total_simpanan?.text = getDecimalFormattedString(hitung_total_simpanan)

    }

    private fun hitung_total_simpanan(): Int? {
        database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        var total: Int? = null
        val res = database_mdismo.rawQuery(
            "select COALESCE(sum(b.deposit),0) "+
                    "FROM productsavingbranch a " +
                    "LEFT JOIN uk_productsavingbranch b ON a.productid = b.productid " +
                    "WHERE a.branchcode = '080' and kode_uk='$kode_uk_dtl' " +
                    "ORDER BY a.productid ASC",
            null
        )
        while (res.moveToNext()) {
            total = res.getInt(0)
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