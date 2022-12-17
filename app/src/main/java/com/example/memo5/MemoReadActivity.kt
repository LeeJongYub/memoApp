package com.example.memo5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.example.memo5.databinding.ActivityMemoReadBinding

class MemoReadActivity : AppCompatActivity() {

    lateinit var binding: ActivityMemoReadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoReadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.readToolbar)
        title = "메모 읽기"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onResume() {
        super.onResume()

        val dbHelper = DBHelper(this)

        val sql = """
            select memo_subject, memo_date, memo_text
            from memoApp5
            where memo_idx = ?
        """.trimIndent()

        val memo_idx = intent.getIntExtra("memo_idx", 0)

        val args = arrayOf(memo_idx.toString())

        val c1 = dbHelper.writableDatabase.rawQuery(sql, args)
        c1.moveToNext()

        val idx1 = c1.getColumnIndex("memo_subject")
        val idx2 = c1.getColumnIndex("memo_date")
        val idx3 = c1.getColumnIndex("memo_text")

        val memo_subject = c1.getString(idx1)
        val memo_date = c1.getString(idx2)
        val memo_text = c1.getString(idx3)

        dbHelper.writableDatabase.close()

        binding.readSubjectTxt.text = "제목 : $memo_subject"
        binding.readDateTxt.text = "작성일자 : $memo_date"
        binding.readTextTxt.text = memo_text

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.modify_memo_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.modify_item -> {
                val memo_idx = intent.getIntExtra("memo_idx", 0)

                val modifyIntent = Intent(this, MemoModifyActivity::class.java)
                modifyIntent.putExtra("memo_idx", memo_idx)

                startActivity(modifyIntent)
            }
            R.id.delete_item -> {
                val builder = AlertDialog.Builder(this)

                builder.setTitle("메모 삭제")
                builder.setMessage("메모를 삭제하시겠습니까?")

                builder.setPositiveButton("삭제") {dialogInterface, i ->
                    val dbHelper = DBHelper(this)

                    val sql = """
                        delete from memoApp5
                        where memo_idx = ?
                    """.trimIndent()

                    val memo_idx = intent.getIntExtra("memo_idx", 0)

                    val args = arrayOf(memo_idx.toString())

                    dbHelper.writableDatabase.execSQL(sql, args)
                    dbHelper.writableDatabase.close()

                    finish()
                }

                builder.setNegativeButton("취소", null)
                builder.show()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}