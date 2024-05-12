package com.example.taskify

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "taskapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "alltaskes"
        private const val COLOMN_ID = "id"
        private const val COLOMN_TITLE = "title"
        private const val COLOMN_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val creatTableQuery = "CREATE TABLE $TABLE_NAME ($COLOMN_ID INTEGER PRIMARY KEY, $COLOMN_TITLE TEXT, $COLOMN_CONTENT TEXT)"
        db?.execSQL(creatTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertTask(task: Task){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLOMN_TITLE, task.title)
            put(COLOMN_CONTENT, task.content)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllTaskes(): List<Task> {
        val taskesList = mutableListOf<Task>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLOMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLOMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLOMN_CONTENT))

            val task = Task(id, title, content)
            taskesList.add(task)
        }
        cursor.close()
        db.close()
        return taskesList
    }

    fun updateTask(task: Task){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLOMN_TITLE, task.title)
            put(COLOMN_CONTENT, task.content)
        }
        val whereClause = "$COLOMN_ID = ?"
        val wherArgs = arrayOf(task.id.toString())
        db.update(TABLE_NAME, values, whereClause, wherArgs)
        db.close()
    }

    fun getTaskByID(taskId: Int): Task{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLOMN_ID = $taskId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLOMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLOMN_TITLE))
        val context = cursor.getString(cursor.getColumnIndexOrThrow(COLOMN_CONTENT))

        cursor.close()
        db.close()
        return Task(id, title, context)
    }

    fun deleteTask(taskId: Int){
        val db = writableDatabase
        val whereClause = "$COLOMN_ID = ?"
        val whereArgs = arrayOf(taskId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

}