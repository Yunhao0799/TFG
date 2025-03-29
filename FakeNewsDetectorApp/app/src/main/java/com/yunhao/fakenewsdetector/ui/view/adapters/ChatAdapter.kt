package com.yunhao.fakenewsdetector.ui.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.ui.view.adapters.data.ChatMessage
import com.yunhao.fakenewsdetector.utils.DateTimeHelper

class ChatAdapter : ListAdapter<ChatMessage, RecyclerView.ViewHolder>(DiffCallback()) {
    companion object {
        private const val TYPE_SENT = 1
        private const val TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isSentByUser) TYPE_SENT else TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = if (viewType == TYPE_SENT) {
            inflater.inflate(R.layout.item_message_sent, parent, false)
        } else {
            inflater.inflate(R.layout.item_message_received, parent, false)
        }
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        (holder as MessageViewHolder).bind(message)
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: ChatMessage) {
            itemView.findViewById<TextView>(R.id.text_message).text = message.message
            itemView.findViewById<TextView>(R.id.text_timestamp).text =
                DateTimeHelper.getFormattedDateTime(message.timestamp, pattern = "HH:mm")
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            // Ideally, compare unique IDs if you have one (message ID, timestamp, hash, etc.)
            return oldItem.timestamp == newItem.timestamp &&
                    oldItem.message == newItem.message
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem == newItem
        }
    }
}