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

class CollectionPointApi(private val baseUrl: String = "http://localhost:8080") {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true; prettyPrint = true })
        }
    }

    suspend fun getAll(): List<CollectionPointDto> {
        return client.get("$baseUrl/api/collection_points").body()
    }

    suspend fun add(point: CreateCollectionPointRequest): Int {
        return client.post("$baseUrl/api/collection_points") {
            contentType(ContentType.Application.Json)
            setBody(point)
        }.body()
    }

    suspend fun update(id: Int, point: UpdateCollectionPointRequest): Boolean {
        val response: HttpResponse = client.put("$baseUrl/api/collection_points/$id") {
            contentType(ContentType.Application.Json)
            setBody(point)
        }
        return response.status == HttpStatusCode.OK
    }

    suspend fun delete(id: Int): Boolean {
        val response: HttpResponse = client.delete("$baseUrl/api/collection_points/$id")
        return response.status == HttpStatusCode.NoContent
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
    val address: String?,
    val latitude: Double?,
    val longitude: Double?
)

@Serializable
data class CreateCollectionPointDto(
    val name: String,
    val address: String,
    val latitude: Double?,
    val longitude: Double?
)

@Serializable
data class CollectionPointDto(
    val id: Int,
    val name: String,
    val address: String,
    val latitude: Double?,
    val longitude: Double?
)