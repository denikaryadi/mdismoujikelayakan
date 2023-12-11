package com.komida.co.id.mdisujikelayakan

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.komida.co.id.mdisujikelayakan.utils.NIKParser
import com.komida.co.id.mdisujikelayakan.utils.NIKParserImpl


class UjiKelayakanActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var kode_uk_tv: TextView
    private lateinit var nama_agt_tv: TextView
    private lateinit var nik_agt_tv: TextView
    private lateinit var tipe_agt_tv: TextView
    private lateinit var tgl_bergabung_tv: TextView
    private lateinit var tempat_lahir_tv: TextView
    private lateinit var tgl_lahir_tv: TextView
    private lateinit var center_tv: TextView
    private lateinit var kelompok_tv: TextView
    private lateinit var handphone_tv: TextView
    private lateinit var status_kawin_tv: TextView
    private lateinit var nama_suami_tv: TextView
    private lateinit var nama_ibu_kandung_tv: TextView

    private lateinit var imageAnggota: ImageView
    private lateinit var info_detail_ktp: ImageButton

    private var backPressedTime: Long = 0
    companion object {
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
    private lateinit var database_mdismo: SQLiteDatabase
    var DB_NAME = "mdismo.db"
    private lateinit var nikParser: NIKParser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uji_kelayakan)

        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)

        initToolbar()

        val detilIntent = this.intent
        nikParser= NIKParserImpl(this)
        getPreferenceAnggota(detilIntent)
        setupView()
        checkFotoAnggota()
        prepareItem()


    }

    private fun prepareItem() {
        kode_uk_tv.text = kode_uk_dtl
        nama_agt_tv.text = nama_anggota_dtl
        nik_agt_tv.text = nik_ktp_dtl
        tipe_agt_tv.text = tipe_dtl
        tgl_bergabung_tv.text = tgl_bergabung_dtl
        tempat_lahir_tv.text = tempat_lahir_dtl+"/"+ tgl_lahir_dtl
        center_tv.text = center_dtl+"/"+ kelompok_dtl
        handphone_tv.text= handphone_dtl
        status_kawin_tv.text = status_kawin_dtl
        nama_suami_tv.text = nama_suami_dtl
        nama_ibu_kandung_tv.text = nama_ibu_kandung_dtl

        val drawableResourceId = resources.getIdentifier("bg_nus", "drawable", packageName)
        val drawable = resources.getDrawable(drawableResourceId) as BitmapDrawable
        val bitmap = drawable.bitmap
        val desiredWidth = bitmap.width / 2
        val desiredHeight = (bitmap.height * (desiredWidth.toFloat() / bitmap.width)).toInt()
        val image_header = findViewById<ImageView>(R.id.image_header)
        Glide.with(this)
            .load(drawableResourceId)
            .override(desiredWidth, desiredHeight)
            .into(image_header)
        // Retrieve data from the intent extras


    }
    private fun showDialogKtp(originalString:String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val nikParserResponse = nikParser.parseNik(originalString)
        println(nikParserResponse)
        val nik = nikParserResponse.nik
        val isValid = nikParserResponse.isValid
        val province = nikParserResponse.province
        val regency = nikParserResponse.regency
        val district = nikParserResponse.district
        val birthDate = nikParserResponse.birthDate
        val gender = nikParserResponse.gender
        val uniqueCode = nikParserResponse.uniqueCode
        var msg:String=""
        println("NIK: $nik")
        println("IsValid: $isValid")
        println("Province: ${province?.name} (ID: ${province?.id})")
        println("Regency: ${regency?.name} (ID: ${regency?.id})")
        println("District: ${district?.name} (ID: ${district?.id}, Zip Code: ${district?.zipCode})")
        println("Birth Date: $birthDate")
        println("Gender: $gender")
        println("Unique Code: $uniqueCode")
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

    private fun initToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbardetailanggota)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "INFORMASI ANGGOTA BARU"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    fun checkFotoAnggota() {
        val kode_uk_to_retrieve = kode_uk_dtl
        val jenis:String = "foto"
        val imageBytes = getImageFromDatabase(kode_uk_to_retrieve,jenis)

        if (imageBytes != null) {
            // Process or display the image as needed
            setImageToImageView(imageBytes, imageAnggota)
        }

    }
    fun setImageToImageView(imageData: ByteArray?, imageView: ImageView?) {
        if (imageData != null && imageView != null) {
            val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
            imageView.setImageBitmap(bitmap)

        }
    }
    fun getImageFromDatabase(kodeUk: String, jenis: String): ByteArray? {
        val database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val projection = arrayOf("foto_anggota") // Assuming "foto_anggota" is the BLOB column name
        val selection = "kode_uk = ? AND jenis = ?"
        val selectionArgs = arrayOf(kodeUk, jenis)
        val cursor = database_mdismo.query("uk_fotoktpttd_anggota", projection, selection, selectionArgs, null, null, null)
        var imageBytes: ByteArray? = null

        if (cursor.moveToFirst()) {
            imageBytes = cursor.getBlob(cursor.getColumnIndex("foto_anggota"))
        }

        cursor.close()
        database_mdismo.close()
        return imageBytes
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

    private fun setupView() {
        kode_uk_tv = findViewById(R.id.kode_uk_tv)
        nama_agt_tv = findViewById(R.id.nama_agt_tv)
        nik_agt_tv = findViewById(R.id.nik_agt_tv)
        tipe_agt_tv = findViewById(R.id.tipe_tv)
        tgl_bergabung_tv = findViewById(R.id.tgl_bergabung_tv)
        tempat_lahir_tv = findViewById(R.id.tmpt_tgl_lahir_tv)
        center_tv = findViewById(R.id.center_kelompok_tv)
        handphone_tv = findViewById(R.id.handphone_tv)
        status_kawin_tv = findViewById(R.id.status_kawin_tv)
        nama_suami_tv = findViewById(R.id.nama_suami_tv)
        nama_ibu_kandung_tv = findViewById(R.id.nama_ibu_kandung_tv)

        info_detail_ktp= findViewById(R.id.info_detail_ktp)

        imageAnggota = findViewById(R.id.image_anggota)
        info_detail_ktp.setOnClickListener  {

            if (nik_ktp_dtl.length == 16) {
                // Your logic for a long click (show tooltip)
                // This will be executed when the button is long-clicked
                // You can show a tooltip or any other information here
                showDialogKtp(nik_ktp_dtl.toString()) // Replace with your tooltip implementation
                true // Return true to indicate that the long click event is consumed
            }
        }


    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        // Check if the back button was pressed twice within a short time frame (e.g., 2 seconds)
        // Check if the back button was pressed twice within a short time frame (e.g., 2 seconds)
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            val listAnggotaIntent = Intent(this, ListAnggotaUk::class.java)
            startActivity(listAnggotaIntent)
            finish()
        } else {
            val snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "Tekan Kembali untuk Ke Daftar Anggota",
                Snackbar.LENGTH_SHORT
            )
            snackbar.show()
        }
        backPressedTime = System.currentTimeMillis()

    }
    private fun showInfoSnackbar(view: View, message: String,status:String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        if(status=="edit")
        {
            snackbarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.green_400)) // Ganti dengan warna yang sesuai

        } // Ganti dengan warna yang sesuai\
        else if(status=="tidakhapus")
        {
            snackbarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.yellow_900)) // Ganti dengan warna yang sesuai
        }
        else
        {
            snackbarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.red_400)) // Ganti dengan warna yang sesuai
        }
        snackbar.show()
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    fun clickAction(view: View) {
        val id = view.id
        if (id == R.id.btn_data_keluarga) {
            val lihat_data_pendapatan = Intent(this, ListAnggotaKeluarga::class.java)
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
            lihat_data_pendapatan.putExtra("nama_ibu_kandung_dtl", nama_ibu_kandung_dtl)

            startActivity(lihat_data_pendapatan)
        }
        else if (id== R.id.btn_data_idx_rumah){
            val lihat_data_pendapatan = Intent(this, ListAnggotaIndexRumah::class.java)
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
            lihat_data_pendapatan.putExtra("nama_ibu_kandung_dtl", nama_ibu_kandung_dtl)

            startActivity(lihat_data_pendapatan)
        }
        else if (id== R.id.btn_data_pendapatan){
            val lihat_data_pendapatan = Intent(this, ListAnggotaPendapatan::class.java)
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
            lihat_data_pendapatan.putExtra("nama_ibu_kandung_dtl", nama_ibu_kandung_dtl)
            startActivity(lihat_data_pendapatan)
        }
        else if (id== R.id.btn_data_foto){
            val lihat_data_foto = Intent(this, ListAnggotaFoto::class.java)
            lihat_data_foto.putExtra("kode_uk_dtl", kode_uk_dtl)
            lihat_data_foto.putExtra("nik_ktp_dtl", nik_ktp_dtl)
            lihat_data_foto.putExtra("nama_anggota_dtl", nama_anggota_dtl)
            lihat_data_foto.putExtra("tipe_dtl", tipe_dtl)
            lihat_data_foto.putExtra("center_dtl", center_dtl)
            lihat_data_foto.putExtra("kelompok_dtl", kelompok_dtl)
            lihat_data_foto.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
            lihat_data_foto.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
            lihat_data_foto.putExtra("handphone_dtl", handphone_dtl)
            lihat_data_foto.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
            lihat_data_foto.putExtra("status_kawin_dtl", status_kawin_dtl)
            lihat_data_foto.putExtra("nama_suami_dtl", nama_suami_dtl)
            lihat_data_foto.putExtra("nama_ibu_kandung_dtl", nama_ibu_kandung_dtl)
            startActivity(lihat_data_foto)
        }
        else if (id== R.id.btn_data_ktp){
            val lihat_data_foto_ktp = Intent(this, ListAnggotaFotoKtp::class.java)
            lihat_data_foto_ktp.putExtra("kode_uk_dtl", kode_uk_dtl)
            lihat_data_foto_ktp.putExtra("nik_ktp_dtl", nik_ktp_dtl)
            lihat_data_foto_ktp.putExtra("nama_anggota_dtl", nama_anggota_dtl)
            lihat_data_foto_ktp.putExtra("tipe_dtl", tipe_dtl)
            lihat_data_foto_ktp.putExtra("center_dtl", center_dtl)
            lihat_data_foto_ktp.putExtra("kelompok_dtl", kelompok_dtl)
            lihat_data_foto_ktp.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
            lihat_data_foto_ktp.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
            lihat_data_foto_ktp.putExtra("handphone_dtl", handphone_dtl)
            lihat_data_foto_ktp.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
            lihat_data_foto_ktp.putExtra("status_kawin_dtl", status_kawin_dtl)
            lihat_data_foto_ktp.putExtra("nama_suami_dtl", nama_suami_dtl)
            lihat_data_foto_ktp.putExtra("nama_ibu_kandung_dtl", nama_ibu_kandung_dtl)
            startActivity(lihat_data_foto_ktp)
        }
        else if (id== R.id.btn_data_tanda_tangan){
            val lihat_data_anggota = Intent(this, ListAnggotaTandaTangan::class.java)
            lihat_data_anggota.putExtra("kode_uk_dtl", kode_uk_dtl)
            lihat_data_anggota.putExtra("nik_ktp_dtl", nik_ktp_dtl)
            lihat_data_anggota.putExtra("nama_anggota_dtl", nama_anggota_dtl)
            lihat_data_anggota.putExtra("tipe_dtl", tipe_dtl)
            lihat_data_anggota.putExtra("center_dtl", center_dtl)
            lihat_data_anggota.putExtra("kelompok_dtl", kelompok_dtl)
            lihat_data_anggota.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
            lihat_data_anggota.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
            lihat_data_anggota.putExtra("handphone_dtl", handphone_dtl)
            lihat_data_anggota.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
            lihat_data_anggota.putExtra("status_kawin_dtl", status_kawin_dtl)
            lihat_data_anggota.putExtra("nama_suami_dtl", nama_suami_dtl)
            lihat_data_anggota.putExtra("nama_ibu_kandung_dtl", nama_ibu_kandung_dtl)
            startActivity(lihat_data_anggota)
        }
        else if (id== R.id.btn_data_anggota){
            val lihat_data_anggota = Intent(this, FormTambahAnggotaActivity::class.java)
            startActivity(lihat_data_anggota)
        }
        else if (id== R.id.btn_rencana_pembiayaan){
            val lihat_data_anggota = Intent(this, ListAnggotaRencanaBiaya::class.java)
            lihat_data_anggota.putExtra("kode_uk_dtl", kode_uk_dtl)
            lihat_data_anggota.putExtra("nik_ktp_dtl", nik_ktp_dtl)
            lihat_data_anggota.putExtra("nama_anggota_dtl", nama_anggota_dtl)
            lihat_data_anggota.putExtra("tipe_dtl", tipe_dtl)
            lihat_data_anggota.putExtra("center_dtl", center_dtl)
            lihat_data_anggota.putExtra("kelompok_dtl", kelompok_dtl)
            lihat_data_anggota.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
            lihat_data_anggota.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
            lihat_data_anggota.putExtra("handphone_dtl", handphone_dtl)
            lihat_data_anggota.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
            lihat_data_anggota.putExtra("status_kawin_dtl", status_kawin_dtl)
            lihat_data_anggota.putExtra("nama_suami_dtl", nama_suami_dtl)
            lihat_data_anggota.putExtra("nama_ibu_kandung_dtl", nama_ibu_kandung_dtl)
            startActivity(lihat_data_anggota)
        }
        else if (id== R.id.btn_produk_biaya){
            val lihat_data_anggota = Intent(this, ListAnggotaProdukBiaya::class.java)
            lihat_data_anggota.putExtra("kode_uk_dtl", kode_uk_dtl)
            lihat_data_anggota.putExtra("nik_ktp_dtl", nik_ktp_dtl)
            lihat_data_anggota.putExtra("nama_anggota_dtl", nama_anggota_dtl)
            lihat_data_anggota.putExtra("tipe_dtl", tipe_dtl)
            lihat_data_anggota.putExtra("center_dtl", center_dtl)
            lihat_data_anggota.putExtra("kelompok_dtl", kelompok_dtl)
            lihat_data_anggota.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
            lihat_data_anggota.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
            lihat_data_anggota.putExtra("handphone_dtl", handphone_dtl)
            lihat_data_anggota.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
            lihat_data_anggota.putExtra("status_kawin_dtl", status_kawin_dtl)
            lihat_data_anggota.putExtra("nama_suami_dtl", nama_suami_dtl)
            lihat_data_anggota.putExtra("nama_ibu_kandung_dtl", nama_ibu_kandung_dtl)
            startActivity(lihat_data_anggota)
        }
        else if (id== R.id.btn_input_ppi){
            val lihat_data_anggota = Intent(this, ListAnggotaPPi::class.java)
            lihat_data_anggota.putExtra("kode_uk_dtl", kode_uk_dtl)
            lihat_data_anggota.putExtra("nik_ktp_dtl", nik_ktp_dtl)
            lihat_data_anggota.putExtra("nama_anggota_dtl", nama_anggota_dtl)
            lihat_data_anggota.putExtra("tipe_dtl", tipe_dtl)
            lihat_data_anggota.putExtra("center_dtl", center_dtl)
            lihat_data_anggota.putExtra("kelompok_dtl", kelompok_dtl)
            lihat_data_anggota.putExtra("tempat_lahir_dtl", tempat_lahir_dtl)
            lihat_data_anggota.putExtra("tgl_bergabung_dtl", tgl_bergabung_dtl)
            lihat_data_anggota.putExtra("handphone_dtl", handphone_dtl)
            lihat_data_anggota.putExtra("tgl_lahir_dtl", tgl_lahir_dtl)
            lihat_data_anggota.putExtra("status_kawin_dtl", status_kawin_dtl)
            lihat_data_anggota.putExtra("nama_suami_dtl", nama_suami_dtl)
            lihat_data_anggota.putExtra("nama_ibu_kandung_dtl", nama_ibu_kandung_dtl)
            startActivity(lihat_data_anggota)
        }
    }
   
}
