package art.kasai.apeiron.zine.interfaces.api

import art.kasai.apeiron.zine.application.usecase.AddImageToProjectUseCase
import art.kasai.apeiron.zine.application.usecase.DeleteImageUseCase
import art.kasai.apeiron.zine.application.usecase.ListImageByProjectUseCase
import art.kasai.apeiron.zine.interfaces.api.dto.ImageRequest
import art.kasai.apeiron.zine.interfaces.api.dto.ImageResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.imageRoutes(
    addImageToProject: AddImageToProjectUseCase,
    listImageByProject: ListImageByProjectUseCase,
    deleteImage: DeleteImageUseCase
) {
    route("/projects/{projectId}/images"){

        get{
            val projectId = call.parameters["projectId"]?.toLongOrNull()
            if(projectId == null) {
                call.respond(HttpStatusCode.BadRequest, "invalid projectId")
                return@get
            }

            val images = listImageByProject(projectId)
            call.respond(images.map { ImageResponse.from(it) })
        }

        post{
            val projectId = call.parameters["projectId"]?.toLongOrNull()
            if(projectId == null) {
                call.respond(HttpStatusCode.BadRequest, "invalid projectId")
                return@post
            }

            val request = call.receive<ImageRequest>()
            if(request.filePath.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "filePath must not be blank")
            }

            val image = addImageToProject(
                projectId = projectId,
                filePath = request.filePath,
                caption = request.caption,
                sortOrder = request.sortOrder,
            )

            call.respond(HttpStatusCode.Created, ImageResponse.from(image))
        }

        delete("{imageId}"){
            val imageId = call.parameters["imageId"]?.toLongOrNull()
            if(imageId == null) {
                call.respond(HttpStatusCode.BadRequest, "invalid projectId")
                return@delete
            }

            val deleted = deleteImage(imageId)
            if(deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "image no found")
            }
        }
    }
}