package org.dvulist.restproject

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.dvulist.restproject.database.DatabaseFactory
import org.dvulist.restproject.routes.collectionPointRoutes
import kotlinx.serialization.Serializable

fun Application.module() {
    DatabaseFactory.init()

    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        collectionPointRoutes()
    }
}