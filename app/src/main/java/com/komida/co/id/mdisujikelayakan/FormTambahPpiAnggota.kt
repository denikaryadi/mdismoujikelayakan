package com.komida.co.id.mdisujikelayakan

import ViewAnimation
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
/*
import com.komida.co.id.mdisujikelayakan.adapter.AdapterInfoJawabanPPI
import com.komida.co.id.mdisujikelayakan.adapter.AdapterInfoPertanyaanPPI
import com.komida.co.id.mdisujikelayakan.model.ModelListJawabanPPi
import com.komida.co.id.mdisujikelayakan.model.ModelListPertanyaanPPi
*/
class FormTambahPpiAnggota : AppCompatActivity() {
    // private lateinit var parent_view: View
    private lateinit var preferences: SharedPreferences
    private lateinit var kode_uk_keluarga_tv: TextView
    private lateinit var nik_ktp_keluarga_tv: TextView
    private lateinit var nama_anggota_keluarga_tv: TextView
    private lateinit var center_keluarga_tv: TextView
    private lateinit var kelompok_keluarga_tv: TextView
    private lateinit var database_mdismo: SQLiteDatabase
    var DB_NAME = "mdismo.db"
    /*
    val itemsList: ArrayList<ModelListPertanyaanPPi> = ArrayList()
    private lateinit var AdapterInfoPertanyaanPPI: AdapterInfoPertanyaanPPI


    val itemsListJawaban: ArrayList<ModelListJawabanPPi> = ArrayList()
    private lateinit var AdapterInfoJawabanPPI: AdapterInfoJawabanPPI
    private var recyclerViewFormPPI: RecyclerView? = null

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
    var items: List<ModelListPertanyaanPPi>? = null
    */

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
    private lateinit var parent_view: View
    private lateinit var nested_scroll_view: NestedScrollView
    private lateinit var nested_content: NestedScrollView

    private lateinit var bt_toggle_info: ImageButton
    private lateinit var bt_toggle_passenger: ImageButton
    private lateinit var bt_hide_info: Button
    private lateinit var lyt_expand_info: View
    private lateinit var lyt_expand_passenger: View
    private var view_pager: ViewPager? = null
    private var tab_layout: TabLayout? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_form_tambah_ppi_anggota)
        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        // val parentLayout: View = findViewById(R.id.parentLayout) // Find the parent layout by its ID
        initToolbar()
        val detilIntent = this.intent
        getPreferenceAnggota(detilIntent)
        getPreferenceStaff()
        setupView()
        prepareitem()
        initComponent()


    }
    private fun initComponent() {
        view_pager = findViewById(R.id.view_pager) as ViewPager
        setupViewPager(view_pager!!)
        tab_layout = findViewById(R.id.tab_layout) as TabLayout
        tab_layout!!.setupWithViewPager(view_pager)
    }
    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = SectionsPagerAdapter(supportFragmentManager)
        adapter.addFragment(PpiScoreFragment(), "PERTANYAAN PPI")
        adapter.addFragment(PpiTambahanFragment(), "PERTANYAAN PPI TAMBAHAN")
        viewPager.adapter = adapter
    }
    class PlaceholderFragment : Fragment() {
        private val ARG_SECTION_NUMBER = "section_number"

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_layout_basic, container, false)
            val textView = rootView.findViewById<TextView>(R.id.section_label)
            textView.text = getString(R.string.section_format, requireArguments().getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(fragment.ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
    class SectionsPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = mutableListOf<Fragment>()
        private val mFragmentTitleList = mutableListOf<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

    private fun setupView() {
        kode_uk_keluarga_tv = findViewById(R.id.kode_uk_keluarga_tv)
        nik_ktp_keluarga_tv = findViewById(R.id.nik_ktp_keluarga_tv)
        nama_anggota_keluarga_tv = findViewById(R.id.nama_anggota_keluarga_tv)
        center_keluarga_tv = findViewById(R.id.center_keluarga_tv)
        kelompok_keluarga_tv = findViewById(R.id.kelompok_keluarga_tv)

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
    fun prepareitem(){
        kode_uk_keluarga_tv.text = kode_uk_dtl
        nik_ktp_keluarga_tv.text = nama_anggota_dtl
        nama_anggota_keluarga_tv.text = nik_ktp_dtl
        center_keluarga_tv.text = center_dtl
        kelompok_keluarga_tv.text = kelompok_dtl
    }
    private fun toggleSectionInfo(view: View) {
        val show = toggleArrow(view)
        if (show) {
            ViewAnimation.expand(lyt_expand_info, object : ViewAnimation.AnimListener {
                override fun onFinish() {
                    Tools.nestedScrollTo(nested_content, lyt_expand_info)
                }
            })
        } else {
            ViewAnimation.collapse(lyt_expand_info)
        }
    }

    private fun toggleSectionPassenger(view: View) {
        val show = toggleArrow(view)
        if (show) {
            ViewAnimation.expand(lyt_expand_passenger, object : ViewAnimation.AnimListener {
                override fun onFinish() {
                    Tools.nestedScrollTo(nested_content, lyt_expand_passenger)
                }
            })
        } else {
            ViewAnimation.collapse(lyt_expand_passenger)
        }
    }

    private fun toggleArrow(view: View): Boolean {
        if (view.rotation == 0f) {
            view.animate().setDuration(200).rotation(180f)
            return true
        } else {
            view.animate().setDuration(200).rotation(0f)
            return false
        }
    }
    private fun initToolbar() {
        val toolbar = findViewById(R.id.toolbartambahanggotappi) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Formulir PPI"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val lihat_data_ppi = Intent(this, ListAnggotaPPi::class.java)
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
