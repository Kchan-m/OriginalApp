package app.sato.kchan.originalapp

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.sato.kchan.originalapp.databinding.ActivityFaveAddBinding

class AddFaveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFaveAddBinding

    val faveDao = Application.database.faveDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaveAddBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.faveAddButton.setOnClickListener {
            if (binding.editFaveText.text.toString() != "") {
                val fave = Fave(0, binding.editFaveText.text.toString())
                faveDao.createFave(fave)
                finish()
            } else {
                Toast.makeText(this, "推しの名前を入力してください", Toast.LENGTH_LONG).show()
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
}