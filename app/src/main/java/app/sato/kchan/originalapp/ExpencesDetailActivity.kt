package app.sato.kchan.originalapp

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import app.sato.kchan.originalapp.databinding.ActivityDetailExpensesBinding

class ExpencesDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailExpensesBinding

    val expensesDao = Application.database.expensesDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailExpensesBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        val intent: Intent = getIntent()
        val id: Long = intent.getLongExtra("id", 0)

        // roomに保存するときのidは1から、positionは0からなので1足す
        getExpenses(id+1)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getExpenses(id: Long){
        val expensesData = expensesDao.findAll()

        val eIdData = ArrayList<Long>()
        expensesData.forEach { expenses -> eIdData.add(expenses.faveID) }
        val expences = ArrayList<String>()
        expensesData.forEach { expenses -> expences.add(expenses.order) }

        val data = ArrayList<String>()
        for (i in 0 until eIdData.size) {
            if (eIdData[i] == id) {
                data.add(expences[i])
            }
        }

        val adapter = ArrayAdapter(
            this, R.layout.simple_list_item_1, data)
        binding.expensesListview.adapter = adapter
    }
}