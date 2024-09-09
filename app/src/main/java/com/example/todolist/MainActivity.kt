package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val tasks = mutableListOf<String>()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addButton = findViewById<Button>(R.id.addButton)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTasks)

        adapter = TaskAdapter(tasks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addButton.setOnClickListener{
            showAddTaskDailogue()
        }
    }

    private fun showAddTaskDailogue() {
        val dailogueView = LayoutInflater.from(this).inflate(R.layout.dailogueform,null)

        val dailogueBuilder = AlertDialog.Builder(this)
            .setView(dailogueView)
            .setCancelable(true)

        val alertDailog = dailogueBuilder.create()

        val saveButton = dailogueView.findViewById<Button>(R.id.saveTaskButton)
        val taskInput = dailogueView.findViewById<EditText>(R.id.editTextTask)

        saveButton.setOnClickListener {
            val task = taskInput.text.toString()
            if(task.isNotEmpty()){
                tasks.add(task)
                adapter.notifyItemInserted(tasks.size - 1)
                alertDailog.dismiss()
            }
        }
        alertDailog.show()
    }
}