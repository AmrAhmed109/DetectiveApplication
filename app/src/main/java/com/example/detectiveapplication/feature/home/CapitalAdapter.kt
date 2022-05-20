package com.example.detectiveapplication.feature.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.detectiveapplication.databinding.CardViewCapitalBinding
import com.example.detectiveapplication.feature.home.utils.Capital


class CapitalAdapter(
    private val capitalList: List<Capital>,
    val checkedCapital: Capital,
    private val onItemClicked: (Capital) -> Unit,
) : RecyclerView.Adapter<CapitalAdapter.CapitalViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CapitalViewHolder {
        val bind = CardViewCapitalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CapitalViewHolder(binding = bind)
    }

    override fun onBindViewHolder(holder: CapitalViewHolder, position: Int) {
        holder.updateUI(capitalList[position])
    }

    override fun getItemCount(): Int {
        return capitalList.size
    }

    inner class CapitalViewHolder(
        private val binding: CardViewCapitalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun updateUI(capital: Capital) {
            binding.chipCapital.isChecked = capital.capitalName == checkedCapital.capitalName
            binding.chipCapital.text = capital.capitalName

            binding.chipCapital.setOnCheckedChangeListener { compoundButton, b ->
                onItemClicked(capital)
            }
        }
    }
}
