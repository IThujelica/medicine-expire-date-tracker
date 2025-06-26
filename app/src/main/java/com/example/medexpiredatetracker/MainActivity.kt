package com.example.medexpiredatetracker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import com.example.medexpiredatetracker.data.DataManager
import com.example.medexpiredatetracker.data.NotificationHelper
import com.example.medexpiredatetracker.data.models.Medicine
import com.example.medexpiredatetracker.fragments.*
import java.util.Date


class MainActivity : AppCompatActivity() {
    // Current active button
    private var activeButton: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataManager.getInstance().dbInit(applicationContext)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setupNotification()

        // System insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Buttons
        val btnProfile = findViewById<ImageButton>(R.id.btn_profile)
        val btnCatalog = findViewById<ImageButton>(R.id.btn_catalog)
        val btnSettings = findViewById<ImageButton>(R.id.btn_settings)

        // Button listeners
        btnProfile.setOnClickListener {
            replaceFragment(ProfileFragment())
            setActiveButton(btnProfile)
        }
        btnCatalog.setOnClickListener {
            replaceFragment(CatalogFragment())
            setActiveButton(btnCatalog)
        }
        btnSettings.setOnClickListener {
            replaceFragment(SettingsFragment())
            setActiveButton(btnSettings)
        }

        // Main page
        if (savedInstanceState == null) {
            replaceFragment(CatalogFragment(), addToBackStack = false)
            setActiveButton(btnCatalog)
        }
        setupBackPressHandler()
    }

    // Bottom buttons change color
    private fun setActiveButton(button: ImageButton) {
        activeButton?.setColorFilter(ContextCompat.getColor(this, R.color.unselected))
        button.setColorFilter(ContextCompat.getColor(this, R.color.selected))
        activeButton = button
    }

    // Replace + BackStack
    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            if (addToBackStack) {
                addToBackStack(fragment::class.java.simpleName)
            }
            commit()
        }
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                    updateActiveButtonAfterBack()
                } else {
                    finish()
                }
            }
        })
    }

    private fun updateActiveButtonAfterBack() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        when (currentFragment) {
            is ProfileFragment -> setActiveButton(findViewById(R.id.btn_profile))
            is CatalogFragment -> setActiveButton(findViewById(R.id.btn_catalog))
            is SettingsFragment -> setActiveButton(findViewById(R.id.btn_settings))
        }
    }

    private fun setupNotification() {

        val currentDate = Date()
        val expireLimitDate = Date(currentDate.year, currentDate.month + 1, 1)

        val expiredMedicines = DataManager.getInstance().getMedicines()
            .filter { medicine: Medicine -> medicine.expireDate <= expireLimitDate }

        NotificationHelper.createNotificationChannel(this)
        for (medicine in expiredMedicines) {
            NotificationHelper.showExpiryNotification(
                this,
                medicine,
                DataManager.getInstance().getCategoryById(medicine.categoryId)
            )
        }
    }
}