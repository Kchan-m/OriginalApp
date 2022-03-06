package app.sato.kchan.originalapp

import android.R
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import app.sato.kchan.originalapp.databinding.FaveAddBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddFave : AppCompatActivity() {
    private lateinit var binding: FaveAddBinding

    val faveDao = Application.database.faveDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FaveAddBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        binding.faveAddButton.setOnClickListener {
            if (binding.editFaveText.text != null) {
                val fave = Fave(0, binding.editFaveText.text.toString())
                faveDao.createFave(fave)
            }
            finish()
        }
    }
}