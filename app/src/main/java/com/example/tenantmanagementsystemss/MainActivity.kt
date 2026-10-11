package com.example.tenantmanagementsystemss

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tenantmanagementsystemss.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Remembers the most recently saved tenant (empty before the first SAVE)
    private var lastTenant: Tenant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Task 2: read the email sent by LoginActivity and welcome the user
        val email = intent.getStringExtra("EMAIL")
        if (email != null) {
            Toast.makeText(this, "Logged in as $email", Toast.LENGTH_LONG).show()
        }

        binding.saveButton.setOnClickListener {
            val name = binding.tenantNameEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            val rent = binding.rentEditText.text.toString().trim()

            // Task 3: validate, showing "Required" on every empty field
            var valid = true
            if (name.isEmpty()) {
                binding.tenantNameEditText.error = "Required"
                valid = false
            }
            if (phone.isEmpty()) {
                binding.phoneEditText.error = "Required"
                valid = false
            }
            if (rent.isEmpty()) {
                binding.rentEditText.error = "Required"
                valid = false
            }
            if (!valid) return@setOnClickListener

            val tenant = Tenant(name, phone, rent)
            lastTenant = tenant
            binding.tenantResultTextView.text = tenant.summary()
        }

        binding.callButton.setOnClickListener {
            val tenant = lastTenant
            if (tenant == null) {
                Toast.makeText(this, "Save a tenant first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${tenant.phone}"))
            startActivity(intent)
        }

        // Task 1: share the saved tenant's details with another app
        binding.shareButton.setOnClickListener {
            val tenant = lastTenant
            if (tenant == null) {
                Toast.makeText(this, "Save a tenant first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, tenant.summary())
            startActivity(Intent.createChooser(intent, "Share tenant"))
        }
    }
}