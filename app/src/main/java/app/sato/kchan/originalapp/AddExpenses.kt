package app.sato.kchan.originalapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.sato.kchan.originalapp.databinding.ExpensesAddBinding
import app.sato.kchan.originalapp.ui.home.HomeFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddExpenses : AppCompatActivity() {
    private lateinit var binding: ExpensesAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ExpensesAddBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        val year = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"))
        binding.editYearText.setText(year)
        val month = LocalDate.now().format(DateTimeFormatter.ofPattern("MM"))
        binding.editMonthText.setText(month)
        val day = LocalDate.now().format(DateTimeFormatter.ofPattern("dd"))
        binding.editDayText.setText(day)

        binding.addButton.setOnClickListener {
            finish()
        }
    }

}