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

        if(getFaveID() == 0) {
            binding.fab.isClickable = false
            binding.firstText.text = "まずは推し画面から推しを登録してね！"
        } else {
            binding.fab.setOnClickListener {
                startActivity(addExpensesIntent)
            }
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

    private fun getFaveID(): Int {
        val faveData = faveDao.findAll()
        val idData = ArrayList<Long>()
        faveData.forEach { fave -> idData.add(fave.id) }
        return idData.size
    }

    private fun getExpenses(size: Int): ArrayList<Float> {
        val expensesData = expensesDao.findAll()
        val eIdData = ArrayList<Long>()
        val priceAllData = ArrayList<Int>()
        expensesData.forEach { expenses ->
            eIdData.add(expenses.faveID)
            priceAllData.add(expenses.price) }

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
        var sum = 0.0f
        for (i in 0 until faveNameData.size) {
            if (priceData[i] != 0.0f) entryList.add(PieEntry(priceData[i], faveNameData[i]))
            sum += priceData[i]
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
        pieChart.setNoDataText("")
        if (getFaveID() != 0) {
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
    }
}