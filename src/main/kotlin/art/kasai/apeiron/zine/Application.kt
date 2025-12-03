package art.kasai.apeiron.zine

import art.kasai.apeiron.zine.application.usecase.CreateProjectUseCase
import art.kasai.apeiron.zine.application.usecase.DeleteProjectUseCase
import art.kasai.apeiron.zine.application.usecase.GetProjectUseCase
import art.kasai.apeiron.zine.application.usecase.ListProjectUseCase
import art.kasai.apeiron.zine.application.usecase.UpdateProjectUseCase
import art.kasai.apeiron.zine.infrastructure.db.DatabaseFactory
import art.kasai.apeiron.zine.infrastructure.repository.ExposedProjectRepository
import art.kasai.apeiron.zine.interfaces.api.projectRoutes
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.http.*

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0"
    ) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()

    install(ContentNegotiation) {
        jackson()
    }

    val projectRepository = ExposedProjectRepository()
    val createProject = CreateProjectUseCase(projectRepository)
    val getProject = GetProjectUseCase(projectRepository)
    val listProject = ListProjectUseCase(projectRepository)
    val updateProject = UpdateProjectUseCase(projectRepository)
    val deleteProject = DeleteProjectUseCase(projectRepository)

    routing {
        get("/health") {
            call.respondText("OK", ContentType.Text.Plain)
        }

        projectRoutes(
            createProject = createProject,
            getProject = getProject,
            listProject = listProject,
            updateProject = updateProject,
            deleteProject = deleteProject
        )
    }
}