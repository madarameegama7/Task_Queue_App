package com.example.taskqueue

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskqueue.TaskListModel.TaskListModel
import com.example.taskqueue.databinding.ActivityAddTaskBinding


class AddTask : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var db: TasksDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        binding.button.setOnClickListener {
//            Log.d("AddTask", "Button clicked")
            val name = binding.editTextTextMultiLine.text.toString()
            val date = binding.editTextDate.text.toString()
            val time = binding.editTextTime.text.toString()
            val task = TaskListModel(0, name, date, time)
            db.insertTask(task)


            // Show a Toast message
            Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show()

            // Finish the current activity after a short delay
            Handler().postDelayed({
                finish()
            }, 5000) // Delay in milliseconds



        }


    }
}
