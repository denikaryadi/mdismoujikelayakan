package com.komida.co.id.mdisujikelayakan

import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.roundToInt

class ListAnggotaFotoKtp : AppCompatActivity() {

    private lateinit var kode_uk_index_tv: TextView
    private lateinit var nik_ktp_uk_index_tv: TextView
    private lateinit var nama_anggota_uk_index_tv: TextView
    private lateinit var center_keluarga_tv: TextView
    private lateinit var kelompok_keluarga_tv: TextView
    private lateinit var imViewKTPAnggota: ImageView
    private lateinit var btnCapture: Button
    private lateinit var preferences: SharedPreferences
    private lateinit var database: SQLiteDatabase
    private lateinit var imageUri: Uri
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var imageCapture: ImageCapture

    private val DB_NAME = "mdismo.db"

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
    private lateinit var preview: Preview // Add this line
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        setContentView(R.layout.activity_list_anggota_foto_ktp)
        val toolbar: Toolbar = findViewById(R.id.toolbarlistanggotafotoktp)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "FOTO KTP ANGGOTA"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val detilIntent = this.intent
        getPreferenceAnggota(detilIntent)
        getPreferenceStaff()
        setupView()
        prepareItem()
        checkFotoAnggota()
        cameraExecutor = Executors.newSingleThreadExecutor()
        val previewView = findViewById<PreviewView>(R.id.previewView)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        preview = Preview.Builder().build()
        preview.setSurfaceProvider(previewView.surfaceProvider)


        val imageCapture = setupImageCapture()
        imageUri = createImageUri()
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        val captureButton = findViewById<Button>(R.id.btn_capture_ktp)

