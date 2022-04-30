package com.example.detectiveapplication.feature.home

import android.content.res.Resources
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.detectiveapplication.R
import com.example.detectiveapplication.databinding.CardViewCaseBinding
import com.example.detectiveapplication.databinding.MissingChildListItemBinding
import com.example.detectiveapplication.dto.cases.Case

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
            binding.tvAge.text = case.age.toString() + " " + "سنة"
            binding.tvNameMissingChild.text = case.name.toString()
            binding.tvDescriptionMissingChild.text = checkText(case.otherInfo.toString())
            binding.tvStatueMissingChild.text = case.status.toString()
            binding.tvCity.text = case.subCity.toString()
            binding.tvCapital.text = case.city.toString()

//            Picasso.get().load(case.image).into(binding.ivMissingChild);

            binding.ivMissingChild.load(case.image)
        }

        private fun checkText(string: String): String {
            return if (string in "other_info") {
                strings
            } else {
                string
            }
        }

        val strings: String =
            "هذا النص هو مثال لنص يمكن أن يستبدل في نفس المساحة، لقد تم توليد هذا النص من مولد النص العربى، حيث يمكنك أن تولد مثل هذا النص أو العديد من النصوص الأخرى إضافة إلى زيادة عدد الحروف التى يولدها التطبيق. "
    }
}
