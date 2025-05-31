package org.dvulist.restproject

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.dvulist.restproject.ui.CollectionPointScreen
import org.dvulist.restproject.ui.WasteTypeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

sealed class Screen {
    object CollectionPoints : Screen()
    object WasteTypes : Screen()
}

@Composable
@Preview
fun App() {
    var currentScreen: Screen by remember { mutableStateOf(Screen.CollectionPoints) }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Приложение по сбору отходов") })
            },
            bottomBar = {
                BottomNavigation {
                    BottomNavigationItem(
                        selected = currentScreen is Screen.CollectionPoints,
                        onClick = { currentScreen = Screen.CollectionPoints },
                        label = { Text("Пункты сбора") },
                        icon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
                    )
                    BottomNavigationItem(
                        selected = currentScreen is Screen.WasteTypes,
                        onClick = { currentScreen = Screen.WasteTypes },
                        label = { Text("Типы отходов") },
                        icon = { Icon(Icons.Default.Refresh, contentDescription = null) }
                    )
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                when (currentScreen) {
                    is Screen.CollectionPoints -> CollectionPointScreen()
                    is Screen.WasteTypes -> WasteTypeScreen()
                }
            }
        }
    }
}