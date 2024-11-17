package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Objects

class Add : AppCompatActivity() {
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userId = mAuth.uid

        val userDocRef = db.collection("Users").document(userId!!)
        val userCollectionRef = userDocRef.collection("dataUser")
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) // Set toolbar as ActionBar
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itSave -> {
                Toast.makeText(
                    this,"Succes", Toast.LENGTH_SHORT
                ).show()
                val user: MutableMap<String, Any> = HashMap()
                user["users"] = Objects.requireNonNull<Editable>(findViewById<EditText>(R.id.et_Judul).text).toString()

                val userId = mAuth.uid

                val userDocRef = db.collection("Users").document(userId!!)
                val userCollectionRef = userDocRef.collection("dataUser")
                userCollectionRef.add(user)
                    .addOnSuccessListener(object : OnSuccessListener<DocumentReference?> {
                        override fun onSuccess(documentReference: DocumentReference?) {
                            Toast.makeText(
                                this@Add,
                                "User Added Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }).addOnFailureListener(OnFailureListener {
                        Toast.makeText(
                            this@Add,
                            "There was an error while adding user",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
            }
        }
        return true
    }
}