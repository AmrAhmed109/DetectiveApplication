package com.example.detectiveapplication.feature.home

import android.content.res.Resources
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.CardViewCaseBinding
import com.example.detectiveapplication.databinding.MissingChildListItemBinding
import com.example.detectiveapplication.dto.cases.Case
import com.example.detectiveapplication.utils.Constants.Companion.LOREM

//import com.squareup.picasso.Picasso

class CasesAdapter(
    private val cases: List<Case>,
    private val onItemClicked: (Case) -> Unit,
) : RecyclerView.Adapter<CasesAdapter.CaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val bind = MissingChildListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CaseViewHolder(binding = bind)
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        holder.updateUI(cases.get(position))
        holder.itemView.setOnClickListener {
            onItemClicked(cases.get(position))
        }
    }

    override fun getItemCount(): Int {
        return cases.size
    }

    class CaseViewHolder(
        private val binding: MissingChildListItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun updateUI(case: Case) {
            if (case.status == "not_found") {
                binding.tvStatueMissingChild.text = "مفقود"
            } else {
                binding.tvStatueMissingChild.text = "تم العثور علية"
                binding.tvStatueMissingChild.setTextColor(ContextCompat.getColorStateList(
                    binding.root.context,
                    R.color.purple_200
                ))
                binding.cardView.setCardBackgroundColor(ContextCompat.getColor(
                    binding.root.context,
                    R.color.light_blue
                ))
            }
            binding.tvAge.text = case.age.toString() + " " + "سنة"
            binding.tvNameMissingChild.text = case.name.toString()
            binding.tvDescriptionMissingChild.text = checkText(case.otherInfo.toString())
            binding.tvCity.text = case.subCity.toString()
            binding.tvCapital.text = case.city.toString()

//            Picasso.get().load(case.image).into(binding.ivMissingChild);

            binding.ivMissingChild.load(case.image)
        }
        fun String?.fixHttpsRequest(): String? {
            return this?.replace("http", "https")
        }
        private fun checkText(string: String): String {
            return if (string in "other_info") {
                LOREM
            } else {
                string
            }
        }

    }
}
