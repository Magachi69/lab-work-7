package com.example.tenantmanagementsystemss

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.tenantmanagementsystemss.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // The most recently saved tenant. Lost on rotation unless we save it
    private var lastTenant: Tenant? = null
    // Save count tracking for rotation exercise
    private var saveCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TENANT_LIFECYCLE", "MainActivity: onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ── RESTORE STATE (only after a rotation) ─────────────────
        if (savedInstanceState != null) {
            val name = savedInstanceState.getString("tenant_name")
            val phone = savedInstanceState.getString("tenant_phone")
            val rent = savedInstanceState.getString("tenant_rent")
            if (name != null && phone != null && rent != null) {
                val tenant = Tenant(name, phone, rent)
                lastTenant = tenant
                binding.tenantResultTextView.text = tenant.summary()
            }

            saveCount = savedInstanceState.getInt("save_count", 0)
            if (saveCount > 0) {
                Toast.makeText(this, "SAVE tapped $saveCount times before rotation", Toast.LENGTH_SHORT).show()
            }
        }

        val email = intent.getStringExtra("EMAIL")
        if (email != null && savedInstanceState == null) {
            Toast.makeText(this, "Logged in as $email", Toast.LENGTH_LONG).show()
        }

        // ── SAVE button ───────────────────────────────────────────
        binding.saveButton.setOnClickListener {
            val name = binding.tenantNameEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            val rent = binding.rentEditText.text.toString().trim()

            var valid = true
            if (name.isEmpty()) { binding.tenantNameEditText.error = "Required"; valid = false }
            if (phone.isEmpty()) { binding.phoneEditText.error = "Required"; valid = false }
            if (rent.isEmpty()) { binding.rentEditText.error = "Required"; valid = false }
            if (!valid) return@setOnClickListener

            val tenant = Tenant(name, phone, rent)
            lastTenant = tenant
            saveCount++
            binding.tenantResultTextView.text = tenant.summary()
        }

        // ── CALL TENANT button (implicit Intent) ──────────────────
        binding.callButton.setOnClickListener {
            val tenant = lastTenant
            if (tenant == null) {
                Toast.makeText(this, "Save a tenant first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(Intent.ACTION_DIAL, "tel:${tenant.phone}".toUri())
            startActivity(intent)
        }

        // ── SHARE button (implicit Intent) ────────────────────────
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

    // ── SAVE STATE (just before the Activity is destroyed) ────────
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("TENANT_LIFECYCLE", "MainActivity: onSaveInstanceState")
        val tenant = lastTenant
        if (tenant != null) {
            outState.putString("tenant_name", tenant.name)
            outState.putString("tenant_phone", tenant.phone)
            outState.putString("tenant_rent", tenant.rent)
        }
        outState.putInt("save_count", saveCount)
    }

    // ── LIFECYCLE METHODS ─────────────────────────────────────────
    override fun onStart() {
        super.onStart()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TENANT_LIFECYCLE", "MainActivity: onDestroy")
    }
}
