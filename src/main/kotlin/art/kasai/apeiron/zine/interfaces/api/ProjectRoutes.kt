package art.kasai.apeiron.zine.interfaces.api

import art.kasai.apeiron.zine.application.usecase.CreateProjectUseCase
import art.kasai.apeiron.zine.application.usecase.DeleteProjectUseCase
import art.kasai.apeiron.zine.application.usecase.GetProjectUseCase
import art.kasai.apeiron.zine.application.usecase.ListProjectUseCase
import art.kasai.apeiron.zine.application.usecase.UpdateProjectUseCase
import art.kasai.apeiron.zine.interfaces.api.dto.ProjectRequest
import art.kasai.apeiron.zine.interfaces.api.dto.ProjectResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.put
import io.ktor.server.routing.delete


fun Route.projectRoutes(
    createProject: CreateProjectUseCase,
    getProject: GetProjectUseCase,
    listProject: ListProjectUseCase,
    updateProject: UpdateProjectUseCase,
    deleteProject: DeleteProjectUseCase,
) {
    route("/projects") {

        post {
            val request= call.receive<ProjectRequest>()
            if(request.title.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "title must not be blank.")
                return@post
            }

            val project = createProject(request.title, request.description)
            call.respond(HttpStatusCode.Created, ProjectResponse.from(project))
        }

        get {
            val projects = listProject()
            call.respond(projects.map { ProjectResponse.from(it) })
        }

        get("{id}") {
            val idParam = call.parameters["id"]
            val id = idParam?.toLongOrNull()
            if(id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@get
            }

            val project = getProject(id)
            if(project == null) {
                call.respond(HttpStatusCode.NotFound, "project not found")
            } else {
                call.respond(ProjectResponse.from(project))
            }
        }

        put("{id}") {
            val idParam = call.parameters["id"]
            val id = idParam?.toLongOrNull()
            if(id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@put
            }

            val request = call.receive<ProjectRequest>()
            val updated = updateProject(id, request.title, request.description)
            if(updated == null) {
                call.respond(HttpStatusCode.NotFound, "project not found")
            } else {
                call.respond(ProjectResponse.from(updated))
            }
        }

        delete("{id}") {
            val idParam = call.parameters["id"]
            val id = idParam?.toLongOrNull()
            if(id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
                return@delete
            }

            val deleted = deleteProject(id)
            if(deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "project not found")
            }
        }
    }
}