package com.example.medexpiredatetracker.data

import android.content.ContentValues

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.medexpiredatetracker.data.models.Category
import com.example.medexpiredatetracker.data.models.Medicine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DataManager private constructor() {
    lateinit var dbHelper: DataBaseHelper
    lateinit var db: SQLiteDatabase
    lateinit var dateFormat: SimpleDateFormat

    fun dbInit(context: Context) {
        dbHelper = DataBaseHelper(context)
        db = dbHelper.writableDatabase
        dateFormat = SimpleDateFormat("MM-yyyy", Locale.getDefault())
    }

    fun createCategory(name: String, color: String): Category {
        val values = ContentValues().apply {
            put(Category.COLUMN_NAME, name)
            put(Category.COLUMN_COLOR, color)
        }

        val newRowId = db.insert(Category.TABLE_NAME, null, values)
        return Category(
            id = newRowId.toInt(),
            name = name,
            color = color
        )
    }

    fun getCategories(): List<Category> {
        val projection = arrayOf(Category.COLUMN_ID, Category.COLUMN_NAME, Category.COLUMN_COLOR)
        val sortOrder = "${Category.COLUMN_COLOR} DESC"
        val cursor = db.query(
            Category.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )
        val categories = mutableListOf<Category>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(Category.COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(Category.COLUMN_NAME))
                val color = getString(getColumnIndexOrThrow(Category.COLUMN_COLOR))
                categories.add(Category(id, name, color))
            }
        }
        cursor.close()
        return categories
    }

    fun getCategoryById(id: Int): Category {
        val projection = arrayOf(Category.COLUMN_ID, Category.COLUMN_NAME, Category.COLUMN_COLOR)
        val selection = "${Category.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = db.query(
            Category.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        with(cursor) {
            moveToNext()
            val categoryId = getInt(getColumnIndexOrThrow(Category.COLUMN_ID))
            val name = getString(getColumnIndexOrThrow(Category.COLUMN_NAME))
            val color = getString(getColumnIndexOrThrow(Category.COLUMN_COLOR))
            close()
            return Category(categoryId, name, color)
        }
    }

    fun deleteCategory(category: Category) {
        val selection = "${Category.COLUMN_ID} LIKE ?"
        val selectionArgs = arrayOf(category.id.toString())
        db.delete(Category.TABLE_NAME, selection, selectionArgs)
    }

    fun updateCategory(category: Category) {
        val values = ContentValues().apply {
            put(Category.COLUMN_NAME, category.name)
            put(Category.COLUMN_COLOR, category.color)
        }

        val selection = "${Category.COLUMN_ID} LIKE ?"
        val selectionArgs = arrayOf(category.id.toString())
        val count = db.update(
            Category.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    fun createMedicine(name: String, expireDate: Date, category: Category): Medicine {
        val values = ContentValues().apply {
            put(Medicine.COLUMN_NAME, name)
            put(Medicine.COLUMN_EXPIRE_DATE, dateFormat.format(expireDate))
            put(Medicine.COLUMN_CATEGORY_ID, category.id)
        }

        val newRowId = db.insert(Medicine.TABLE_NAME, null, values)
        return Medicine(
            id = newRowId.toInt(),
            name = name,
            expireDate = expireDate,
            categoryId = category.id
        )
    }

    fun getMedicines(): List<Medicine> {
        val projection = arrayOf(
            Medicine.COLUMN_ID,
            Medicine.COLUMN_NAME,
            Medicine.COLUMN_EXPIRE_DATE,
            Medicine.COLUMN_CATEGORY_ID
        )
        val sortOrder = "${Medicine.COLUMN_EXPIRE_DATE} DESC"
        val cursor = db.query(
            Medicine.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )
        val medicines = mutableListOf<Medicine>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(Medicine.COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(Medicine.COLUMN_NAME))
                val expireDate = getString(getColumnIndexOrThrow(Medicine.COLUMN_EXPIRE_DATE))
                val categoryId = getInt(getColumnIndexOrThrow(Medicine.COLUMN_CATEGORY_ID))
                medicines.add(
                    Medicine(
                        id,
                        name,
                        dateFormat.parse(expireDate) ?: Date(),
                        categoryId
                    )
                )
            }
        }
        cursor.close()
        return medicines
    }

    fun getMedicinesByCategory(category: Category): List<Medicine> {
        val projection = arrayOf(
            Medicine.COLUMN_ID,
            Medicine.COLUMN_NAME,
            Medicine.COLUMN_EXPIRE_DATE,
            Medicine.COLUMN_CATEGORY_ID
        )
        val sortOrder = "${Medicine.COLUMN_EXPIRE_DATE} DESC"
        val selection = "${Medicine.COLUMN_CATEGORY_ID} = ?"
        val selectionArgs = arrayOf("${category.id}")
        val cursor = db.query(
            Medicine.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
        val medicines = mutableListOf<Medicine>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(Medicine.COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(Medicine.COLUMN_NAME))
                val expireDate = getString(getColumnIndexOrThrow(Medicine.COLUMN_EXPIRE_DATE))
                val categoryId = getInt(getColumnIndexOrThrow(Medicine.COLUMN_CATEGORY_ID))
                medicines.add(
                    Medicine(
                        id,
                        name,
                        dateFormat.parse(expireDate) ?: Date(),
                        categoryId
                    )
                )
            }
        }
        cursor.close()
        return medicines
    }

    fun deleteMedicine(medicine: Medicine) {
        val selection = "${Medicine.COLUMN_ID} LIKE ?"
        val selectionArgs = arrayOf(medicine.id.toString())
        db.delete(Medicine.TABLE_NAME, selection, selectionArgs)
    }

    fun updateMedicine(medicine: Medicine) {
        val values = ContentValues().apply {
            put(Medicine.COLUMN_NAME, medicine.name)
            put(Medicine.COLUMN_EXPIRE_DATE, dateFormat.format(medicine.expireDate))
            put(Medicine.COLUMN_CATEGORY_ID, medicine.categoryId)
        }

        val selection = "${Medicine.COLUMN_ID} LIKE ?"
        val selectionArgs = arrayOf(medicine.id.toString())
        val count = db.update(
            Medicine.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    companion object {
        private var instance: DataManager? = null
        fun getInstance(): DataManager {
            if (instance == null) {
                instance = DataManager()
            }
            return instance!!
        }
    }
}