package com.example.medexpiredatetracker.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ToggleButton
import androidx.fragment.app.DialogFragment
import com.example.medexpiredatetracker.R

class NewCatalogFragment : DialogFragment() {

    private lateinit var yellowToggle: ToggleButton
    private lateinit var redToggle: ToggleButton
    private lateinit var blueToggle: ToggleButton
    private lateinit var greenToggle: ToggleButton
    private lateinit var purpleToggle: ToggleButton

    private lateinit var selectedColor: String

    private lateinit var enterNameField: EditText

    private lateinit var saveButton: Button

    private lateinit var scaleUp: Animation
    private lateinit var scaleDown: Animation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_catalog_group_create, container, false)

        scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.color_button_scale_up)
        scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.color_button_scale_down)

        // setup Interactions
        setupToggleButtons(view)
        setupEnterNameField(view)
        setupSaveButton(view)

        return view
    }

    private fun setupToggleButtons(view: View) {
        yellowToggle = view.findViewById(R.id.yellowToggle)
        redToggle = view.findViewById(R.id.redToggle)
        blueToggle = view.findViewById(R.id.blueToggle)
        greenToggle = view.findViewById(R.id.greenToggle)
        purpleToggle = view.findViewById(R.id.purpleToggle)

        yellowToggle.setOnClickListener { onColorToggleClick(yellowToggle) }
        redToggle.setOnClickListener { onColorToggleClick(redToggle) }
        blueToggle.setOnClickListener { onColorToggleClick(blueToggle) }
        greenToggle.setOnClickListener { onColorToggleClick(greenToggle) }
        purpleToggle.setOnClickListener { onColorToggleClick(purpleToggle) }

        selectedColor = "blue"
        blueToggle.isChecked = true
        blueToggle.startAnimation(scaleUp)
    }

    private fun onColorToggleClick(clickedToggle: ToggleButton) {
        if (!clickedToggle.isChecked) {
            val allToggles = listOf(redToggle, blueToggle, greenToggle, yellowToggle, purpleToggle)
            allToggles.forEach { toggle ->
                if (toggle.isChecked) {
                    toggle.startAnimation(scaleDown)
                    toggle.isChecked = false
                }
            }
            clickedToggle.isChecked = true
            clickedToggle.startAnimation(scaleUp)

            selectedColor = when (clickedToggle.id) {
                R.id.redToggle -> "red"
                R.id.blueToggle -> "blue"
                R.id.greenToggle -> "green"
                R.id.yellowToggle -> "yellow"
                R.id.purpleToggle -> "purple"
                else -> "blue"
            }
        }
    }

    private fun setupEnterNameField(view: View) {
        enterNameField = view.findViewById(R.id.EnterTextCategoryName)
        val maxLength = 25

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
            }
        })
    }

    private fun setupSaveButton (view: View){
        saveButton = view.findViewById(R.id.buttonSave)

        saveButton.setOnClickListener {
            val groupName = enterNameField.text.toString().trim()

            if (groupName.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter name", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            (parentFragment as? CatalogGroupCreateListener)?.onCatalogGroupCreated(
                groupName, selectedColor
            ) ?: (activity as? CatalogGroupCreateListener)?.onCatalogGroupCreated(
                groupName, selectedColor
            )
            dismiss()
        }

    }

    interface CatalogGroupCreateListener {
        fun onCatalogGroupCreated(groupName: String, color: String)
        }

    companion object {
        fun newInstance() = NewCatalogFragment()
        const val TAG = "new_catalog_fragment"
    }
}