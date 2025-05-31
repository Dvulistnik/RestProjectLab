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
import org.dvulist.restproject.data.WasteTypeApi
import org.dvulist.restproject.data.WasteTypeDto
import org.dvulist.restproject.data.CreateWasteTypeRequest
import org.dvulist.restproject.data.UpdateWasteTypeRequest

@Composable
fun WasteTypeScreen(modifier: Modifier = Modifier) {
    val api = remember { WasteTypeApi() }

    val scope = rememberCoroutineScope()

    var wasteTypes by remember { mutableStateOf<List<WasteTypeDto>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedTypeId by remember { mutableStateOf<Int?>(null) } // Для отслеживания редактируемого ID
    var newName by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }

    // Загрузка данных при первом запуске
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                wasteTypes = api.getAll()
            } catch (e: Exception) {
                // Обработка ошибок, например, отображение Snackbar
                println("Ошибка загрузки данных типов отходов: ${e.message}")
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Типы отходов") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showDialog = true
                selectedTypeId = null // Указываем, что это добавление
                newName = ""
                newDescription = ""
            }) {
                Text("+")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(wasteTypes) { wasteType ->
                Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = wasteType.name, style = MaterialTheme.typography.h6)
                            wasteType.description?.let {
                                Text(text = it, style = MaterialTheme.typography.body2)
                            }
                        }
                        Row {
                            Button(onClick = {
                                selectedTypeId = wasteType.id
                                newName = wasteType.name
                                newDescription = wasteType.description ?: ""
                                showDialog = true
                            }, modifier = Modifier.padding(end = 8.dp)) {
                                Text("Изменить")
                            }
                            Button(onClick = {
                                scope.launch {
                                    try {
                                        if (api.delete(wasteType.id)) {
                                            wasteTypes = wasteTypes.filter { it.id != wasteType.id }
                                        } else {
                                            // Обработка ошибки удаления
                                            println("Не удалось удалить тип отходов: ${wasteType.name}")
                                        }
                                    } catch (e: Exception) {
                                        println("Ошибка при удалении типа отходов: ${e.message}")
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
                onDismissRequest = { showDialog = false; selectedTypeId = null },
                title = { Text(if (selectedTypeId == null) "Добавить тип отходов" else "Изменить тип отходов") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Название") }
                        )
                        OutlinedTextField(
                            value = newDescription,
                            onValueChange = { newDescription = it },
                            label = { Text("Описание (необязательно)") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        scope.launch {
                            if (selectedTypeId == null) {
                                // Добавление нового типа отходов
                                try {
                                    val createdId = api.add(CreateWasteTypeRequest(newName, newDescription.ifEmpty { null }))
                                    // Можно обновить список, запросив все заново или добавив созданный объект
                                    wasteTypes = api.getAll() // Перезагружаем данные после добавления
                                } catch (e: Exception) {
                                    println("Ошибка при добавлении типа отходов: ${e.message}")
                                }
                            } else {
                                // Обновление существующего типа отходов
                                try {
                                    if (api.update(selectedTypeId!!, UpdateWasteTypeRequest(newName, newDescription.ifEmpty { null }))) {
                                        wasteTypes = wasteTypes.map {
                                            if (it.id == selectedTypeId) {
                                                it.copy(name = newName, description = newDescription.ifEmpty { null })
                                            } else {
                                                it
                                            }
                                        }
                                    } else {
                                        println("Не удалось обновить тип отходов: ${selectedTypeId}")
                                    }
                                } catch (e: Exception) {
                                    println("Ошибка при обновлении типа отходов: ${e.message}")
                                }
                            }
                            showDialog = false
                            selectedTypeId = null
                        }
                    }) {
                        Text("Сохранить")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false; selectedTypeId = null }) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}