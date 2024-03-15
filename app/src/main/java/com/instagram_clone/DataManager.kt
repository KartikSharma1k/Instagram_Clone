package com.instagram_clone

import android.text.TextUtils
import android.util.Patterns
import com.instagram_clone.models.UserData
import java.util.regex.Pattern

object DataManager {

    val userPlaceHolder = "https://firebasestorage.googleapis.com/v0/b/instagram-clone-86a1d.appspot.com/o/profilePics%2Finstagram_profile_place_holder.svg?alt=media&token=61d77911-27e2-4440-9abf-b9b553f340d4"
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

    var userData: UserData = UserData()

}