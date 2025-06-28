package com.example.medexpiredatetracker.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.medexpiredatetracker.R
import com.example.medexpiredatetracker.data.models.Category

class CategoryAdapter(
    context: Context,
    private val items: List<Category>,
    private val onItemClick: (Category) -> Unit,
    private val onItemLongClick: (Category, View) -> Boolean
) : ArrayAdapter<Category>(context, R.layout.item_catalog, items) {

    private inner class ViewHolder {
        lateinit var textView: TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position) ?: return View(context)
        val holder: ViewHolder
        val view: View

        if (convertView == null) {
            holder = ViewHolder()
            view = LayoutInflater.from(context).inflate(R.layout.item_catalog, parent, false)
            holder.textView = view.findViewById(R.id.category_item)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val color = when (item.color) {
            "red" -> R.color.red
            "blue" -> R.color.blue
            "green" -> R.color.green
            "yellow" -> R.color.yellow
            "purple" -> R.color.purple
            else -> R.color.blue
        }

        holder.textView.text = item.name
        holder.textView.setBackgroundColor(ContextCompat.getColor(context, color))
        view.setOnClickListener { onItemClick(item) }
        view.setOnLongClickListener{onItemLongClick(item, view)}
        return view
    }
}