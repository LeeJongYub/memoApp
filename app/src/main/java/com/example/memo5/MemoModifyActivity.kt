package com.example.memo5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import com.example.memo5.databinding.ActivityMemoModifyBinding

class MemoModifyActivity : AppCompatActivity() {

    lateinit var binding: ActivityMemoModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.modifyToolbar)
        title = "메모 수정"

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val Helper = DBHelper(this)

        val sql = """
            select memo_subject, memo_text
            from memoApp5
            where memo_idx = ?
        """.trimIndent()

        val memo_idx = intent.getIntExtra("memo_idx", 0)

        val args = arrayOf(memo_idx.toString())

        val c1 = Helper.writableDatabase.rawQuery(sql, args)
        c1.moveToNext()

        val idx1 = c1.getColumnIndex("memo_subject")
        val idx2 = c1.getColumnIndex("memo_text")

        val memo_subject = c1.getString(idx1)
        val memo_text = c1.getString(idx2)

        Helper.writableDatabase.close()

        binding.modifySaveSubjectEdit.setText(memo_subject)
        binding.modifySaveTextEdit.setText(memo_text)

        Thread {
            SystemClock.sleep(300)
            runOnUiThread {
                binding.modifySaveSubjectEdit.requestFocus()

                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.modifySaveSubjectEdit,InputMethodManager.SHOW_IMPLICIT)
            }
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.modify_save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.modify_save_item -> {
                val dbHelper = DBHelper(this)

                val sql = """
                    update memoApp5
                    set memo_subject = ?, memo_text = ?
                    where memo_idx = ?
                """.trimIndent()

                val memo_subject = binding.modifySaveSubjectEdit.text
                val memo_text = binding.modifySaveTextEdit.text
                val memo_idx = intent.getIntExtra("memo_idx", 0)

                val args = arrayOf(memo_subject, memo_text, memo_idx.toString())

                dbHelper.writableDatabase.execSQL(sql, args)
                dbHelper.writableDatabase.close()

                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}