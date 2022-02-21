package com.example.chatapplication

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class UserAdapter(val context:Context,val userList:ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var messageAdapter:MessageAdapter
    private lateinit var messageList: ArrayList<Message>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        mAuth= FirebaseAuth.getInstance()
        mDbRef= FirebaseDatabase.getInstance().getReference()

        val view:View=LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textName.text=currentUser.name

        val senderRoom=currentUser.uid.toString() + mAuth.currentUser?.uid.toString()
        println("Call: SenderRoom= $senderRoom")
        messageList= ArrayList()
        messageAdapter= MessageAdapter(context,messageList)
        mDbRef.child("chats").child(senderRoom!!).child("messages").
        addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for(postSnapshot in snapshot.children){
                    val message=postSnapshot.getValue(Message::class.java)
                    holder.recentMsg.text = "${message?.message}"
                    holder.recentTime.text = "${message?.time}"
                }
                messageAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
        if(holder.recentMsg.text=="empty"){
            holder.recentTime.text=""
            holder.recentMsg.text=""
        }

        val storageRef=FirebaseStorage.getInstance().reference.child("images/${currentUser.uid}")
        val localfile=File.createTempFile("tempImage","webp")
        storageRef.getFile(localfile).addOnSuccessListener {
            val bitmap=BitmapFactory.decodeFile(localfile.absolutePath)
            holder.userImage.setImageBitmap(bitmap)
        }.addOnFailureListener{
            holder.userImage.setImageResource(R.drawable.new_wine)
        }

        holder.itemView.setOnClickListener{
            val intent=Intent(context,ChatActivity::class.java)
            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
            val textName=itemView.findViewById<TextView>(R.id.txt_name)
            val userImage=itemView.findViewById<ImageView>(R.id.user_image)
            val recentMsg=itemView.findViewById<TextView>(R.id.txt_peek)
            val recentTime=itemView.findViewById<TextView>(R.id.txt_Time)
    }
}