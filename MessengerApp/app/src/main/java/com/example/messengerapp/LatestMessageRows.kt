package com.example.messengerapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessageRow(val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {
    var chatPartnerUser: User? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_latest_message_bot.text = chatMessage.text

        val chatPartnerId: String
        //from = man selber hat als letztes was geschrieben - else = der andere hat als letztes was geschrieben
        if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
            chatPartnerId = chatMessage.toId
        } else{
            chatPartnerId = chatMessage.fromId
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(User::class.java)
                viewHolder.itemView.textView_latest_message_top.text = chatPartnerUser?.username

                //lade bild ein in recyclerview mit 3rd party lib picasso und circleview
                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(viewHolder.itemView.imageView_latest_message)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }
}