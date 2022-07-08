package com.example.detectiveapplication.feature.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.MissingChildListItemBinding
import com.example.detectiveapplication.dto.search_response.SearchDataX
import com.example.detectiveapplication.utils.Constants

class SearchAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil
.ItemCallback<SearchDataX>() {

        override fun areItemsTheSame(oldItem: SearchDataX, newItem: SearchDataX): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchDataX, newItem: SearchDataX): Boolean {
            return false

        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

     class ViewHolder
        constructor(
            private val binding: MissingChildListItemBinding,
            private val interaction: Interaction?
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(item: SearchDataX) = with(binding.root) {
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
                binding.tvAge.text = item.age.toString() + " " + "سنة"
                binding.tvCapital.text = item.city
                binding.tvCity.text = item.subCity
                binding.ivMissingChild.load(item.image)
                binding.container.setOnClickListener {
                    interaction?.onItemSelected(position,item ,0)
                }


            }
         fun String?.fixHttpsRequest(): String? {
             return this?.replace("http", "https")
         }
         private fun checkText(string: String): String {
             return if (string in "other_info" || string.isNullOrEmpty()) {
                 Constants.LOREM
             } else {
                 string
             }
         }
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {

        return ViewHolder(
            MissingChildListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
                holder.bind(differ.currentList.get(position))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<SearchDataX>) {
        differ.submitList(list)
    }


    interface Interaction {
        fun onItemSelected(position: Int, item: SearchDataX, state: Int)
    }

}