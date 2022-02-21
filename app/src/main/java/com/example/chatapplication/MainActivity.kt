package com.example.chatapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList:ArrayList<User>
    private  lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference
    private  lateinit var toolbar:androidx.appcompat.widget.Toolbar

    private lateinit var dialogBuilder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private lateinit var btn_popup_ok:Button
    private lateinit var btn_popup_cancel:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar=findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener{

            val alertDialog = Dialog(this)
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val view: View = layoutInflater.inflate(R.layout.popup_logout, null)
            alertDialog.setContentView(view)
            alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
            val btn_popup_ok : Button = view.findViewById(R.id.btn_popup_ok)
            val btn_popup_cancel: Button = view.findViewById(R.id.btn_popup_cancel)
            btn_popup_ok.setOnClickListener{
                var settings = getSharedPreferences("DATA", 0)
                settings.edit()
                    .putString("email","")
                    .putString("password","")
                    .apply()

                val intent=Intent(this,Login::class.java)
                startActivity(intent)
            }
            btn_popup_cancel.setOnClickListener{
                alertDialog.hide()
            }
        }
        toolbar.title="Chat"
        toolbar.setOnMenuItemClickListener(){
            when(it.itemId){
                R.id.menu_share-> {
                    Toast.makeText(this, "Coming Soon~", Toast.LENGTH_SHORT).show()
                    //TODO:share
                }
                R.id.menu_user -> {
                    val intent=Intent(this@MainActivity,user_profile_edit::class.java)
                    startActivity(intent)
                }
                R.id.menu_setting -> {
                    val intent=Intent(this@MainActivity,SettingsActivity::class.java)
                    startActivity(intent)
                }
            }
            false
        }

        mAuth= FirebaseAuth.getInstance()
        mDbRef=FirebaseDatabase.getInstance().getReference()

        userList= ArrayList()
        adapter= UserAdapter(this,userList)

        userRecyclerView=findViewById(R.id.userRecyclerView)

        userRecyclerView.layoutManager=LinearLayoutManager(this)
        userRecyclerView.adapter=adapter

        mDbRef.child("user").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser=postSnapshot.getValue(User::class.java)

                    if(mAuth.currentUser?.uid!=currentUser?.uid){
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}