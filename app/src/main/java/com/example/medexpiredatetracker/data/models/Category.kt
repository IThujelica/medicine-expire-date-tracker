package com.example.medexpiredatetracker.data.models

class Category(val id: Int, val name: String, val color: String) {
    companion object {
        const val TABLE_NAME = "category"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_COLOR = "color"

        const val CREATE_TABLE_QUERY =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_NAME TEXT," +
                    "$COLUMN_COLOR TEXT)"

        const val DROP_TABLE_QUERY = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}