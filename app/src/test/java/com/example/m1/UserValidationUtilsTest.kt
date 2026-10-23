package com.example.m1

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for UserValidationUtils helper methods
 */
class UserValidationUtilsTest {

    @Test
    fun testValidEmail() {
        assertTrue(UserValidationUtils.isValidEmail("user@example.com"))
        assertTrue(UserValidationUtils.isValidEmail("jay.dobariya@metro.gujarat.gov.in"))
        assertFalse(UserValidationUtils.isValidEmail("invalid-email"))
        assertFalse(UserValidationUtils.isValidEmail(""))
        assertFalse(UserValidationUtils.isValidEmail(null))
    }

    @Test
    fun testValidPhone() {
        assertTrue(UserValidationUtils.isValidPhone("9876543210"))
        assertTrue(UserValidationUtils.isValidPhone("+919876543210"))
        assertFalse(UserValidationUtils.isValidPhone("12345"))
        assertFalse(UserValidationUtils.isValidPhone("abcdefghij"))
        assertFalse(UserValidationUtils.isValidPhone(""))
    }

    @Test
    fun testValidPassword() {
        assertTrue(UserValidationUtils.isValidPassword("secret123"))
        assertTrue(UserValidationUtils.isValidPassword("123456"))
        assertFalse(UserValidationUtils.isValidPassword("12345"))
        assertFalse(UserValidationUtils.isValidPassword(null))
    }

    @Test
    fun testValidOtp() {
        assertTrue(UserValidationUtils.isValidOtp("1234"))
        assertFalse(UserValidationUtils.isValidOtp("123"))
        assertFalse(UserValidationUtils.isValidOtp("12345"))
        assertFalse(UserValidationUtils.isValidOtp("12a4"))
    }
}
