package com.example.memo5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.memo5.databinding.ActivityMainBinding
import com.example.memo5.databinding.RecyclerViewRowBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val subject_list = ArrayList<String>()
    val text_list = ArrayList<String>()
    val idx_list = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolbar)
        title = "메모앱5"

        val adapter1 = RvAdapter()
        binding.mainRecycler.adapter = adapter1
        binding.mainRecycler.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_memo_menu, menu)
        return true
    }

    override fun onResume() {
        super.onResume()

        subject_list.clear()
        text_list.clear()
        idx_list.clear()

        val dbHelper = DBHelper(this)

        val sql = """
            select memo_subject, memo_text, memo_idx
            from memoApp5
            order by memo_idx desc
        """.trimIndent()

        val c1 = dbHelper.writableDatabase.rawQuery(sql, null)
        while (c1.moveToNext()) {
            val idx1 = c1.getColumnIndex("memo_subject")
            val idx2 = c1.getColumnIndex("memo_text")
            val idx3 = c1.getColumnIndex("memo_idx")

            val memo_subject = c1.getString(idx1)
            val memo_text = c1.getString(idx2)
            val memo_idx = c1.getInt(idx3)

            subject_list.add(memo_subject)
            text_list.add(memo_text)
            idx_list.add(memo_idx)

            binding.mainRecycler.adapter?.notifyDataSetChanged()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val memoAddIntent = Intent(this, MemoAddActivity::class.java)
        startActivity(memoAddIntent)

        return super.onOptionsItemSelected(item)
    }

    inner class RvAdapter : RecyclerView.Adapter<RvAdapter.RvViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvViewHolder {
            val recyclerBinding = RecyclerViewRowBinding.inflate(layoutInflater)
            val holder = RvViewHolder(recyclerBinding)

            val layoutParams1 = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            recyclerBinding.root.layoutParams = layoutParams1
            recyclerBinding.root.setOnClickListener(holder)

            return holder
        }

        override fun onBindViewHolder(holder: RvViewHolder, position: Int) {
            holder.subject_rv.text = subject_list[position]
            holder.text_rv.text = text_list[position]
        }

        override fun getItemCount(): Int {
            return subject_list.size
        }

        inner class RvViewHolder(recyclerViewRowBinding: RecyclerViewRowBinding) : RecyclerView.ViewHolder(recyclerViewRowBinding.root), OnClickListener {
            val subject_rv = recyclerViewRowBinding.recyclerSubjectEdit
            val text_rv = recyclerViewRowBinding.recyclerTextEdit

            override fun onClick(p0: View?) {
                val memo_idx = idx_list[adapterPosition]

                val memoModifyIntent = Intent(baseContext, MemoReadActivity::class.java)
                memoModifyIntent.putExtra("memo_idx", memo_idx)
                startActivity(memoModifyIntent)
            }
        }
    }


}