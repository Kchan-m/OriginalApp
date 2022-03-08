package app.sato.kchan.originalapp.ui.fave

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import app.sato.kchan.originalapp.AddFaveActivity
import app.sato.kchan.originalapp.Application
import app.sato.kchan.originalapp.ExpencesDetailActivity
import app.sato.kchan.originalapp.FaveDao
import app.sato.kchan.originalapp.databinding.FragmentFaveBinding

class FaveFragment : Fragment() {

    private var _binding: FragmentFaveBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var faveDao: FaveDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        faveDao = Application.database.faveDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaveBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.faveList.setOnItemClickListener { adapterView, view, position, id ->
            val expencesDetailActivityIntent: Intent = Intent(activity, ExpencesDetailActivity::class.java)
            expencesDetailActivityIntent.putExtra("id", position.toLong())
            startActivity(expencesDetailActivityIntent)
        }

        val addFaveIntent: Intent = Intent(activity, AddFaveActivity::class.java)

        binding.fab.setOnClickListener {
            startActivity(addFaveIntent)
            getFave(requireContext())
        }
        return root
    }

    override fun onStart() {
        super.onStart()
        getFave(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        binding.faveList.adapter = adapter
    }
}