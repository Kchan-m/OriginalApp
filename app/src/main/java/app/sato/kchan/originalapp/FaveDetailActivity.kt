package app.sato.kchan.originalapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import app.sato.kchan.originalapp.databinding.ActivityDetailFaveBinding

class FaveDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFaveBinding

    val expensesDao = Application.database.expensesDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFaveBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        val intent: Intent = getIntent()
        val id: Long = intent.getLongExtra("id", 0)

        // roomに保存するときのidは1から、positionは0からなので1足す
        getExpenses(id+1)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.expensesListview.setOnItemClickListener { _, _, position, _ ->
            val expensesActivityIntent: Intent = Intent(this, ExpensesDetailActivity::class.java)
            expensesActivityIntent.putExtra("id", id+1)
            expensesActivityIntent.putExtra("position", position)
            startActivity(expensesActivityIntent)
        }
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
        val allYearData = ArrayList<Int>()
        val allMonthData = ArrayList<Int>()
        val allDayData = ArrayList<Int>()
        val allOrderData = ArrayList<String>()
        val allPriceData = ArrayList<Int>()
        expensesData.forEach {
                expenses -> eIdData.add(expenses.faveID)
                            allYearData.add(expenses.year)
                            allMonthData.add(expenses.month)
                            allDayData.add(expenses.day)
                            allPriceData.add(expenses.price)
                            allOrderData.add(expenses.order) }

        val listViewData = mutableListOf<ListExpensesData>()
        for (i in 0 until eIdData.size) {
            if (eIdData[i] == id) {
                listViewData.add(
                    ListExpensesData(
                        allYearData[i].toString() + "/" + allMonthData[i].toString() + "/" + allDayData[i].toString(),
                        allOrderData[i],
                        allPriceData[i].toString() + "円"
                    )
                )
            }
        }

        val adapter = ExpensesAdapter(this, listViewData)
        binding.expensesListview.adapter = adapter
    }
}