package com.example.todolist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todolist.R
import dagger.hilt.android.AndroidEntryPoint


//benötigte libraries importiert man in build.gradle für project und build.gradle für module

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}