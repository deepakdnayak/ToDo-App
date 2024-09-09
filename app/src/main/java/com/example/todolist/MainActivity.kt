package com.example.todolist

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private val tasks = mutableListOf<String>()
    private lateinit var adapter: TaskAdapter
    private val sharedPrefFile = "com.example.todolist.PREFERENCE_FILE_KEY"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addButtonW = findViewById<ImageButton>(R.id.addButtonW)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTasks)

        adapter = TaskAdapter(tasks) {position -> deleteTask(position)}
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadTasks()

        addButtonW.setOnClickListener {
            showAddTaskDailogue()
        }
    }

    private fun showAddTaskDailogue() {
        val dailogueView = LayoutInflater.from(this).inflate(R.layout.dailogueform,null)

        val dailogueBuilder = AlertDialog.Builder(this)
            .setView(dailogueView)
            .setCancelable(true)

        val alertDailogue = dailogueBuilder.create()

        val saveButton = dailogueView.findViewById<Button>(R.id.saveTaskButton)
        val taskInput = dailogueView.findViewById<EditText>(R.id.editTextTask)

        saveButton.setOnClickListener {
            val task = taskInput.text.toString()
            if(task.isNotEmpty()){
                tasks.add(task)
                adapter.notifyItemInserted(tasks.size - 1)
                alertDailogue.dismiss()
            }
            saveTasks()
        }
        alertDailogue.show()
    }

    private fun deleteTask(position: Int) {
        tasks.removeAt(position)
        adapter.notifyItemRemoved(position)
        saveTasks()
    }

    private fun saveTasks() {
        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val jsonTasks = gson.toJson(tasks)

        editor.putString("TASKS_KEY", jsonTasks)
        editor.apply()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadTasks() {
        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val jsonTasks = sharedPreferences.getString("TASKS_KEY",null)

        if(!jsonTasks.isNullOrEmpty()) {
            val gson = Gson()
            val type = object : TypeToken<List<String>>() {}.type
            val loadedTasks : List<String> = gson.fromJson(jsonTasks, type)

            tasks.addAll(loadedTasks)
            adapter.notifyDataSetChanged()
        }
    }
}