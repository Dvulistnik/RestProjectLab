# Экологическая платформа для учета отходов

## Описание

Проект реализует REST API на Ktor и десктоп-клиент на Jetpack Compose Desktop. Клиент взаимодействует с сервером и поддерживает базовые CRUD-операции.

## Требования

- **JDK 21** (Можно скачать в IDE, либо https://jdk.java.net/21/ или https://www.oracle.com/java/technologies/downloads)
- **Android Studio**
- **Интернет** (для загрузки зависимостей Gradle)

## Запуск
1. Откройте два терминала.
2. В первом запустите сервер:
   ```bash
   ./gradlew :server:run
3. Во втором запустите клиент:
   ```bash
   ./gradlew :composeApp:run
