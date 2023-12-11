package com.komida.co.id.mdisujikelayakan

import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import java.io.File
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import java.io.FileOutputStream
import java.io.IOException
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ListAnggotaFoto : AppCompatActivity() {

    private lateinit var imageCapture: ImageCapture
    private lateinit var preview: Preview
    private  var kode_uk_index_tv: TextView? = null
    private  var nik_ktp_uk_index_tv: TextView? = null
    private  var nama_anggota_uk_index_tv: TextView? = null
    private  var center_keluarga_tv: TextView? = null
    private  var kelompok_keluarga_tv: TextView? = null
    private lateinit var preferences: SharedPreferences
    private  var imViewFotoAnggota: ImageView? = null
    private var btn_capture :Button?=null

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
    private lateinit var imageUri :Uri

    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()){
        imViewFotoAnggota?.setImageURI(null)
        imViewFotoAnggota?.setImageURI(imageUri)
        if (imageUri != null) {
            // Save the captured image here
            saveCapturedImage(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", MODE_PRIVATE)
        setContentView(R.layout.activity_list_anggota_foto)
        val toolbar: Toolbar = findViewById(R.id.toolbarlistanggotafotoprofil)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "FOTO PROFIL ANGGOTA"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val detilIntent = this.intent
        getPreferenceAnggota(detilIntent)
        getPreferenceStaff()
        setupView()
        prepareItem()
        checkFotoAnggota()
        imageUri = CreateImageUri()

        btn_capture?.setOnClickListener { view ->
            contract.launch(imageUri)
        }


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

    private fun saveCapturedImage(imageUri: Uri) {
        val contentResolver = contentResolver

        // Generate a unique image filename or use a timestamp-based filename
        val imageFileName = "foto_angggota"

        // Define the directory where the images will be stored on the device
        val imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Create the file to save the image
        val imageFile = File(imageDirectory, imageFileName)

        try {
            // Copy the image from the content resolver to the file
            val inputStream = contentResolver.openInputStream(imageUri)
            val outputStream = FileOutputStream(imageFile)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            // Resize the image before compressing it
            val originalBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            val targetWidth = 420 // Set the desired width
            val targetHeight = (originalBitmap.height * (targetWidth.toFloat() / originalBitmap.width)).toInt()
            val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, false)

            // Add a watermark to the resized image
            val watermarkedBitmap = addWatermark(resizedBitmap)

            // Compress the watermarked image to PNG format before saving it
            val compressedImageStream = ByteArrayOutputStream()
            watermarkedBitmap.compress(Bitmap.CompressFormat.PNG, 50, compressedImageStream)

            val compressedImageBytes: ByteArray = compressedImageStream.toByteArray()

            saveImagePathToDatabase(compressedImageBytes)

            // Now, you can use 'compressedImageBytes' as needed, e.g., to display the image or send it to a server
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    private fun addWatermark(source: Bitmap): Bitmap {
        val result = source.copy(Bitmap.Config.ARGB_8888, true) // Ensure ARGB_8888 for transparency
        val canvas = Canvas(result)

        // Load the logo image (replace 'logoBitmap' with your logo)
        val logoBitmap = BitmapFactory.decodeResource(resources, R.drawable.logo_komida)

        // Calculate the desired logo width as a percentage of the image width
        val desiredLogoWidth = result.width / 4 // 25% of the image width

        if (desiredLogoWidth > 0) {
            // Calculate the desired logo height to maintain the aspect ratio
            val scaleFactor = desiredLogoWidth.toFloat() / logoBitmap.width
            val desiredLogoHeight = (logoBitmap.height * scaleFactor).toInt()

            // Scale the logo to the desired size
            val scaledLogo = Bitmap.createScaledBitmap(logoBitmap, desiredLogoWidth, desiredLogoHeight, true)

            // Calculate the position of the logo (e.g., 10 pixels from the top-left corner)
            val logoX = 10f // X-coordinate, 10 pixels from the left edge
            val logoY = result.height - 120f // Y-coordinate, 10 pixels from the bottom edge
            // Draw the scaled logo on the canvas
            canvas.drawBitmap(scaledLogo, logoX, logoY, null)
        }

        // Define the watermark text
        // val CurrentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        // Configure the text size and color
        val textSize = 24f // Adjust the size as needed
        val textColor = Color.WHITE // Change to a visible color

        val currentDate = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID")).format(Date())
        val textX = 120f // X-coordinate, 10 pixels from the right edge

        val textY = result.height - 40f // Y-coordinate, 10 pixels from the bottom edge

        // Create a Paint object for drawing the text
        val paint = Paint()
        paint.textSize = textSize
        paint.color = textColor
        paint.textAlign = Paint.Align.LEFT // Align the text to the right

        canvas.drawText(currentDate, textX, textY, paint)


        val currentTime = SimpleDateFormat("'Jam' HH:mm zzz", Locale("id", "ID")).format(Date())
        val textXa = 120f // X-coordinate, 10 pixels from the right edge
        val textYa = result.height - 20f // Y-coordinate, 10 pixels from the bottom edge
        // Create a Paint object for drawing the text
        val painta = Paint()
        painta.textSize = textSize
        painta.color = textColor
        painta.textAlign = Paint.Align.LEFT // Align the text to the right

        canvas.drawText(currentTime, textXa, textYa, painta)


        val addWaterMarkAgain = "$nama_anggota_dtl"
        val addtextX = 120f // X-coordinate, 10 pixels from the right edge

        val addtextY = result.height - 60f // Y-coordinate, 10 pixels from the bottom edge

        // Create a Paint object for drawing the text
        val paint1 = Paint()
        paint1.textSize = textSize
        paint1.color = textColor
        paint1.textAlign = Paint.Align.LEFT // Align the text to the right

        canvas.drawText(addWaterMarkAgain, addtextX, addtextY, paint1)


        return result
    }


    fun saveImagePathToDatabase(imageBytes: ByteArray) {
        val kode_uk_foto = generateRandom("ukfoto")
        val jenis = "foto"
        // Insert the image file path into the database
        val database_mdismo = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null)
         val deleteSQL = "DELETE FROM uk_fotoktpttd_anggota WHERE jenis='$jenis' and kode_uk = '$kode_uk_dtl';"
         database_mdismo.execSQL(deleteSQL)
         val insertSQL = "INSERT INTO uk_fotoktpttd_anggota (" +
                 "kode_uk_foto, kode_uk, jenis, foto_anggota, ttd_anggota, usr_crt, dt_crt, usr_upd, dt_upd) VALUES (" +
                 "'$kode_uk_foto', '$kode_uk_dtl', '$jenis', ?, '', " +
                 "'$nik_st', CURRENT_TIMESTAMP, '$nik_st', CURRENT_TIMESTAMP);"

         val statement = database_mdismo.compileStatement(insertSQL)
         statement.bindBlob(1, imageBytes)

         statement.execute()
        database_mdismo.close() // Close the database when you're done
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
    fun generateRandom(prefix: String): String {
        val length = 10 // Specify the desired length of the random string
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val randomString = RandomStringGenerator().generateRandomString(length)
        val resultString = "$prefix-$currentDate-$randomString"
        println("Random String: $resultString")
        return resultString
    }
    private fun CreateImageUri():Uri {
        val image = File(filesDir,"camera_photos.png")
        return FileProvider.getUriForFile(this,"com.komida.co.id.mdisujikelayakan.FileProvider"
            ,image)

    }
    fun checkFotoAnggota() {
        val kode_uk_to_retrieve = kode_uk_dtl
        val jenis:String = "foto"
        val imageBytes = getImageFromDatabase(kode_uk_to_retrieve,jenis)

        if (imageBytes != null) {
            // Process or display the image as needed
            setImageToImageView(imageBytes, imViewFotoAnggota)
        }

    }
    fun setImageToImageView(imageData: ByteArray?, imageView: ImageView?) {
        if (imageData != null && imageView != null) {
            val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
            imageView.setImageBitmap(bitmap)
            getImageInfo(imageData)
        }
    }

    private fun setupView() {
        kode_uk_index_tv = findViewById(R.id.kode_uk_index_tv)
        nik_ktp_uk_index_tv = findViewById(R.id.nik_ktp_uk_index_tv)
        nama_anggota_uk_index_tv = findViewById(R.id.nama_anggota_uk_index_tv)
        center_keluarga_tv = findViewById(R.id.center_keluarga_tv)
        kelompok_keluarga_tv = findViewById(R.id.kelompok_keluarga_tv)

        imViewFotoAnggota = findViewById(R.id.imViewFotoAnggota)
        btn_capture = findViewById(R.id.btn_capture)
    }
    private fun getPreferenceAnggota(intent: Intent?) {
        kode_uk_dtl = intent?.getStringExtra("kode_uk_dtl") ?: ""
        nama_anggota_dtl = intent?.getStringExtra("nama_anggota_dtl") ?: ""
        nik_ktp_dtl = intent?.getStringExtra("nik_ktp_dtl") ?: ""
        tipe_dtl = intent?.getStringExtra("tipe_dtl") ?: ""
        center_dtl = intent?.getStringExtra("center_dtl") ?: ""
        kelompok_dtl = intent?.getStringExtra("kelompok_dtl") ?: ""
    }
    fun prepareItem() {
        kode_uk_index_tv?.text = kode_uk_dtl
        nik_ktp_uk_index_tv?.text = nik_ktp_dtl
        nama_anggota_uk_index_tv?.text = nama_anggota_dtl
        center_keluarga_tv?.text = center_dtl
        kelompok_keluarga_tv?.text = kelompok_dtl

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