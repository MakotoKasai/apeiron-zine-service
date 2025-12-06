package art.kasai.apeiron.zine.interfaces.api.dto

data class ImageRequest(
    val filePath: String,
    val caption: String? = null,
    val sortOrder: Int? = null
)
