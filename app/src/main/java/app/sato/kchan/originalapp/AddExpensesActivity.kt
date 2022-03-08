package app.sato.kchan.originalapp

import android.R
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.sato.kchan.originalapp.databinding.ActivityExpensesAddBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddExpensesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpensesAddBinding

    val faveDao = Application.database.faveDao()
    val expensesDao = Application.database.expensesDao()

    companion object {
        private const val READ_REQUEST_CODE: Int = 42
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpensesAddBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        val year = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"))
        binding.editYearText.setText(year)
        val month = LocalDate.now().format(DateTimeFormatter.ofPattern("MM"))
        binding.editMonthText.setText(month)
        val day = LocalDate.now().format(DateTimeFormatter.ofPattern("dd"))
        binding.editDayText.setText(day)

        getFave(this)

        binding.addImagaviewButton.setOnClickListener {

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            startActivityForResult(intent, READ_REQUEST_CODE)
        }

        binding.addButton.setOnClickListener {

            val id = getFaveID()

            val expenses = Expenses(
                0,
                binding.editYearText.text.toString().toInt(),
                binding.editMonthText.text.toString().toInt(),
                binding.editDayText.text.toString().toInt(),
                id,
                //binding.imageView,
                binding.order.text.toString(),
                binding.price.text.toString().toInt(),
                binding.memo.text.toString()
            )
            expensesDao.createExpenses(expenses)

            finish()
        }
    }

    //写真が選択された後の動き
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            READ_REQUEST_CODE -> {
                try {
                    resultData?.data?.also { uri ->
                        val inputStream = contentResolver?.openInputStream(uri)
                        val image = BitmapFactory.decodeStream(inputStream)
                        val imageView = binding.imageView
                        imageView.setImageBitmap(image)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "エラーが発生しました", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getFave(context: Context){
        //ioスレッド：DBからデータ取得
        //mainスレッド：取得結果をUIに表示
        val faveData = faveDao.findAll()
        val data = ArrayList<String>()
        faveData.forEach { fave -> data.add(fave.name) }
        //リスト項目とListViewを対応付けるArrayAdapterを用意する
        //リストで使用するlayout（simple_list_item_1）を指定する
        val adapter = ArrayAdapter(
            context, R.layout.simple_list_item_1, data)
        binding.spinner.adapter = adapter
    }

    private fun getFaveID(): Long{
        val faveData = faveDao.findAll()
        val data = ArrayList<String>()
        val idData = ArrayList<Long>()
        var id: Long = -1
        faveData.forEach {
                fave -> data.add(fave.name)
                        idData.add(fave.id)
        }

        for (i in 0 until data.size) {
            if (binding.spinner.selectedItem.toString() == data[i]) {
                id = idData[i]
            }
        }
        return id
    }
}