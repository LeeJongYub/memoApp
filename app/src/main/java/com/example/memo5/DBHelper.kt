package com.example.memo5

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper : SQLiteOpenHelper {

    constructor(context: Context) : super(context, "memo1.db", null, 1)

    override fun onCreate(p0: SQLiteDatabase?) {
        val sql = """
        create table memoApp5
        (memo_idx integer primary key autoincrement,
        memo_subject text not null,
        memo_text text not null,
        memo_date date not null)
    """.trimIndent()

        p0?.execSQL(sql)
    }


    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}