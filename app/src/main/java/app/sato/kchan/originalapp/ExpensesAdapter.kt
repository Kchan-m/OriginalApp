package app.sato.kchan.originalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ExpensesAdapter(context: Context, var list: List<ListExpensesData>): ArrayAdapter<ListExpensesData>(context, 0, list) {

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val data = list[position]

        // レイアウトの設定
        var view = convertView
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.listview_expenses, parent, false)
        }

        // 各Viewの設定
        val date = view?.findViewById<TextView>(R.id.date_text)
        date?.text = data.date
        val order = view?.findViewById<TextView>(R.id.order_text)
        order?.text = data.order
        val price = view?.findViewById<TextView>(R.id.price_text)
        price?.text = data.price

        return view!!
    }
}