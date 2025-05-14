package org.dvulist.restproject

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform