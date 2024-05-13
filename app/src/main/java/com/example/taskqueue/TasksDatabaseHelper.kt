package com.example.taskqueue

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.taskqueue.TaskListModel.TaskListModel

class TasksDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "tasksapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "alltasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_TIME = "time"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY,$COLUMN_NAME TEXT,$COLUMN_DATE TEXT,$COLUMN_TIME TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    //insert
    fun insertTask(task: TaskListModel){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, task.name)
            put(COLUMN_DATE, task.date)
            put(COLUMN_TIME, task.time)
        }

        db.insert(TABLE_NAME, null, values)
        db.close()
    }
//    fun deleteTask(_id:Int) : Boolean{
//        val db: SQLiteDatabase = this.writableDatabase
//    }
}

//    fun getTask(_id: Int): Tasks {
//        val db: SQLiteDatabase = writableDatabase
//        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $_id"
//        val cursor = db.rawQuery(selectQuery, null)
//
//        val task = Tasks() // Instantiate a Tasks object
//
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                task.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
//                task.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
//                task.date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
//                task.time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME))
//            }
//            cursor.close()
//            return task
//        }
//
//    }












//    fun getAllTasks(): List<TaskListModel> {
//        val taskList = ArrayList<TaskListModel>() // Corrected ArrayList declaration
//        val db : SQLiteDatabase? = writableDatabase
//
//        val selectQuery = "SELECT * FROM $TABLE_NAME"
//        val cursor = db.rawQuery(selectQuery, null)
//
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                do {
//                    val task = Tasks(
//                        id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
//                        name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
//                        date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
//                        time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME))
//                    )
//                    taskList.add(task)
//                } while (cursor.moveToNext())
//
//            }
//            cursor.close()
//        }
//        db.close()
//        return taskList // Added return statement
//    }


