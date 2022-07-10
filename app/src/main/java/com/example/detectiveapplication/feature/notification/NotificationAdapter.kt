package com.example.detectiveapplication.feature.notification

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.NotificationItemBinding
import com.example.detectiveapplication.dto.notification.NotificationFeed
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil
    .ItemCallback<NotificationFeed>() {

        override fun areItemsTheSame(
            oldItem: NotificationFeed,
            newItem: NotificationFeed
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NotificationFeed,
            newItem: NotificationFeed
        ): Boolean {
            return false
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    class ViewHolder
    constructor(
        private val binding: NotificationItemBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationFeed, position: Int) = with(binding.root) {

            binding.tvTitle.text = item.tittle
            binding.tvDescription.text = item.body
            binding.tvDate.text = date(item.createdAt)
            if (item.image == null) {
                binding.ivPhoto.load(R.drawable.ic_logo)
            } else {
                binding.ivPhoto.load(item.image.toString()) {
                    crossfade(1000)
                    error(R.drawable.ic_launcher_background)
                }
            }

            binding.notificationLayout.setOnClickListener {
                interaction?.onItemSelected(position, item, 5)
            }

            Log.d("checkSomthing", "id:${item.id}  bind: ${item.readable}")
            if (item.readable == 0) {
                binding.materialCardView4.strokeWidth = 4
                binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.purple_200))
                binding.tvDescription.setTextColor(ContextCompat.getColor(context, R.color.black))
                binding.tvDate.setTextColor(ContextCompat.getColor(context, R.color.black))
            } else if(item.readable == 1) {
                binding.materialCardView4.strokeWidth = 0
                binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.dark_grey))
                binding.tvDescription.setTextColor(ContextCompat.getColor(context, R.color.dark_grey))
                binding.tvDate.setTextColor(ContextCompat.getColor(context, R.color.dark_grey))
            }
        }

        fun convertStringToDate(string: String): String {
            val date = string.split("T")[0]
            val time = string.split("T")[1]
            val dateTime = "$date $time"
            return dateTime
        }

        @SuppressLint("SimpleDateFormat")
        fun date(string: String): String {
            val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").parse(string)
            return convertMilliToDayMonthYear(date.time)
        }

        fun convertMilliToDayMonthYear(milli: Long): String {
            val date = Date(milli)
            val format = SimpleDateFormat("dd MMM yyyy")
            return format.format(date)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NotificationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position], position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<NotificationFeed>) {
        differ.submitList(list)
    }


    interface Interaction {
        fun onItemSelected(position: Int, item: NotificationFeed, state: Int)
    }

}