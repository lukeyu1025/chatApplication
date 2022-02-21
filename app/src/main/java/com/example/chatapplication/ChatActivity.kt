package com.example.chatapplication

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.util.*


class ChatActivity : AppCompatActivity() {

    private lateinit var ChatRecyclerView: RecyclerView
    private lateinit var messageBox:EditText
    private lateinit var sendButton:ImageView
    private lateinit var imageButton:ImageView
    private lateinit var emojiButton:ImageView
    private lateinit var messageAdapter:MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef:DatabaseReference
    private  lateinit var toolbar:androidx.appcompat.widget.Toolbar
    private lateinit var mAuth: FirebaseAuth

    var receiverRoom:String?=null
    var senderRoom:String?=null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        toolbar=findViewById(R.id.toolbar)
        //toolbar.inflateMenu(R.menu.menu)
        toolbar.setNavigationOnClickListener{
            finish()
        }
        toolbar.setOnMenuItemClickListener(){
            when(it.itemId){
                R.id.menu_share-> {
                    //TODO:SHARE APP WITH FRIEND
                    Toast.makeText(this, "Coming Soon~", Toast.LENGTH_SHORT).show()
                }
                R.id.menu_user -> {
                    val intent=Intent(this,user_profile_edit::class.java)
                    startActivity(intent)
                }
                R.id.menu_setting -> {
                    val intent=Intent(this@ChatActivity,SettingsActivity::class.java)
                    startActivity(intent)
                }
            }
            false
        }
        val name=intent.getStringExtra("name")
        val receiverUid=intent.getStringExtra("uid")

        val senderUid=FirebaseAuth.getInstance().uid

        senderRoom=receiverUid + senderUid
        receiverRoom=senderUid+receiverUid

        toolbar.title=name

        ChatRecyclerView=findViewById(R.id.chatRecyclerView)
        messageBox=findViewById(R.id.messageBox)
        sendButton=findViewById(R.id.sentButton)
        imageButton=findViewById(R.id.imageButton)
        emojiButton=findViewById(R.id.stickerButton)
        messageList= ArrayList()
        messageAdapter= MessageAdapter(this,messageList)
        mDbRef=FirebaseDatabase.getInstance().getReference()

        ChatRecyclerView.layoutManager=LinearLayoutManager(this)
        ChatRecyclerView.adapter=messageAdapter

        mDbRef.child("chats").child(senderRoom!!).child("messages").
            addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children){

                        val message=postSnapshot.getValue(Message::class.java)
                        println("Call: message=${message?.message}")
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {

                }

            })

        //adding data to database
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            if (message != "") {
                val currentTime = LocalDateTime.now()
                val time =
                    "${currentTime.monthValue}/${currentTime.dayOfMonth} ${currentTime.hour}:${currentTime.minute}"
                val messageObject = Message(message, senderUid, time)
                println("Time:$time")
                mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject)
                    .addOnSuccessListener {
                        mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }
                messageBox.setText("")
            }
        }
        emojiButton.setOnClickListener{
            val alertDialog = Dialog(this)
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val view: View = layoutInflater.inflate(R.layout.emoji_keyboard, null)
            alertDialog.setContentView(view)
            alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val window: Window? = alertDialog.getWindow()
            val wlp = window?.attributes
            wlp?.gravity = Gravity.BOTTOM
            wlp?.y=250
            wlp?.x=500
            wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv())
            window?.attributes = wlp

            alertDialog.show()
            val emoji00 : Button = view.findViewById(R.id.emoji_00)
            val emoji01 : Button = view.findViewById(R.id.emoji_01)
            val emoji02 : Button = view.findViewById(R.id.emoji_02)
            val emoji03 : Button = view.findViewById(R.id.emoji_03)
            val emoji04 : Button = view.findViewById(R.id.emoji_04)
            val emoji05 : Button = view.findViewById(R.id.emoji_05)
            emoji00.setOnClickListener{
                messageBox.text.append("${emoji00.text}")
            }
            emoji01.setOnClickListener{
                messageBox.text.append("${emoji01.text}")
            }
            emoji02.setOnClickListener{
                messageBox.text.append("${emoji02.text}")
            }
            emoji03.setOnClickListener{
                messageBox.text.append("${emoji03.text}")
            }
            emoji04.setOnClickListener{
                messageBox.text.append("${emoji04.text}")
            }
            emoji05.setOnClickListener{
                messageBox.text.append("${emoji05.text}")
            }
        }
        imageButton.setOnClickListener{
            //TODO:SEND IMAGE IN CHAT
            Toast.makeText(this, "Coming Soon~", Toast.LENGTH_SHORT).show()
        }
    }
//    override fun onCreateOptionsMenu(menu:Menu?):Boolean{
//        menuInflater.inflate(R.menu.menu,menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(item.itemId==R.id.logout){
//            return true
//        }
//        return true
//    }
}