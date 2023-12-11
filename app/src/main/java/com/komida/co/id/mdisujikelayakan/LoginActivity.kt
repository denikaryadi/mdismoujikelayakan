package com.komida.co.id.mdisujikelayakan

import android.annotation.SuppressLint
import android.content.ContextWrapper
import android.content.Context
import android.content.Intent

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dmax.dialog.SpotsDialog
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import javax.crypto.Cipher

class LoginActivity : AppCompatActivity() {
    private lateinit var logoMdismo :ImageView
    private lateinit var tilNikHris : TextInputLayout
    private lateinit var etNikHris :TextInputEditText
    private lateinit var tilPassword  :TextInputLayout
    private lateinit var etPassword :TextInputEditText
    private lateinit var loading :ProgressBar
    private lateinit var buttonLogin :Button
    private val URL_LOGIN = "https://komida.co.id/regist/login_mdismo.php"
    private lateinit var preferences: SharedPreferences
    var RELEASE: String? = null
    var DEVICE: String? = null
    var MODEL: String? = null
    var PRODUCT: String? = null
    var BRAND: String? = null
    var OSNAME: String? = null
    var OSVERSION: String? = null
    var ID: String? = null
    var SERIAL: String? = null
    var MANUFACTURER: String? = null
    var USER: String? = null
    var HOST: String? = null
    var devid: String? = null
    var nik: String? = null
    var bitm: String? = null
    private lateinit var progressDialog: AlertDialog
    private val URL = "https://komida.co.id/mdismo/simpan_device.php"
    private val KEY_NAME = "hriskomida"
    private lateinit var cipher: Cipher
    private lateinit var textView: TextView
    private lateinit var executor: Executor
    private lateinit var executor2: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var biometricPrompt2: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var promptInfo2: BiometricPrompt.PromptInfo
    private val BIOMETRIC_STRONG = 125
    private val DEVICE_CREDENTIAL = 125
    private val REQUEST_CODE = 125

