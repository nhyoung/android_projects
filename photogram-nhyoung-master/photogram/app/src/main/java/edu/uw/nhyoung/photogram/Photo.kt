package edu.uw.nhyoung.photogram

data class Photo(
    var uri: String = "",
    var title: String = "",
    var user: String? = "",
    var uid: String? = "",
    var likes: MutableMap<String, Boolean>? = null
)