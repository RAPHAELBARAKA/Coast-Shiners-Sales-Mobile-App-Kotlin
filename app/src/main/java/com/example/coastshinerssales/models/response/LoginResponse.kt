package com.example.coastshinerssales.models.response

import java.util.Date

class LoginResponse (
    val message: String,
    val isAdmin: Boolean? = null,
    val isDoctor: Boolean? = null,
    val lastLogin: Date? = null
)