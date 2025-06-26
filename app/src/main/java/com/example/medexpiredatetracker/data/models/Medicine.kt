package com.example.medexpiredatetracker.data.models

import java.util.Date

class Medicine(val id: Int, val name: String, val expireDate: Date, val categoryId: Int) {
    companion object {
        const val TABLE_NAME = "medicine"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_EXPIRE_DATE = "expireDate"
        const val COLUMN_CATEGORY_ID = "categoryId"
        const val CREATE_TABLE_QUERY =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_CATEGORY_ID INTEGER," +
                    "$COLUMN_NAME TEXT," +
                    "$COLUMN_EXPIRE_DATE TEXT," +
                    "FOREIGN KEY ($COLUMN_CATEGORY_ID) REFERENCES ${Category.TABLE_NAME} (${Category.COLUMN_ID})"+
                    "ON DELETE CASCADE)"


        const val DROP_TABLE_QUERY = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}