        captureButton.setOnClickListener {
            takePhoto(imageCapture, imageUri)
        }

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, preview)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun checkFotoAnggota() {
        val kodeUkToRetrieve = kode_uk_dtl
        val jenis = "ktp"
        val imageBytes = getImageFromDatabase(kodeUkToRetrieve, jenis)

        if (imageBytes != null) {
            setImageToImageView(imageBytes, imViewKTPAnggota)
        }
    }

    private fun setImageToImageView(imageData: ByteArray?, imageView: ImageView?) {
        if (imageData != null && imageView != null) {
            val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
            imageView.setImageBitmap(bitmap)
        }
    }

    private fun getImageInfo(imageData: ByteArray) {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        BitmapFactory.decodeByteArray(imageData, 0, imageData.size, options)

        val imageWidth = options.outWidth
        val imageHeight = options.outHeight
        val imageMimeType = options.outMimeType
        val imageSize = imageData.size
        val imageSizeInKilobytes = imageSize / 1024.0
        val imageSizeInKilobytesRounded = imageSizeInKilobytes.roundToInt()

        println("Image Width: $imageWidth")
        println("Image Height: $imageHeight")
        println("Image MIME Type: $imageMimeType")
        println("Image Size (bytes): $imageSize")
        println("Image Size (kilobytes): $imageSizeInKilobytesRounded")
    }

    private fun getImageFromDatabase(kodeUk: String, jenis: String): ByteArray? {
        val database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val projection = arrayOf("foto_anggota")
        val selection = "kode_uk = ? AND jenis = ?"
        val selectionArgs = arrayOf(kodeUk, jenis)
        val cursor = database.query("uk_fotoktpttd_anggota", projection, selection, selectionArgs, null, null, null)
        var imageBytes: ByteArray? = null

        if (cursor.moveToFirst()) {
            imageBytes = cursor.getBlob(cursor.getColumnIndex("foto_anggota"))
        }

        cursor.close()

        database.close()
        return imageBytes
    }

    private fun createImageUri(): Uri {
        val image = File(filesDir, "camera_photos.png")
        return FileProvider.getUriForFile(this, "com.komida.co.id.mdisujikelayakan.FileProvider", image)
    }

    private fun setupImageCapture(): ImageCapture {
        return ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()
    }

    private fun getOutputDirectory(): File {
        val mediaDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val appDir = File(mediaDir, "MDISMOUK")

        if (!appDir.exists()) {
            appDir.mkdir()
        }

        return appDir
    }

    private fun takePhoto(imageCapture: ImageCapture, imageUri: Uri) {
        val imageFile = File(getOutputDirectory(), "foto_ktp_$kode_uk_dtl")

        val outputStream = FileOutputStream(imageFile)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(imageFile).build()

        imageCapture.takePicture(outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = outputFileResults.savedUri ?: imageUri
                val imageBytes = loadImageBytes(savedUri)

                runOnUiThread {
                    if (imageBytes != null) {
                        saveImageToDatabase(imageBytes)
                        setImageToImageView(imageBytes, imViewKTPAnggota)
                    }
                }
            }

            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
            }
        })
    }

    private fun loadImageBytes(imageUri: Uri): ByteArray? {
        val contentResolver = contentResolver
        val inputStream = contentResolver.openInputStream(imageUri)
        val outputStream = ByteArrayOutputStream()
        val previewView = findViewById<PreviewView>(R.id.previewView)
        val targetWidth = previewView.width
        val targetHeight = previewView.height
        inputStream?.use { input ->
            input.copyTo(outputStream)
        }

        // Decode the input stream to obtain a Bitmap
        val originalBitmap = BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())

        if (originalBitmap != null && originalBitmap.width > 0 && originalBitmap.height > 0) {
            val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height

            // Calculate the dimensions for cropping
            val cropWidth: Int
            val cropHeight: Int
            val xOffset: Int
            val yOffset: Int

            if (aspectRatio > targetWidth.toFloat() / targetHeight) {
                cropHeight = originalBitmap.height
                cropWidth = (originalBitmap.height * (targetWidth.toFloat() / targetHeight)).toInt()
                yOffset = 0
                xOffset = (originalBitmap.width - cropWidth) / 2
            } else {
                cropWidth = originalBitmap.width
                cropHeight = (originalBitmap.width * (targetHeight.toFloat() / targetWidth)).toInt()
                xOffset = 0
                yOffset = (originalBitmap.height - cropHeight) / 2
            }

            // Crop the image
            val croppedBitmap = Bitmap.createBitmap(originalBitmap, xOffset, yOffset, cropWidth, cropHeight)

            // Create a scaled and fully compressed version of the cropped image
            val scaledBitmap = Bitmap.createScaledBitmap(croppedBitmap, targetWidth, targetHeight, false)

            // Compress the image to PNG format
            val compressedImageStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, compressedImageStream)

            val compressedImageBytes: ByteArray = compressedImageStream.toByteArray()

            return compressedImageBytes
        } else {
            // Handle the case when the originalBitmap is null or has invalid dimensions
            // You can return null or handle this case based on your requirements
            return null
        }
    }



    private fun saveImageToDatabase(imageBytes: ByteArray) {
        val database = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
        val kodeUkFoto = generateRandom("ukfoto")
        val jenis = "ktp"

        val deleteSQL = "DELETE FROM uk_fotoktpttd_anggota WHERE jenis='$jenis' and kode_uk = '$kode_uk_dtl';"
        println(deleteSQL)
        database.execSQL(deleteSQL)

        val insertSQL = "INSERT INTO uk_fotoktpttd_anggota (" +
                "kode_uk_foto, kode_uk, jenis, foto_anggota, ttd_anggota, usr_crt, dt_crt, usr_upd, dt_upd) VALUES (" +
                "'$kodeUkFoto', '$kode_uk_dtl', '$jenis', ?, '', " +
                "'$nik_st', CURRENT_TIMESTAMP, '$nik_st', CURRENT_TIMESTAMP);"

        val statement = database.compileStatement(insertSQL)
        statement.bindBlob(1, imageBytes)

        statement.execute()
        database.close()
        println(insertSQL)
    }

    private fun generateRandom(prefix: String): String {
        val length = 10
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val randomString = RandomStringGenerator().generateRandomString(length)
        return "$prefix-$currentDate-$randomString"
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
        nama_st = preferences.getString("nama", "") ?: ""
        nik_st = preferences.getString("nik", "") ?: ""
        dev_st = preferences.getString("devid", "") ?: ""
        jab_st = preferences.getString("jabatan", "") ?: ""
        cab_st = preferences.getString("cabang", "") ?: ""
        kode_cab_st = preferences.getString("kodecabang", "") ?: ""
        hp_st = preferences.getString("hp", "") ?: ""
    }

    private fun setupView() {
        kode_uk_index_tv = findViewById(R.id.kode_uk_index_tv)
        nik_ktp_uk_index_tv = findViewById(R.id.nik_ktp_uk_index_tv)
        nama_anggota_uk_index_tv = findViewById(R.id.nama_anggota_uk_index_tv)
        center_keluarga_tv = findViewById(R.id.center_keluarga_tv)
        kelompok_keluarga_tv = findViewById(R.id.kelompok_keluarga_tv)
        imViewKTPAnggota = findViewById(R.id.imViewKTPAnggota)
        btnCapture = findViewById(R.id.btn_capture_ktp)
    }

    private fun prepareItem() {
        kode_uk_index_tv.text = kode_uk_dtl
        nik_ktp_uk_index_tv.text = nik_ktp_dtl
        nama_anggota_uk_index_tv.text = nama_anggota_dtl
        center_keluarga_tv.text = center_dtl
        kelompok_keluarga_tv.text = kelompok_dtl
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
