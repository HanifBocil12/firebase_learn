package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var etPas: EditText
    //    private lateinit var etPhone: EditText
    private lateinit var etGmail: EditText

    companion object{
        private  const val Rc_dign = 1001
    }
//

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser!=null){
            Intent(this,MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mAuth.currentUser.hashCode()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        etPas = findViewById(R.id.etPas)
        etGmail = findViewById(R.id.etGmail)

        findViewById<ImageButton>(R.id.btn_gogle).setOnClickListener{
            val signIntent = googleSignInClient.signInIntent
            startActivityForResult(signIntent, Rc_dign)
        }

        findViewById<Button>(R.id.btn_Login).setOnClickListener {

            if (etGmail.text.toString().isNotEmpty() && etPas.text.toString().isNotEmpty()) {
                processLogin()
            } else {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btn_Regisis).setOnClickListener {
            val intent = Intent(this, Regis::class.java)
            startActivity(intent);
        }
    }

    private fun processLogin() {

        mAuth.signInWithEmailAndPassword(etGmail.text.toString(),etPas.text.toString())
            .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Rc_dign){
            //Penaganan Proses Login
            val task= GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //hasil berhasil
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthGoogle(account.idToken!!)
            }catch (e: ApiException){
                Toast.makeText(application, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthGoogle(idToken:String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }
}