package com.komida.co.id.mdisujikelayakan

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.sanojpunchihewa.updatemanager.UpdateManager
import id.co.komida.mdismojava.CustomDialog
import java.io.File
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE = 1
    private lateinit var parent_view: View
    private lateinit var cDate: Date
    private lateinit var nowDate: String
    private lateinit var fDate: String
    private lateinit var status_sinkron: String
    private lateinit var cekdev: String
    private lateinit var cekver: String

    private lateinit var nama_staff_tv: TextView
    private lateinit var nik_staff_tv: TextView
    private lateinit var cab_staff_tv: TextView
    private lateinit var jab_staff_tv: TextView
    private lateinit var dev_staff_tv: TextView
    private lateinit var hp_staff_tv: TextView
    private lateinit var kode_cab_staff_tv: TextView
    private lateinit var preferences: SharedPreferences
    private lateinit var mUpdateManager: UpdateManager
    private lateinit var database_mdismo: SQLiteDatabase
    private lateinit var dialog: CustomDialog
    private val DB_NAME = "mdismo.db"
    companion object {
        lateinit var nama_st: String
        lateinit var nik_st: String
        lateinit var cab_st: String
        lateinit var jab_st: String
        lateinit var dev_st: String
        lateinit var hp_st: String
        lateinit var kode_cab_st: String
    }
    private lateinit var ONESIGNAL_APP_ID: String
    private lateinit var URL_CEK: String
    private lateinit var URL_CEK_NIK: String

    private var mTask2: AsyncTask<Void, Void, Void>? = null



    @SuppressLint("StaticFieldLeak")
    override fun onCreate(savedInstanceState: Bundle?) {
        // In your initialization code, you should assign values to these properties.
      //  ONESIGNAL_APP_ID = "d4ad5afe-273c-49c6-b0be-fe1ab2d01c21"
        URL_CEK = "https://komida.co.id/hris/cek_abs_nik.php"
        URL_CEK_NIK = "https://komida.co.id/mdismo/syn_java/cek_nik.php"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        parent_view = findViewById<View>(android.R.id.content)
        getPrefenceStaff ()
        setupView()

        nama_staff_tv.text= nama_st
        nik_staff_tv.text = nik_st
        jab_staff_tv.text = jab_st
        cab_staff_tv.text = cab_st
        kode_cab_staff_tv.text = kode_cab_st
        hp_staff_tv.text = hp_st
        dev_staff_tv.text = dev_st

        //fDate = nowDate;
        verifyPermission()
        poto_profil()

        //sinkron ke sqlite\
        // Copy database
        val assetDatabaseOpenHelper = AssetDatabaseOpenHelper(this, DB_NAME)
        assetDatabaseOpenHelper.saveDatabase()
       // database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)


    }
    private fun verifyPermission() {
        //Log.d(TAG, "verifyPermissions : asking for permissions");
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                permissions[0]
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this.applicationContext,
                permissions[1]
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this.applicationContext,
                permissions[2]
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this.applicationContext,
                permissions[3]
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this.applicationContext,
                permissions[4]
            ) == PackageManager.PERMISSION_GRANTED
        ) {


            //mTaskjam.execute();
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                permissions,
                REQUEST_CODE
            )
            Snackbar.make(parent_view, "Tolong Berikan Izin Akses Aplikasi ", Snackbar.LENGTH_SHORT)
                .show()
        }
    }
    private fun poto_profil() {
        val niks = nik_st.replace("/", "-")
        val urlp = "https://www.komida.co.id/apphris/muka/$niks.jpg"

        val cw = ContextWrapper(applicationContext)
        val directory = cw.getDir("imageDir", MODE_PRIVATE)
        val mypath = File(directory, "profil.jpg")
        if (mypath.exists()) {
            val myBitmap = BitmapFactory.decodeFile(mypath.absolutePath)
            val imgLogos = findViewById<ImageView>(R.id.image)
            imgLogos.setImageBitmap(myBitmap)
        } else {
            val pathds =
                Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/drawable/logo")
            val imgLogo = findViewById<ImageView>(R.id.image)
            imgLogo.setImageURI(pathds)
        }
    }

    private fun getPrefenceStaff() {
        nama_st =preferences.getString("nama","")!!
        nik_st = preferences.getString("nik", "")!!
        dev_st = preferences.getString("devid", "")!!
        jab_st = preferences.getString("jabatan", "")!!
        cab_st = preferences.getString("cabang", "")!!
        kode_cab_st = preferences.getString("kodecabang", "")!!
        hp_st = preferences.getString("hp", "")!!
    }

    private fun setupView() {
//        TODO("Not yet implemented")
        nama_staff_tv = findViewById(R.id.nama_staff)
        nik_staff_tv = findViewById(R.id.nik_staff)
        dev_staff_tv = findViewById(R.id.devid_staff)
        cab_staff_tv = findViewById(R.id.cabang_staff)
        kode_cab_staff_tv = findViewById(R.id.kode_cabang_staff)
        jab_staff_tv = findViewById(R.id.jabatan_staff)
        hp_staff_tv = findViewById(R.id.hp_staff)
    }

    //cek bisa konek pa ga, klo ga bisa cek ada file profil apa ga, klo ga ada pake logo
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        //toolbar.setNavigationIcon(R.mipmap.logo_mdismo_round);
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("MDIS MOBILE")
        Tools.setSystemBarColor(this, R.color.green_400)
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    fun clickAction(view: View) {
        val id = view.id
        if (id == R.id.btn_add_anggota) {
            val tambah_anggota = Intent(this, FormTambahAnggotaActivity::class.java)
            startActivity(tambah_anggota)
        }
        else if (id == R.id.btnlihatlistanggota) {
            val list_anggota = Intent(this, ListAnggotaUk::class.java)
            startActivity(list_anggota)
        }
        else if (id == R.id.btnlihatlistkelompok) {
            val list_kelompok = Intent(this, ListAnggotaKelompok::class.java)
            startActivity(list_kelompok)
        }
        else if (id == R.id.sinkron_tombol) {
            Toast.makeText(this, "SINKRON DATA", Toast.LENGTH_SHORT).show()
        }

    }
    override fun onBackPressed() {
        val txtCancel: TextView
        val btnUpdate: Button
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val builder = AlertDialog.Builder(this@MainActivity)
        val view1: View =
                LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog, viewGroup, false)
        builder.setCancelable(false)
        builder.setView(view1)
        val alertDialog = builder.create()
        txtCancel = view1.findViewById<TextView>(R.id.batal)
        btnUpdate = view1.findViewById(R.id.btn_dialog)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnUpdate.setOnClickListener { finishAffinity() }
        txtCancel.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }

}