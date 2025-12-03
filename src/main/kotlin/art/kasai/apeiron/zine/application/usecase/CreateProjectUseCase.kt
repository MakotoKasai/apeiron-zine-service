package art.kasai.apeiron.zine.application.usecase

import art.kasai.apeiron.zine.domain.model.Project
import art.kasai.apeiron.zine.domain.repository.ProjectRepository

class CreateProjectUseCase(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(title: String, description: String): Project =
        repository.create(title, description)
}