package com.example.detectiveapplication.feature.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.NotificationItemBinding
import com.example.detectiveapplication.dto.notification.NotificationFeed

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
            binding.apply {
                if (item.readable == 1){
                    tvTitle.setTextColor(ContextCompat.getColor(context, R.color.dark_grey))
                    tvDescription.setTextColor(ContextCompat.getColor(context, R.color.dark_grey))
                }
                tvTitle.text = item.tittle
                tvDescription.text = item.body
                if (item.image == null){
                    ivPhoto.load(R.drawable.ic_logo)
                }else{
                    ivPhoto.load(item.image.toString()){
                        error(R.drawable.ic_launcher_background)
                    }
                }
            }
            
            binding.notificationLayout.setOnClickListener { 
                interaction?.onItemSelected(position, item,5)
            }
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
        holder.bind(differ.currentList.get(position),position)

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