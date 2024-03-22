package com.instagram_clone.models

data class UserData(val fullName:String = "", val username:String = "", val uid:String = "", val email:String = "", val followers:ArrayList<String> = arrayListOf<String>(), val following:ArrayList<String> = arrayListOf<String>(), val bio:String = "", var photoUrl:String = "", var type:String = "")
