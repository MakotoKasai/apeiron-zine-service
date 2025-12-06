package art.kasai.apeiron.zine.application.usecase

import art.kasai.apeiron.zine.domain.repository.ImageRepository

class DeleteImageUseCase(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(imageId: Long): Boolean =
        repository.delete(imageId)
}