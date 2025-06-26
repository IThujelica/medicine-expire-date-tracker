package com.example.medexpiredatetracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.example.medexpiredatetracker.R
import com.example.medexpiredatetracker.adapters.MedicineAdapter
import com.example.medexpiredatetracker.data.DataManager
import com.example.medexpiredatetracker.data.models.Category
import com.example.medexpiredatetracker.data.models.Medicine
import java.util.Date


class MedicineFragment(val category: Category) : Fragment(),
    NewMedicineFragment.MedicineItemCreateListener {

    private lateinit var button: ImageButton
    private lateinit var listView: ListView
    private lateinit var adapter: MedicineAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_medicine, container, false)
        setupButton(view)
        setupList(view)
        return view
    }

    private fun setupButton(view: View) {
        button = view.findViewById(R.id.btn_create_medicine)

        button.setOnClickListener {
            showMedicineCreateDialog()
        }
    }

    private fun setupList(view: View) {
        listView = view.findViewById(R.id.list)

        adapter = MedicineAdapter(
            requireActivity().applicationContext,
            DataManager.getInstance().getMedicinesByCategory(category),
            {
                print("medicine")
            },
            { medicine ->
                showCustomContextMenu(view, medicine)
                true
            })
        listView.adapter = adapter
    }

    private fun showCustomContextMenu(view: View, medicine: Medicine) {
        val popup = PopupMenu(requireActivity().applicationContext, view) //
        popup.menuInflater.inflate(R.menu.context_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.edit_action -> {
                    DataManager.getInstance().updateMedicine(medicine)
                    true
                }

                R.id.delete_action -> {
                    DataManager.getInstance().deleteMedicine(medicine)
                    adapter.remove(medicine)
                    true
                }

                else -> false
            }
        }

        popup.show()
    }

    private fun showMedicineCreateDialog() {
        val dialog = NewMedicineFragment.newInstance()

        dialog.show(childFragmentManager, NewMedicineFragment.TAG)
    }

    override fun onMedicineItemCreated(name: String, date: Date) {
        println("$name, $date")

        val newMedicine = DataManager.getInstance().createMedicine(name, date, category)
        adapter.add(newMedicine)
    }
}