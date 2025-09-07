package com.example.message

import com.example.message.models.Message
import com.expediagroup.graphql.server.operations.Subscription
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map


class MessageSubscription(private val sharedFlow: SharedFlow<Pair<String, List<Message>>>) : Subscription {
    private val allowedEmails = setOf("test@example.com", "test1@example.com")

    fun messages(destinationEmail: String): Flow<List<Message>> {
        require(destinationEmail in allowedEmails) { "Subscription to $destinationEmail is not allowed." }

        return sharedFlow
            .filter { (email, _) -> destinationEmail == email } // Only send messages for this email
            .map { it.second } // Send only the list of messages
    }
}
