package art.kasai.apeiron.zine.domain.model

data class Image(
    val id: Long,
    val projectId: Long,
    val filePath: String,
    val caption: String,
    val sortOrder: Int,
    val createdAt: Long,
)
