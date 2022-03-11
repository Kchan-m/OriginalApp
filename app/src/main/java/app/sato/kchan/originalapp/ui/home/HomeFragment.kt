package app.sato.kchan.originalapp.ui.home

import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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

        val priceData = getExpenses(faveNameData.size)
        println(priceData)

        //①Entryにデータ格納
        var entryList = mutableListOf<PieEntry>()
        var sum = 0.0f
        for (i in 0 until faveNameData.size){
            entryList.add(
                PieEntry(priceData[i], faveNameData[i])
            )
            sum += priceData[i]
            text(faveNameData[i], priceData[i])
        }

        //②PieDataSetにデータ格納
        val pieDataSet = PieDataSet(entryList, "expenses")
        //③DataSetのフォーマット指定
        pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        pieDataSet.setDrawValues(true)
        //④PieDataにPieDataSet格納
        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(false)
        //⑤PieChartにPieData格納
        val pieChart = binding.pieChart
        pieChart.data = pieData
        //⑥Chartのフォーマット指定
        pieChart.legend.isEnabled = false
        pieChart.setCenterTextSize(20f)
        pieChart.centerText = "合計 ${sum.toInt()}円"
        pieChart.description.text = ""
        pieChart.setTouchEnabled(false)
        //⑦PieChart更新
        pieChart.invalidate()
    }

    private fun text(fave: String, price: Float) {
        val textView = TextView(requireContext())
        textView.text = "$fave  ${price.toInt()}円"

        textView.textSize = 22.0f
        textView.setPadding(24, 24, 24, 24)
        val linearLayout = binding.container

        linearLayout.addView(
            textView,
            LinearLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
            )
        )
    }

}