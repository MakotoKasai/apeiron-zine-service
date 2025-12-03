package art.kasai.apeiron.zine.application.usecase

import art.kasai.apeiron.zine.domain.model.Project
import art.kasai.apeiron.zine.domain.repository.ProjectRepository

class ListProjectUseCase(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(): List<Project> =
        repository.findAll()
}