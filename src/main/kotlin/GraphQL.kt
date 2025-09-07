package com.example

import com.example.message.MessageSubscription
import com.example.message.notificationSharedFlow
import com.expediagroup.graphql.server.ktor.GraphQL

import com.expediagroup.graphql.server.ktor.graphQLSubscriptionsRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute

import com.expediagroup.graphql.server.operations.Query
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing

class DummyQuery: Query {
    fun ping(): String = "pong"
}

fun Application.configureGraphQL() {
    println("Configuring GraphQL...")
    install(GraphQL) {
        schema {
            packages = listOf("com.example")
            subscriptions = listOf(
                MessageSubscription(notificationSharedFlow)
            )
            queries = listOf(DummyQuery())
        }
    }

    routing {
        graphQLSubscriptionsRoute()
        graphiQLRoute()
    }
}