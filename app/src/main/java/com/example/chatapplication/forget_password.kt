package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class forget_password : AppCompatActivity() {

    private  lateinit var toolbar:androidx.appcompat.widget.Toolbar
    private lateinit var btnVerify:Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var edtEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        toolbar=findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener{
            val intent= Intent(this,Login::class.java)
            startActivity(intent)
        }

        edtEmail=findViewById(R.id.edt_verify_email)
        btnVerify=findViewById(R.id.btn_verify)

        mAuth= FirebaseAuth.getInstance()

        btnVerify.setOnClickListener{
            val email=edtEmail.text.toString()
            if(email==""){
                Toast.makeText(this, "Email empty", Toast.LENGTH_SHORT).show()
            }else{
                verify(email)
            }
        }
    }
    private fun verify(email:String){
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            val intent=Intent(this@forget_password,Login::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "Password Reset Email has been sent", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Account does not exist", Toast.LENGTH_SHORT).show()
        }
    }
}