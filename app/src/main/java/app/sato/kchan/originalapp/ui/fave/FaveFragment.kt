package app.sato.kchan.originalapp.ui.fave

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import app.sato.kchan.originalapp.*
import app.sato.kchan.originalapp.databinding.FragmentFaveBinding

class FaveFragment : Fragment() {

    private var _binding: FragmentFaveBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var faveDao: FaveDao
    private lateinit var expensesDao: ExpensesDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        faveDao = Application.database.faveDao()
        expensesDao = Application.database.expensesDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaveBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.faveList.setOnItemClickListener { _, _, position, _ ->
            val faveDetailActivityIntent: Intent = Intent(activity, FaveDetailActivity::class.java)
            faveDetailActivityIntent.putExtra("id", position.toLong())
            startActivity(faveDetailActivityIntent)
        }

        return root
    }

    override fun onStart() {
        super.onStart()

        if(getFaveID() == 0) {
            binding.yenFab.isClickable = false
            binding.fFirstText.text = "まずは推しを登録してね！"
        } else {
            binding.yenFab.setOnClickListener {
                val addExpensesIntent: Intent = Intent(activity, AddExpensesActivity::class.java)
                startActivity(addExpensesIntent)
            }
        }

        binding.faveFab.setOnClickListener {
            val addFaveIntent: Intent = Intent(activity, AddFaveActivity::class.java)
            startActivity(addFaveIntent)
            getFave(requireContext())
        }

        getFave(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getFave(context: Context){
        val faveData = faveDao.findAll()
        val faveNameData = ArrayList<String>()
        faveData.forEach { fave -> faveNameData.add(fave.name) }

        val listViewData = mutableListOf<ListFaveData>()
        for (i in 1 .. faveNameData.size) {
            val sum = getSum(i)
            listViewData.add(ListFaveData(faveNameData[i-1],sum.toString()+"円"))
        }

        val adapter = FaveAdapter(requireContext(), listViewData)
        binding.faveList.adapter = adapter

        if (getFaveID() != 0) binding.fFirstText.text = ""
    }

    private fun getSum(id: Int): Int{
        val expensesData = expensesDao.findAll()
        var sum = 0

        val eIdData = ArrayList<Long>()
        expensesData.forEach { expenses -> eIdData.add(expenses.faveID) }
        val priceData = ArrayList<Int>()
        expensesData.forEach { expenses ->  priceData.add(expenses.price)}

        val data = ArrayList<String>()
        for (i in 0 until eIdData.size)  {
            if (eIdData[i] == id.toLong()) sum += priceData[i]
        }
        return sum
    }

    private fun getFaveID(): Int {
        val faveData = faveDao.findAll()
        val idData = ArrayList<Long>()
        faveData.forEach { fave -> idData.add(fave.id) }
        return idData.size
    }
}