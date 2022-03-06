package app.sato.kchan.originalapp.ui.fave

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FaveViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is fave Fragment"
    }
    val text: LiveData<String> = _text
}