package com.example.medexpiredatetracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.example.medexpiredatetracker.adapters.CategoryAdapter
import com.example.medexpiredatetracker.R
import com.example.medexpiredatetracker.data.DataManager
import com.example.medexpiredatetracker.data.models.Category


class CatalogFragment : Fragment(), NewCatalogFragment.CatalogGroupCreateListener {

    private lateinit var button: ImageButton
    private lateinit var listView: ListView
    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalog, container, false)
        setupButton(view)
        setupList(view)
        return view
    }

    private fun setupButton(view: View) {
        button = view.findViewById(R.id.btn_create_catalog)

        button.setOnClickListener {
            showCatalogDialog()
        }
    }

    private fun setupList(view: View) {
        listView = view.findViewById(R.id.list)

        adapter = CategoryAdapter(
            requireActivity().applicationContext,
            DataManager.getInstance().getCategories(),
            { category ->
                val medicineFragment = MedicineFragment(category)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, medicineFragment)
                    .addToBackStack(null)
                    .commit()
            },
            { category ->
                showCustomContextMenu(view, category)
                true
            })
        listView.adapter = adapter
    }

    private fun showCustomContextMenu(view: View, category: Category) {
        val popup = PopupMenu(requireActivity().applicationContext, view) //
        popup.menuInflater.inflate(R.menu.context_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.edit_action -> {
                    DataManager.getInstance().updateCategory(category)
                    true
                }

                R.id.delete_action -> {
                    DataManager.getInstance().deleteCategory(category)
                    adapter.remove(category)
                    true
                }

                else -> false
            }
        }

        popup.show()
    }


    private fun showCatalogDialog() {
        val dialog = NewCatalogFragment.newInstance()

        dialog.show(childFragmentManager, NewCatalogFragment.TAG)
    }

    override fun onCatalogGroupCreated(groupName: String, color: String) {
        println("$groupName, $color")

        val newCategory = DataManager.getInstance().createCategory(groupName, color)
        adapter.add(newCategory)
    }
}