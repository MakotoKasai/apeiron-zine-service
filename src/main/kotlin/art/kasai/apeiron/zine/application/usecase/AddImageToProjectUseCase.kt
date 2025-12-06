package art.kasai.apeiron.zine.application.usecase

import art.kasai.apeiron.zine.domain.model.Image
import art.kasai.apeiron.zine.domain.repository.ImageRepository

class AddImageToProjectUseCase(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(
        projectId: Long,
        filePath: String,
        caption: String?,
        sortOrder: Int?,
    ): Image =
        repository.add(projectId, filePath, caption, sortOrder)
}