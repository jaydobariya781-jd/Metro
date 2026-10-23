package com.example.m1.admin

data class UserModel(
    val id: String,
    var name: String,
    var email: String,
    var role: String,   // "ADMIN", "USER"
    var status: String  // "Active", "Unactive"
)
