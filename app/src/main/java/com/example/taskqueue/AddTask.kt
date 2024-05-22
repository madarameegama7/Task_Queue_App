
package com.example.taskqueue

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskqueue.TaskListModel.TaskListModel
import com.example.taskqueue.databinding.ActivityAddTaskBinding
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddTask : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var db: TasksDatabaseHelper
    private var selectedDate: Calendar? = null
    private var selectedTime: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

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

            scheduleNotification()

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
        dpd.minDate = now // Set the minimum date to today
        dpd.show(supportFragmentManager, "Datepickerdialog")
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        selectedDate = Calendar.getInstance().apply {
            set(year, monthOfYear, dayOfMonth)
        }
        val date = "$dayOfMonth/${monthOfYear + 1}/$year"
        binding.editTextDate.setText(date)
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

        binding.editTextTime.setText(formattedTime)

        Toast.makeText(this, "Time set to: $formattedTime", Toast.LENGTH_SHORT).show()
    }

    private fun scheduleNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                setExactAlarm(alarmManager)
            } else {
                // Request the SCHEDULE_EXACT_ALARM permission from the user
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        } else {
            setExactAlarm(alarmManager)
        }
    }

    private fun setExactAlarm(alarmManager: AlarmManager) {
        val intent = Intent(applicationContext, Notification::class.java).apply {
            putExtra(titleExtra, binding.editTextTextMultiLine.text.toString())
            putExtra(messageExtra, binding.editTextTextMultiLine.text.toString())
        }

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val time = getTime()
        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
            showAlert(time, binding.editTextTextMultiLine.text.toString(), binding.editTextTextMultiLine.text.toString())
        } catch (e: SecurityException) {
            Toast.makeText(this, "Permission to schedule exact alarms is not granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: $title\nMessage: $message\nAt: ${dateFormat.format(date)} ${timeFormat.format(date)}"
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun getTime(): Long {
        if (selectedDate == null || selectedTime == null) {
            throw IllegalStateException("Date or Time not selected")
        }

        return Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedDate!!.get(Calendar.YEAR))
            set(Calendar.MONTH, selectedDate!!.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, selectedDate!!.get(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, selectedTime!!.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, selectedTime!!.get(Calendar.MINUTE))
            set(Calendar.SECOND, selectedTime!!.get(Calendar.SECOND))
        }.timeInMillis
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notif Channel"
            val descriptionText = "A Description of the Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
