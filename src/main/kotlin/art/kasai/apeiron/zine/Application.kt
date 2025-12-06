package art.kasai.apeiron.zine

import art.kasai.apeiron.zine.application.usecase.AddImageToProjectUseCase
import art.kasai.apeiron.zine.application.usecase.CreateProjectUseCase
import art.kasai.apeiron.zine.application.usecase.DeleteImageUseCase
import art.kasai.apeiron.zine.application.usecase.DeleteProjectUseCase
import art.kasai.apeiron.zine.application.usecase.GetProjectUseCase
import art.kasai.apeiron.zine.application.usecase.ListImageByProjectUseCase
import art.kasai.apeiron.zine.application.usecase.ListProjectUseCase
import art.kasai.apeiron.zine.application.usecase.UpdateProjectUseCase
import art.kasai.apeiron.zine.infrastructure.db.DatabaseFactory
import art.kasai.apeiron.zine.infrastructure.repository.ExposedImageRepository
import art.kasai.apeiron.zine.infrastructure.repository.ExposedProjectRepository
import art.kasai.apeiron.zine.interfaces.api.imageRoutes
import art.kasai.apeiron.zine.interfaces.api.projectRoutes
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.serialization.jackson.jackson
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.routing.get
import io.ktor.server.response.respondText
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.*

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
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        // allowMethod(HttpMethod.Patch)
    }

    val projectRepository = ExposedProjectRepository()
    val createProject = CreateProjectUseCase(projectRepository)
    val getProject = GetProjectUseCase(projectRepository)
    val listProject = ListProjectUseCase(projectRepository)
    val updateProject = UpdateProjectUseCase(projectRepository)
    val deleteProject = DeleteProjectUseCase(projectRepository)

    val imageRepository = ExposedImageRepository()
    val addImageToProject = AddImageToProjectUseCase(imageRepository)
    val listImageByProject = ListImageByProjectUseCase(imageRepository)
    val deleteImage = DeleteImageUseCase(imageRepository)

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

        imageRoutes(
            addImageToProject = addImageToProject,
            listImageByProject = listImageByProject,
            deleteImage = deleteImage,
        )
    }
}