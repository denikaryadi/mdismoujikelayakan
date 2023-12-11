package com.komida.co.id.mdisujikelayakan

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.komida.co.id.mdisujikelayakan.adapter.AdapterInfoPertanyaanTambahan
import com.komida.co.id.mdisujikelayakan.model.ModelListPertanyaanTambahan
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PpiTambahanFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var parent_view: View
    private lateinit var parentLayout: View
    private lateinit var preferences: SharedPreferences
    private lateinit var kode_uk_keluarga_tv: TextView
    private lateinit var nik_ktp_keluarga_tv: TextView
    private lateinit var nama_anggota_keluarga_tv: TextView
    private lateinit var center_keluarga_tv: TextView
    private lateinit var kelompok_keluarga_tv: TextView
    val itemsList: ArrayList<ModelListPertanyaanTambahan> = ArrayList()
    private lateinit var AdapterInfoPertanyaanTambahan: AdapterInfoPertanyaanTambahan

    val itemsListJawaban: ArrayList<ModelListPertanyaanTambahan> = ArrayList()
    private var recyclerViewFormPPITambahan: RecyclerView? = null

    private var nestedSV: NestedScrollView? = null
    var doubleBackToExitPressedOnce = false
    private lateinit var database_mdismo: SQLiteDatabase
    var DB_NAME = "mdismo.db"
    var currentOffset = 0
    var currentOffsetCari: Int = 0
    var LIMIT = 20
    var LIMITCARI: Int = 20
    var total_cari: Int = 0
    var loadMoreButton: Button? = null
    var mSearchView: SearchView? = null
    var loadingProgressBar: ProgressBar? = null
    private lateinit var btn_simpandatappitambahan: Button
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

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PpiTambahanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_ppi_tambahan, container, false)
        preferences = requireActivity().getSharedPreferences("id.co.komida.mdismoujikelayakan",
            Context.MODE_PRIVATE
        )

        // Initialize your views here
        parentLayout = rootView.findViewById(R.id.parentLayout)
        val detilIntent = activity?.intent // Access activity's intent
        if (detilIntent != null) {
            getPreferenceAnggota(detilIntent)
        }
        getPreferenceStaff()
        setupView(rootView)
        prepareitem()

        val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerViewFormPPITambahan)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        // Initialize the adapter here
        AdapterInfoPertanyaanTambahan = AdapterInfoPertanyaanTambahan(itemsList, parentLayout)
        recyclerView.adapter = AdapterInfoPertanyaanTambahan



        loadInitialData(AdapterInfoPertanyaanTambahan)

        btn_simpandatappitambahan.setOnClickListener {
            simpanData(rootView)
        }
        return rootView
    }

    private fun simpanData(rootView: View) {
        val inflater = LayoutInflater.from(requireContext())
        val itemView = inflater.inflate(R.layout.item_data_ppi_pertanyaan_tambahan, null, false)

        val itemCount = itemsList.count { it.selectedAnswerIndex in 0 until it.pg.size }

        if (itemCount != 14) {
            gagal_app(rootView, "Pertanyaan PPI Tambahan belum diisi semua")
        } else {
            database_mdismo = requireContext().openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
            val kode_uk_agt = kode_uk_dtl
            var no_urutan = 1
            val tv_jml_pr : TextView = parentLayout.findViewById(R.id.tv_jml_pr)
            val tv_jml_pr_sekolah : TextView = parentLayout.findViewById(R.id.tv_jml_pr_sekolah)
            val tv_jml_lk : TextView = parentLayout.findViewById(R.id.tv_jml_lk)
            val tv_jml_lk_sekolah : TextView = parentLayout.findViewById(R.id.tv_jml_lk_sekolah)
            val tv_jml_total : TextView = parentLayout.findViewById(R.id.tv_jml_total)
            val tv_jml_total_sekolah : TextView = parentLayout.findViewById(R.id.tv_jml_total_sekolah)
            val tv_id_jawaban: TextView = parentLayout.findViewById(R.id.tv_id_jawaban)
            val tv_id_soal: TextView = parentLayout.findViewById(R.id.tv_id_soal)
            val tv_pg_answer: TextView = parentLayout.findViewById(R.id.tv_pg_answer)


            val deleteUkAgt = "delete from  uk_ppi_anggota where kode_uk='$kode_uk_agt' and id_soal between '11' and '24'"
           database_mdismo.execSQL(deleteUkAgt)

            for (item in itemsList) {
                val selectedAnswerIndex = item.selectedAnswerIndex
                if (selectedAnswerIndex in 0 until item.pg.size) {
                    var  str_tv_jml_pr="0"
                    var str_tv_jml_pr_sekolah="0"
                    var str_tv_jml_lk="0"
                    var str_tv_jml_lk_sekolah="0"
                    var str_tv_jml_total="0"
                    var str_tv_jml_total_sekolah="0"
                    val input = item.pg[selectedAnswerIndex].split("^")
                    if (input.size >= 4) {
                        val (pg, jawaban, poin, nourutan) = input
                        val kode_uk_ppi = generateRandom("ukagtppi")
                        if (nourutan == "14") {
                            val pg = input[0]
                            str_tv_jml_pr = tv_jml_pr?.text?.toString() ?: "0"
                            str_tv_jml_pr_sekolah = tv_jml_pr_sekolah?.text?.toString() ?: "0"
                            str_tv_jml_lk = tv_jml_lk?.text?.toString() ?: "0"
                            str_tv_jml_lk_sekolah = tv_jml_lk_sekolah?.text?.toString() ?: "0"
                            str_tv_jml_total = tv_jml_total?.text?.toString() ?: "0"
                            str_tv_jml_total_sekolah = tv_jml_total_sekolah?.text?.toString() ?: "0"

                        }
                        val execUkAnggotaKeluarga = " insert into uk_ppi_anggota (kode_uk_ppi,kode_uk,id_soal,id_jawaban,score,total_score,jml_pr,jml_pr_sklh,jml_lk,jml_lk_sklh,tot_jml,tot_jml_sklh,status,usr_crt,dt_crt,usr_upd,dt_upd ) " +
                                " values ('$kode_uk_ppi','$kode_uk_agt','$nourutan','$pg','0','0','$str_tv_jml_pr','$str_tv_jml_pr_sekolah','$str_tv_jml_lk','$str_tv_jml_lk_sekolah','$str_tv_jml_total','$str_tv_jml_total_sekolah',1,'$nik_st',CURRENT_TIMESTAMP,'$nik_st',CURRENT_TIMESTAMP )"
                       //println(execUkAnggotaKeluarga)
                       database_mdismo.execSQL(execUkAnggotaKeluarga)

                        no_urutan++
                    }
                }
            }
            snackBarWithAction("Data Berhasil Disimpan")
        }
    }
    private fun snackBarWithAction(text: String) {
        val snackbar = Snackbar.make(parentLayout, text, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green_400))

        val textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))


        snackbar.show()
    }
    fun generateRandom(prefix: String): String {
        val length = 10 // Specify the desired length of the random string
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val randomString = RandomStringGenerator().generateRandomString(length)
        val resultString = "$prefix-$currentDate-$randomString"

        return resultString
    }
    private fun gagal_app(rootView:View,ketnih: String) {
        val teksket: TextView
        val btnUpdate: Button
        val viewGroup = rootView.findViewById<ViewGroup>(android.R.id.content)
        val builder = AlertDialog.Builder(requireContext())
        val view1 = LayoutInflater.from(requireContext()).inflate(R.layout.gagal_app, viewGroup, false)
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

    private fun loadInitialData(AdapterInfoPertanyaanTambahan: AdapterInfoPertanyaanTambahan) {
        // Initialize the database
        database_mdismo = requireContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null)

        val query = "select id_soal,urutan,tipe_pertanyaan,pertanyaan,status from ppi_soal where tipe_pertanyaan='2' order by urutan asc "

        val res = this.database_mdismo.rawQuery(query, null)
        var no_urutan = 1 // Initialize the question numbering
/* check dulu uk_ppi_anggota*/
        val querycek = "SELECT COUNT(*) as jml FROM uk_ppi_anggota WHERE kode_uk='$kode_uk_dtl' and id_soal between '11' and '24'"
        val rescek = this.database_mdismo.rawQuery(querycek, null)
        val total: Int? = rescek.use { it.takeIf { it.moveToNext() }?.getInt(0) }
        rescek?.close()
        while (res.moveToNext()) {
            val id_soal = res.getString(res.getColumnIndex("id_soal"))
            val urutan = res.getString(res.getColumnIndex("urutan"))
            val pertanyaan = res.getString(res.getColumnIndex("pertanyaan"))
            val tipe_pertanyaan = res.getString(res.getColumnIndex("tipe_pertanyaan"))
            val status = res.getString(res.getColumnIndex("status"))

            var queryjawaban=""
            if (total?.compareTo(0) ?: 0 > 0) {
                queryjawaban = "" +
                        " select " +
                        " a.id_jawaban,a.id_soal,a.urutan_jawaban,a.pg,a.isi_jawaban,a.poin,a.score,a.status, " +
                        " b.jml_pr,b.jml_pr_sklh,b.jml_lk,b.jml_lk_sklh,b.tot_jml,b.tot_jml_sklh,"+
                        " CASE WHEN  b.id_jawaban = a.pg THEN 'checked' ELSE 'unchecked' END checked " +
                        " from ppi_jawaban a left join uk_ppi_anggota b on a.id_soal=b.id_soal " +
                        " WHERE a.id_soal = '$urutan' and kode_uk='$kode_uk_dtl' " +
                        " order by urutan_jawaban asc "

            }
            else
            {
                queryjawaban = "select id_jawaban,id_soal,urutan_jawaban,pg,isi_jawaban,poin,score,status,'' as checked, " +
                        " '' as jml_pr,'' as jml_pr_sklh,'' as jml_lk,'' as jml_lk_sklh,'' as tot_jml,'' as tot_jml_sklh"+
                        " from ppi_jawaban " +
                        " WHERE id_soal = '$id_soal'" +
                        " order by urutan_jawaban asc "
            }


            val resa = this.database_mdismo.rawQuery(queryjawaban, null)
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
                flag_view = "edit"
            )

            itemsList.add(item)
            no_urutan++
        }

        // Close the database cursor and database
        res.close()
        try {
            this.database_mdismo.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Notify the adapter that the data has changed
        AdapterInfoPertanyaanTambahan.notifyDataSetChanged()
        AdapterInfoPertanyaanTambahan.setOnItemClickListener(object : AdapterInfoPertanyaanTambahan.OnItemClickListener {
            override fun onItemClick(view: View?, obj: ModelListPertanyaanTambahan?, position: Int) {
                val context = view?.context

                var no_urutan = obj?.urutan
                var pg = obj?.pg

                if(no_urutan=="14") {

                    panggilDialogKeluarga(context, obj, view)
                }
            }
        })
    }

    private fun panggilDialogKeluarga(context: Context?, obj: ModelListPertanyaanTambahan?, view: View?) {
        if (context != null) {
            val tv_jml_pr : TextView = parentLayout.findViewById(R.id.tv_jml_pr)
            val tv_jml_pr_sekolah : TextView = parentLayout.findViewById(R.id.tv_jml_pr_sekolah)
            val tv_jml_lk : TextView = parentLayout.findViewById(R.id.tv_jml_lk)
            val tv_jml_lk_sekolah : TextView = parentLayout.findViewById(R.id.tv_jml_lk_sekolah)
            val tv_jml_total : TextView = parentLayout.findViewById(R.id.tv_jml_total)
            val tv_jml_total_sekolah : TextView = parentLayout.findViewById(R.id.tv_jml_total_sekolah)
            val tv_id_jawaban: TextView = parentLayout.findViewById(R.id.tv_id_jawaban)
            val tv_id_soal: TextView = parentLayout.findViewById(R.id.tv_id_soal)
            val tv_pg_answer: TextView = parentLayout.findViewById(R.id.tv_pg_answer)





            val str_tv_jml_pr = tv_jml_pr?.text?.toString()
            val str_tv_jml_pr_sekolah = tv_jml_pr_sekolah?.text?.toString()
            val str_tv_jml_lk = tv_jml_lk?.text?.toString()
            val str_tv_jml_lk_sekolah = tv_jml_lk_sekolah?.text?.toString()
            val str_tv_jml_total = tv_jml_total?.text?.toString()
            val str_tv_jml_total_sekolah = tv_jml_total_sekolah?.text?.toString()
            val str_tv_pg_answer = tv_pg_answer?.text?.toString()

            //println("TEST"+str_tv_pg_answer)

            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_input_keluarga_ppi)
            val btClose = dialog.findViewById<ImageButton>(R.id.bt_close)
            val btn_simpankeluarga = dialog.findViewById<Button>(R.id.btn_simpankeluarga)

            val tv_kode_uk_agt = dialog.findViewById<TextView>(R.id.tv_kode_uk_agt)
            val tilPerempuan = dialog.findViewById<TextInputLayout>(R.id.tilPerempuan)
            val jumlah_pr = dialog.findViewById<TextInputEditText>(R.id.jumlah_pr)
            val jml_pr_sekolah = dialog.findViewById<TextInputEditText>(R.id.jml_pr_sekolah)

            val tilLakiLaki = dialog.findViewById<TextInputLayout>(R.id.tilLakiLaki)
            val jml_lk = dialog.findViewById<TextInputEditText>(R.id.jml_lk)
            val jml_lk_sekolah = dialog.findViewById<TextInputEditText>(R.id.jml_lk_sekolah)

            val tilTotal = dialog.findViewById<TextInputLayout>(R.id.tilTotal)
            val total_pr_lk = dialog.findViewById<TextInputEditText>(R.id.total_pr_lk)
            val total_pr_lk_sekolah = dialog.findViewById<TextInputEditText>(R.id.total_pr_lk_sekolah)

            jumlah_pr.text = Editable.Factory.getInstance().newEditable(str_tv_jml_pr)
            jml_pr_sekolah.text = Editable.Factory.getInstance().newEditable(str_tv_jml_pr_sekolah)
            jml_lk.text = Editable.Factory.getInstance().newEditable(str_tv_jml_lk)
            jml_lk_sekolah.text = Editable.Factory.getInstance().newEditable(str_tv_jml_lk_sekolah)
            total_pr_lk.text = Editable.Factory.getInstance().newEditable(str_tv_jml_total)
            total_pr_lk_sekolah.text = Editable.Factory.getInstance().newEditable(str_tv_jml_total_sekolah)

            fun calculateTotal() {
                // Parse the values from EditTexts and handle potential empty input
                val jumlahPrText = jumlah_pr.text.toString().replace(",", "")
                val jmlLkText = jml_lk.text.toString().replace(",", "")

                val jumlahPrValue = if (jumlahPrText.isNotEmpty()) jumlahPrText.toLong() else 0L
                val jmlLkValue = if (jmlLkText.isNotEmpty()) jmlLkText.toLong() else 0L

                // Calculate the total
                val total = jumlahPrValue + jmlLkValue

                // Format the total with commas and set it to the total_pr_lk TextView
                val formatter = DecimalFormat("#,###,###,###")
                total_pr_lk.text = Editable.Factory.getInstance().newEditable(formatter.format(total))
            }
            fun calculateTotalSekolah() {
                // Parse the values from EditTexts and handle potential empty input
                val jumlahPrText = jml_pr_sekolah.text.toString().replace(",", "")
                val jmlLkText = jml_lk_sekolah.text.toString().replace(",", "")

                val jumlahPrValue = if (jumlahPrText.isNotEmpty()) jumlahPrText.toLong() else 0L
                val jmlLkValue = if (jmlLkText.isNotEmpty()) jmlLkText.toLong() else 0L

                // Calculate the total
                val total = jumlahPrValue + jmlLkValue

                // Format the total with commas and set it to the total_pr_lk TextView
                val formatter = DecimalFormat("#,###,###,###")
                total_pr_lk_sekolah.text = Editable.Factory.getInstance().newEditable(formatter.format(total))
            }

            jumlah_pr.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    jumlah_pr.removeTextChangedListener(this)

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
                        jumlah_pr.setText(formattedString)
                        calculateTotal()
                        // Move the cursor to the end of the text
                        jumlah_pr.setSelection(formattedString.length)
                    } catch (nfe: NumberFormatException) {
                        nfe.printStackTrace()
                    }

                    jumlah_pr.addTextChangedListener(this)
                }


            })

            jml_pr_sekolah.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    jml_pr_sekolah.removeTextChangedListener(this)

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
                        jml_pr_sekolah.setText(formattedString)
                        calculateTotalSekolah()
                        // Move the cursor to the end of the text
                        jml_pr_sekolah.setSelection(formattedString.length)
                    } catch (nfe: NumberFormatException) {
                        nfe.printStackTrace()
                    }

                    jml_pr_sekolah.addTextChangedListener(this)
                }
            })

            jml_lk.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    jml_lk.removeTextChangedListener(this)

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
                        jml_lk.setText(formattedString)
                        calculateTotal()
                        // Move the cursor to the end of the text
                        jml_lk.setSelection(formattedString.length)
                    } catch (nfe: NumberFormatException) {
                        nfe.printStackTrace()
                    }

                    jml_lk.addTextChangedListener(this)
                }
            })

            jml_lk_sekolah.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    jml_lk_sekolah.removeTextChangedListener(this)

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
                        jml_lk_sekolah.setText(formattedString)
                        calculateTotalSekolah()
                        // Move the cursor to the end of the text
                        jml_lk_sekolah.setSelection(formattedString.length)
                    } catch (nfe: NumberFormatException) {
                        nfe.printStackTrace()
                    }

                    jml_lk_sekolah.addTextChangedListener(this)
                }
            })

            btClose.setOnClickListener {
                dialog.dismiss()
            }
            btn_simpankeluarga.setOnClickListener {
                tv_jml_pr?.text = jumlah_pr.text.toString()
                tv_jml_pr_sekolah?.text = jml_pr_sekolah.text.toString()
                tv_jml_lk?.text = jml_lk.text.toString()
                tv_jml_lk_sekolah?.text = jml_lk_sekolah.text.toString()
                tv_jml_total?.text = total_pr_lk.text.toString()
                tv_jml_total_sekolah?.text = total_pr_lk_sekolah.text.toString()
                tv_pg_answer?.text=str_tv_pg_answer
                dialog.dismiss()
            }

            // Initialize views and handle interactions within the dialog
            // For example, if you have buttons or input fields in the dialog, you can set up their listeners here.

            if(str_tv_pg_answer=="A") {
                dialog.show()
            }
        }
    }
    private fun prepareitem() {
        kode_uk_keluarga_tv.text = kode_uk_dtl
        nik_ktp_keluarga_tv.text = nama_anggota_dtl
        nama_anggota_keluarga_tv.text = nik_ktp_dtl
        center_keluarga_tv.text = center_dtl
        kelompok_keluarga_tv.text = kelompok_dtl
    }

    private fun setupView(rootView: View) {
        kode_uk_keluarga_tv = rootView.findViewById(R.id.kode_uk_keluarga_tv)
        nik_ktp_keluarga_tv = rootView.findViewById(R.id.nik_ktp_keluarga_tv)
        nama_anggota_keluarga_tv = rootView.findViewById(R.id.nama_anggota_keluarga_tv)
        center_keluarga_tv = rootView.findViewById(R.id.center_keluarga_tv)
        kelompok_keluarga_tv = rootView.findViewById(R.id.kelompok_keluarga_tv)

        btn_simpandatappitambahan =rootView.findViewById(R.id.btn_simpandatappitambahan)
    }

    private fun getPreferenceStaff() {
        nama_st = this.preferences.getString("nama", "")!!
        nik_st = this.preferences.getString("nik", "")!!
        dev_st = this.preferences.getString("devid", "")!!
        jab_st = this.preferences.getString("jabatan", "")!!
        cab_st = this.preferences.getString("cabang", "")!!
        kode_cab_st = this.preferences.getString("kodecabang", "")!!
        hp_st = this.preferences.getString("hp", "")!!
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the Up button click
                findNavController().navigateUp() // Navigate back to the previous fragment
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}