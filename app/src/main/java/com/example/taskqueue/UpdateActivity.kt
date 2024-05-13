package com.example.taskqueue

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskqueue.TaskListModel.TaskListModel
import com.example.taskqueue.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db : TasksDatabaseHelper
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        taskId = intent.getIntExtra("task_id",-1)
        if(taskId == -1){
            finish()
            return
        }

        val task = db.getTaskByID(taskId)
        binding.updateTaskName.setText(task.name)
        binding.updateTaskDate.setText(task.date)
        binding.updateTaskTime.setText(task.time)

        binding.updateTaskButton.setOnClickListener{
            val newName = binding.updateTaskName.text.toString()
            val newDate = binding.updateTaskDate.text.toString()
            val newTime = binding.updateTaskTime.text.toString()
            val updatedTask = TaskListModel(taskId,newName,newDate,newTime)
            db.updateTask(updatedTask)
            finish()
            Toast.makeText(this,"Changes saved",Toast.LENGTH_SHORT).show()
        }


    }
}