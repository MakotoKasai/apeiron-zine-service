package art.kasai.apeiron.zine.domain.repository

import art.kasai.apeiron.zine.domain.model.Image

interface ImageRepository {
    suspend fun add(
        projectId: Long,
        filePath: String,
        caption: String?,
        sortOrder: Int?,
    ): Image

    suspend fun findByProject(projectId: Long): List<Image>
    suspend fun delete(imageId: Long): Boolean
}