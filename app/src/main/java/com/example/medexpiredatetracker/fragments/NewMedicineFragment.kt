package com.example.medexpiredatetracker.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.medexpiredatetracker.R
import java.util.Calendar
import java.util.Date


class NewMedicineFragment : DialogFragment() {
    private lateinit var enterNameField: EditText
    private lateinit var saveButton: Button
    private var selectedMonth: Int? = null
    private var selectedYear: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_medicine_item_create, container, false)

        // setup Interactions
        setupMonthSpinner(view)
        setupYearSpinner(view)
        setupEnterNameField(view)
        setupSaveButton(view)

        return view
    }

    private fun setupYearSpinner(view: View) {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (currentYear..currentYear + 10).map { it.toString() }.toTypedArray()
        val yearSpinner: Spinner = view.findViewById(R.id.yearSpinner)
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext().applicationContext,
                android.R.layout.simple_spinner_item,
                years
            )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = adapter

        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedYear = null
                updateButton()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedYear = years[position].toInt()
                updateButton()
            }
        }
    }

    private fun setupMonthSpinner(view: View) {
        val monthSpinner: Spinner = view.findViewById(R.id.monthSpinner)
        val adapter: ArrayAdapter<CharSequence> =
            ArrayAdapter.createFromResource(
                requireContext().applicationContext,
                R.array.month,
                android.R.layout.simple_spinner_item
            )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSpinner.adapter = adapter

        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedMonth = null
                updateButton()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedMonth = position + 1
                updateButton()
            }
        }
    }

    private fun setupEnterNameField(view: View) {
        enterNameField = view.findViewById(R.id.EnterTextMedicineName)
        val maxLength = 35

        enterNameField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                enteredText: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                enteredText: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(enteredText: Editable?) {
                enteredText?.let {
                    if (it.length > maxLength) {
                        val trimmedText = it.toString().substring(0, maxLength)
                        enterNameField.setText(trimmedText)
                        enterNameField.setSelection(trimmedText.length)

                        Toast.makeText(
                            requireContext(),
                            "Maximum length is $maxLength characters",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                updateButton()
            }
        })
    }

    private fun setupSaveButton(view: View) {
        saveButton.isEnabled = false
        saveButton = view.findViewById(R.id.buttonSave)


        saveButton.setOnClickListener {
            val name = enterNameField.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter name", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            val currentDate = Date()
            val date = Date(selectedYear ?: currentDate.year, selectedMonth ?: currentDate.month, 1)

            (parentFragment as? MedicineItemCreateListener)?.onMedicineItemCreated(
                name, date
            ) ?: (activity as? MedicineItemCreateListener)?.onMedicineItemCreated(
                name, date
            )
            dismiss()
        }
    }

    private fun updateButton() {
        val groupName = enterNameField.text.toString().trim()
        saveButton.isEnabled =
            groupName.isNotEmpty() && selectedYear != null && selectedMonth != null
    }

    interface MedicineItemCreateListener {
        fun onMedicineItemCreated(name: String, date: Date)
    }

    companion object {
        fun newInstance() = NewMedicineFragment()
        const val TAG = "new_medicine_fragment"
    }

}