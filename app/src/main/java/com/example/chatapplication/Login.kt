package com.example.chatapplication

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var edtEmail:EditText
    private lateinit var edtPassword:EditText
    private lateinit var btnLogin:Button
    private lateinit var btnSignUp:Button
    private lateinit var mAuth:FirebaseAuth
    private lateinit var visiblePassword: ImageView
    private lateinit var forgetPassword:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this);
        supportActionBar?.hide()

        mAuth= FirebaseAuth.getInstance()

        edtEmail=findViewById(R.id.edt_email)
        edtPassword=findViewById(R.id.edt_password)
        btnLogin=findViewById(R.id.btnLogin)
        btnSignUp=findViewById(R.id.btnSignup)
        visiblePassword=findViewById(R.id.visibleLoginPassword)
        forgetPassword=findViewById(R.id.forgetPassword)

        var initsetting = getSharedPreferences("DATA", 0)
        val initemail=initsetting.getString("email", "")
        val initpassword=initsetting.getString("password", "")
        if(initemail!=""&&initpassword!=""){
            login(initemail.toString(),initpassword.toString())
        }

        btnSignUp.setOnClickListener{
            val intent=Intent(this,SignUp::class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener{
            val email=edtEmail.text.toString()
            val password=edtPassword.text.toString()
            login(email,password)
        }

        var touch1=false
        visiblePassword.setOnClickListener(){
            println(edtPassword.inputType)
            if(!touch1){
                visiblePassword.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                edtPassword.inputType=145
                touch1=true
            }
            else{
                visiblePassword.setImageResource(R.drawable.ic_baseline_visibility_24)
                edtPassword.inputType=129
                touch1=false
            }
        }

        forgetPassword.setOnClickListener{
            val intent=Intent(this,forget_password::class.java)
            startActivity(intent)
        }

    }

    private fun login(email:String,password:String){
        if (email!=""&&email!=null&&password!=""&&password!=null) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        var settings = getSharedPreferences("DATA", 0)
                        settings.edit()
                            .putString("email", email)
                            .putString("password", password)
                            .apply()

                        val intent = Intent(this@Login, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@Login, "User does not exist", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        else{
            Toast.makeText(this@Login, "Invalid input", Toast.LENGTH_SHORT).show()
        }
    }
}