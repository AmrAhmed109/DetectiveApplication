package com.example.detectiveapplication.feature.search_result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.MissingChildListItemBinding
import com.example.detectiveapplication.dto.recognition.RecognitionData
import com.example.detectiveapplication.utils.Constants

class ResultAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil
    .ItemCallback<RecognitionData>() {
        override fun areItemsTheSame(oldItem: RecognitionData, newItem: RecognitionData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RecognitionData,
            newItem: RecognitionData
        ): Boolean {
            return false

        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    class ViewHolder
    constructor(
        private val binding: MissingChildListItemBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecognitionData, position: Int) = with(binding.root) {
            if (item.status == "not_found"){
                binding.cardView.setCardBackgroundColor(
                    ContextCompat.getColor(context,
                    R.color.light_red
                ))
                binding.tvStatueMissingChild.setTextColor(
                    ContextCompat.getColor(context,
                    R.color.red
                ))
                binding.tvStatueMissingChild.setText("مفقود")
            }else{
                binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_blue))
                binding.tvStatueMissingChild.setTextColor(ContextCompat.getColor(context, R.color.purple_200))
                binding.tvStatueMissingChild.setText("تم العثور علية")
            }

            binding.tvNameMissingChild.text = item.name
            binding.tvDescriptionMissingChild.text = item.otherInfo?.let { checkText(it) }
            binding.tvAge.text = "سنوات" +"${item.age}"
            binding.tvCapital.text = item.city
            binding.tvCity.text = item.subCity
//            binding.textView7 =
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultAdapter.ViewHolder {

        return ViewHolder(
            MissingChildListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: ResultAdapter.ViewHolder, position: Int) {
        holder.bind(differ.currentList.get(position), position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<RecognitionData>) {
        differ.submitList(list)
    }


    interface Interaction {
        fun onItemSelected(position: Int, item: RecognitionData, state: Int)
    }

}