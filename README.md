# Экологическая платформа для учета отходов

## Описание

Проект реализует REST API на Ktor и десктоп-клиент на Jetpack Compose Desktop. Клиент взаимодействует с сервером и поддерживает базовые CRUD-операции.

## Требования

- **JDK 21** (скачать: https://jdk.java.net/21/)
- **Java SDK 21 должен быть выбран в IDE**
- **Android Studio** (или IntelliJ IDEA с Kotlin Multiplatform plugin)
- **Интернет** (для загрузки зависимостей Gradle)

## Запуск
1. Откройте два терминала.
2. В первом запустите сервер:
   ```bash
   ./gradlew :server:run
3. Во втором запустите клиент:
   ```bash
   ./gradlew :composeApp:run