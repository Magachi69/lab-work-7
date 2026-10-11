package com.example.tenantmanagementsystemss

// Holds one tenant's details
data class Tenant(
    val name: String,
    val phone: String,
    val rent: String
) {
    // Returns the details as a formatted text, also used for sharing later
    fun summary(): String {
        return "Tenant: $name\nPhone: $phone\nRent Paid: $rent"
    }
}