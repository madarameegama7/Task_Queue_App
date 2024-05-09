package com.example.taskqueue

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TasksDatabaseHelper (context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,
    DATABASE_VERSION){
    companion object{
        private const val DATABASE_NAME="tasksapp.db"
        private const val DATABASE_VERSION=1
        private const val TABLE_NAME="alltasks"
        private const val COLUMN_ID="id"
        private const val COLUMN_NAME="name"
        private const val COLUMN_DATE="date"
        private const val COLUMN_TIME="time"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY,$COLUMN_NAME TEXT,$COLUMN_DATE TEXT,$COLUMN_TIME TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }
    fun insertTask(task : Tasks){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, task.name)
            put(COLUMN_DATE, task.date)
            put(COLUMN_TIME, task.time)
        }
        db.insert(TABLE_NAME,null, values)
        db.close()
    }
}