package app.sato.kchan.originalapp.ui.home

import android.R
import android.app.ActionBar
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.sato.kchan.originalapp.AddExpensesActivity
import app.sato.kchan.originalapp.Application
import app.sato.kchan.originalapp.ExpensesDao
import app.sato.kchan.originalapp.FaveDao
import app.sato.kchan.originalapp.databinding.FragmentHomeBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val addExpensesIntent: Intent = Intent(activity, AddExpensesActivity::class.java)

        binding.fab.setOnClickListener {
            startActivity(addExpensesIntent)
        }
        return root
    }

    override fun onStart() {
        super.onStart()
        draw()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getExpenses(size: Int): ArrayList<Float> {
        val expensesData = expensesDao.findAll()
        val eIdData = ArrayList<Long>()
        expensesData.forEach { expenses -> eIdData.add(expenses.faveID) }
        val priceAllData = ArrayList<Int>()
        expensesData.forEach { expenses -> priceAllData.add(expenses.price) }

        var priceData = ArrayList<Float>()
        for (i in 1..size) {
            var price = 0.0f
            for (j in 0 until eIdData.size) {
                if (eIdData[j].toInt() == i) {
                    price += priceAllData[j].toFloat()
                }
            }
            priceData.add(price)
        }
        return priceData
    }

    private fun draw() {
        val faveData = faveDao.findAll()
        val faveNameData = ArrayList<String>()
        faveData.forEach { fave ->  faveNameData.add(fave.name)}

        val priceData: ArrayList<Float> = getExpenses(faveNameData.size)

        //①Entryにデータ格納
        var entryList = mutableListOf<PieEntry>()
        var listViewData = mutableListOf<Map<String, String>>()
        var sum = 0.0f
        for (i in 0 until faveNameData.size){
            if (priceData[i] != 0.0f) entryList.add(PieEntry(priceData[i], faveNameData[i]))
            sum += priceData[i]
            listViewData.add(mapOf("main" to faveNameData[i] ,"sub" to priceData[i].toInt().toString()+"円"))
        }

        //②PieDataSetにデータ格納
        val pieDataSet = PieDataSet(entryList, "expenses")
        //③DataSetのフォーマット指定

        pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        //④PieDataにPieDataSet格納
        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(20f)
        pieData.setValueTextColor(Color.WHITE)
        pieData.setDrawValues(true)
        //⑤PieChartにPieData格納
        val pieChart = binding.pieChart
        pieChart.data = pieData
        pieData.setValueFormatter(PercentFormatter(pieChart))
        //⑥Chartのフォーマット指定
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(15f)
        pieChart.setCenterTextColor(Color.WHITE)
        pieChart.legend.isEnabled = false
        pieChart.setCenterTextSize(20f)
        pieChart.centerText = "合計 ${sum.toInt()}円"
        pieChart.setCenterTextColor(Color.BLACK)
        pieChart.description.text = ""
        pieChart.setTouchEnabled(false)
        //⑦PieChart更新
        pieChart.invalidate()
    }

//    private fun text(listViewData: MutableList<Map<String, String>>) {
//        val adapter = SimpleAdapter(
//            requireContext(),
//            listViewData,
//            R.layout.simple_list_item_2,
//            arrayOf("main", "sub"),
//            intArrayOf(R.id.text1, R.id.text2))
//        binding.listView.adapter = adapter
//    }

}