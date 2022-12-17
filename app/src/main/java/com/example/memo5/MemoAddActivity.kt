package com.example.memo5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import com.example.memo5.databinding.ActivityMemoAddBinding
import java.text.SimpleDateFormat
import java.util.*

class MemoAddActivity : AppCompatActivity() {
    lateinit var binding: ActivityMemoAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.memoAddToolbar)
        title = "메모 추가"

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Thread {
            SystemClock.sleep(300)
            runOnUiThread {
                binding.addMemoSubjectEdit.requestFocus()

                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.addMemoSubjectEdit, InputMethodManager.SHOW_IMPLICIT)
            }
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_memo_menu, menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.save_memo_item -> {
                val dbHelper = DBHelper(this)

                val sql = """
                    insert into memoApp5(memo_subject, memo_text, memo_date)
                    values(?, ?, ?)
                """.trimIndent()

                val memo_subject = binding.addMemoSubjectEdit.text
                val memo_text = binding.addMemoTextEdit.text
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val now = sdf.format(Date())

                val args = arrayOf(memo_subject, memo_text, now)

                dbHelper.writableDatabase.execSQL(sql, args)
                dbHelper.writableDatabase.close()

                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}