package com.example.todolist.ui.tasks

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todolist.R
import dagger.hilt.android.AndroidEntryPoint

//injecte in unser fragment
//deklariere layout das wir benutzen wollen im konstruktor - siehe xml im layout
@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks) {

    private val viewModel: TasksViewModel by viewModels()
}