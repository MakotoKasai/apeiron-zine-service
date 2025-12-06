package art.kasai.apeiron.zine.application.usecase

import art.kasai.apeiron.zine.domain.model.Image
import art.kasai.apeiron.zine.domain.repository.ImageRepository

class ListImageByProjectUseCase(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(projectId: Long): List<Image> =
        repository.findByProject(projectId)
}