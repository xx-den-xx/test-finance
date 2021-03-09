package com.bda.finance_test.model.repository

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class WebSocketRepository(val uri: URI) {

    fun interface ReadMessageListener {
        fun message(message: String)
    }

    private lateinit var webSocketClient: WebSocketClient
    var message: String = ""
        set(value) {
            field = "{\n" +
                    " \"event\": \"subscribe\",\n" +
                    "\"channel\": \"ticker\", \n" +
                    "\"pair\": \"${value.toUpperCase()}\"\n" +
                    "}"
        }


    fun startWebSocket(listener: ReadMessageListener) {
        webSocketClient = object : WebSocketClient(uri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                webSocketClient.send(message)
            }

            override fun onMessage(message: String?) {
                listener.message(message = message!!)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
            }

            override fun onError(ex: Exception?) {
            }
        }
        webSocketClient.connect()
    }

    fun closeWebSocket() {
        webSocketClient.close()
    }
}