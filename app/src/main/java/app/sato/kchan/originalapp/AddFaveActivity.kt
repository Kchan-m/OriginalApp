package app.sato.kchan.originalapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.sato.kchan.originalapp.databinding.ActivityFaveAddBinding

class AddFaveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFaveAddBinding

    val faveDao = Application.database.faveDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaveAddBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        binding.faveAddButton.setOnClickListener {
            if (binding.editFaveText.text != null) {
                val fave = Fave(0, binding.editFaveText.text.toString())
                faveDao.createFave(fave)
            }
            finish()
        }
    }
}