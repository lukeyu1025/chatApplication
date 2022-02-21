package com.example.chatapplication

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class MessageAdapter(val context: Context,val messageList:ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE=1
    val ITEM_SENT=2

    private lateinit var time:TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==1) {
            val view:View= LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            return ReceiveViewHolder(view)
        }else{
            val view:View= LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage=messageList[position]
        var Time=currentMessage.time
        if(Time=="")Time="MM/DD HH/mm"
        if(holder.javaClass == SentViewHolder::class.java){
            //sent view holder
            val viewHolder=holder as SentViewHolder
            holder.sentMessage.text= currentMessage.message
            holder.sentTime.text=Time
        }else{
            //receive view holder
            val viewHolder=holder as ReceiveViewHolder
            val storageRef= FirebaseStorage.getInstance().reference.child("images/${currentMessage.senderId}")
            val localfile= File.createTempFile("tempImage","webp")
            holder.receiveMessage.text=currentMessage.message
            holder.ReceiveTime.text=Time
            storageRef.getFile(localfile).addOnSuccessListener {
                val bitmap= BitmapFactory.decodeFile(localfile.absolutePath)
                holder.userImage.setImageBitmap(bitmap)
            }.addOnFailureListener{
                holder.userImage.setImageResource(R.drawable.new_wine)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage=messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid?.equals(currentMessage.senderId) == true){
            return ITEM_SENT}
        else{
            return ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val sentMessage=itemView.findViewById<TextView>(R.id.txt_sent_message)
        val sentTime=itemView.findViewById<TextView>(R.id.txt_sentTime)
    }
    class ReceiveViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val receiveMessage=itemView.findViewById<TextView>(R.id.txt_receive_message)
        val ReceiveTime=itemView.findViewById<TextView>(R.id.txt_receiveTime)
        val userImage=itemView.findViewById<ImageView>(R.id.sender)
    }
}