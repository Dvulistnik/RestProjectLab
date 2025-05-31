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
import org.dvulist.restproject.services.CollectionPointService

fun Route.collectionPointRoutes() {
    val service = CollectionPointService()

    route("/api/collection_points") {
        get {
            val collectionPoints = service.getAll()
            call.respond(collectionPoints)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val collectionPoint = service.get(id)
            if (collectionPoint == null) {
                call.respond(HttpStatusCode.NotFound, "Collection point not found")
            } else {
                call.respond(collectionPoint)
            }
        }

        post {
            val request = call.receive<CreateCollectionPointRequest>()
            val id = service.add(request.name, request.address, request.latitude, request.longitude)
            call.respond(HttpStatusCode.Created, id)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }
            val request = call.receiveNullable<UpdateCollectionPointRequest>()
            val updated = service.update(
                id = id,
                name = request?.name,
                address = request?.address,
                latitude = request?.latitude,
                longitude = request?.longitude
            )

            if (updated) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Collection point not found")
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
                call.respond(HttpStatusCode.NotFound, "Collection point not found")
            }
        }
    }
}

@Serializable
data class CreateCollectionPointRequest(
    val name: String,
    val address: String,
    val latitude: Double?,
    val longitude: Double?
)

@Serializable
data class UpdateCollectionPointRequest(
    val name: String?,
    @Optional val address: String? = null,
    @Optional val latitude: Double? = null,
    @Optional val longitude: Double? = null
)