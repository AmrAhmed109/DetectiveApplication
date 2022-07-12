package com.example.detectiveapplication.feature.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.detectiveapplication.databinding.CardViewCapitalBinding
import com.example.detectiveapplication.feature.home.utils.Capital


class CapitalAdapter(
    private val capitalList: List<Capital>,
    private var selectedCapitalPosition: Int,
    private val onItemClicked: (Capital) -> Unit,
) : RecyclerView.Adapter<CapitalAdapter.CapitalViewHolder>() {

    val viewHolders = hashMapOf<Int, CapitalViewHolder>()

    companion object {
        private const val TAG = "CapitalAdapter"
    }

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
        viewHolders[position] = holder
        holder.bind(position)
        holder.updateUI()
    }

    override fun getItemCount(): Int {
        return capitalList.size
    }

    fun setSelectedCapital(capital: Capital) {
        selectedCapitalPosition = capitalList.indexOf(capital)
    }

    inner class CapitalViewHolder(
        val binding: CardViewCapitalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(postion: Int) {
            binding.chipCapital.text = capitalList[postion].capitalName
        }
        init {
            binding.chipCapital.setOnClickListener {
                Log.d(TAG, "onBindViewHolder: ")
                selectedCapitalPosition = layoutPosition
                viewHolders.forEach {
                    it.value.updateUI()
                }
                onItemClicked(capitalList[layoutPosition])
            }
        }

        fun updateUI() {
            binding.chipCapital.isChecked = layoutPosition == selectedCapitalPosition
//            binding.chipCapital.text = capital.capitalName
        }
    }
}
