package app.sato.kchan.originalapp

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import app.sato.kchan.originalapp.databinding.ActivityDetailExpensesBinding
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

class ExpensesDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailExpensesBinding

    val expensesDao = Application.database.expensesDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailExpensesBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        val intent: Intent = getIntent()
        val faveId = intent.getLongExtra("id", 1)
        val position = intent.getIntExtra("position", 0)

        getExpenses(faveId, position)
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

    private fun getExpenses(id: Long, position: Int){

        val expensesData = expensesDao.findAll()

        val faveIdData = ArrayList<Long>()
        expensesData.forEach { expenses -> faveIdData.add(expenses.faveID) }
        val allData = ArrayList<Expenses>()
        expensesData.forEach { expenses -> allData.add(expenses) }

        val data = ArrayList<Expenses>()
        val idData = ArrayList<Long>()
        for (i in 0 until faveIdData.size) {
            if (faveIdData[i] == id) {
                data.add(allData[i])
                idData.add(allData[i].id)
            }
        }

        val storage = FirebaseStorage.getInstance().reference
        val userImageRef = storage.child("image" + idData[position] + ".jpg")

        Glide.with(this)
            .load(userImageRef)
            .into(binding.orderImageView)

        val expenses = data[position]

        binding.dateTextView.setText("${expenses.year}年${expenses.month}月${expenses.day}日")
        binding.orderTextView.setText(expenses.order)
        binding.priceTextView.setText("${expenses.price}円")
        binding.memoTextView.setText(expenses.memo)
    }
}