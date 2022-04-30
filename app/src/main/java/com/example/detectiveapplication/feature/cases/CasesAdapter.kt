package com.example.detectiveapplication.feature.cases

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.detectiveapplication.databinding.CardViewCaseBinding
import com.example.detectiveapplication.dto.cases.Case

//import com.squareup.picasso.Picasso

class CasesAdapter(
    private val cases: List<Case>,
    private val onItemClicked: (Case) -> Unit,
) : RecyclerView.Adapter<CasesAdapter.CaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val bind = CardViewCaseBinding.inflate(
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
        private val binding: CardViewCaseBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun updateUI(case: Case) {
            binding.tvAge.text = case.age.toString() + " " +"سنة"
            binding.tvNameMissingChild.text = case.name.toString()
            binding.tvStatueMissingChild.text = case.status.toString()
            binding.tvCity.text = case.subCity.toString()
            binding.tvCapital.text = case.city.toString()

//            Picasso.get().load(case.image).into(binding.ivMissingChild);

            binding.ivMissingChild.load(case.image)
        }
    }
}
