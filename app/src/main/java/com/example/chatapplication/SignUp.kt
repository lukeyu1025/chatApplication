package com.example.chatapplication

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SignUp : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var edtName: EditText
    private lateinit var btnSignUp: Button
    private lateinit var login:TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference
    private lateinit var visiblePassword:ImageView
    private lateinit var visibleConfirmPassword:ImageView
    private lateinit var userImage:ImageView
    private lateinit var editPicture:TextView
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        mAuth= FirebaseAuth.getInstance()

        edtEmail=findViewById(R.id.edt_email)
        edtPassword=findViewById(R.id.edt_password)
        edtConfirmPassword=findViewById(R.id.edt_checkPassword)
        btnSignUp=findViewById(R.id.btnSignup)
        edtName=findViewById(R.id.edt_name)
        login=findViewById(R.id.haveAccount)
        visiblePassword=findViewById(R.id.visiblePassword)
        visibleConfirmPassword=findViewById(R.id.visibleConfirmPassword)
        userImage=findViewById(R.id.user_image)
        editPicture=findViewById(R.id.editPic)

        btnSignUp.setOnClickListener{
            val name=edtName.text.toString()
            val email=edtEmail.text.toString()
            val password=edtPassword.text.toString()
            val confirmPassword=edtConfirmPassword.text.toString()
            if(password==""||confirmPassword==""){
                Toast.makeText(
                    this,
                    "Password and Confirm Password shouldn't be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                if (password == confirmPassword) {
                    if (name == "" || email == "") {
                        Toast.makeText(
                            this,
                            "Name and Email shouldn't be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        signUp(name, email, password)
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Conform Password is different from Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        login.setOnClickListener{
            val intent=Intent(this,Login::class.java)
            startActivity(intent)
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
        var touch2=false
        visibleConfirmPassword.setOnClickListener(){
            println(edtPassword.inputType)
            if(!touch2){
                visibleConfirmPassword.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                edtConfirmPassword.inputType=145
                touch2=true
            }
            else{
                visibleConfirmPassword.setImageResource(R.drawable.ic_baseline_visibility_24)
                edtConfirmPassword.inputType=129
                touch2=false
            }
        }


        editPicture.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            userImage.setImageURI(imageUri)
        }
    }

    private fun signUp(name:String,email:String,password:String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name,email,mAuth.currentUser?.uid!!,userImage)
                    val user=mAuth.currentUser
                    user?.sendEmailVerification()?.addOnSuccessListener {
                        Toast.makeText(this, "Verify Email has been sent", Toast.LENGTH_SHORT).show()
                    }

                    val intent=Intent(this@SignUp,Login::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this@SignUp, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }

    }
    private  fun addUserToDatabase(name:String,email: String,uid:String,img:ImageView){
        mDbRef=FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name,email,uid))
        val storageRef=FirebaseStorage.getInstance().getReference("images/$uid")
        storageRef.putFile(imageUri as Uri).addOnSuccessListener {
            Toast.makeText(this, "Upload Success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Upload Failed, something may have occurred", Toast.LENGTH_SHORT).show()
        }
    }
}