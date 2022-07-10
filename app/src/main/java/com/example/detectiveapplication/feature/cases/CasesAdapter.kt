package com.example.detectiveapplication.feature.cases

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.CardViewCaseBinding
import com.example.detectiveapplication.dto.cases.Case
import com.example.detectiveapplication.ui.home.FollowingAdapter
import com.example.detectiveapplication.utils.Constants
import com.squareup.picasso.Picasso

//import com.squareup.picasso.Picasso

class CasesAdapter(
    private val cases: List<Case>,
    private val interaction: Interaction? = null,
    private val onItemClicked: (Case) -> Unit
) : RecyclerView.Adapter<CasesAdapter.CaseViewHolder>() {
    interface Interaction {
        fun onFoundSelected(position: Int, case: Case, state: Int)
        fun onDeleteSelected(position: Int, case: Case, state: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val bind = CardViewCaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CaseViewHolder(binding = bind, interaction = interaction)
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
        private val binding: CardViewCaseBinding,
        private val interaction: Interaction?
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
                binding.cardView.setBackgroundColor( ContextCompat.getColor(
                    binding.root.context,
                    R.color.light_blue
                ))
            }
            binding.tvAge.text = case.age.toString() + " " + "سنة"
            binding.tvNameMissingChild.text = case.name.toString()
            binding.tvCity.text = case.subCity.toString()
            binding.tvCapital.text = case.city.toString()
            binding.tvDescriptionMissingChild.text = checkText(case.otherInfo.toString())
            binding.ivMissingChild.load(case.image){
                crossfade(1000)
            }

            binding.cardView67.setOnClickListener {
                interaction?.onFoundSelected(1, case, 1)
            }

            binding.cardView697.setOnClickListener {
                interaction?.onDeleteSelected(1, case, 1)
            }
        }






        private fun checkText(string: String): String {
            return if (string in "other_info") {
                Constants.LOREM
            } else {
                string
            }
        }
    }

}
