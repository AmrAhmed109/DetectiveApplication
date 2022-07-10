package com.example.detectiveapplication.ui.home.watingCases

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.MissingChildListItemBinding
import com.example.detectiveapplication.dto.pendingCases.DataList
import com.example.detectiveapplication.utils.Constants

class WatingCasesAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<WatingCasesAdapter.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataList>() {
        override fun areItemsTheSame(
            oldItem: DataList,
            newItem: DataList
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DataList,
            newItem: DataList
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
        fun bind(item: DataList, position: Int) = with(binding.root) {
            binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.lighter_grey))
            binding.tvStatueMissingChild.setTextColor(ContextCompat.getColor(context,R.color.dark_grey))
            binding.tvStatueMissingChild.setText("قيد المراجعة")

            binding.tvNameMissingChild.text = item.name
            binding.tvDescriptionMissingChild.text = checkText(item.otherInfo)
            binding.tvAge.text = item.age.toString() + " " + "سنة"
            binding.tvCapital.text = item.city
            binding.tvCity.text = item.subCity
//            binding.textView7 =
            binding.ivMissingChild.load(item.image){
                crossfade(1000)
            }
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

    fun submitList(list: List<DataList>) {
        differ.submitList(list)
    }


    interface Interaction {
        fun onItemSelected(position: Int, item: DataList, state: Int)
    }


}