package com.example

import com.example.message.listenToMessageNotifications
import com.example.message.routing.configureMessageRoutes
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureWebsockets()
    configureHttp()
    configureDatabase()
    listenToMessageNotifications()
    configureMessageRoutes()
    configureGraphQL()
}
