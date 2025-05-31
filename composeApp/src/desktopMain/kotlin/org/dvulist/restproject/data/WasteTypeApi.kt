package org.dvulist.restproject.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class WasteTypeApi(private val baseUrl: String = "http://localhost:8080") {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true; prettyPrint = true })
        }
    }

    suspend fun getAll(): List<WasteTypeDto> {
        return client.get("$baseUrl/api/waste_types").body()
    }

    suspend fun get(id: Int): WasteTypeDto? {
        val response = client.get("$baseUrl/api/waste_types/$id")
        return if (response.status == HttpStatusCode.OK) {
            response.body()
        } else null
    }

    suspend fun add(request: CreateWasteTypeRequest): Int {
        return client.post("$baseUrl/api/waste_types") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun update(id: Int, request: UpdateWasteTypeRequest): Boolean {
        val response = client.put("$baseUrl/api/waste_types/$id") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.status == HttpStatusCode.OK
    }

    suspend fun delete(id: Int): Boolean {
        val response = client.delete("$baseUrl/api/waste_types/$id")
        return response.status == HttpStatusCode.NoContent
    }
}

@Serializable
data class WasteTypeDto(
    val id: Int,
    val name: String,
    val description: String? = null
)

@Serializable
data class CreateWasteTypeRequest(
    val name: String,
    val description: String? = null
)

@Serializable
data class UpdateWasteTypeRequest(
    val name: String? = null,
    val description: String? = null
)
