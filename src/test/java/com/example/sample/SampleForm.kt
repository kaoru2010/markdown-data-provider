package com.example.sample

class SampleForm {
    var email: String? = null
    var emailConfirm: String? = null
    var password: String? = null

    fun canSubmit() = !(email.isNullOrEmpty() || emailConfirm.isNullOrEmpty() || password.isNullOrEmpty())

    fun showsErrorDialog() = !(email == emailConfirm && (password ?: "").length >= 8)
}
