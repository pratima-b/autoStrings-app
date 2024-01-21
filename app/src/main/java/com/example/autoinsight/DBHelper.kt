package com.example.autoinsight

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VER) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val query = "CREATE TABLE users(mobile VARCHAR(10) PRIMARY KEY, fname TEXT , lname TEXT , houseno TEXT , city TEXT , state TEXT , pincode TEXT , manf TEXT , model TEXT, year TEXT, email TEXT, regno TEXT, fuel TEXT )"
        sqLiteDatabase.execSQL(query)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users")
        onCreate(sqLiteDatabase)
    }

    fun addData(mobile: String, fname: String, lname: String, houseno: String, city: String, state: String, pincode: String, manf: String, model: String, year: String, email: String, regno: String, fuel: String): Boolean {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        try {

            contentValues.put("fname", fname)
            contentValues.put("lname", lname)
            contentValues.put("houseno", houseno)
            contentValues.put("city", city)
            contentValues.put("state", state)
            contentValues.put("pincode", pincode)
            contentValues.put("manf", manf)
            contentValues.put("model", model)
            contentValues.put("year", year)
            contentValues.put("mobile", mobile)
            contentValues.put("email", email)
            contentValues.put("fuel", fuel)
            contentValues.put("regno", regno)

            sqLiteDatabase.insert("users", null,contentValues)

            return true
        } catch (e: Exception) {
            println(e.message)
            return false
        } finally {

            sqLiteDatabase.close()
        }
    }

    companion object {
        private const val DB_NAME = "users"
        private const val DB_VER = 1
    }
}