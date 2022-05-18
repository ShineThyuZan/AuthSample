package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PersonalChatRoomViewModel @Inject constructor(private val socket: Socket) : ViewModel() {
    val gson: Gson = Gson()
    var receiveJsonMessage = ""

    init {
        receiveConservation()
    }

    private var _conservationData = MutableSharedFlow<String>()
    val conservationData: SharedFlow<String> get() = _conservationData
   private fun receiveConservation() {
        socket.on("message") {
            Timber.tag("arrrr").d("arrary$it")
            viewModelScope.launch {
                if (it[0] != null) {
                    val receiveJsonData = JSONObject(it[0].toString())
                    Timber.tag("messsagesocket").d("$receiveJsonData")
                    receiveJsonMessage = receiveJsonData.getString("message")
                    Timber.tag("messsagesocket").d("$receiveJsonMessage")
                }
                _conservationData.emit(receiveJsonMessage)
            }

            Timber.tag("messsage").d("$_conservationData")
        }
    }

   private fun leaveRoomFromServer(data: String) {
        viewModelScope.launch {
            socket.emit(
                "leaveRoom", data
            )
        }
    }


    fun joinRoom() {
        val data = JoinRoom(userId = "1001", conversationId = "1")
        val jsonData = gson.toJson(data)
        socket.emit("joinRoom", jsonData)
    }

    fun sendMessage(message: String) {
        val data = ChatMessage(message)
        val jsonData = gson.toJson(data)
        socket.emit("chatMessage", jsonData)
    }

    fun leaveRoom() {
        val data = LeaveRoom(userId = "1001", conversationId = "1")
        val jsonData = gson.toJson(data)
        leaveRoomFromServer(jsonData)
    }
}