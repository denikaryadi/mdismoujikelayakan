    package com.komida.co.id.mdisujikelayakan

    import android.content.Context.MODE_PRIVATE
    import android.content.Intent
    import android.content.SharedPreferences
    import android.database.sqlite.SQLiteDatabase
    import android.graphics.Color
    import android.graphics.drawable.ColorDrawable
    import android.os.Bundle
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.MenuItem
    import android.view.View
    import android.view.ViewGroup
    import android.widget.*
    import androidx.appcompat.app.AlertDialog
    import androidx.core.content.ContextCompat
    import androidx.core.widget.NestedScrollView
    import androidx.navigation.fragment.findNavController
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.google.android.material.snackbar.Snackbar
    import com.komida.co.id.mdisujikelayakan.adapter.AdapterInfoJawabanPPI
    import com.komida.co.id.mdisujikelayakan.adapter.AdapterInfoPertanyaanPPI
    import com.komida.co.id.mdisujikelayakan.model.ModelListJawabanPPi
    import com.komida.co.id.mdisujikelayakan.model.ModelListPertanyaanPPi
    import java.text.SimpleDateFormat
    import java.util.*
    import kotlin.collections.ArrayList

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private const val ARG_PARAM1 = "param1"
    private const val ARG_PARAM2 = "param2"

    /**
     * A simple [Fragment] subclass.
     * Use the [PpiScoreFragment.newInstance] factory method to
     * create an instance of this fragment.
     */
    class PpiScoreFragment : Fragment() {
        private lateinit var parent_view: View
        private lateinit var parentLayout: View

        private lateinit var preferences: SharedPreferences
        private lateinit var kode_uk_keluarga_tv: TextView
        private lateinit var nik_ktp_keluarga_tv: TextView
        private lateinit var nama_anggota_keluarga_tv: TextView
        private lateinit var center_keluarga_tv: TextView
        private lateinit var kelompok_keluarga_tv: TextView
        val itemsList: ArrayList<ModelListPertanyaanPPi> = ArrayList()
        private lateinit var adapterInfoPertanyaanPPI: AdapterInfoPertanyaanPPI

        val itemsListJawaban: ArrayList<ModelListJawabanPPi> = ArrayList()
        private lateinit var adapterInfoJawabanPPI: AdapterInfoJawabanPPI
        private var recyclerViewFormPPI: RecyclerView? = null

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
        var items: List<ModelListPertanyaanPPi>? = null
        private lateinit var btn_simpandatappi:Button
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
                PpiScoreFragment().apply {
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
            val rootView = inflater.inflate(R.layout.fragment_ppi_score, container, false)
            preferences = requireActivity().getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)

            // Initialize your views here
            parentLayout= rootView.findViewById(R.id.parentLayout)
            val detilIntent = activity?.intent // Access activity's intent
            if (detilIntent != null) {
                getPreferenceAnggota(detilIntent)
            }
            getPreferenceStaff()
            setupView(rootView)
            prepareitem()

            val recyclerView: RecyclerView = rootView.findViewById(R.id.recyclerViewFormPPI)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)

            // Initialize the adapter here
            var database = requireContext().openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)

            val adapterInfoPertanyaanPPI = AdapterInfoPertanyaanPPI(itemsList, parentLayout)
            recyclerView.adapter = adapterInfoPertanyaanPPI

            loadInitialData(adapterInfoPertanyaanPPI)

            btn_simpandatappi.setOnClickListener {
                simpanData(rootView)
            }
            return rootView
        }

        private fun simpanData(rootView: View) {

            //check dulu
            var  itemCount=0
            for (item in itemsList) {
                val selectedAnswerIndex = item.selectedAnswerIndex
                if (selectedAnswerIndex >= 0 && selectedAnswerIndex < item.pg.size) {
                    itemCount++
                }
            }
            if(itemCount!=10)
            {
                gagal_app(rootView, "Pertanyaan PPI belum di Isi Semua")

            }
            else
            {
                database_mdismo = requireContext().openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
                val kode_uk_agt = kode_uk_dtl

                val totalSkorPpiTextView = parentLayout.findViewById<TextView>(R.id.total_skor_ppi)
                val total_skor_ppi = totalSkorPpiTextView.text.toString()
                var no_urutan=1
                val deleteUkAgt = "delete from  uk_ppi_anggota where kode_uk='$kode_uk_agt' and id_soal between '1' and '10'"
                database_mdismo.execSQL(deleteUkAgt)
                for (item in itemsList) {
                    val selectedAnswerIndex = item.selectedAnswerIndex
                    if (selectedAnswerIndex >= 0 && selectedAnswerIndex < item.pg.size) {

                        val input = item.pg[selectedAnswerIndex]
                        val parts = input.split("^")
                        if (parts.size >= 4) {
                            val kode_uk_ppi = generateRandom("ukagtppi")
                            val pg = parts[0]
                            val jawaban = parts[1]
                            val poin = parts[2]
                            val ane = parts[3]

                            val execUkAnggotaKeluarga = " insert into uk_ppi_anggota (kode_uk_ppi,kode_uk,id_soal,id_jawaban,score,total_score,status,usr_crt,dt_crt,usr_upd,dt_upd ) " +
                                    " values ('$kode_uk_ppi','$kode_uk_agt','$no_urutan','$pg','$poin','$total_skor_ppi',1,'$nik_st',CURRENT_TIMESTAMP,'$nik_st',CURRENT_TIMESTAMP )"
                            database_mdismo.execSQL(execUkAnggotaKeluarga)

                            no_urutan++

                        }
                    }
                }
                snackBarWithAction("Data Berhasil Di Simpan")
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
        private fun loadInitialData(
            adapterInfoPertanyaanPPI: AdapterInfoPertanyaanPPI
        ) {
            // Initialize the database
            database_mdismo = requireContext().openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)

            val query = "SELECT pq.Comp_ID, " +
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

            val res = this.database_mdismo.rawQuery(query, null)
            var no_urutan = 1 // Initialize the question numbering

            /* check dulu uk_ppi_anggota*/
            val querycek = "SELECT COUNT(*) as jml FROM uk_ppi_anggota WHERE kode_uk='$kode_uk_dtl'"
            val rescek = this.database_mdismo.rawQuery(querycek, null)
            val total: Int? = rescek.use { it.takeIf { it.moveToNext() }?.getInt(0) }
            rescek?.close()


            while (res.moveToNext()) {
                val Comp_ID = res.getString(res.getColumnIndex("Comp_ID"))
                val id_soal = res.getString(res.getColumnIndex("id_soal"))
                val pertanyaan = res.getString(res.getColumnIndex("pertanyaan"))
                val tipe_pertanyaan = res.getString(res.getColumnIndex("tipe_pertanyaan"))
                val status = res.getString(res.getColumnIndex("status"))
                val usr_crt = res.getString(res.getColumnIndex("usr_crt"))
                val dt_crt = res.getString(res.getColumnIndex("dt_crt"))

                var queryjawaban=""
                if (total?.compareTo(0) ?: 0 > 0) {
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
                }
                else
                {
                    queryjawaban = "SELECT" +
                            "    pqp.Comp_ID as Comp_ID," +
                            "    pqp.QuestionID as id_soal," +
                            "    pqp.Answer as pg," +
                            "    pqp.AnswerDesc as isi_jawaban," +
                            "    pqp.Poin as poin," +
                            "    pqp.Score as score," +
                            "    '1' as status ," +
                            "    '' as checked" +
                            " FROM PPI_QuestionPoin pqp " +
                            " WHERE pqp.QuestionID = '$id_soal'" +
                            " ORDER BY pqp.QuestionID ASC "
                }

                val resa = this.database_mdismo.rawQuery(queryjawaban, null)
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

                    pgs.add(pg+"^"+isi_jawaban+"^"+poin+"^"+score+"^"+id_soal+"^"+checked)
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
            adapterInfoPertanyaanPPI.notifyDataSetChanged()
            adapterInfoPertanyaanPPI.setDatabase(database_mdismo)

        }
        fun check_data_ppi(): Int? {
            database_mdismo = requireContext().openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
            var total: Int? = null
            val queryjawaban ="select count(*) as jml from uk_ppi_anggota where kode_uk='$kode_uk_dtl'"
            val res = this.database_mdismo.rawQuery(queryjawaban, null)

            while (res.moveToNext()) {
                total = res.getInt(0)
            }
            database_mdismo.close()
            return total
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

            btn_simpandatappi =rootView.findViewById(R.id.btn_simpandatappi)
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