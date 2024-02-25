package com.instagram_clone.models

data class UserData(val fullName:String = "", val username:String = "", val uid:String = "", val email:String = "", val followers:List<String> = emptyList<String>(), val following:List<String> = emptyList<String>(), val bio:String = "", val photoUrl:String = "")
