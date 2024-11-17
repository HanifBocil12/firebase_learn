package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.Objects

class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onStart() {
        super.onStart()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val db = FirebaseFirestore.getInstance()
        val userId = mAuth.uid

        val userDocRef = db.collection("Users").document(userId!!)
        val userCollectionRef = userDocRef.collection("dataUser")

        // Menggunakan addSnapshotListener untuk mendengarkan perubahan realtime pada Firestore
        userCollectionRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(
                    this@MainActivity,
                    exception.message,
                    Toast.LENGTH_SHORT
                ).show()
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val arrayList: ArrayList<User> = ArrayList<User>()

                // Mengambil data yang ada di snapshot
                for (document in snapshot.documents) {
                    val user = document.toObject(User::class.java)
                    user?.id = document.id
                    arrayList.add(user!!)
                }

                // Set adapter dengan data terbaru
                val adapter = UserAdapter(this@MainActivity, arrayList)
                recyclerView.adapter = adapter

                // Menetapkan onClick listener untuk item RecyclerView
                adapter.setOnItemClickListenerManually(object : UserAdapter.OnItemClickListener {
                    override fun onClick(user: User?) {
                        App.user = user
                        startActivity(
                            Intent(this@MainActivity, Edit::class.java)
                        )
                    }
                })
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = FirebaseFirestore.getInstance()
        val add = findViewById<FloatingActionButton>(R.id.addUser)
        val userId = mAuth.uid
        val recyclerView= findViewById<RecyclerView>(R.id.recyclerView)

        val userDocRef = db.collection("Users").document(userId!!)
        val userCollectionRef = userDocRef.collection("dataUser")
//        val data = hashMapOf(
//            "field1" to "value1",
//            "field2" to "value2"
//        )
//        userCollectionRef.add(data)
        // Reference ke document user berdasarkan UID

        val fireUser = mAuth.currentUser
        if (fireUser != null) {
//            db.collection("Users")
//                .get()
//                .addOnSuccessListener { documents ->
//                    for (document in documents) {
//                        // Ambil data dari dokumen dan tampilkan
//                        val data = document.getString("users")
//                        findViewById<TextView>(R.id.nm).text = data.toString()
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Toast.makeText(this, "Gagal mengambil data: ${exception.message}", Toast.LENGTH_SHORT).show()
//                }

//            findViewById<Button>(R.id.btn_Regisis).setOnClickListener {
//                val intent = Intent(this, Regis::class.java)
//                startActivity(intent);
//            }

            add.setOnClickListener {
                val intent = Intent(this, Add::class.java)
                startActivity(intent)
            }


            userCollectionRef.get().addOnCompleteListener(
                object : OnCompleteListener<QuerySnapshot?> {
                    override fun onComplete(task: Task<QuerySnapshot?>) {
                        if (task.isSuccessful) {
                            val arrayList = ArrayList<User>()
                            for (document in task.result!!) {
                                val user = document.toObject(User::class.java)
                                user.id = document.id
                                arrayList.add(user)
                            }
                            val adapter = UserAdapter(this@MainActivity, arrayList)
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                            adapter.setOnItemClickListenerManually(object : UserAdapter.OnItemClickListener {
                                override fun onClick(user: User?) {
                                    App.user = user
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            Edit::class.java
                                        )
                                    )
                                }
                            })
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                Objects.requireNonNull(task.exception)?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        }
        else{
            val intent = Intent(this, Login::class.java)
            finish()
        }
    }
}