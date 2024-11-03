package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.userProfileChangeRequest

class Regis : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var verfyId: String
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    // Inisialisasi EditText dan ProgressBar
    private lateinit var etName: EditText
    private lateinit var etPas: EditText
//    private lateinit var etPhone: EditText
    private lateinit var etGmail: EditText
    private lateinit var bar: ProgressBar


    override fun onStart() {
        super.onStart()
        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (user!=null){
            Intent(this,MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_regis)

        //deklar()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //btn()

        etName = findViewById(R.id.etName)
        etGmail = findViewById(R.id.etGmail)
        etPas = findViewById(R.id.etPas)

        findViewById<Button>(R.id.btn_Regis).setOnClickListener {

            if (etName.text.toString().isNotEmpty() && etGmail.text.toString().isNotEmpty() && etPas.text.toString().isNotEmpty()) {
                processRegis()
            } else {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btn_Login).setOnClickListener {
            Intent(this, Login::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun processRegis() {

        mAuth.createUserWithEmailAndPassword(etGmail.text.toString(),etPas.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val userUpdate = userProfileChangeRequest {
                        displayName = etName.text.toString()
                    }
                    val user = task.result.user
                    user!!.updateProfile(userUpdate)
                        .addOnCompleteListener {
                            startActivity(Intent(this, MainActivity::class.java))
                        }

                        .addOnFailureListener { error2 ->
                            Toast.makeText(this, error2.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }


    /*
    private fun btn() {
        findViewById<Button>(R.id.btn_Regis).setOnClickListener {
            val name = etName.text.toString()
            val phone = etPhone.text.toString()
            val password = etPas.text.toString()

            if (name.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty()) {
                Otp(phone)
                bar.visibility = View.VISIBLE
                findViewById<LinearLayout>(R.id.llOtp).visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btn_Verfy).setOnClickListener {
            val code = findViewById<EditText>(R.id.etOtp).text.toString()
            if (code.isEmpty()) {
                Toast.makeText(this, "Kode OTP Salah", Toast.LENGTH_SHORT).show()
            } else {
                verfyCode(code)
            }
        }
    }

    private fun deklar() {
        etName = findViewById(R.id.etName)
        etPas = findViewById(R.id.etPas)
        etPhone = findViewById(R.id.etPhone)
        bar = findViewById(R.id.bar)


        // Inisialisasi Callback
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                loginByCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e("FirebaseAuth", "Verification failed", e)
                Toast.makeText(this@Regis, "Verifikasi gagal: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                verfyId = verificationId
                Toast.makeText(application, "Mengirim OTP", Toast.LENGTH_SHORT).show()
                bar.visibility = View.VISIBLE
            }
        }
    }

    private fun reset() {
        etName.setText("")
        etPhone.setText("")
        etPas.setText("")
    }

    private fun verfyCode(code: String) {
        val auth: PhoneAuthCredential = PhoneAuthProvider.getCredential(verfyId, code)
        loginByCredential(auth)
    }

    private fun loginByCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Verifikasi Berhasil", Toast.LENGTH_SHORT).show()
                    Intent(this, Login::class.java).also {
                        startActivity(it)
                    }
                } else {
                    Toast.makeText(this, "Verifikasi Gagal", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun Otp(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+62$phoneNumber") // Pastikan format nomor benar
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }*/
}
