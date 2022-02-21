package com.example.chatapplication

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class user_profile_edit : AppCompatActivity() {

    private  lateinit var toolbar:androidx.appcompat.widget.Toolbar
    private lateinit var edtName: EditText
    private lateinit var btnSave: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var userImage: ImageView
    private lateinit var editPicture: TextView
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_edit)

        mAuth= FirebaseAuth.getInstance()
        mDbRef= FirebaseDatabase.getInstance().getReference()

        toolbar=findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener{
            finish()
        }
        toolbar.title="Profile"

        edtName=findViewById(R.id.edt_name)
        btnSave=findViewById(R.id.btnSave)
        userImage=findViewById(R.id.user_image)
        editPicture=findViewById(R.id.editPic)

        editPicture.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        val storageRef= FirebaseStorage.getInstance().reference.child("images/${mAuth.currentUser?.uid.toString()}")
        val localfile= File.createTempFile("tempImage","webp")
        storageRef.getFile(localfile).addOnSuccessListener {
            val bitmap= BitmapFactory.decodeFile(localfile.absolutePath)
            userImage.setImageBitmap(bitmap)
        }.addOnFailureListener{
            userImage.setImageResource(R.drawable.new_wine)
        }
        mDbRef.child("user").child("${mAuth.currentUser?.uid}").child("name").get().addOnSuccessListener {
            edtName.setText(it.value.toString())
        }.addOnFailureListener{

        }
        btnSave.setOnClickListener{

            val storageRef=FirebaseStorage.getInstance().getReference("images/${mAuth.currentUser?.uid.toString()}")
            if(imageUri!=null) {
                storageRef.putFile(imageUri as Uri).addOnSuccessListener {
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Image upload Failed, something may have occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            val name=edtName.text.toString()
            mDbRef.child("user").child("${mAuth.currentUser?.uid}").child("name")
                .setValue("$name")
                .addOnSuccessListener {
                    mDbRef.child("user").child("${mAuth.currentUser?.uid}").child("name")
                        .setValue("$name")
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this, "User Name upload Failed, something may have occurred", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            userImage.setImageURI(imageUri)
        }
    }
}