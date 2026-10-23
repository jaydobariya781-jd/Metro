package com.example.m1

import java.util.regex.Pattern

/**
 * Utility functions for user input validation (Email, Phone, Password, OTP)
 */
object UserValidationUtils {

    private val EMAIL_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun isValidEmail(email: String?): Boolean {
        if (email.isNullOrBlank()) return false
        return EMAIL_PATTERN.matcher(email.trim()).matches()
    }

    fun isValidPhone(phone: String?): Boolean {
        if (phone.isNullOrBlank()) return false
        val digits = phone.trim().replace("+91", "").replace("-", "").replace(" ", "")
        return digits.length == 10 && digits.all { it.isDigit() }
    }

    fun isValidPassword(password: String?): Boolean {
        if (password == null) return false
        return password.length >= 6
    }

    fun isValidOtp(otp: String?): Boolean {
        if (otp.isNullOrBlank()) return false
        return otp.trim().length == 4 && otp.trim().all { it.isDigit() }
    }

    fun isValidName(name: String?): Boolean {
        if (name.isNullOrBlank()) return false
        return name.trim().length >= 2
    }
}
