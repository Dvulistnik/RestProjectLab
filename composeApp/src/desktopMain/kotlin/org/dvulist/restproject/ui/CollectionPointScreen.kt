package org.dvulist.restproject.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.dvulist.restproject.data.CollectionPointApi
import org.dvulist.restproject.data.CollectionPointDto
import org.dvulist.restproject.data.CreateCollectionPointRequest
import org.dvulist.restproject.data.UpdateCollectionPointRequest

@Composable
fun CollectionPointScreen(modifier: Modifier = Modifier) {
    val api = remember { CollectionPointApi() }

    val scope = rememberCoroutineScope()

    var collectionPoints by remember { mutableStateOf<List<CollectionPointDto>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedPointId by remember { mutableStateOf<Int?>(null) } // Для отслеживания редактируемого ID
    var newName by remember { mutableStateOf("") }
    var newAddress by remember { mutableStateOf("") }
    var newLatitude by remember { mutableStateOf("") }
    var newLongitude by remember { mutableStateOf("") } // <-- ИСПРАВЛЕНО ЗДЕСЬ!

    // Загрузка данных при первом запуске
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                collectionPoints = api.getAll()
            } catch (e: Exception) {
                // Обработка ошибок, например, отображение Snackbar
                println("Ошибка загрузки данных: ${e.message}")
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Пункты приема отходов") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showDialog = true
                selectedPointId = null // Указываем, что это добавление
                newName = ""
                newAddress = ""
                newLatitude = ""
                newLongitude = ""
            }) {
                Text("+")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(collectionPoints) { point ->
                Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = point.name, style = MaterialTheme.typography.h6)
                            Text(text = point.address, style = MaterialTheme.typography.body2)
                        }
                        Row {
                            Button(onClick = {
                                selectedPointId = point.id
                                newName = point.name
                                newAddress = point.address
                                newLatitude = point.latitude?.toString() ?: ""
                                newLongitude = point.longitude?.toString() ?: ""
                                showDialog = true
                            }, modifier = Modifier.padding(end = 8.dp)) {
                                Text("Изменить")
                            }
                            Button(onClick = {
                                scope.launch {
                                    try {
                                        if (api.delete(point.id)) {
                                            collectionPoints = collectionPoints.filter { it.id != point.id }
                                        } else {
                                            // Обработка ошибки удаления
                                            println("Не удалось удалить пункт: ${point.name}")
                                        }
                                    } catch (e: Exception) {
                                        println("Ошибка при удалении: ${e.message}")
                                    }
                                }
                            }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)) {
                                Text("Удалить")
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false; selectedPointId = null },
                title = { Text(if (selectedPointId == null) "Добавить пункт" else "Изменить пункт") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Название") }
                        )
                        OutlinedTextField(
                            value = newAddress,
                            onValueChange = { newAddress = it },
                            label = { Text("Адрес") }
                        )
                        OutlinedTextField(
                            value = newLatitude,
                            onValueChange = { newLatitude = it },
                            label = { Text("Широта (необязательно)") }
                        )
                        OutlinedTextField(
                            value = newLongitude,
                            onValueChange = { newLongitude = it },
                            label = { Text("Долгота (необязательно)") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        scope.launch {
                            val latitude = newLatitude.toDoubleOrNull()
                            val longitude = newLongitude.toDoubleOrNull()
                            if (selectedPointId == null) {
                                // Добавление нового пункта
                                try {
                                    api.add(CreateCollectionPointRequest(newName, newAddress, latitude, longitude))
                                    collectionPoints = api.getAll() // Перезагружаем данные после добавления
                                } catch (e: Exception) {
                                    println("Ошибка при добавлении: ${e.message}")
                                }
                            } else {
                                // Обновление существующего пункта
                                try {
                                    if (api.update(selectedPointId!!, UpdateCollectionPointRequest(newName, newAddress, latitude, longitude))) {
                                        collectionPoints = collectionPoints.map {
                                            if (it.id == selectedPointId) {
                                                it.copy(name = newName, address = newAddress, latitude = latitude, longitude = longitude)
                                            } else {
                                                it
                                            }
                                        }
                                    } else {
                                        println("Не удалось обновить пункт: ${selectedPointId}")
                                    }
                                } catch (e: Exception) {
                                    println("Ошибка при обновлении: ${e.message}")
                                }
                            }
                            showDialog = false
                            selectedPointId = null
                        }
                    }) {
                        Text("Сохранить")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false; selectedPointId = null }) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}