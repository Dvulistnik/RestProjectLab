package org.dvulist.restproject.routes

import com.typesafe.config.Optional
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import org.dvulist.restproject.services.WasteTypeService

fun Route.wasteTypeRoutes() {
    val service = WasteTypeService()

    route("/api/waste_types") {
        get {
            val wasteTypes = service.getAll()
            call.respond(wasteTypes)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val wasteType = service.get(id)
            if (wasteType == null) {
                call.respond(HttpStatusCode.NotFound, "Waste type not found")
            } else {
                call.respond(wasteType)
            }
        }

        post {
            val request = call.receive<CreateWasteTypeRequest>()
            val id = service.add(request.name, request.description)
            call.respond(HttpStatusCode.Created, id)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val request = call.receiveNullable<UpdateWasteTypeRequest>()
            val update = service.update(
                id = id,
                name = request?.name,
                description = request?.description
            )

            if (update) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Waste type not found")
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@delete
            }

            val deleted = service.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Waste type not found")
            }
        }
    }
}

@Serializable
data class CreateWasteTypeRequest(
    val name: String,
    val description: String?
)

@Serializable
data class UpdateWasteTypeRequest(
    val name: String?,
    @Optional val description: String? = null
)