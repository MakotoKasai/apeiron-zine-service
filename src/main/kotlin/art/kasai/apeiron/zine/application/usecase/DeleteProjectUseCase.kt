package art.kasai.apeiron.zine.application.usecase

import art.kasai.apeiron.zine.domain.repository.ProjectRepository

class DeleteProjectUseCase(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(id: Long): Boolean =
        repository.delete(id)
}