package com.galaxytechno.chat.presentation.ui.top_lvl_dest.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Constant
import com.galaxytechno.chat.model.dto.MessageObj

class PersonalChatRoomAdapter(
    val context: Context,
    val chatList: ArrayList<MessageObj>
) : RecyclerView.Adapter<PersonalChatRoomAdapter.ViewHolder>() {

    val CHAT_MINE = Constant.CHAT_MINE
    val CHAT_PARTNER = Constant.CHAT_PARTNER
    val USER_JOIN = 2
    val USER_LEAVE = 3
    val isTop = true

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        var view: View? = null

        when (viewType) {
            CHAT_MINE -> {
                    view = LayoutInflater.from(context).inflate(
                        R.layout.item_container_send_message,
                        parent,
                        false
                    )
            }
            CHAT_PARTNER -> {
                view = LayoutInflater.from(context).inflate(
                    R.layout.item_container_receive_message,
                    parent,
                    false
                )
            }

        }
       return ViewHolder(view!!)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        return chatList[position].viewType
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val messageData  = chatList[position]
        val userName = messageData.headUrl;
        val content = messageData.messageContent;
        val viewType = messageData.viewType;

        when(viewType) {

            CHAT_MINE -> {
                //Check last message send interval
                if(position == itemCount -1 || chatList[position+1].viewType == CHAT_PARTNER ) {
                    holder.sendMessage.text = content
                    holder.sendMessage.setBackgroundResource(R.drawable.background_chat_send_bubble_last)
                    holder.timeTextSend.visibility = View.VISIBLE
                } else {
                    holder.sendMessage.text = content
                    holder.sendMessage.setBackgroundResource(R.drawable.background_chat_send_bubble)
                    holder.timeTextSend.visibility = View.GONE
                }
            }
            CHAT_PARTNER ->{
                //Check last message receive interval
                if(position == itemCount -1 || chatList[position+1].viewType == CHAT_MINE) {
                    holder.receiveMessage.text = content
                    holder.receiveMessage.setBackgroundResource(R.drawable.background_chat_receive_bubble_last)
                    holder.timeTextReceive.visibility = View.VISIBLE
                } else {
                    holder.receiveMessage.text = content
                    holder.receiveMessage.setBackgroundResource(R.drawable.background_chat_receive_bubble)
                    holder.timeTextReceive.visibility = View.GONE
                }
            }
//            USER_JOIN -> {
//                val text = "${userName} has entered the room"
//                holder.text.setText(text)
//            }
//            USER_LEAVE -> {
//                val text = "${userName} has leaved the room"
//                holder.text.setText(text)
//            }
        }
    }

    inner class ViewHolder(itemView : View):  RecyclerView.ViewHolder(itemView) {

        val sendMessage = itemView.findViewById<TextView>(R.id.tv_send_message)
        val sendMessageLast = itemView.findViewById<TextView>(R.id.tv_send_message_last)
        val receiveMessage = itemView.findViewById<TextView>(R.id.tv_receive_message)
        val receiveMessageLast = itemView.findViewById<TextView>(R.id.tv_receive_message_last)
        val text = itemView.findViewById<TextView>(R.id.text)
        val timeTextSend = itemView.findViewById<TextView>(R.id.tv_message_time_send)
        val timeTextReceive = itemView.findViewById<TextView>(R.id.tv_msg_time_receive)
    }


}

