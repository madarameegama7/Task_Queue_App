package com.example.taskqueue

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taskqueue.TaskListModel.TaskListModel
import com.example.taskqueue.databinding.ActivityAddTaskBinding
import android.util.Log


class AddTask : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var db: TasksDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        binding.button.setOnClickListener {
            Log.d("AddTask", "Button clicked")
            val name = binding.editTextTextMultiLine.text.toString()
            val date = binding.editTextDate.text.toString()
            val time = binding.editTextTime.text.toString()
            val b1 = TaskListModel(0, name, date, time)
            db.insertTask(b1)

            // Start MainActivity3 after adding task
            startActivity(Intent(this, MainActivity3::class.java))

            // Finish the current activity
            finish()

            Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show()
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_add_task)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
