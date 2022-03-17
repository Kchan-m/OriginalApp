package app.sato.kchan.originalapp

import android.R
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.sato.kchan.originalapp.databinding.ActivityExpensesAddBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.FileInputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val year = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"))
        binding.editYearText.setText(year)
        val month = LocalDate.now().format(DateTimeFormatter.ofPattern("MM"))
        binding.editMonthText.setText(month)
        val day = LocalDate.now().format(DateTimeFormatter.ofPattern("dd"))
        binding.editDayText.setText(day)

        getFave(this)

        binding.addImagaviewButton.setOnClickListener {

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            // カテゴリーを設定
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            // MIMEタイプを設定
            intent.type = "image/*"

            startActivityForResult(intent, READ_REQUEST_CODE)
        }

        binding.addButton.setOnClickListener {
            if (binding.editYearText.text.toString() == "" ||
                binding.editMonthText.text.toString() == "" ||
                binding.editDayText.text.toString() == "") {
                Toast.makeText(this, "日付を入力してください", Toast.LENGTH_LONG).show()
            } else if (binding.spinner.selectedItem.toString() == "") {
                Toast.makeText(this, "推しを選択してください", Toast.LENGTH_LONG).show()
            } else if (binding.order.text.toString() == "") {
                Toast.makeText(this, "購入したものを入力してください", Toast.LENGTH_LONG).show()
            } else if (binding.price.text.toString() == "") {
                Toast.makeText(this, "価格を入力してください", Toast.LENGTH_LONG).show()
            } else {
                val expenses = Expenses(
                    0,
                    binding.editYearText.text.toString().toInt(),
                    binding.editMonthText.text.toString().toInt(),
                    binding.editDayText.text.toString().toInt(),
                    getFaveID(),
                    binding.order.text.toString(),
                    binding.price.text.toString().toInt(),
                    binding.memo.text.toString()
                )
                expensesDao.createExpenses(expenses)
                finish()
            }
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

    //写真が選択された後の動き
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (resultCode != RESULT_OK) {
            return
        }

        val pref: SharedPreferences = getSharedPreferences("uniqueID", Context.MODE_PRIVATE)
        val uniqueID = pref.getString("uniqueID", UUID.randomUUID().toString())
        val editor = pref.edit()
        editor.putString("uniqueID", uniqueID)
        editor.apply()

        val storage = FirebaseStorage.getInstance()
        val userImageRef = storage.reference.child(uniqueID + "image" + getExpensesID() + ".jpg")
        when (requestCode) {
            READ_REQUEST_CODE -> {
                resultData?.data?.also { uri ->
                    val inputStream = contentResolver?.openInputStream(uri)
                    val image = BitmapFactory.decodeStream(inputStream)
                    val imageView = binding.imageView
                    imageView.setImageBitmap(image)
                    userImageRef.putFile(uri)
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

    private fun getExpensesID(): Int {
        val expensesData = expensesDao.findAll()
        val idData = ArrayList<Long>()
        expensesData.forEach {
            expenses -> idData.add(expenses.id)
        }

        return idData.size+1
    }
}