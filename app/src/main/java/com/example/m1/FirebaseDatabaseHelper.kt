package com.example.m1

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseDatabaseHelper {

    private fun getDb(): FirebaseFirestore = FirebaseFirestore.getInstance()

    fun saveUserProfile(
        phone: String,
        name: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        val cleanPhone = if (phone.startsWith("+91")) phone else "+91 $phone"
        val userMap = hashMapOf(
            "phone" to cleanPhone,
            "name" to name,
            "createdAt" to System.currentTimeMillis(),
            "lastLogin" to System.currentTimeMillis()
        )

        getDb().collection("users")
            .document(cleanPhone)
            .set(userMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { error -> onError(error.message ?: "Failed to save user profile") }
    }

    fun getUserProfile(
        phone: String,
        onSuccess: (name: String, phone: String) -> Unit,
        onError: (String) -> Unit = {}
    ) {
        val cleanPhone = if (phone.startsWith("+91")) phone else "+91 $phone"
        getDb().collection("users")
            .document(cleanPhone)
            .get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    val name = doc.getString("name") ?: "Aarav Patel"
                    val userPhone = doc.getString("phone") ?: cleanPhone
                    onSuccess(name, userPhone)
                } else {
                    onError("User not found in database")
                }
            }
            .addOnFailureListener { error ->
                onError(error.message ?: "Failed to fetch profile")
            }
    }

    fun saveTestTicket(
        ticketId: String,
        fromStation: String,
        toStation: String,
        fare: Double,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val ticket = hashMapOf(
            "ticketId" to ticketId,
            "fromStation" to fromStation,
            "toStation" to toStation,
            "fare" to fare,
            "status" to "BOOKED",
            "createdAt" to System.currentTimeMillis()
        )

        getDb().collection("tickets")
            .document(ticketId)
            .set(ticket)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { error ->
                onError(error.message ?: "Unable to save ticket")
            }
    }
}
