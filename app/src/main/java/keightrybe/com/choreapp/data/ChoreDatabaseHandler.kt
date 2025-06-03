package keightrybe.com.choreapp.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.text.DateFormat
import android.util.Log
import keightrybe.com.choreapp.model.Chore
import keightrybe.com.choreapp.model.ChoreConstants.DATABASE_NAME
import keightrybe.com.choreapp.model.ChoreConstants.DATABASE_VERSION
import keightrybe.com.choreapp.model.ChoreConstants.KEY_CHORE_ASSIGNED_BY
import keightrybe.com.choreapp.model.ChoreConstants.KEY_CHORE_ASSIGNED_TIME
import keightrybe.com.choreapp.model.ChoreConstants.KEY_CHORE_ASSIGNED_TO
import keightrybe.com.choreapp.model.ChoreConstants.KEY_CHORE_NAME
import keightrybe.com.choreapp.model.ChoreConstants.KEY_ID
import keightrybe.com.choreapp.model.ChoreConstants.TABLE_NAME
import java.util.Date

class ChoreDatabaseHandler (context: Context) :
SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        //SQL - STRUCTURED QUERY LANGUAGE
        val CREATE_CHORE_TABLE = """
        CREATE TABLE $TABLE_NAME (
            $KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $KEY_CHORE_NAME TEXT,
            $KEY_CHORE_ASSIGNED_BY TEXT,
            $KEY_CHORE_ASSIGNED_TO TEXT,
            $KEY_CHORE_ASSIGNED_TIME INTEGER
        )
    """.trimIndent()

        db?.execSQL(CREATE_CHORE_TABLE)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")

        //Create Table again
        onCreate(db)
    }

    /**
     * CRUD - Create, Read, Update, Delete
     */

    fun createChore(chore: Chore) {
        var db: SQLiteDatabase = writableDatabase

        var values: ContentValues = ContentValues()
        values.put(KEY_CHORE_NAME, chore.choreName)
        values.put(KEY_CHORE_ASSIGNED_TO, chore.assignedTo)
        values.put(KEY_CHORE_ASSIGNED_BY, chore.assignedBy)
        values.put(KEY_CHORE_ASSIGNED_TIME, System.currentTimeMillis())

        db.insert(TABLE_NAME, null, values)

        Log.d("DATA INSERTED", "SUCCESS")
        db.close()

    }
    fun readChores(i: Int): List<Chore> {
        val db = readableDatabase
        val choreList = mutableListOf<Chore>()

        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val chore = Chore()
                chore.id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                chore.choreName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CHORE_NAME))
                chore.assignedBy = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CHORE_ASSIGNED_BY))
                chore.assignedTo = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CHORE_ASSIGNED_TO))
                chore.timeAssigned = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_CHORE_ASSIGNED_TIME))

                var dateFormat: DateFormat? = DateFormat.getDateInstance()
                dateFormat?.format(Date(cursor.getLong(cursor.getColumnIndexOrThrow(KEY_CHORE_ASSIGNED_TIME))))

                choreList.add(chore)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return choreList
    }
    fun updateChore(chore: Chore): Int {
        var db: SQLiteDatabase = writableDatabase

        var values: ContentValues = ContentValues()
        values.put(KEY_CHORE_NAME, chore.choreName)
        values.put(KEY_CHORE_ASSIGNED_TO, chore.assignedTo)
        values.put(KEY_CHORE_ASSIGNED_BY, chore.assignedBy)
        values.put(KEY_CHORE_ASSIGNED_TIME, System.currentTimeMillis())

        //update a row
        return db.update(TABLE_NAME, values, "$KEY_ID=?", arrayOf(chore.id.toString()))
    }
    fun deleteChore(id: Int){
        var db: SQLiteDatabase = writableDatabase
        db.delete(TABLE_NAME,"$KEY_ID=?", arrayOf(id.toString()))
        db.close()
    }
    fun getChoresCount(): Int {
        val db = readableDatabase
        val countQuery = "SELECT * FROM $TABLE_NAME"
        val cursor: Cursor = db.rawQuery(countQuery, null)
        val count = cursor.count
        cursor.close()
        return count
    }

}