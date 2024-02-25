package com.instagram_clone

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern

object DataManager {

    fun isValidEmail(target: String): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun isSpecialCharacter(string: String): Boolean {
        val regex = Pattern.compile("[\$&+,:;=\\\\\\\\?@#|/'<>.^*()%!-/]")
        if (regex.matcher(string).find()) {
            return true
        }
        return false
    }

}