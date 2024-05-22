package com.example.taskqueue

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskqueue.TaskListModel.TaskListModel
import com.example.taskqueue.databinding.ActivityUpdateBinding
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db : TasksDatabaseHelper
    private var taskId: Int = -1
    private var selectedDate: Calendar? = null
    private var selectedTime: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        binding.btnShowDatePicker2.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnShowTimePicker.setOnClickListener {
            showTimePickerDialog()
        }

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
    private fun showTimePickerDialog() {
        val now = Calendar.getInstance()
        val tpd = TimePickerDialog.newInstance(
            this,
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE),
            true
        )
        tpd.show(supportFragmentManager, "Timepickerdialog")
    }
    private fun showDatePickerDialog() {
        val now = Calendar.getInstance()
        val dpd = DatePickerDialog.newInstance(
            this,
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        )
        dpd.minDate = now // Set the minimum date to today
        dpd.show(supportFragmentManager, "Datepickerdialog")
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        selectedDate = Calendar.getInstance().apply {
            set(year, monthOfYear, dayOfMonth)
        }
        val date = "$dayOfMonth/${monthOfYear + 1}/$year"
        binding.updateTaskDate.setText(date)
    }

    override fun onTimeSet(view: TimePickerDialog, hourOfDay: Int, minute: Int, second: Int) {
        selectedTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, second)
        }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, second)
        }

        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedTime = timeFormat.format(calendar.time)

        binding.updateTaskTime.setText(formattedTime)

        Toast.makeText(this, "Time set to: $formattedTime", Toast.LENGTH_SHORT).show()
    }
}