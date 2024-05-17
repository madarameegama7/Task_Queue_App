package com.example.taskqueue

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskqueue.TaskListModel.TaskListModel
import com.example.taskqueue.databinding.ActivityAddTaskBinding
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTask : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var db: TasksDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        binding.btnShowDatePicker.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnShowTimePicker.setOnClickListener {
            showTimePickerDialog()
        }

        binding.button.setOnClickListener {
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
        dpd.show(supportFragmentManager, "Datepickerdialog")
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = "$dayOfMonth/${monthOfYear + 1}/$year"
        binding.editTextDate.setText(date)
    }

//    override fun onTimeSet(view: TimePickerDialog, hourOfDay: Int, minute: Int, second: Int) {
//        val time = "$hourOfDay:$minute"
//        binding.editTextTime.setText(time)
//    }

    override fun onTimeSet(view: TimePickerDialog, hourOfDay: Int, minute: Int, second: Int) {
        // Create a Calendar object to extract hour, minute, and AM/PM
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, second)
        }

        val hour = calendar.get(Calendar.HOUR) // Get the hour in 12-hour format
        val minute = calendar.get(Calendar.MINUTE)
        val amPm = calendar.get(Calendar.AM_PM) // 0 for AM, 1 for PM

        val amPmString = if (amPm == Calendar.AM) "AM" else "PM"

        // Format the time as "hh:mm a" (e.g., "02:30 PM")
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedTime = timeFormat.format(calendar.time)

        // Set the formatted time to the EditText
        binding.editTextTime.setText(formattedTime)

        // Optionally, you can display a Toast with the extracted hour, minute, and AM/PM
        Toast.makeText(this, "Hour: $hour, Minute: $minute, AM/PM: $amPmString", Toast.LENGTH_SHORT).show()
    }

}
