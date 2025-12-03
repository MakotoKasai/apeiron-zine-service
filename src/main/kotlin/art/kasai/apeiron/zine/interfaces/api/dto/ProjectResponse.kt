package art.kasai.apeiron.zine.interfaces.api.dto

import art.kasai.apeiron.zine.domain.model.Project

data class ProjectResponse(
    val id: Long,
    val title: String,
    val description: String,
    val createdAt: Long,
){
    companion object {
        fun from(project: Project): ProjectResponse =
            ProjectResponse(
                id = project.id,
                title = project.title,
                description = project.description,
                createdAt = project.createdAt
            )
    }
}
