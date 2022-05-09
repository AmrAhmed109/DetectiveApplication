package com.example.detectiveapplication.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.MissingChildListItemBinding
import com.example.detectiveapplication.dto.followedCases.FollowedCasesItem
import com.example.detectiveapplication.utils.Constants

class FollowingAdapter(private val interaction:Interaction? = null) :
    RecyclerView.Adapter<FollowingAdapter.ViewHolder>()  {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowedCasesItem>() {
        override fun areItemsTheSame(
            oldItem: FollowedCasesItem,
            newItem: FollowedCasesItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FollowedCasesItem,
            newItem: FollowedCasesItem
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    class ViewHolder
    constructor(
        private val binding: MissingChildListItemBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FollowedCasesItem, position: Int) = with(binding.root) {
            if (item.status == "not_found"){
                binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,
                    R.color.light_red
                ))
                binding.tvStatueMissingChild.setTextColor(ContextCompat.getColor(context,
                    R.color.red
                ))
                binding.tvStatueMissingChild.setText("مفقود")
            }else{
                binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_blue))
                binding.tvStatueMissingChild.setTextColor(ContextCompat.getColor(context, R.color.purple_200))
                binding.tvStatueMissingChild.setText("تم العثور علية")
            }

            binding.tvNameMissingChild.text = item.name
            binding.tvDescriptionMissingChild.text = checkText(item.otherInfo)
            binding.tvAge.text = "سنوات" +"${item.age}"
            binding.tvCapital.text = item.city
            binding.tvCity.text = item.subCity
//            binding.textView7 =
            binding.ivMissingChild.load(item.image)
            binding.container.setOnClickListener {
                interaction?.onItemSelected(position,item ,0)
            }


        }
        private fun checkText(string: String): String {
            return if (string in "other_info" || string.isNullOrEmpty()) {
                Constants.LOREM
            } else {
                string
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            MissingChildListItemBinding.inflate(
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

    fun submitList(list: List<FollowedCasesItem>) {
        differ.submitList(list)
    }


    interface Interaction {
        fun onItemSelected(position: Int, item: FollowedCasesItem, state: Int)
    }


}