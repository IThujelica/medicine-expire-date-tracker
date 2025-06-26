package com.example.medexpiredatetracker.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.medexpiredatetracker.data.models.Category
import com.example.medexpiredatetracker.data.models.Medicine


class DataBaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Category.CREATE_TABLE_QUERY)
        db.execSQL(Medicine.CREATE_TABLE_QUERY)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(Category.DROP_TABLE_QUERY)
        db.execSQL(Medicine.DROP_TABLE_QUERY)
        //TODO make migration without drop data
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "app.db"
    }
}