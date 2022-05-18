package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {

    @Synchronized
    fun startSocket(): Socket {
        val socket: Socket
        try {
            socket = IO.socket("http://192.168.100.207:8888") //this is your socket server
        } catch (e: URISyntaxException) {
            throw URISyntaxException(e.message, "Wrong URI")
        } catch (e: Exception) {
            throw Exception(e.message)
        }
        return socket
    }

    @Synchronized
    fun launchConnection(socket: Socket) {
        socket.connect()
    }

    @Synchronized
    fun endConnection(socket: Socket) {
        socket.disconnect()
    }

}