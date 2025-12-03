package art.kasai.apeiron.zine.domain.model

data class Project(
    val id: Long,
    val title: String,
    val description: String,
    val createdAt: Long,
)
