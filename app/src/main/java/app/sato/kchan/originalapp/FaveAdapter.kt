package app.sato.kchan.originalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class FaveAdapter(context: Context, var list: List<ListFaveData>): ArrayAdapter<ListFaveData>(context, 0, list) {

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val data = list[position]

        // レイアウトの設定
        var view = convertView
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.listview_fave, parent, false)
        }

        // 各Viewの設定
        val name = view?.findViewById<TextView>(R.id.fave_text)
        name?.text = data.name
        val sum = view?.findViewById<TextView>(R.id.sum_text)
        sum?.text = data.sum

        return view!!
    }
}