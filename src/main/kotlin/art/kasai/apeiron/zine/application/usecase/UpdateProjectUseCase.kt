package art.kasai.apeiron.zine.application.usecase

import art.kasai.apeiron.zine.domain.model.Project
import art.kasai.apeiron.zine.domain.repository.ProjectRepository

class UpdateProjectUseCase(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(id: Long, title: String, description: String): Project? =
        repository.update(id, title, description)
}