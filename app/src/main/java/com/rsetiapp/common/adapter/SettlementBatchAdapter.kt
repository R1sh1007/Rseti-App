package com.rsetiapp.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.rsetiapp.common.fragments.FollowUpCandidateFragmentDirections
import com.rsetiapp.common.fragments.SettlementVeryficationFragmentDirections
import com.rsetiapp.common.model.response.SettlementPercentage
import com.rsetiapp.core.util.AppUtil
import com.rsetiapp.databinding.ItemSettlementVeryficationBatchBinding


class SettlementBatchAdapter(
    private val candidateList: MutableList<SettlementPercentage>
) : RecyclerView.Adapter<SettlementBatchAdapter.CandidateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val binding =
            ItemSettlementVeryficationBatchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CandidateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        holder.bind(candidateList[position])
    }

    override fun getItemCount(): Int = candidateList.size

    /** ✅ THIS is where update() is implemented */
    fun update(newList: List<SettlementPercentage>) {
        candidateList.clear()
        candidateList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class CandidateViewHolder(
        private val binding: ItemSettlementVeryficationBatchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(candidate: SettlementPercentage) {

            binding.tvBatchIdName.text = candidate.batchRegNo

            binding.tvSettlementPerctangeName.text = candidate.settledPercentage
            binding.tvInstituteName.text = candidate.instituteName
            binding.tvBatchName.text = candidate.batchName
            binding.tvtvCandidateIDName.text = candidate.candidateId
            val context = binding.root.context
            binding.root.setOnClickListener {
                if (position != RecyclerView.NO_POSITION && position < candidateList.size) {
                    val data = candidateList[position]
                    AppUtil.saveCandidateIdPreference(context,""+data.candidateId)
                    val action = SettlementVeryficationFragmentDirections
                        .actionSettlementVeryficationBatchFragmentToSettlemt(data)
                    binding.root.findNavController().navigate(action)
                }



            }
        }
    }
}