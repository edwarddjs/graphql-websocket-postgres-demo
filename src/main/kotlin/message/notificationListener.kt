package com.example.message

import com.example.listenerConnection
import com.example.message.models.Message
import com.example.message.repository.getAllMessages
import io.ktor.server.application.Application
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.postgresql.PGConnection
import java.util.logging.Logger

val notificationSharedFlow = MutableSharedFlow<Pair<String, List<Message>>>(
    extraBufferCapacity = 100,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)

fun listenForNotificationsUnsafe(pgConn: PGConnection): Flow<Pair<String, List<Message>>> = flow {
    val sqlConn = if (pgConn is java.sql.Connection) pgConn else error("Not a SQL Connection")
    sqlConn.createStatement().use { stmt ->
        stmt.execute("LISTEN tickets_channel;")
    }

    while (true) {
        delay(500)
        val notifications = pgConn.notifications
        notifications
            ?.filter { it.name == "tickets_channel" }
            ?.forEach { notification ->
                val json = parseToJsonElement(notification.parameter).jsonObject
                val destinationEmail = json["destinationEmail"]?.jsonPrimitive?.contentOrNull ?: return@forEach

                val updatedMessages = getAllMessages(destinationEmail)
                emit(destinationEmail to updatedMessages)
            }
    }
}

fun listenForNotifications(pgConn: PGConnection, maxRetries: Long = 5): Flow<Pair<String, List<Message>>> {
    return listenForNotificationsUnsafe(pgConn)
        .retry(retries = maxRetries) { e ->
            Logger.getLogger("MessageListener")
                .severe("Flow failed, retrying: ${e.message}")
            true // retry on any exception
        }
        .catch { e ->
            Logger.getLogger("MessageListener").severe("Flow failed permanently: ${e.message}")
            throw e // propagate to let Kubernetes restart pod
        }
}

fun Application.listenToMessageNotifications() {
    launch {
        val pgConn = listenerConnection.unwrap(PGConnection::class.java)
        listenForNotifications(pgConn).collect { (email, messages) ->
            notificationSharedFlow.emit(email to messages)
        }
    }
}