    override fun onCreate(savedInstanceState: Bundle?) {
        // Remove the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)

// Set the application to full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupVIew()


        buttonLogin.setOnClickListener {
            val strNikHris = etNikHris.text.toString()
            val strPassword = etPassword.text.toString()

            tilNikHris.error = null
            tilPassword.error = null

            if (strNikHris.isEmpty()) {
                tilNikHris.error = "Tolong Masukan Nik Hris Anda"
                etNikHris.requestFocus()
                return@setOnClickListener
            }

            if (strPassword.isEmpty()) {
                tilPassword.error = "Tolong Masukan Password Anda"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            // If both fields are not empty, call the login function
            login(strNikHris, strPassword)
        }

        val form = findViewById<View>(R.id.form)


        val alphaAnimation = AlphaAnimation(0.2f, 1.0f)
        alphaAnimation.duration = 600
        alphaAnimation.repeatCount = 2
        alphaAnimation.repeatMode = Animation.REVERSE
        logoMdismo.startAnimation(alphaAnimation)

        val alphaAnimation2 = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation2.duration = 500

        alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                // onAnimationStart callback
            }

            override fun onAnimationEnd(animation: Animation) {
                form.visibility = View.VISIBLE
                form.startAnimation(alphaAnimation2)

                logoMdismo.animate()
                    .translationY(-(form.height / 2.0f))
                    .setInterpolator(LinearOutSlowInInterpolator())
                    .setDuration(1000)
                    .withEndAction {
                        // After animation ends, check if the user is logged in
                        if (isLoggedIn()) {
                            val nikmat = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(nikmat)
                            finish()
                        }
                    }
            }

            override fun onAnimationRepeat(animation: Animation) {
                // onAnimationRepeat callback
            }
        })


    }
    fun isLoggedIn(): Boolean {
        return preferences.getBoolean("login_app", false)
    }
    private fun daftar_device_login() {
        val stringRequest = object : StringRequest(
            Request.Method.POST, URL,
            Response.Listener { response ->
                val s = response.trim()
                if (!s.equals("Loi", ignoreCase = true)) {
                    // Device data successfully saved
                } else {
                    // Data failed to be saved
                }
            },
            Response.ErrorListener { error ->
                // Handle error when there is a network issue
            }) {
            override fun getParams(): Map<String, String> {
                val niki = etNikHris.text?.toString()?.trim() ?: ""

                val params = HashMap<String, String>()
                params["devid"] = devid ?: ""
                params["release"] = RELEASE ?: ""
                params["model"] = MODEL ?: ""
                params["brand"] = BRAND ?: ""
                params["os"] = OSNAME ?: ""
                params["buatan"] = MANUFACTURER ?: ""
                params["nik"] = niki

                return params
            }

        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }


    private fun poto_profil(nik: String) {
        val niks = nik.replace("/", "-")
        val urlp = "https://www.komida.co.id/apphris/muka/$niks.jpg"

        if (adaInternet()) {
            val bitm: Bitmap? = getBitmapFromURL(urlp)
            if (bitm != null) {
                saveToInternalStorage(bitm)
            } else {
                // Handle the case when getBitmapFromURL returns null
            }

        } else {
            // Handle the case when there is no internet connection
        }
    }
    fun getBitmapFromURL(imageUrl: String): Bitmap? {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection

            connection.doInput = true
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val imageBitmap = BitmapFactory.decodeStream(inputStream)

            return imageBitmap
        } catch (e: IOException) {
            return null
        }
    }
    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(applicationContext)
        // Path to /data/data/yourapp/app_data/imageDir
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "profil.jpg")

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the Bitmap object to write the image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.absolutePath
    }



    private fun adaInternet(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null

    }

    private fun gagal_app(ketnih: String) {
        val teksket: TextView
        val btnUpdate: Button
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val builder = AlertDialog.Builder(this)
        val view1 = LayoutInflater.from(this).inflate(R.layout.gagal_app, viewGroup, false)
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

    private fun daftar_device(ketnih: String) {
        val teksket: TextView
        val btnUpdate: Button
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val builder = AlertDialog.Builder(this)
        val view1 = LayoutInflater.from(this).inflate(R.layout.daftar_device, viewGroup, false)
        builder.setCancelable(false)
        builder.setView(view1)
        val alertDialog = builder.create()

        btnUpdate = view1.findViewById(R.id.btn_dialog)

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnUpdate.setOnClickListener { view ->
            // Handle button click
            progressDialog = SpotsDialog(this, R.style.simpandevice) as AlertDialog
            progressDialog.show()
            //Toast.makeText(this, "MASUK SINI GAES "+devid, Toast.LENGTH_SHORT).show()

            val stringRequest = object : StringRequest(
                Request.Method.POST, URL,
                Response.Listener { response ->
                    val s = response.trim()
                    //Toast.makeText(this, "MASUK SIMPAN DEVICE INTENT"+s, Toast.LENGTH_SHORT).show()

                    if (!s.equals("Loi", ignoreCase = true)) {
                        preferences.edit().putBoolean("login", true).apply()
                        //Toast.makeText(this, "Device berhasil disimpan", Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
                        alertDialog.dismiss()
                        masuk_abis_daftar()
                    } else {
                        progressDialog.dismiss()
                       // Toast.makeText(this, "Data gagal disimpan", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    //Toast.makeText(this, "Koneksi sedang tidak baik-baik saja", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }) {
                override fun getParams(): Map<String, String> {


                    val niki = etNikHris.text.toString().trim()
                    val params = HashMap<String, String>()
                    params["devid"] = devid ?: ""
                    params["release"] = RELEASE ?: ""
                    params["model"] = MODEL ?: ""
                    params["brand"] = BRAND ?: ""
                    params["os"] = OSNAME ?: ""
                    params["buatan"] = MANUFACTURER ?: ""
                    params["nik"] = niki
                    return params
                }
            }

            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(stringRequest)
        }

        alertDialog.show()
    }

    private fun masuk_abis_daftar() {
       // TODO("Not yet implemented")
        Toast.makeText(this, "MASUK LOGIN INTENT"+devid, Toast.LENGTH_SHORT).show()

        val nikmat = Intent(this, MainActivity::class.java)
        startActivity(nikmat)
        finish()
    }


    private fun login(strNikHris: String, strPassword: String) {
        loading.visibility = View.VISIBLE
        buttonLogin.setVisibility(View.GONE)
        //Toast.makeText(this, "cek Volley"+strNikHris+"_"+strPassword, Toast.LENGTH_SHORT).show()
        //Toast.makeText(this, "URL Volley"+URL_LOGIN, Toast.LENGTH_SHORT).show()

        val stringRequest = object : StringRequest(
            Request.Method.POST, URL_LOGIN,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val sukses = jsonObject.getString("sukses")
                    val jsonArray = jsonObject.getJSONArray("login")
                  //  Toast.makeText(this, "tipe sukses"+sukses, Toast.LENGTH_SHORT).show()

                    if (sukses == "1") {
                        for (i in 0 until jsonArray.length()) {
                            val object1 = jsonArray.getJSONObject(i)
                            val nama = object1.getString("nama").trim()
                            val nik = object1.getString("nik").trim()
                            val hp = object1.getString("hp").trim()
                            val cabang = object1.getString("cabang").trim()
                            val jabatan = object1.getString("jabatan").trim()
                            val email = object1.getString("email").trim()
                            val kodecabang = object1.getString("kodecabang").trim()
                            val devidD = object1.getString("devid").trim()
                            val rilis = object1.getString("rilis").trim()
                            val model = object1.getString("model").trim()
                            val brand = object1.getString("brand").trim()
                            val os = object1.getString("os").trim()
                            val buatan = object1.getString("buatan").trim()
                            val pakai = object1.getString("pakai").trim()
                            val pakainama = object1.getString("pakainama").trim()
                            val pakaiuser = object1.getString("pakaiuser").trim()
                            Thread {
                                poto_profil(nik)
                            }.start()
                            //Toast.makeText(this, "DEVICE ID Hasil "+devidD, Toast.LENGTH_SHORT).show()
                            //Toast.makeText(this, "DEVICE ID SISTEM Hasil "+devid, Toast.LENGTH_SHORT).show()

                            // If devidD matches devid
                           // if (devidD == devid) {
                                preferences.edit().putBoolean("login_app", true).apply()
                                preferences.edit().putString("nik", nik).apply()
                                preferences.edit().putString("nama", nama).apply()
                                preferences.edit().putString("hp", hp).apply()
                                preferences.edit().putString("devid", devid).apply()
                                preferences.edit().putString("cabang", cabang).apply()
                                preferences.edit().putString("jabatan", jabatan).apply()
                                preferences.edit().putString("kodecabang", kodecabang).apply()
                                preferences.edit().putString("email", email).apply()
                                preferences.edit().putString("devidD", devidD).apply()
                                //daftar_device_login()
                                Thread {
                                    poto_profil(nik)
                                }.start()
                                val nikmat = Intent(this, MainActivity::class.java)
                                startActivity(nikmat)
                                finish()
                           // }

                            // If devidD is different
                            /*
                            if (devidD != devid) {
                                Toast.makeText(this, "MASUK SINI HARUSNYA "+devid, Toast.LENGTH_SHORT).show()

                                if (devidD == "") {
                                    if (pakai == "terpakai") {
                                        gagal_app("Anda login di device staff lain  : \n " +
                                                "NIK : $pakaiuser\n" +
                                                "Nama : $pakainama\n" +
                                                "Silahkan hubungi HRD untuk mereset device user tersebut"
                                        )
                                        loading.visibility = View.GONE
                                        buttonLogin.visibility = View.VISIBLE
                                    } else {

                                        preferences.edit().putBoolean("login_app", true).apply()
                                        preferences.edit().putString("nik", nik).apply()
                                        preferences.edit().putString("nama", nama).apply()
                                        preferences.edit().putString("hp", hp).apply()
                                        preferences.edit().putString("devid", devid).apply()
                                        preferences.edit().putString("cabang", cabang).apply()
                                        preferences.edit().putString("jabatan", jabatan).apply()
                                        preferences.edit().putString("kodecabang", kodecabang).apply()
                                        preferences.edit().putString("email", email).apply()
                                        preferences.edit().putString("devidD", devidD).apply()
                                        Toast.makeText(this, "MASUK DAFTAR DEVICE "+devid, Toast.LENGTH_SHORT).show()

                                        daftar_device("")
                                    }
                                } else {
                                    Toast.makeText(this, "GAGAL LOGIN HARUSNYA "+devid, Toast.LENGTH_SHORT).show()

                                    gagal_app("Anda login di device yang berbeda \n \n \n Device anda sebelumnya : \n " +
                                            "$rilis\n" +
                                            "$model $os\n" +
                                            "$brand $buatan\n \n" +
                                            "Silahkan hubungi HRD untuk mereset device anda"
                                    )
                                    loading.visibility = View.GONE
                                    buttonLogin.visibility = View.VISIBLE
                                }
                            }
                             */
                        }
                    }

                    if (sukses == "0") {
                        preferences.edit().putBoolean("login_app", false).apply()
                        val pesan = jsonObject.getString("pesan")
                        loading.visibility = View.VISIBLE
                        buttonLogin.visibility = View.GONE
                        gagal_app("NIK atau Password anda salah")
                        loading.visibility = View.GONE
                        buttonLogin.visibility = View.VISIBLE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    gagal_app("Server Error, Coba Kembali")
                    loading.visibility = View.GONE
                    buttonLogin.visibility = View.VISIBLE
                }
            },
            Response.ErrorListener { error ->
                gagal_app("Cek Koneksi Anda")
                Toast.makeText(this, "Cek Koneksi Anda", Toast.LENGTH_SHORT).show()
                loading.visibility = View.GONE
                buttonLogin.visibility = View.VISIBLE
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["nik"] = strNikHris
                params["password"] = strPassword
                params["devid"] =   devid ?: ""
                params["versi"] = BuildConfig.VERSION_CODE.toString()
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)


    }
    /*
    private fun isLoggedIn(): Boolean {

    }
    */

    @SuppressLint("HardwareIds")
    private fun setupVIew() {
        tilNikHris  =findViewById(R.id.tilNikHris)
        etNikHris = findViewById(R.id.etNikHris)
        tilPassword = findViewById(R.id.tilPassword)
        etPassword = findViewById(R.id.etPassword)
        loading = findViewById(R.id.loading)
        buttonLogin = findViewById(R.id.login)
        logoMdismo = findViewById(R.id.logo)

        preferences = getSharedPreferences("id.co.komida.mdismoujikelayakan", Context.MODE_PRIVATE)
        devid = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        RELEASE = android.os.Build.VERSION.RELEASE
        DEVICE = android.os.Build.DEVICE
        MODEL = android.os.Build.MODEL
        PRODUCT = android.os.Build.PRODUCT
        BRAND = android.os.Build.BRAND
        OSNAME = System.getProperty("os.name")
        OSVERSION = System.getProperty("os.version")
        ID = android.os.Build.ID
        MANUFACTURER = android.os.Build.MANUFACTURER
        SERIAL = android.os.Build.SERIAL
        USER = android.os.Build.USER
        HOST = android.os.Build.HOST
        var bitm: Bitmap? = null

    }
}