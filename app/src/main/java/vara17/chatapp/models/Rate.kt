package vara17.chatapp.models

import java.util.*

data class Rate(
    val text: String = "",
    val rate: Float = 0f,
    val createAt: Date = Date(),
    val profileImgURL: String = ""
)