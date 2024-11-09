package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        val recyclerView= findViewById<RecyclerView>(R.id.recyclerView)

        val fireUser = mAuth.currentUser
        if (fireUser != null) {
            findViewById<TextView>(R.id.nm).text = fireUser.displayName

//            findViewById<Button>(R.id.btn_Regisis).setOnClickListener {
//                val intent = Intent(this, Regis::class.java)
//                startActivity(intent);
//            }

            add.setOnClickListener {
                val intent = Intent(this, Add::class.java)
                startActivity(intent)
            }

            db.collection("users").get().addOnCompleteListener(
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
                            recyclerView.setAdapter(adapter)

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

            findViewById<Button>(R.id.reload).setOnClickListener {
                db.collection("users").get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val arrayList:ArrayList<User> = ArrayList<User>()
                        for (document in task.result) {
                            val user = document.toObject(User::class.java)
                            user.id = document.id
                            arrayList.add(user)
                        }
                        val adapter = UserAdapter(this@MainActivity, arrayList)
                        recyclerView.adapter = adapter

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
                            Objects.requireNonNull(task.exception)!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        else{
            val intent = Intent(this, Login::class.java)
            finish()
        }
    }
}