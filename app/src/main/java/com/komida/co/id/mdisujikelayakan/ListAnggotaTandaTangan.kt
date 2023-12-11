package com.komida.co.id.mdisujikelayakan

import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.github.gcacace.signaturepad.views.SignaturePad
import com.google.zxing.common.BitArray
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ListAnggotaTandaTangan : AppCompatActivity() {

    private lateinit var signaturePad: SignaturePad
    private lateinit var signatureImagePad: ByteArray
    private lateinit var clearButton: Button
    private lateinit var saveButton: Button
    private lateinit var ivSignature: ImageView

    private  var kode_uk_index_tv: TextView? = null
    private  var nik_ktp_uk_index_tv: TextView? = null
    private  var nama_anggota_uk_index_tv: TextView? = null
    private  var center_keluarga_tv: TextView? = null
    private  var kelompok_keluarga_tv: TextView? = null
    private lateinit var preferences: SharedPreferences
    companion object {
        lateinit var kode_uk_dtl: String
        lateinit var nama_anggota_dtl: String
        lateinit var nik_ktp_dtl: String
        lateinit var tipe_dtl: String
        lateinit var tempat_lahir_dtl: String
        lateinit var center_dtl: String
        lateinit var kelompok_dtl: String

        lateinit var nama_st: String
        lateinit var nik_st: String
        lateinit var cab_st: String
        lateinit var jab_st: String
        lateinit var dev_st: String
        lateinit var hp_st: String
        lateinit var kode_cab_st: String


    }
    private lateinit var database_mdismo: SQLiteDatabase
    var DB_NAME = "mdismo.db"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_anggota_tanda_tangan)
        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        val toolbar: Toolbar = findViewById(R.id.toolbarlistanggotatandatangan)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "TANDA TANGAN ANGGOTA"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detilIntent = this.intent
        getPreferenceAnggota(detilIntent)
        getPreferenceStaff()
        setupView()
        prepareItem()
        checkTtdAnggota()
        initialize()

    }
    private fun getPreferenceAnggota(intent: Intent?) {
        kode_uk_dtl = intent?.getStringExtra("kode_uk_dtl") ?: ""
        nama_anggota_dtl = intent?.getStringExtra("nama_anggota_dtl") ?: ""
        nik_ktp_dtl = intent?.getStringExtra("nik_ktp_dtl") ?: ""
        tipe_dtl = intent?.getStringExtra("tipe_dtl") ?: ""
        center_dtl = intent?.getStringExtra("center_dtl") ?: ""
        kelompok_dtl = intent?.getStringExtra("kelompok_dtl") ?: ""
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
    private fun setupView() {
        kode_uk_index_tv = findViewById(R.id.kode_uk_index_tv)
        nik_ktp_uk_index_tv = findViewById(R.id.nik_ktp_uk_index_tv)
        nama_anggota_uk_index_tv = findViewById(R.id.nama_anggota_uk_index_tv)
        center_keluarga_tv = findViewById(R.id.center_keluarga_tv)
        kelompok_keluarga_tv = findViewById(R.id.kelompok_keluarga_tv)

        signaturePad = findViewById(R.id.signaturePadAnggota)
        clearButton = findViewById(R.id.btn_hapus_ttd)
        saveButton = findViewById(R.id.btn_save_ttd)
        ivSignature = findViewById(R.id.ivSignature)

    }
    fun prepareItem() {
        kode_uk_index_tv?.text = kode_uk_dtl
        nik_ktp_uk_index_tv?.text = nik_ktp_dtl
        nama_anggota_uk_index_tv?.text = nama_anggota_dtl
        center_keluarga_tv?.text = center_dtl
        kelompok_keluarga_tv?.text = kelompok_dtl

    }
    private fun initialize() {


        signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                // Handle signature start
            }

            override fun onClear() {
                // Handle signature cleared
            }

            override fun onSigned() {
                // Handle signature saved
            }
        })

        clearButton.setOnClickListener {
            signaturePad.clear()
            ivSignature.setImageResource(0)
        }

        saveButton.setOnClickListener {
            ivSignature.setImageResource(0)

            val signatureBitmap = signaturePad.signatureBitmap
            val byteArray = ByteArrayOutputStream().apply {
                signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, this)
            }.toByteArray()

            ivSignature.setImageBitmap(signatureBitmap)
            // Now you have the signature as a byte array (byteArray)
            saveImagePathToDatabase(byteArray)

        }
    }
    fun checkTtdAnggota() {
        val kode_uk_to_retrieve = kode_uk_dtl
        val jenis:String = "ttd"
        val imageBytes = getTtdFromDatabase(kode_uk_to_retrieve,jenis)
        if (imageBytes != null) {
            // Process or display the image as needed
            setImageToImageView(imageBytes, ivSignature)
        }
    }
    fun setImageToImageView(imageData: ByteArray?, imageView: ImageView?) {
        if (imageData != null && imageView != null) {
            val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
            imageView.setImageBitmap(bitmap)
            getImageInfo(imageData)
        }
    }
    fun getImageInfo(imageData: ByteArray) {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true // Set this to true to only get image info, not load the whole image

        BitmapFactory.decodeByteArray(imageData, 0, imageData.size, options)

        val imageWidth = options.outWidth
        val imageHeight = options.outHeight
        val imageMimeType = options.outMimeType // The MIME type of the image (e.g., "image/jpeg" or "image/png")
        val imageSize = imageData.size
        val imageSizeInKilobytes = imageSize / 1024.0
        val imageSizeInKilobytesRounded = Math.round(imageSizeInKilobytes)
        println("Image Width: $imageWidth")
        println("Image Height: $imageHeight")
        println("Image MIME Type: $imageMimeType")
        println("Image Size (bytes): $imageSize")
        println("Image Size (kilobytes): $imageSizeInKilobytesRounded")
    }
    fun getTtdFromDatabase(kodeUk: String, jenis: String): ByteArray? {
        val database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val projection = arrayOf("ttd_anggota") // Assuming "foto_anggota" is the BLOB column name
        val selection = "kode_uk = ? AND jenis = ?"
        val selectionArgs = arrayOf(kodeUk, jenis)
        val cursor = database_mdismo.query("uk_fotoktpttd_anggota", projection, selection, selectionArgs, null, null, null)
        var imageBytes: ByteArray? = null

        if (cursor.moveToFirst()) {
            imageBytes = cursor.getBlob(cursor.getColumnIndex("ttd_anggota"))
        }

        cursor.close()
        println(imageBytes);
        database_mdismo.close()
        return imageBytes
    }
    fun saveImagePathToDatabase(signatureBitmap: ByteArray) {
        val kode_uk_foto = generateRandom("ukttd")
        val jenis = "ttd"
        // Insert the image file path into the database
        val database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val deleteSQL = "DELETE FROM uk_fotoktpttd_anggota WHERE jenis='$jenis' and kode_uk = '$kode_uk_dtl';"
        database_mdismo.execSQL(deleteSQL)
        val insertSQL = "INSERT INTO uk_fotoktpttd_anggota (" +
                "kode_uk_foto, kode_uk, jenis, foto_anggota, ttd_anggota, usr_crt, dt_crt, usr_upd, dt_upd) VALUES (" +
                "'$kode_uk_foto', '$kode_uk_dtl', '$jenis', '', ?, " +
                "'$nik_st', CURRENT_TIMESTAMP, '$nik_st', CURRENT_TIMESTAMP);"

        val statement = database_mdismo.compileStatement(insertSQL)
        statement.bindBlob(1, signatureBitmap)

        statement.execute()
        //database_mdismo.close() // Close the database when you're done
    }
    fun generateRandom(prefix: String): String {
        val length = 10 // Specify the desired length of the random string
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val randomString = RandomStringGenerator().generateRandomString(length)
        val resultString = "$prefix-$currentDate-$randomString"
        println("Random String: $resultString")
        return resultString
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
