package com.example.medexpiredatetracker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.medexpiredatetracker.R
import com.example.medexpiredatetracker.data.models.Medicine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MedicineAdapter(
    context: Context,
    private val items: List<Medicine>,
    private val onItemClick: (Medicine) -> Unit,
    private val onItemLongClick: (Medicine) -> Boolean
) : ArrayAdapter<Medicine>(context, R.layout.item_medicine, items) {

    private inner class ViewHolder {
        lateinit var numberView: TextView
        lateinit var nameView: TextView
        lateinit var dateView: TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position) ?: return View(context)
        val holder: ViewHolder
        val view: View
        var dateFormat = SimpleDateFormat("MM/yy", Locale.getDefault())

        if (convertView == null) {
            holder = ViewHolder()
            view = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false)
            holder.numberView = view.findViewById(R.id.medicine_number)
            holder.nameView = view.findViewById(R.id.medicine_name)
            holder.dateView = view.findViewById(R.id.medicine_expire_date)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        holder.numberView.text = "${position + 1}."
        holder.nameView.text = item.name
        holder.dateView.text = dateFormat.format(item.expireDate)
        val currentDate = Date()

        if (currentDate.year == item.expireDate.year && currentDate.month == item.expireDate.month) {
            val closeToExpireColor = ContextCompat.getColor(context, R.color.closeToExpireColor)
            holder.nameView.setBackgroundColor(closeToExpireColor)
            holder.dateView.setBackgroundColor(closeToExpireColor)
            holder.numberView.setBackgroundColor(closeToExpireColor)
        }
        else if (currentDate < item.expireDate) {
            val expiredColor = ContextCompat.getColor(context, R.color.expire)
            holder.nameView.setBackgroundColor(expiredColor)
            holder.dateView.setBackgroundColor(expiredColor)
            holder.numberView.setBackgroundColor(expiredColor)
        }
        else {
            val defaultColor = ContextCompat.getColor(context, R.color.white)
            holder.nameView.setBackgroundColor(defaultColor)
            holder.dateView.setBackgroundColor(defaultColor)
            holder.numberView.setBackgroundColor(defaultColor)
        }

        view.setOnClickListener { onItemClick(item) }
        view.setOnLongClickListener{onItemLongClick(item)}

        return view
    }
}