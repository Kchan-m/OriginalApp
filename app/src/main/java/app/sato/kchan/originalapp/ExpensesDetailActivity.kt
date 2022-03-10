package app.sato.kchan.originalapp

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
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
        val id = intent.getLongExtra("id", 0)

        getExpenses(id)
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
        val storage = FirebaseStorage.getInstance()
        val userImageRef = storage.reference.child("images")

        Glide.with(this)
            .asBitmap()
            .load(userImageRef)
            .into(binding.orderImageView)

        val expenses = expensesDao.findId(id)

        binding.dateTextView.setText("${expenses.year}年${expenses.month}月${expenses.day}日")
        binding.orderTextView.setText(expenses.order)
        binding.priceTextView.setText("${expenses.price}円")
        binding.memoTextView.setText(expenses.memo)
    }
}