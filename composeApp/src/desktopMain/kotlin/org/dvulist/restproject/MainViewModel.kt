package org.dvulist.restproject

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel {
    private val client = HttpClient(CIO)

    private val _greetingState = MutableStateFlow<String?>(null)
    val greetingState: StateFlow<String?> = _greetingState

    fun loadGreeting() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val text = client.get("http://localhost:8080/").bodyAsText()
                _greetingState.value = text
            } catch (e: Exception) {
                _greetingState.value = "Ошибка: ${e.localizedMessage}"
            }
        }
    }
}
