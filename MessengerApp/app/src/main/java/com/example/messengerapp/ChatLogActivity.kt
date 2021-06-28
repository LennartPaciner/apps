package com.example.messengerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*


class ChatLogActivity : AppCompatActivity() {

    var toUser: User? = null
    val adapter = GroupAdapter<GroupieViewHolder>()


    companion object {
        val TAG = "ChatLog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recycler_view_chat_log.adapter = adapter

        //val username = intent.getStringExtra(NewMessageActivity.USER_KEY)
        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.username

        listenForMessages()


        button_chat_log.setOnClickListener {
            performSendMessage()

            editTextChatLog.requestFocus()
            editTextChatLog.text.clear()
        }
    }


    private fun performSendMessage() {
        val text = editTextChatLog.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

        //val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")

        val chatMessage = ChatMessage(reference.key!!, text, fromId!!, toId!!, System.currentTimeMillis() / 1000)

        reference.setValue(chatMessage).addOnSuccessListener {
            //editTextChatLog.text.clear()
            //recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)
        }
        toReference.setValue(chatMessage).addOnSuccessListener {
            //editTextChatLog.text.clear()
            //recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)
        }

        latestMessageRef.setValue(chatMessage)

        latestMessageToRef.setValue(chatMessage)



    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if (chatMessage?.fromId == FirebaseAuth.getInstance().uid) {
                    val currentUser = LatestMessagesActivity.currentUser
                    adapter.add(ChatFromItem(chatMessage?.text!!, currentUser!!))

                    recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)
                } else {
                    adapter.add(ChatToItem(chatMessage?.text!!, toUser!!))

                    //das hier wegmachen falls view nicht runterscrollen soll falls anderer was schreibt
                    recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)
                }

                recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)

            }

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })
    }
}


class ChatFromItem(val text : String, val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_chat_to.text = text

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_chat_to)
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}

class ChatToItem(val text : String, val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_chat_from.text = text

        val uri = user.profileImageUrl
        val target = viewHolder.itemView.imageView_chat_from
        Picasso.get().load(uri).into(target)
    }

    override fun getLayout(): Int {
        //layout umgedreht weil der im video hatte das man selber links ist
        return R.layout.chat_from_row
    }
}

















