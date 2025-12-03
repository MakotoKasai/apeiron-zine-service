package art.kasai.apeiron.zine.application.usecase

import art.kasai.apeiron.zine.domain.model.Project
import art.kasai.apeiron.zine.domain.repository.ProjectRepository

class GetProjectUseCase(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(id: Long): Project? =
        repository.findById(id)
}