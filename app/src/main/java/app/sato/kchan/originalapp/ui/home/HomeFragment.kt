package app.sato.kchan.originalapp.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dimensions = listOf("sum")
        val values = listOf(1f)

        //①Entryにデータ格納
        var entryList = mutableListOf<PieEntry>()
        for(i in values.indices){
            entryList.add(
                PieEntry(values[i], dimensions[i])
            )
        }

        //②PieDataSetにデータ格納
        val pieDataSet = PieDataSet(entryList, "candle")
        //③DataSetのフォーマット指定
        pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        pieDataSet.setDrawValues(true)
        //④PieDataにPieDataSet格納
        val pieData = PieData(pieDataSet)
        pieData.setValueFormatter(PercentFormatter())
        pieData.setValueTextSize(12f)
        pieData.setValueTextColor(Color.WHITE)
        //⑤PieChartにPieData格納
        val pieChart = binding.pieChart
        pieChart.data = pieData
        //⑥Chartのフォーマット指定
        pieChart.legend.isEnabled = false
        //⑦PieChart更新
        pieChart.invalidate()


        val addExpensesIntent: Intent = Intent(activity, AddExpensesActivity::class.java)

        binding.fab.setOnClickListener {
            startActivity(addExpensesIntent)
        }

        return root
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
    }

    private fun getExpenses(context: Context)
    {
        val expensesData = expensesDao.findAll()
        val data = ArrayList<Int>()
        expensesData.forEach { expenses -> data.add(expenses.price) }
    }
}