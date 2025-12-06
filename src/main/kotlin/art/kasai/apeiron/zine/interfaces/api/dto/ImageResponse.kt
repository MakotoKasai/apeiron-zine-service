package art.kasai.apeiron.zine.interfaces.api.dto

import art.kasai.apeiron.zine.domain.model.Image

data class ImageResponse(
    val id: Long,
    val projectId: Long,
    val filePath: String,
    val caption: String,
    val sortOrder: Int,
    val createdAt: Long,
) {
    companion object {
        fun from(image: Image): ImageResponse =
            ImageResponse(
                id = image.id,
                projectId = image.projectId,
                filePath = image.filePath,
                caption = image.caption,
                sortOrder = image.sortOrder,
                createdAt = image.createdAt,
            )
    }
}
