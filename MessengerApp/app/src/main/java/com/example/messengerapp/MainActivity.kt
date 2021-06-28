package com.example.messengerapp

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonRegister.setOnClickListener {

            performRegister()
        }

        textViewAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        button_round_photo.setOnClickListener {
            // TODO gibt neue variante - vllt in zukunft mal anpassen: https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RegisterAct", "Photo selected")

            selectedPhotoUri = data.data

            //catch für sdk unter 28 mit alter methode und für neuere android versionen mit neuer methode das foto für register
            val uri = data?.data
            val bitmap = when {
                Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                    this.contentResolver,
                    selectedPhotoUri
                )
                else -> {
                    val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri!!)
                    ImageDecoder.decodeBitmap(source)
                }
            }

            //val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri!!)
            //val bitmap = ImageDecoder.decodeBitmap(source)
            //val bitmapDrawable = BitmapDrawable(this.resources, bitmap)
            photo_circle.setImageBitmap(bitmap)

            button_round_photo.alpha = 0f
        }
    }

    private fun performRegister() {
        val email = editTextTextEmailAddress.text.toString()
        val password = editTextTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter Email and Password", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "Email is " + email)
        Log.d("MainActivity", "Password $password")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                //else -> darauf achten: pw vom user muss mindestens 6 char lang sein
                // TODO noch abfangen dass user beim register mind. 6 char angibt
                Log.d("Main", "Successfully created User mit ${it.result?.user?.uid}")

                uploadImageToFirebaseStorage()

            }.addOnFailureListener {
                Log.d("Main", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Wrong format.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
            Log.d("Register", "succesfully uploaded image ${it.metadata?.path}")

            ref.downloadUrl.addOnSuccessListener {
                Log.d("Register", "file location: $it")

                saveUserToFirebaseDatabase(it.toString())
            }

        }

    }


    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        //val ref = "https://messengerapp-655c6-default-rtdb.europe-west1.firebasedatabase.app/users"

        val user = User(uid, editTextTextPersonName.text.toString(), profileImageUrl)

        //braucht us datenbank in firebase
        ref.setValue(user).addOnSuccessListener {
            Log.d("Register", "saved user to db")

            val intent = Intent(this, LatestMessagesActivity::class.java)
            //raus aus app
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}



// TODO besser splitten mit mehr files und ordnern
@Parcelize
class User(val uid: String, val username: String, val profileImageUrl: String) : Parcelable {
    constructor() : this("", "", "")
}


















